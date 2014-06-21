package net.jaumainmathieu.fusion.methods;

import net.jaumainmathieu.fusion.IImageFusion;
import net.jaumainmathieu.fusion.ImagingUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Date;

/**
 * Created by inv on 19/06/14.
 */
public class StandardIHSFusion implements IImageFusion {
    @Override
    public BufferedImage Fuse(BufferedImage msImage, BufferedImage panImage) {

        int w = msImage.getWidth(null);
        int h = msImage.getHeight(null);

        BufferedImage resultImage = new BufferedImage(w,
                h,BufferedImage.TYPE_INT_RGB);
        Raster raster1 = msImage.getData();
        resultImage.setData(raster1);
        ImagingUtils.toGreyScale(panImage);

        // Convert RGB en IHS (see "The IHS Transformations Based Image Fusion",
        // Firouz Abdullah Al-Wassai, N.V. Kalyankar, Ali A. Al-Zuky

        Color color;
        Raster raster2 = panImage.getData();
        Date now = new Date();
        for(int j = 0; j < h; j++) {

            for(int i = 0; i < w; i++){

                int[] pixel = new int[3];
                int[] panPix = new int[3];
                raster1.getPixel(i,j, pixel);
                raster2.getPixel(i,j, panPix);
                // convert to IHS
                float[] ihsVal = ImagingUtils.ConvertRGB2IHS(pixel);
                // Exchange the brightness value
                ihsVal[2] = panPix[0] / 255f;

                // Unconvert
                int colorConverted = ImagingUtils.ConvertIHS2RGB(ihsVal);

                color = new Color(colorConverted);
                resultImage.setRGB(i, j, color.getRGB());
            }
        }
        Date newNow = new Date();

        System.out.println("Time Taken = " + (newNow.getTime() - now.getTime()));

        return resultImage;

    }
}
