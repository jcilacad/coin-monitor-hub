package com.projects.coin_monitor_hub.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoinController {

    @GetMapping
    public String getDocumentation() {
        return """ 
                # Personal Cryptocurrency Tracker API Documentation 
                ## Overview The Personal Cryptocurrency Tracker API fetches data from the CoinGecko API and sends alerts via mail and SMS to keep users updated on market trends and price changes. 
                
                ## Endpoints 
                
                ### Get Documentation 
                - **URL:** /api/documentation 
                - **Method:** GET 
                - **Description:** Returns a string containing the API documentation. 
                - **Response:** ```json { "message": "Documentation" } ``` 
                
                ## How It Works 
                
                1. **Fetch Data:** The API fetches the latest cryptocurrency data from the CoinGecko API. 
                2. **Process Data:** The data is processed to identify significant market trends and price changes. 
                3. **Send Alerts:** Alerts are sent via mail and SMS to notify users of important updates. 
                
                ## Dependencies 
                
                - **CoinGecko API:** Used to fetch cryptocurrency data. 
                - **Mail Service:** Used to send email alerts. 
                - **SMS Service:** Used to send SMS alerts. 
                
                """;
    }
}
