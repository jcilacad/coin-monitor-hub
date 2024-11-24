package com.projects.coin_monitor_hub.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.coin_monitor_hub.constants.TokenConstants;
import com.projects.coin_monitor_hub.dto.ExpectedPriceRequestDto;
import com.projects.coin_monitor_hub.dto.TokenPriceRequestDto;
import com.projects.coin_monitor_hub.dto.TokenPriceResponseDto;
import com.projects.coin_monitor_hub.mapper.TokenPriceMapper;
import com.projects.coin_monitor_hub.service.CoinService;
import com.projects.coin_monitor_hub.service.SMSService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    @Value("${coin.gecko.api.key}")
    private String coinGeckoApiKey;

    private final SMSService smsService;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final Set<String> history;

    @Value("${spring.mail.username}")
    private String gmailUsername;

    @Value("${personal.phone-number}")
    private String personalPhoneNumber;

    @Value("${twilio.account-sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth-token}")
    private String twilioAuthToken;

    @Autowired
    public CoinServiceImpl(SMSService smsService, WebClient.Builder webClientBuilder, ObjectMapper objectMapper, JavaMailSender emailSender,
                           TemplateEngine templateEngine, Set<String> history) {
        this.smsService = smsService;
        this.webClient = webClientBuilder.baseUrl("https://api.coingecko.com").build();
        this.objectMapper = objectMapper;
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.history = history;
    }

    @Async
    @Override
    @Scheduled(cron = "0 */13 * * * *")
    public void getTokenPrice() {
        log.debug("getTokenPrice()");
        getDatasets().stream()
                .forEach(tokenPriceResponseDto -> {
                    String assetPlatformId = tokenPriceResponseDto.getAssetPlatformId();
                    String tokenContractAddress = tokenPriceResponseDto.getTokenContractAddress().toLowerCase();
                    String targetCurrency = tokenPriceResponseDto.getTargetCurrency();
                    String tokenName = tokenPriceResponseDto.getTokenName();
                    String rawResponse = webClient.get()
                            .uri("/api/v3/simple/token_price/{assetPlatformId}" +
                                            "?contract_addresses={tokenContractAddress}&vs_currencies={targetCurrency}",
                                    assetPlatformId, tokenContractAddress, targetCurrency)
                            .header("accept", "application/json")
                            .header("x-cg-demo-api-key", coinGeckoApiKey)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

                    Map<String, Map<String, BigDecimal>> response;
                    try {
                        response = objectMapper.readValue(rawResponse, new TypeReference<>() {
                        });
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse API response", e);
                    }

                    Map<String, BigDecimal> tokenData = response.get(tokenContractAddress);
                    BigDecimal extractedPrice = tokenData != null ? tokenData.get(targetCurrency) : null;

                    List<ExpectedPriceRequestDto> priceRequestDtoList = tokenPriceResponseDto.getExpectedPriceRequestDto();

                    for (ExpectedPriceRequestDto expectedPriceRequestDto : priceRequestDtoList) {
                        BigDecimal lowLimitExpectedPrice = expectedPriceRequestDto.getLowLimitExpectedPrice();
                        BigDecimal highLimitExpectedPrice = expectedPriceRequestDto.getHighLimitExpectedPrice();
                        int percent = expectedPriceRequestDto.getPercent();

                        boolean isInRange = extractedPrice.compareTo(lowLimitExpectedPrice) >= 0 &&
                                extractedPrice.compareTo(highLimitExpectedPrice) <= 0;

                        String priceRangeUniqueId = DigestUtils.sha256Hex(LocalDateTime.now().toLocalDate()
                                + assetPlatformId + tokenContractAddress
                                + lowLimitExpectedPrice + highLimitExpectedPrice);

                        if (isInRange && !history.contains(priceRangeUniqueId)) {
                            Currency currency = Currency.getInstance(targetCurrency.toUpperCase());
                            String targetPrice = currency.getSymbol(Locale.GERMAN).concat(" ").concat(extractedPrice.toPlainString());

                            // Send Notification via SMS
                            String smsMessage = String.format("""
                                    Token Alert
                                                                        
                                    Token Name: %s
                                    Current Price: %s
                                    Percent: %d
                                    """, tokenName, targetPrice, percent);
                            Twilio.init(twilioAccountSid, twilioAuthToken);
                            Message twilioMessage = (Message) smsService.sendSms(personalPhoneNumber, smsMessage);
                            log.info("Twilio Response : {}", twilioMessage.getStatus());

                            // Send Notification via Email
                            try {
                                MimeMessage mimeMessage = emailSender.createMimeMessage();
                                MimeMessageHelper helper;
                                Context context = new Context();
                                Map<String, Object> variables = new HashMap<>();
                                variables.put(TokenConstants.TARGET_PRICE, targetPrice);
                                variables.put(TokenConstants.TOKEN_NAME, tokenName);
                                variables.put(TokenConstants.PERCENT, String.valueOf(percent));

                                context.setVariables(variables);
                                helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                                helper.setTo(gmailUsername);
                                helper.setSubject("Coin Market Hub Alert");
                                String htmlContent = templateEngine.process(TokenConstants.EMAIL_TEMPLATE, context);
                                helper.setText(htmlContent, true);
                                emailSender.send(mimeMessage);
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }

                            history.add(priceRangeUniqueId);
                            break;
                        }
                    }

                });
    }

    private List<TokenPriceResponseDto> getDatasets() {

        TokenPriceResponseDto metaStrikeTokenPriceResponseDto = getExpectedPriceRequestDto(
                BigDecimal.valueOf(0.20), BigDecimal.valueOf(0.30),
                BigDecimal.valueOf(0.31), BigDecimal.valueOf(0.40),
                BigDecimal.valueOf(0.41), BigDecimal.valueOf(0.50),
                BigDecimal.valueOf(0.51), BigDecimal.valueOf(0.60),
                TokenConstants.BINANCE_SMART_CHAIN, TokenConstants.METASTRIKE_CONTRACT_ADDRESS,
                TokenConstants.USD, TokenConstants.METASTRIKE_TOKEN_NAME);

        TokenPriceResponseDto octaviaTokenPriceResponseDto = getExpectedPriceRequestDto(
                BigDecimal.valueOf(0.10), BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(0.21), BigDecimal.valueOf(0.40),
                BigDecimal.valueOf(0.41), BigDecimal.valueOf(1.00),
                BigDecimal.valueOf(1.01), BigDecimal.valueOf(2.00),
                TokenConstants.BINANCE_SMART_CHAIN, TokenConstants.OCTAVIA_CONTRACT_ADDRESS,
                TokenConstants.USD, TokenConstants.OCTAVIA_TOKEN_NAME);

        TokenPriceResponseDto exverseTokendPriceResponseDto = getExpectedPriceRequestDto(
                BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.075),
                BigDecimal.valueOf(0.076), BigDecimal.valueOf(0.10),
                BigDecimal.valueOf(0.11), BigDecimal.valueOf(0.125),
                BigDecimal.valueOf(0.126), BigDecimal.valueOf(0.15),
                TokenConstants.BINANCE_SMART_CHAIN, TokenConstants.EXVERSE_CONTRACT_ADDRESS,
                TokenConstants.USD, TokenConstants.EXVERSE_TOKEN_NAME);

        TokenPriceResponseDto andyBaseTokendPriceResponseDto = getExpectedPriceRequestDto(
                BigDecimal.valueOf(0.04), BigDecimal.valueOf(0.050),
                BigDecimal.valueOf(0.051), BigDecimal.valueOf(0.06),
                BigDecimal.valueOf(0.061), BigDecimal.valueOf(0.07),
                BigDecimal.valueOf(0.071), BigDecimal.valueOf(0.08),
                TokenConstants.BASE, TokenConstants.ANDY_BASE_CONTRACT_ADDRESS,
                TokenConstants.USD, TokenConstants.ANDY_BASE_TOKEN_NAME);

        return List.of(metaStrikeTokenPriceResponseDto, octaviaTokenPriceResponseDto, exverseTokendPriceResponseDto, andyBaseTokendPriceResponseDto);
    }

    private TokenPriceResponseDto getExpectedPriceRequestDto(BigDecimal lowTwentyFive, BigDecimal highTwentyFive,
                                                             BigDecimal lowFifty, BigDecimal highFifty,
                                                             BigDecimal lowSeventyFive, BigDecimal highSeventyFive,
                                                             BigDecimal lowOneHundred, BigDecimal highOneHundred,
                                                             String assetPlatformId, String tokenContractAddress,
                                                             String targetCurrency, String tokenName) {

        List<ExpectedPriceRequestDto> expectedPriceRequestDtoList = Arrays.asList(
                ExpectedPriceRequestDto.builder().lowLimitExpectedPrice(lowTwentyFive)
                        .highLimitExpectedPrice(highTwentyFive).percent(25).build(),

                ExpectedPriceRequestDto.builder().lowLimitExpectedPrice(lowFifty)
                        .highLimitExpectedPrice(highFifty).percent(50).build(),

                ExpectedPriceRequestDto.builder().lowLimitExpectedPrice(lowSeventyFive)
                        .highLimitExpectedPrice(highSeventyFive).percent(75).build(),

                ExpectedPriceRequestDto.builder().lowLimitExpectedPrice(lowOneHundred)
                        .highLimitExpectedPrice(highOneHundred).percent(100).build()
        );

        TokenPriceRequestDto tokenPriceRequestDto = TokenPriceRequestDto.builder()
                .assetPlatformId(assetPlatformId)
                .tokenContractAddress(tokenContractAddress)
                .targetCurrency(targetCurrency)
                .tokenName(tokenName)
                .expectedPriceRequestDto(expectedPriceRequestDtoList)
                .build();

        return TokenPriceMapper.INSTANCE.tokenPriceRequestToResponse(tokenPriceRequestDto);
    }
}
