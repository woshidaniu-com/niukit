package com.woshidaniu.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 图片处理工具类,主要用于生成缩略图
 *
 * @author 438523432@qq.com(740)
 */
public final class ImageTools {

    /**
     * 将指定路径下的源图片按指定尺寸生成同原图片格式一样的缩略图到指定的目录下
     * @param srcFileName 原图片名
     * @param srcFileName 生成缩略图的名称
     * @param path 源图片或生成的缩略畋所在的目录
     * @param fW          生成图片宽度
     * @param fH          生成图片高度
     */
    public static void saveMiniature(String srcFileName, String saveFileName, String path, int fW, int fH) throws IOException {
        String imgType = saveFileName.substring(saveFileName.lastIndexOf(".") + 1).toUpperCase();
        saveMiniature(new File(path, srcFileName), new File(path, saveFileName), imgType, fW, fH);
    }

    /**
     * 按指定尺寸生成指定图片格式的缩略图输出到磁盘
     * @param srcFile  原图片对象
     * @param saveFile 生成缩略图对象
     * @param imgType  生成图片类型
     * @param maxW     生成图片宽度
     * @param maxH     生成图片高度
     */
    public static void saveMiniature(File srcFile, File saveFile, String imgType, int maxW, int maxH) throws IOException {
        ImageIO.write(createMiniature(srcFile, maxW, maxH, false), imgType, saveFile);
    }

    /**
     * @param srcFile  原图片地址,完整路径
     * @param saveFile 生成缩略图地址完整路径
     * @param fW       生成图片宽度
     * @param fH       生成图片高度
     */
    public static void saveMiniature(String srcFile, String saveFile, int fW, int fH) throws IOException {
        String imgType = saveFile.substring(saveFile.lastIndexOf(".") + 1).toUpperCase();
        ImageIO.write(createMiniature(new File(srcFile), fW, fH, false), imgType, new File(saveFile));
    }

    /**
     * @param srcFile 要缩放图像的路径
     * @param maxW    要缩放宽度
     * @param maxH    要缩放高度
     * @param clip    是否剪切
     * @return 缩放后的图片
     * @throws IOException
     */
    public static BufferedImage createMiniature(File srcFile, int maxW, int maxH, boolean clip) throws IOException {
        return ImageResizer.createMiniature(srcFile, maxW, maxH, clip);
    }


    /**
     * 将源文件以指定的尺寸转换后转换成BufferedImage返回
     * @param srcFile 要缩放图像的路径
     * @param maxW    要缩放宽度
     * @param maxH    要缩放高度
     * @return 缩放后的图片
     * @throws IOException
     */
    public static BufferedImage createMiniature(File srcFile, int maxW, int maxH) throws IOException {
        return ImageScale.createMiniature(srcFile, maxW, maxH);
    }

    private static final class ImageResizer {

        /**
         * @param srcFile 要缩放图像的路径
         * @param maxW    要缩放宽度
         * @param maxH    要缩放高度
         * @param clip    是否剪切
         * @return 缩放后的图片
         * @throws IOException
         */
        public static BufferedImage createMiniature(File srcFile, int maxW, int maxH, boolean clip) throws IOException {
            BufferedImage srcImage = ImageIO.read(srcFile);
            if (maxW > 0 || maxH > 0) {
                int sw = srcImage.getWidth(), sh = srcImage.getHeight();// 原图的大小
                if (sw > maxW || sh > maxH) {//如果原图像的大小小于要缩放的图像大小，直接将要缩放的图像复制过去
                    srcImage = resize(srcImage, sw, sh, maxW, maxH);
                    if (clip) srcImage = clipImage(srcImage, maxW, maxH);
                }
            }
            return srcImage;
        }

        public static BufferedImage clipImage(BufferedImage srcImage, int maxW, int maxH) throws IOException {
            int sw = srcImage.getWidth(), sh = srcImage.getHeight();
            if (sw == maxW) {//如果缩放后的图像和要求的图像宽度一样，就对缩放的图像的高度进行截取
                srcImage = subImage(srcImage, new Rectangle(0, sh / 2 - maxH / 2, maxW, maxH));
            } else if (sh == maxH) {//否则如果是缩放后的图像的高度和要求的图像高度一样，就对缩放后的图像的宽度进行截取
                srcImage = subImage(srcImage, new Rectangle(sw / 2 - maxW / 2, 0, maxW, maxH));
            }
            return srcImage;
        }

        /**
         * 实现图像的等比缩放
         *
         * @param srcImage 原图片
         * @param maxW     目标宽
         * @param maxH     目标高
         * @return 缩放后的图片
         */
        private static BufferedImage resize(BufferedImage srcImage, int sw, int sh, int maxW, int maxH) {
            double sx = (double) maxW / sw, sy = (double) maxH / sh;
            if (sx < sy) {// 这里想实现在targetW，targetH范围内实现等比缩放
                maxW = (int) (sy * sw);
            } else {
                maxH = (int) (sx * sh);
            }
            BufferedImage targetImage = null;
            if (srcImage.getType() == BufferedImage.TYPE_CUSTOM) {
                ColorModel cm = srcImage.getColorModel();
                targetImage = new BufferedImage(cm,
                        cm.createCompatibleWritableRaster(maxW, maxH),
                        cm.isAlphaPremultiplied(), null);
            } else {
                targetImage = new BufferedImage(maxW, maxH, srcImage.getType());
            }
            Graphics2D g = targetImage.createGraphics();
            g.drawImage(srcImage.getScaledInstance(maxW, maxH, Image.SCALE_SMOOTH)
                    , 0, 0, maxW, maxH, null);
            g.dispose();
            return targetImage;
        }

        /**
         * 实现缩放后的截图
         *
         * @param image          缩放后的图像
         * @param subImageBounds 要截取的子图的范围
         * @throws IOException
         */
        private static BufferedImage subImage(BufferedImage image, Rectangle subImageBounds) throws IOException {
            if (subImageBounds.x < 0 || subImageBounds.y < 0
                    || subImageBounds.width - subImageBounds.x > image.getWidth()
                    || subImageBounds.height - subImageBounds.y > image.getHeight()) {
                System.err.println("Bad   subimage   bounds");
                return image;
            }
            return image.getSubimage(subImageBounds.x, subImageBounds.y, subImageBounds.width, subImageBounds.height);
        }
    }

    private static final class ImageScale {
        public static BufferedImage createMiniature(File srcFile, int maxW, int maxH) throws IOException {
            BufferedImage srcImage = ImageIO.read(srcFile);
            int sW = srcImage.getWidth();
            int sH = srcImage.getHeight();
            int tW = sW;
            int tH = sH;
            if (sW > 0 && sH > 0) {
                if (sW / sH >= maxW / maxH) {
                    if (sW > maxW) {
                        tW = maxW;
                        tH = (sH * maxW) / sW;
                    }
                } else {
                    if (sH > maxH) {
                        tH = maxH;
                        tW = (sW * maxH) / sH;
                    }
                }
                return imageZoomOut(srcImage, tW, tH, sW, sH);
            }
            return srcImage;
        }

        private static BufferedImage imageZoomOut(BufferedImage srcImage, int tW, int tH, int sW, int sH) {
            if (DetermineResultSize(tW, tH, sW, sH)) return srcImage;
            int nHalfDots = (int) ((double) sW * support / (double) tW);
            int nDots = nHalfDots * 2 + 1;
            double[][] arrays = CalContrib(sW, tW, nDots, nHalfDots);
            BufferedImage pbOut = HorizontalFiltering(srcImage, tW, nHalfDots, nDots, arrays[0], arrays[2]);
            BufferedImage pbFinalOut = VerticalFiltering(pbOut, tH, nHalfDots, nDots, arrays[0], arrays[1], arrays[2]);
            return pbFinalOut;
        }

        /**
         * 决定图像尺寸
         */
        private static boolean DetermineResultSize(int w, int h, int sW, int sH) {
            double scaleH = (double) w / (double) sW;
            double scaleV = (double) h / (double) sH;
            return scaleH >= 1.0 && scaleV >= 1.0; //需要判断一下scaleH，scaleV，不做放大操作

        }

        private static double Lanczos(int i, int inWidth, int outWidth) {
            double x = (double) i * (double) outWidth / (double) inWidth;
            return Math.sin(x * Math.PI) / (x * Math.PI) * Math.sin(x * Math.PI / support)
                    / (x * Math.PI / support);

        }

        private static double[][] CalContrib(int sW, int tW, int nDots, int nHalfDots) {
            double[] contrib = new double[nDots];
            double[] normContrib = new double[nDots];
            double[] tmpContrib = new double[nDots];
            int center = nHalfDots;
            contrib[center] = 1.0;
            double weight = 0.0;
            for (int i = 1; i <= center; i++) {
                contrib[center + i] = Lanczos(i, sW, tW);
                weight += contrib[center + i];
            }

            for (int i = center - 1; i >= 0; i--) {
                contrib[i] = contrib[center * 2 - i];
            }
            weight = weight * 2 + 1.0;
            for (int i = 0; i <= center; i++) {
                normContrib[i] = contrib[i] / weight;
            }

            for (int i = center + 1; i < nDots; i++) {
                normContrib[i] = normContrib[center * 2 - i];
            }
            return new double[][]{normContrib, contrib, tmpContrib};
        }

        // 处理边缘
        private static void CalTempContrib(int start, int stop, double[] contrib, double[] tmpContrib) {
            double weight = 0;
            for (int i = start; i <= stop; i++) {
                weight += contrib[i];
            }
            for (int i = start; i <= stop; i++) {
                tmpContrib[i] = contrib[i] / weight;
            }
        }

        private static int GetRedValue(int rgbValue) {
            return (rgbValue & 0x00ff0000) >> 16;
        }

        private static int GetGreenValue(int rgbValue) {
            return (rgbValue & 0x0000ff00) >> 8;
        }

        private static int GetBlueValue(int rgbValue) {
            return rgbValue & 0x000000ff;
        }

        private static int ComRGB(int redValue, int greenValue, int blueValue) {
            return (redValue << 16) + (greenValue << 8) + blueValue;
        }

        // 行水平滤波
        private static int HorizontalFilter(BufferedImage bufImg, int startX, int stopX,
                                            int start, int stop, int y, double[] pContrib) {
            double valueRed = 0.0;
            double valueGreen = 0.0;
            double valueBlue = 0.0;
            int valueRGB = 0;
            for (int i = startX, j = start; i <= stopX; i++, j++) {
                valueRGB = bufImg.getRGB(i, y);
                valueRed += GetRedValue(valueRGB) * pContrib[j];
                valueGreen += GetGreenValue(valueRGB) * pContrib[j];
                valueBlue += GetBlueValue(valueRGB) * pContrib[j];
            }
            valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
                    Clip((int) valueBlue));
            return valueRGB;

        }

        // 图片水平滤波
        private static BufferedImage HorizontalFiltering(BufferedImage bufImage, int iOutW, int nHalfDots, int nDots, double[] normContrib, double[] tmpContrib) {
            int dwInW = bufImage.getWidth();
            int dwInH = bufImage.getHeight();
            int value = 0;
            BufferedImage pbOut = new BufferedImage(iOutW, dwInH,
                    BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < iOutW; x++) {
                int start;
                int X = (int) (((double) x) * ((double) dwInW) / ((double) iOutW) + 0.5);
                int y = 0;
                int startX = X - nHalfDots;
                if (startX < 0) {
                    startX = 0;
                    start = nHalfDots - X;
                } else {
                    start = 0;
                }
                int stop;
                int stopX = X + nHalfDots;
                if (stopX > (dwInW - 1)) {
                    stopX = dwInW - 1;
                    stop = nHalfDots + (dwInW - 1 - X);
                } else {
                    stop = nHalfDots * 2;
                }

                if (start > 0 || stop < nDots - 1) {
                    CalTempContrib(start, stop, normContrib, tmpContrib);
                    for (y = 0; y < dwInH; y++) {
                        value = HorizontalFilter(bufImage, startX, stopX, start,
                                stop, y, tmpContrib);
                        pbOut.setRGB(x, y, value);
                    }
                } else {
                    for (y = 0; y < dwInH; y++) {
                        value = HorizontalFilter(bufImage, startX, stopX, start,
                                stop, y, normContrib);
                        pbOut.setRGB(x, y, value);
                    }
                }
            }

            return pbOut;

        }

        private static int VerticalFilter(BufferedImage pbInImage, int startY, int stopY,
                                          int start, int stop, int x, double[] pContrib) {
            double valueRed = 0.0;
            double valueGreen = 0.0;
            double valueBlue = 0.0;
            int valueRGB = 0;
            for (int i = startY, j = start; i <= stopY; i++, j++) {
                valueRGB = pbInImage.getRGB(x, i);
                valueRed += GetRedValue(valueRGB) * pContrib[j];
                valueGreen += GetGreenValue(valueRGB) * pContrib[j];
                valueBlue += GetBlueValue(valueRGB) * pContrib[j];
            }

            valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
                    Clip((int) valueBlue));
            return valueRGB;

        }

        private static BufferedImage VerticalFiltering(BufferedImage pbImage, int iOutH, int nHalfDots, int nDots, double[] normContrib, double[] contrib, double[] tmpContrib) {
            int iW = pbImage.getWidth();
            int iH = pbImage.getHeight();
            int value = 0;
            BufferedImage pbOut = new BufferedImage(iW, iOutH,
                    BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < iOutH; y++) {
                int start;
                int Y = (int) (((double) y) * ((double) iH) / ((double) iOutH) + 0.5);
                int startY = Y - nHalfDots;
                if (startY < 0) {
                    startY = 0;
                    start = nHalfDots - Y;
                } else {
                    start = 0;
                }

                int stop;
                int stopY = Y + nHalfDots;
                if (stopY > (iH - 1)) {
                    stopY = iH - 1;
                    stop = nHalfDots + (iH - 1 - Y);
                } else {
                    stop = nHalfDots * 2;
                }

                if (start > 0 || stop < nDots - 1) {
                    CalTempContrib(start, stop, contrib, tmpContrib);
                    for (int x = 0; x < iW; x++) {
                        value = VerticalFilter(pbImage, startY, stopY, start, stop,
                                x, tmpContrib);
                        pbOut.setRGB(x, y, value);
                    }
                } else {
                    for (int x = 0; x < iW; x++) {
                        value = VerticalFilter(pbImage, startY, stopY, start, stop,
                                x, normContrib);
                        pbOut.setRGB(x, y, value);
                    }
                }
            }
            return pbOut;
        }

        private static int Clip(int x) {
            return x < 0 ? 0 : (x > 255 ? 255 : x);
        }

        private static final double support = 3.0d;
    }
}  