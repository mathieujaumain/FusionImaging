package net.jaumainmathieu.fusion.methods;

import net.jaumainmathieu.fusion.IImageFusion;
import net.jaumainmathieu.fusion.ImagingUtils;
import net.jaumainmathieu.fusion.WaveletUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Date;

/**
 * Created by inv on 20/06/14.
 */
public class DWT2Fusion implements IImageFusion {
    @Override
    public BufferedImage Fuse(BufferedImage msImage, BufferedImage panImage) {
        int w = panImage.getWidth(null);
        int h = panImage.getHeight(null);

        int newW = (int) (w * 0.25f);
        int newH = (int) (h * 0.25f);

        WaveletUtils.ApplyDWT(panImage, 2);
        BufferedImage resizedMultichro = msImage;

        if(msImage.getHeight() == panImage.getHeight() * 0.5f
                && msImage.getWidth() == panImage.getWidth() * 0.5f)
            resizedMultichro = ImagingUtils.resizeImage(msImage, (int)(panImage.getHeight() * 0.5f),
                    (int)(panImage.getWidth() * 0.5f));

        //WaveletUtils.ApplyDWT();

        BufferedImage resultImage = new BufferedImage(w,
                h,BufferedImage.TYPE_INT_RGB);
        Raster raster1 = resizedMultichro.getData();


        Color color;
        Raster raster2 = panImage.getData();
        resultImage.setData(raster2);
        Date now = new Date();
        for(int j = 0; j < newH; j++) {

            for(int i = 0; i < newW; i++){

                int[] pixel1 = new int[3];

                raster1.getPixel(i,j, pixel1);

                int[] pixelResult = new int[3];
                pixelResult[0] = pixel1[0];
                pixelResult[1] = pixel1[1];
                pixelResult[2] = pixel1[2];

                color = new Color(pixelResult[0], pixelResult[1], pixelResult[2]);
                //Finally set the image's new pixel
                resultImage.setRGB(i, j, color.getRGB());

            }
        }
        WaveletUtils.ApplyInverseDWT(resultImage, 2);

        Date newNow = new Date();

        System.out.println("Time Taken = " + (newNow.getTime() - now.getTime()));

        return resultImage;
    }
}
