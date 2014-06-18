package net.jaumainmathieu.fusion.methods;

import net.jaumainmathieu.fusion.IImageFusion;
import net.jaumainmathieu.fusion.ImagingUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Date;

/**
 * Created by inv on 18/06/14.
 */
public class BroveyFusion implements IImageFusion {
    @Override
    public BufferedImage Fuse(BufferedImage msImage, BufferedImage panImage) {

        int w = msImage.getWidth(null);
        int h = msImage.getHeight(null);

        BufferedImage resultImage = new BufferedImage(w,
                h,BufferedImage.TYPE_INT_RGB);
        Raster rasterMs = msImage.getData();
        panImage = ImagingUtils.toGreyScale(panImage);
        Raster rasterPan = panImage.getData();

        Color color = null;

        Date now = new Date();
        for(int j = 0; j < h; j++) {

            for(int i = 0; i < w; i++){

                int[] pixelMs = new int[3];
                int[] pixelPan = new int[3];
                rasterMs.getPixel(i,j, pixelMs);
                rasterPan.getPixel(i,j, pixelPan);
                int[] pixelResult = new int[3];

                float sum = pixelMs[0] + pixelMs[1] + pixelMs[2];


                int pan = pixelPan[0] / 255;

                pixelResult[0] = (int) (pixelMs[0] / sum * pan * 255);
                pixelResult[1] = (int) (pixelMs[1] / sum * pan * 255);
                pixelResult[2] = (int) (pixelMs[2] / sum * pan * 255);
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
