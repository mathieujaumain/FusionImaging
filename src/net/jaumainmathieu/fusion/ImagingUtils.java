package net.jaumainmathieu.fusion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * Created by inv on 18/06/14.
 */
public class ImagingUtils {

    public static BufferedImage toGreyScale(BufferedImage image){

        Raster raster = image.getData();
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage resultImage = new BufferedImage(w,
                h,BufferedImage.TYPE_INT_RGB);

        for(int j = 0; j < h; j++) {

            for(int i = 0; i < w; i++){

                int[] pixel = new int[3];
                raster.getPixel(i,j, pixel);
                int grey = toGreyValue(pixel);
                Color greyColor = new Color(grey, grey, grey);

                resultImage.setRGB(i, j, greyColor.getRGB());
            }
        }
        return  resultImage;
    }

    public static  int toGreyValue(int[] rgb) {
        return (int) (0.30 * rgb[0] + 0.59 * rgb[1] + 0.11 * rgb[2]);
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight){

        BufferedImage resizedImage = new BufferedImage(newWidth , newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resizedImage;
    }


    public static float[] ConvertRGB2IHS(int[] RGB){
        if(RGB.length != 3){
            throw new IllegalArgumentException("Input isn't RGB");
        }

        // see "The IHS Transformations Based Image Fusion",
        // Firouz Abdullah Al-Wassai, N.V. Kalyankar, Ali A. Al-Zuky

        return  Color.RGBtoHSB(RGB[0], RGB[1], RGB[2], null);
    }

    public static int ConvertIHS2RGB(float[] IHS){
        if(IHS.length != 3){
            throw new IllegalArgumentException("Input isn't IHS");
        }

        // see "The IHS Transformations Based Image Fusion",
        // Firouz Abdullah Al-Wassai, N.V. Kalyankar, Ali A. Al-Zuky


        //Use HSV definition cause standard IHS conversion isn't reversible
        return Color.HSBtoRGB(IHS[0], IHS[1], IHS[2]);
    }


}
