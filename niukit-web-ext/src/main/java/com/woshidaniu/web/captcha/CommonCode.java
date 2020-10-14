package com.woshidaniu.web.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @desc 通用生成验证码的类
 * @author 438523432@qq.com(740)
 */
public class CommonCode implements IAuthCode{
	
    @Override
    public void code(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            int width = 65, height = 19;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            Random random = new Random();
            g.setColor(new Color(255, 255, 255));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
            g.setColor(new Color(255, 255, 255));//--del
            for (int i = 0; i < 40; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(3);
                g.setColor(new Color(255, 255, 255));//--del
                g.drawLine(x, y, x + xl, y + yl);
            }
            String sRand = "";
            for (int i = 0; i < 4; i++) {
                int j = (random.nextInt(35) + 48);
                if (j > 57) {
                    j += 8;
                }
                if (j == 48) {
                    j += random.nextInt(7) + 1;
                }
                if (j == 49) {
                    j += random.nextInt(7) + 1;
                }
                if (j == 73) {
                    j += random.nextInt(7) + 1;
                }
                if (j == 79) {
                    j += random.nextInt(7) + 1;
                }
                if (j == 111) {
                    j += random.nextInt(7) + 1;
                }

                String rand = String.valueOf((char) j);
                sRand += rand;
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
                g.drawString(rand, 13 * i + 6, 16);
            }
            g.dispose();
            session.setAttribute("yzm", sRand);
//            setAttibute("yzm",sRand);

            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建验证码
     */
    public String code(String codeName,HttpServletRequest request, HttpServletResponse response) {
    	String yzm="";
        try {
            HttpSession session = request.getSession();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            int width = 65, height = 19;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            Random random = new Random();
            g.setColor(new Color(255, 255, 255));
            g.fillRect(0, 0, width, height);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
            g.setColor(new Color(255, 255, 255));//--del
            for (int i = 0; i < 40; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(3);
                g.setColor(new Color(255, 255, 255));//--del
                g.drawLine(x, y, x + xl, y + yl);
            }
            String sRand = "";
            for (int i = 0; i < 4; i++) {
                int j = (random.nextInt(35) + 48);
                if (j > 57) {
                    j += 8;
                }
                if (j == 48) {
                    j += random.nextInt(7) + 1;
                }
                if (j == 49) {
                    j += random.nextInt(7) + 1;
                }
                if (j == 73) {
                    j += random.nextInt(7) + 1;
                }
                if (j == 79) {
                    j += random.nextInt(7) + 1;
                }
                if (j == 111) {
                    j += random.nextInt(7) + 1;
                }

                String rand = String.valueOf((char) j);
                sRand += rand;
                g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
                g.drawString(rand, 13 * i + 6, 16);
            }
            g.dispose();
            session.setAttribute(codeName, sRand);
//            setAttibute("yzm",sRand);

            ServletOutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return yzm;
    }
}
