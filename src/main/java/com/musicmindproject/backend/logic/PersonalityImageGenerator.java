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
    private static final int TOP = 110;
    private static final int BETWEEN = 35;
    private static final int SIDE = 100;

    private static final int IMAGE_WIDTH = 1280;
    private static final int IMAGE_HEIGHT = 720;

    private static final int BORDER_WIDTH = 4;

    private static final int BAR_WIDTH = IMAGE_WIDTH - (2 * SIDE + 2 * BORDER_WIDTH);
    private static final int BAR_HEIGHT = (IMAGE_HEIGHT - (2 * TOP + 10 * BORDER_WIDTH + 4 * BETWEEN)) / 5;

    private static final Color[] PERSONALITY_COLORS = {
            Color.decode("#29B765"),
            Color.decode("#EEC20F"),
            Color.decode("#E74C3C"),
            Color.decode("#9B59B6"),
            Color.decode("#3498DB")
    };
    private static final Color BACKGROUND_COLOR = Color.decode("#212121");
    private static final Color BORDER_COLOR = Color.decode("#FFFFFF");

    private static BufferedImage[] ICONS = null;
    static {
        try {
            ICONS = new BufferedImage[]{
                    ImageIO.read(new File("/mnt/personality_images/icons/star.png")),
                    ImageIO.read(new File("/mnt/personality_images/icons/star_border.png")),
                    ImageIO.read(new File("/mnt/personality_images/icons/shuffle.png")),
                    ImageIO.read(new File("/mnt/personality_images/icons/list.png")),
                    ImageIO.read(new File("/mnt/personality_images/icons/person.png")),
                    ImageIO.read(new File("/mnt/personality_images/icons/person_add.png")),
                    ImageIO.read(new File("/mnt/personality_images/icons/lock_outline.png")),
                    ImageIO.read(new File("/mnt/personality_images/icons/lock_open.png")),
                    ImageIO.read(new File("/mnt/personality_images/icons/mood.png")),
                    ImageIO.read(new File("/mnt/personality_images/icons/mood_bad.png"))
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
        int iconSize = ICONS[0].getHeight();
        try {
            File output = new File(pathname);
            graphic2D.setBackground(BACKGROUND_COLOR);
            graphic2D.setStroke(new BasicStroke(BORDER_WIDTH));

            for (int i = 0; i < PERSONALITY_COLORS.length; i++) {
                int rightIconX = (SIDE - iconSize)/2;
                int leftIconX = IMAGE_WIDTH - (rightIconX + iconSize);
                int iconY = (TOP + i * (BORDER_WIDTH + BAR_HEIGHT + BETWEEN)) + (BAR_HEIGHT - iconSize)/2;
                int barBorderX = SIDE;
                int barBorderY = TOP + i * (BORDER_WIDTH + BAR_HEIGHT + BETWEEN);
                int barX = SIDE + BORDER_WIDTH / 2;
                int barY = TOP + BORDER_WIDTH / 2 + i * (BORDER_WIDTH + BAR_HEIGHT + BETWEEN);
                int barWidth = (int) (BAR_WIDTH * (values[i] / 100));

                graphic2D.drawImage(ICONS[i*2],rightIconX , iconY, null);
                graphic2D.setPaint(BORDER_COLOR);
                graphic2D.drawRect(barBorderX, barBorderY, BAR_WIDTH + BORDER_WIDTH, BAR_HEIGHT + BORDER_WIDTH);
                graphic2D.setPaint(PERSONALITY_COLORS[i]);
                graphic2D.fillRect(barX, barY, barWidth, BAR_HEIGHT);
                graphic2D.drawImage(ICONS[i*2+1], leftIconX, iconY, null);
            }

            ImageIO.write(bufimage, "png", output);
        } catch (IOException log) {
            System.out.println(log);
        }
    }
}
