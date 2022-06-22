package fr.cnam.stefangeorgesco.dmp.api;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csrf")
public class CSRFController {

    @GetMapping
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }

}