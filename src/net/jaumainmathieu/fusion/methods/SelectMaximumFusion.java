package net.jaumainmathieu.fusion.methods;

import net.jaumainmathieu.fusion.IImageFusion;
import net.jaumainmathieu.fusion.ImagingUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Date;

public class SelectMaximumFusion implements IImageFusion {


    @Override
    public BufferedImage Fuse(BufferedImage msImage, BufferedImage panImage) {

        int w = msImage.getWidth(null);
        int h = msImage.getHeight(null);
        Raster rasterMs = msImage.getData();
        Raster rasterPan = panImage.getData();

        BufferedImage msGreyImage = new BufferedImage(w,
                h,BufferedImage.TYPE_INT_RGB);
        msGreyImage.setData(rasterMs);
        ImagingUtils.toGreyScale(msGreyImage);
        Raster greyMs = msGreyImage.getData();

        BufferedImage resultImage = new BufferedImage(w,
                h,BufferedImage.TYPE_INT_RGB);

        resultImage.setData(rasterMs);

        Date now = new Date();
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++){

                int[] pixelGreyMs = new int[3];
                int[] pixelPan = new int[3];

                greyMs.getPixel(i, j, pixelGreyMs);
                rasterPan.getPixel(i, j, pixelPan);
                Color color;

                if(pixelGreyMs[0] >= pixelPan[0]){
                    int[] pixelMs = new int[3];
                    rasterMs.getPixel(i, j, pixelMs);

                    color = new Color(pixelMs[0], pixelMs[1], pixelMs[2]);

                } else {
                    color = new Color(pixelPan[0], pixelPan[1], pixelPan[2]);
                }


                resultImage.setRGB(i, j, color.getRGB());

            }
        }

        Date newNow = new Date();

        System.out.println("Time Taken = " + (newNow.getTime() - now.getTime()));

        return resultImage;
    }
}
