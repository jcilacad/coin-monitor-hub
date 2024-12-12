package com.projects.coin_monitor_hub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling requests to the home page.
 */
@Controller
public class HomeController {

    /**
     * Handles requests to the /home endpoint.
     *
     * @return the name of the HTML file to be rendered
     */
    @RequestMapping("/home")
    public String home() {
        return "index.html";
    }
}
