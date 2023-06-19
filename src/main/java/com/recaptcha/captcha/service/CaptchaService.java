package com.recaptcha.captcha.service;

import java.io.IOException;

public interface CaptchaService {
    EkycServiceResponse createCaptcha() throws IOException;
    EkycServiceResponse validateCaptcha(Long id, String sum) throws IOException;
}