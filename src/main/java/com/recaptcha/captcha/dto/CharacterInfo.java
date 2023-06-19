package com.recaptcha.captcha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CharacterInfo {
    private char character;
    private Font font;
    private Color color;
    private int x;
    private int y;

}