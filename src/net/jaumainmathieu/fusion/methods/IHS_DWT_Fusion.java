package net.jaumainmathieu.fusion.methods;

import net.jaumainmathieu.fusion.IImageFusion;
import net.jaumainmathieu.fusion.ImagingUtils;
import net.jaumainmathieu.fusion.WaveletUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Date;

/**
 * "Wavelet for Image Fusion"
    Shih-Gu Huang
 */
public class IHS_DWT_Fusion implements IImageFusion {


    @Override
    public BufferedImage Fuse(BufferedImage msImage, BufferedImage panImage) {
        int w = panImage.getWidth(null);
        int h = panImage.getHeight(null);

        int newW = (int) (w * 0.5f);
        int newH = (int) (h * 0.5f);

        ImagingUtils.toGreyScale(panImage);

        // CONVERT RGB INTO IHS SPACE
        Date now = new Date();
        Raster raster1 = msImage.getData();
        Raster raster2 = panImage.getData();

        float[][][] IhsData = new float[w][h][3];
        System.out.println("IHS conversion...");
        long index = 0;
        for(int j = 0; j < h; j++) {

            for(int i = 0; i < w; i++){

                int[] pixel = new int[3];

                msImage.getData().getPixel(i, j, pixel);


                // convert to IHS
                float[] ihsVal = ImagingUtils.ConvertRGB2IHS(pixel); //H S B
                IhsData[i][j][0] = ihsVal[0];
                IhsData[i][j][1] = ihsVal[1];
                IhsData[i][j][2] = ihsVal[2];

                index += 1;

            }
            System.out.println("pixel num = " + index);
        }

        System.out.println("DWT transform...");
        // DWT TRANSFORM
        WaveletUtils.ApplyDWT(panImage, 1);
        WaveletUtils.ApplyDWTonHSB(IhsData, 1, w, h);


        //PUT IHS APPROXIMATION INTO PAN DWT
        System.out.println("Subtitution...");
        float[][][] fuseIHSdata = new float[w][h][3];
        for(int j = 0; j < h; j++) {


            for(int i = 0; i < w; i++){
                if(i < newW && j < newH){
                    fuseIHSdata[i][j][0] = IhsData[i][j][0];
                    fuseIHSdata[i][j][1] = IhsData[i][j][1];
                    fuseIHSdata[i][j][2] = IhsData[i][j][2];
                } else {
                    int[] pixel = new int[3];

                    panImage.getData().getPixel(i, j, pixel);
                    float[] ihsPix = ImagingUtils.ConvertRGB2IHS(pixel);
                    fuseIHSdata[i][j][0] = ihsPix[0];
                    fuseIHSdata[i][j][1] = ihsPix[1];
                    fuseIHSdata[i][j][2] = ihsPix[2];
                }
            }
        }

        System.out.println("Inverse DWT transform...");
        // INVERSE DWT
        WaveletUtils.ApplyInverseDWTonHSB(fuseIHSdata, 1, w, h);


        // COME BACK TO RGB SPACE
        BufferedImage resultImage = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
        Color color;
        System.out.println("Returning to RBG space...");
        for(int j = 0; j < h; j++) {

            for(int i = 0; i < w; i++){

                color = Color.getHSBColor(fuseIHSdata[i][j][0], fuseIHSdata[i][j][1], fuseIHSdata[i][j][2] );
                resultImage.setRGB(i, j, color.getRGB());

            }
        }

        Date newNow = new Date();

        System.out.println("Time Taken = " + (newNow.getTime() - now.getTime()));

        return resultImage;
    }
}
