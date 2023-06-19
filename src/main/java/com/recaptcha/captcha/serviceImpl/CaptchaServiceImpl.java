package com.recaptcha.captcha.serviceImpl;

import ch.qos.logback.core.util.StringCollectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recaptcha.captcha.dto.CaptchaDto;
import com.recaptcha.captcha.dto.CharacterInfo;
import com.recaptcha.captcha.model.CaptchaImage;
import com.recaptcha.captcha.repository.CaptchaImageRepository;
import com.recaptcha.captcha.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {
    private final CaptchaImageRepository captchaImageRepository;

    public CaptchaServiceImpl(CaptchaImageRepository captchaImageRepository) {
        this.captchaImageRepository = captchaImageRepository;
    }

    @Override
    public EkycServiceResponse createCaptcha() throws IOException {
        String imagePath = "./src/main/resources/images/captchaBackground.png";
        EkycServiceResponse ekycServiceResponse =
                new EkycServiceResponse();
        CaptchaDto captchaDto =
                new CaptchaDto();
        ObjectMapper objectMapper =
                new ObjectMapper();

        int width = 200;
        int height = 70;
        List<CharacterInfo> characterInfoList = new ArrayList<>();
        try{
            BufferedImage sourceImage = ImageIO.read(new File(imagePath));
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            g2d.drawImage(sourceImage, 0, 0, null);
            String text = randomAlphanumeric(5);
            characterInfoList.add(new CharacterInfo(text.charAt(0), new Font("Arial", Font.ITALIC, 30), Color.ORANGE, 30, 35));
            characterInfoList.add(new CharacterInfo(text.charAt(1), new Font("Arial", Font.ITALIC, 35), Color.BLUE, 50, 40));
            characterInfoList.add(new CharacterInfo(text.charAt(2), new Font("Arial", Font.ITALIC, 35), Color.PINK, 65, 45));
            characterInfoList.add(new CharacterInfo(text.charAt(3), new Font("Arial", Font.ITALIC, 25), Color.MAGENTA, 80, 35));
            characterInfoList.add(new CharacterInfo(text.charAt(4), new Font("Arial", Font.ITALIC, 30), Color.YELLOW, 100, 30));
            for (CharacterInfo characterInfo : characterInfoList) {
                Font font = characterInfo.getFont();
                g2d.setFont(font);
                g2d.setColor(characterInfo.getColor());
                char character = characterInfo.getCharacter();
                FontRenderContext frc = g2d.getFontRenderContext();
                GlyphVector gv = font.createGlyphVector(frc, Character.toString(character));
                Shape shape = gv.getOutline();
                Rectangle2D bounds = shape.getBounds2D();
                int x = characterInfo.getX() - (int) bounds.getX();
                int y = characterInfo.getY() - (int) bounds.getY();g2d.fill(shape);
                g2d.drawString(Character.toString(character), x, y);
            }
            g2d.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            //log.info("Captcha Created!");
            CaptchaImage captchaImage = new CaptchaImage();
            captchaImage.setImageByte(imageBytes);
            captchaImage.setText(text);
            captchaImageRepository.save(captchaImage);
            //log.info("Captcha saved for id:"+ captchaImage.getId());
            captchaDto.setId(captchaImage.getId());
            captchaDto.setImageByte(Base64.getEncoder().encodeToString(captchaImage.getImageByte()));
            ekycServiceResponse.setHasError(false);
            ekycServiceResponse.setDecentMessage("Captcha generated successfully!");
            ekycServiceResponse.setContent(objectMapper.writeValueAsString(captchaDto));
        }
        catch (IOException exception){
            ekycServiceResponse.setHasError(true);
            ekycServiceResponse.setDecentMessage(exception.getMessage());
        }
        return ekycServiceResponse;

    }

    @Override
    public EkycServiceResponse validateCaptcha(Long id, String text) {
        EkycServiceResponse ekycServiceResponse =
                new EkycServiceResponse();
        try {
            if (captchaImageRepository.findById(id).get()
                    .getText().equals(text)) {
                ekycServiceResponse.setHasError(false);
                ekycServiceResponse.setDecentMessage("Captcha Matched");
                ekycServiceResponse.setContent("true");
                //log.info("Captcha matched for id: "+ captchaImageRepository.findById(id).get().getId());
            } else {
                ekycServiceResponse.setHasError(true);
                ekycServiceResponse.setContent("false");
                ekycServiceResponse.setDecentMessage("Captcha did not match");
                //log.info("Captcha did not match for id: "+ captchaImageRepository.findById(id).get().getId());
            }
        }
        catch (IllegalArgumentException e){
            ekycServiceResponse.setHasError(true);
            ekycServiceResponse.setDecentMessage(e.getMessage());
        }
        return ekycServiceResponse;
    }

    private Resource showCaptcha(long id) {
        Resource resource = captchaImageRepository.findById(id)
                .map(captchaImage -> new ByteArrayResource(captchaImage.getImageByte()))
                .orElse(null);
        return resource;
    }
}