package com.projects.coin_monitor_hub.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.coin_monitor_hub.constants.TokenConstants;
import com.projects.coin_monitor_hub.dto.request.CoinRequestDto;
import com.projects.coin_monitor_hub.dto.request.ExpectedPriceRequestDto;
import com.projects.coin_monitor_hub.dto.request.TokenPriceRequestDto;
import com.projects.coin_monitor_hub.dto.response.CoinResponseDto;
import com.projects.coin_monitor_hub.dto.response.TokenPriceResponseDto;
import com.projects.coin_monitor_hub.entity.Coin;
import com.projects.coin_monitor_hub.exception.CoinAlreadyExistsException;
import com.projects.coin_monitor_hub.exception.ResourceNotFoundException;
import com.projects.coin_monitor_hub.mapper.CoinMapper;
import com.projects.coin_monitor_hub.mapper.TokenPriceMapper;
import com.projects.coin_monitor_hub.repository.CoinRepository;
import com.projects.coin_monitor_hub.service.CoinService;
import com.projects.coin_monitor_hub.service.SMSService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class CoinServiceImpl implements CoinService {

    private final SMSService smsService;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final Set<String> history;
    private final CoinRepository coinRepository;

    @Value("${coin.gecko.api.key}")
    private String coinGeckoApiKey;

    @Value("${spring.mail.username}")
    private String gmailUsername;

    @Value("${personal.phone-number}")
    private String personalPhoneNumber;

    @Value("${twilio.account-sid}")
    private String twilioAccountSid;

    @Value("${twilio.auth-token}")
    private String twilioAuthToken;

    @Autowired
    public CoinServiceImpl(SMSService smsService, WebClient.Builder webClientBuilder, ObjectMapper objectMapper,
                           JavaMailSender emailSender, TemplateEngine templateEngine, Set<String> history, CoinRepository coinRepository) {
        this.smsService = smsService;
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.history = history;
        this.coinRepository = coinRepository;
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
                            .uri(uriBuilder ->
                                    uriBuilder.path("/api/v3/simple/token_price/{assetPlatformId}")
                                            .queryParam("contract_addresses", tokenContractAddress)
                                            .queryParam("vs_currencies", targetCurrency).build(assetPlatformId))
                            .header("accept", "application/json")
                            .header("User-Agent", "C")
                            .header("x-cg-demo-api-key", coinGeckoApiKey)
                            .retrieve()
                            .onStatus(status -> status.value() == 403, clientResponse -> {
                                log.error("Received 403 Forbidden from CoinGecko API");
                                return clientResponse.createException();
                            })
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

    @Override
    public List<CoinResponseDto> getAllCoins() {
        log.debug("getAllCoins()");
        return coinRepository.findAll()
                .stream()
                .map(CoinMapper.INSTANCE::coinToCoinResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CoinResponseDto getCoinById(Long id) {
        log.debug("getCoinById({})", id);
        Coin foundCoin = coinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coin", "id", id.toString()));
        log.info("Coin retrieved with id : {}", id);
        return CoinMapper.INSTANCE.coinToCoinResponse(foundCoin);
    }

    @Override
    public CoinResponseDto createCoin(CoinRequestDto coinRequestDto) {
        log.debug("createCoin({})", coinRequestDto.getAssetPlatformId());
        String tokenContractAddress = coinRequestDto.getTokenContractAddress();
        boolean isCoinExists = coinRepository.existsByTokenContractAddress(tokenContractAddress);
        if (isCoinExists) {
            throw new CoinAlreadyExistsException(tokenContractAddress);
        }

        Coin coin = CoinMapper.INSTANCE.coinRequestToCoin(coinRequestDto);
        Coin savedCoin = coinRepository.save(coin);
        log.info("Coin created with id : {}", savedCoin.getId());
        return CoinMapper.INSTANCE.coinToCoinResponse(savedCoin);
    }

    @Override
    public CoinResponseDto updateCoin(Long coinId, CoinRequestDto updatedCoinRequestDto) {
        log.debug("updateCoin({}, {})", coinId.toString(), updatedCoinRequestDto.getAssetPlatformId());
        Coin foundCoin = coinRepository.findById(coinId)
                .orElseThrow(() -> new ResourceNotFoundException("Coin", "id", coinId.toString()));
        foundCoin.setAssetPlatformId(updatedCoinRequestDto.getAssetPlatformId());
        foundCoin.setTokenContractAddress(updatedCoinRequestDto.getTokenContractAddress());
        foundCoin.setTargetCurrency(updatedCoinRequestDto.getTargetCurrency());
        foundCoin.setTokenName(updatedCoinRequestDto.getTokenName());
        foundCoin.setLowTwentyFive(updatedCoinRequestDto.getLowTwentyFive());
        foundCoin.setHighTwentyFive(updatedCoinRequestDto.getHighTwentyFive());
        foundCoin.setLowFifty(updatedCoinRequestDto.getLowFifty());
        foundCoin.setHighFifty(updatedCoinRequestDto.getHighFifty());
        foundCoin.setLowSeventyFive(updatedCoinRequestDto.getLowSeventyFive());
        foundCoin.setHighSeventyFive(updatedCoinRequestDto.getHighSeventyFive());
        foundCoin.setLowOneHundred(updatedCoinRequestDto.getLowOneHundred());
        foundCoin.setHighOneHundred(updatedCoinRequestDto.getHighOneHundred());
        Coin updatedCoin = coinRepository.save(foundCoin);
        log.info("Coin updated with id : {}", updatedCoin.getId());
        return CoinMapper.INSTANCE.coinToCoinResponse(updatedCoin);
    }

    @Override
    public void deleteCoin(Long coinId) {
        log.debug("deleteCoin({})", coinId);
        Coin foundCoin = coinRepository.findById(coinId)
                .orElseThrow(() -> new ResourceNotFoundException("Coin", "id", coinId.toString()));
        coinRepository.delete(foundCoin);
        log.info("Coin deleted with id : {}", coinId);
    }

    private List<TokenPriceResponseDto> getDatasets() {

        TokenPriceResponseDto metaStrikeTokenPriceResponseDto = getExpectedPriceRequestDto(
                BigDecimal.valueOf(0.21), BigDecimal.valueOf(0.30),
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
