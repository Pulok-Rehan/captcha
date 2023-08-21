package com.recaptcha.captcha.response;

import jakarta.persistence.NamedStoredProcedureQueries;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse {
    private Boolean hasError;
    private String code;
    private String content;
    private String message;

}
