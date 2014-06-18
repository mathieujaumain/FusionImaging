package net.jaumainmathieu.fusion.methods;

import net.jaumainmathieu.fusion.IImageFusion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Date;

/**
 * Must be used for a panchromatic/multispectral fusion
 * (not a multispectral/multispectral fusion)
 */
public class MultiplicativeFusion implements IImageFusion {



    @Override
    public BufferedImage Fuse(BufferedImage msImage, BufferedImage panImage) {

        int w = msImage.getWidth(null);
        int h = msImage.getHeight(null);

        BufferedImage resultImage = new BufferedImage(w,
                h,BufferedImage.TYPE_INT_RGB);
        Raster raster1 = msImage.getData();
        resultImage.setData(raster1);

        Color color;
        Raster raster2 = panImage.getData();
        Date now = new Date();
        for(int j = 0; j < h; j++) {

            for(int i = 0; i < w; i++){

                int[] pixel1 = new int[3];
                int[] pixel2 = new int[3];
                raster1.getPixel(i,j, pixel1);
                raster2.getPixel(i,j, pixel2);
                int[] pixelResult = new int[3];
                pixelResult[0] = (pixel1[0] * pixel2[0])/256;
                pixelResult[1] = (pixel1[1] * pixel2[1])/256;
                pixelResult[2] = (pixel1[2] * pixel2[2])/256;

                color = new Color(pixelResult[0], pixelResult[1], pixelResult[2]);
                //Finally set the image's new pixel
                resultImage.setRGB(i, j, color.getRGB());

            }
        }
        Date newNow = new Date();

        System.out.println("Time Taken = " + (newNow.getTime() - now.getTime()));

        return resultImage;
    }
}
