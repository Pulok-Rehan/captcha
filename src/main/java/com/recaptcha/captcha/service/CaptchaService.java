package com.recaptcha.captcha.service;

import com.recaptcha.captcha.response.CommonResponse;

import java.io.IOException;

public interface CaptchaService {
    CommonResponse createCaptcha() throws IOException;
    CommonResponse validateCaptcha(Long id, String sum) throws IOException;
}