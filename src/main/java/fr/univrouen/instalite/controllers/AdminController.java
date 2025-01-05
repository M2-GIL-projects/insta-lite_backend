package fr.univrouen.instalite.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @GetMapping("/portfolio")
    public String getPortfolio() {
        return "InstaLite 1.0";
    }
}
