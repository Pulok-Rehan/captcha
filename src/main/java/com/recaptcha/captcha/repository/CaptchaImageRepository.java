package com.recaptcha.captcha.repository;

import com.recaptcha.captcha.model.CaptchaImage;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class CaptchaImageRepository implements JpaRepository<CaptchaImage, Long> {

}
