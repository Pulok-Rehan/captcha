package com.recaptcha.captcha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaptchaDto {
    long id;
    String imageByte;
    public long getId() {
        return id;
    }
}
