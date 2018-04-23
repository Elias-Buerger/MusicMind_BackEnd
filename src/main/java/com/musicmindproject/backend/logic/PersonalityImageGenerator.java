package com.musicmindproject.backend.logic;

import com.musicmindproject.backend.entities.User;

import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@Stateless
public class PersonalityImageGenerator {
    private static final int TOP = 100;
    private static final int BETWEEN = 30;
    private static final int SIDE = 70;

    private static final int IMAGE_WIDTH = 1200;
    private static final int IMAGE_HEIGHT = 630;

    private static final int BORDER_WIDTH = 4;

    private static final int BAR_WIDTH = IMAGE_WIDTH - (2 * SIDE + 2 * BORDER_WIDTH);
    private static final int BAR_HEIGHT = (IMAGE_HEIGHT - (2 * TOP + 10 * BORDER_WIDTH + 4* BETWEEN))/5;

    private static final Color[] PERSONALITY_COLORS = {
            Color.decode("#29B765"),
            Color.decode("#EEC20F"),
            Color.decode("#E74C3C"),
            Color.decode("#9B59B6"),
            Color.decode("#3498DB")
    };
    private static final Color BACKGROUND_COLOR = Color.decode("#212121");
    private static final Color BORDER_COLOR = Color.decode("#FFFFFF");


    public void generatePersonalityImage(User user) {
        double[] values = {
                user.getOpenness(),
                user.getConscientiousness(),
                user.getExtraversion(),
                user.getAgreeableness(),
                user.getNeuroticism()
        };
        String pathname = String.format("/mnt/personality_images/%s_%s.png", user.getUserId().hashCode(), user.getUserName());
        BufferedImage bufimage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic2D = bufimage.createGraphics();
        try {
            File output = new File(pathname);
            graphic2D.setBackground(BACKGROUND_COLOR);
            graphic2D.setStroke(new BasicStroke(BORDER_WIDTH));
            System.out.println(PERSONALITY_COLORS.length);
            for(int i = 0; i < PERSONALITY_COLORS.length; i++){
                graphic2D.setPaint(BORDER_COLOR);
                graphic2D.drawRect(SIDE, TOP + i*(BORDER_WIDTH +BAR_HEIGHT+ BETWEEN), BAR_WIDTH+BORDER_WIDTH, BAR_HEIGHT+BORDER_WIDTH);
                graphic2D.setPaint(PERSONALITY_COLORS[i]);
                System.out.println(PERSONALITY_COLORS[i].toString());
                graphic2D.fillRect(SIDE + BORDER_WIDTH /2, TOP + BORDER_WIDTH /2+ i*(BORDER_WIDTH +BAR_HEIGHT+ BETWEEN), (int)(BAR_WIDTH * (values[i] / 100)), BAR_HEIGHT);
            }
            ImageIO.write(bufimage, "png", output);
        } catch (IOException log) {
            System.out.println(log);
        }
    }
}
