package org.tahmasebi.keycloak.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomeService {
    @GetMapping
    public String welcome() {
        return "Welcome to Server side. try /public, /private, /private/superuser";
    }
}
