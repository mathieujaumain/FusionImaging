package net.jaumainmathieu.fusion.methods;

import net.jaumainmathieu.fusion.IImageFusion;
import net.jaumainmathieu.fusion.ImagingUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Date;


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
                float[] pixelResult = new float[3];

                float sum = (pixelMs[0] + pixelMs[1] + pixelMs[2]) / 255;


                float pan = pixelPan[0] / 255;

                pixelResult[0] =  (pixelMs[0]/255 / sum * pan);
                pixelResult[1] =  (pixelMs[1]/255 / sum * pan);
                pixelResult[2] =  (pixelMs[2]/255 / sum * pan);

                try {
                    color = new Color(pixelResult[0], pixelResult[1], pixelResult[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Finally set the image's new pixel
                resultImage.setRGB(i, j, color.getRGB());

            }
        }
        Date newNow = new Date();

        System.out.println("Time Taken = " + (newNow.getTime() - now.getTime()));

        return resultImage;
    }
}
