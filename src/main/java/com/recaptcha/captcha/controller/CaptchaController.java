package com.recaptcha.captcha.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@RestController
@RequestMapping(path = ("/api/v1/public"), produces = MediaType.APPLICATION_JSON_VALUE)
public class CaptchaController {
    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping(value = "/captcha", produces = MediaType.APPLICATION_JSON_VALUE)
    public EkycServiceResponse generateCaptchaImage() throws IOException {
        return captchaService.createCaptcha();
    }
    @PostMapping(value = "/captcha",produces = MediaType.APPLICATION_JSON_VALUE)
    public EkycServiceResponse validateCaptcha(@RequestParam("id")Long id,
                                               @RequestParam("text") String text) throws IOException {
        return captchaService.validateCaptcha(id, text);
    }


}
