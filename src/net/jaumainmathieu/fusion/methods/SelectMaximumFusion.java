package net.jaumainmathieu.fusion.methods;

import net.jaumainmathieu.fusion.IImageFusion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Date;

/**
 * Created by inv on 17/06/14.
 */
public class SelectMaximumFusion implements IImageFusion {


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

                int max = Math.max(msImage.getRGB(i, j), panImage.getRGB(i, j));
                resultImage.setRGB(i, j, max);

            }
        }

        Date newNow = new Date();

        System.out.println("Time Taken = " + (newNow.getTime() - now.getTime()));

        return resultImage;
    }
}
