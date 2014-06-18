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
}
