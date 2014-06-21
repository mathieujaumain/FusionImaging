package net.jaumainmathieu.fusion;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * Created by inv on 19/06/14.
 */
public class WaveletUtils {

    private static final float w0 = 0.5f;
    private static final float w1 = -0.5f;
    private static final float s0 = 0.5f;
    private static final float s1 = 0.5f;


    /**
     * 1D Haar Discreet Wavelet transform
     * @param data
     */
    public static void WaveletTransform(float[] data)
    {
        float[] temp = new float[data.length];

        int h = data.length >> 1;// == /2
        for (int i = 0; i < h; i++)
        {
            int k = (i << 1);
            temp[i] = data[k] * s0 + data[k + 1] * s1;
            temp[i + h] = data[k] * w0 + data[k + 1] * w1;
        }

        for (int i = 0; i < data.length; i++)
            data[i] = temp[i];
    }


    public static void InverseWaveletTransform(float[]data){
        float[] temp = new float[data.length];

        int h = data.length >> 1; // == /2
        for (int i = 0; i < h; i++)
        {
            int k = (i << 1);
            temp[k] = (data[i] * s0 + data[i + h] * w0) / w0;
            temp[k + 1] = (data[i] * s1 + data[i + h] * w1) / s0;
        }

        for (int i = 0; i < data.length; i++)
            data[i] = temp[i];
    }


    /**
     * 2D Haar Discreet Wavelet Transform
     * @param data
     * @param iterations
     */
    public static void WaveletTransform(float[][] data, int iterations)
    {
        int rows = data.length;
        int cols = data[0].length;

        float[] row = new float[cols];
        float[] col = new float[rows];

        for (int k = 0; k < iterations; k++)
        {
            for (int i = 0; i < rows; i++)
            {
                System.arraycopy(data[i], 0,row,0, row.length);

                WaveletTransform(row);

                System.arraycopy(row, 0,data[i],0, row.length);

            }

            for (int j = 0; j < cols; j++)
            {
                for (int i = 0; i < col.length; i++)
                    col[i] = data[i][j];

                WaveletTransform(col);

                for (int i = 0; i < col.length; i++)
                    data[i][j] = col[i];
            }
        }
    }

    public static void InverseWaveletTransform(float[][] data, int iterations) {
        int rows = data.length;
        int cols = data[0].length;

        float[] row = new float[cols];
        float[] col = new float[rows];

        for (int l = 0; l < iterations; l++)
        {
            for (int j = 0; j < cols; j++)
            {
                for (int i = 0; i < col.length; i++)
                    col[i] = data[i][j];

                InverseWaveletTransform(col);

                for (int i = 0; i < col.length; i++)
                    data[i][j] = col[i];
            }

            for (int i = 0; i < rows; i++)
            {

                System.arraycopy(data[i], 0,row,0, row.length);

                InverseWaveletTransform(row);

                System.arraycopy(row, 0,data[i],0, row.length);
            }
        }
    }

    public static float ScaleValue(float fromMin, float fromMax, float toMin, float toMax, float x)
    {
        if (fromMax - fromMin == 0) return 0;
        float value = (toMax - toMin) * (x - fromMin) / (fromMax - fromMin) + toMin;
        if (value > toMax)
        {
            value = toMax;
        }
        if (value < toMin)
        {
            value = toMin;
        }
        return value;
    }


    public static void ApplyDWT(BufferedImage outImage, int iterations)
    {

        int w = outImage.getWidth();
        int h = outImage.getHeight();

        float[][] Red = new float[w][h];
        float[][] Green = new float[w][h];
        float[][] Blue = new float[w][h];


        Raster raster = outImage.getData();
        int[] color = new int[3];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                raster.getPixel(i,j, color);
                Red[i][j] = ScaleValue(0, 255, -1, 1, color[0]);
                Green[i][j] = ScaleValue(0, 255, -1, 1, color[1]);
                Blue[i][j] = ScaleValue(0, 255, -1, 1, color[2]);
            }
        }

        WaveletTransform(Red, iterations);
        WaveletTransform(Green, iterations);
        WaveletTransform(Blue, iterations);

        Color c;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                c = new Color((int)ScaleValue(-1, 1, 0, 255, Red[i][j]),
                        (int)ScaleValue(-1, 1, 0, 255, Green[i][j]),
                        (int)ScaleValue(-1, 1, 0, 255, Blue[i][j]));

                outImage.setRGB(i, j, c.getRGB());
            }
        }
    }


    public static void ApplyDWTonHSB(float[][][] data, int iterations, int width, int height) {


        float[][] Hue = new float[width][height];
        float[][] Saturation = new float[width][height];
        float[][] Bright = new float[width][height];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                Hue[i][j] = ScaleValue(0, 360, 0, 1, data[i][j][0]);
                Saturation[i][j] = data[i][j][1];
                Bright[i][j] = data[i][j][2];
            }
        }



        WaveletTransform(Hue, iterations);
        WaveletTransform(Saturation, iterations);
        WaveletTransform(Bright, iterations);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                data[i][j][0] = ScaleValue(0, 1, 0, 360, Hue[i][j]);
                data[i][j][1] = Saturation[i][j];
                data[i][j][2] = Bright[i][j];

            }
        }
    }

    public static void ApplyInverseDWT(BufferedImage outImage, int iterations) {

        int w = outImage.getWidth();
        int h = outImage.getHeight();

        float[][] Red = new float[w][h];
        float[][] Green = new float[w][h];
        float[][] Blue = new float[w][h];


        Raster raster = outImage.getData();
        int[] color = new int[3];
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                raster.getPixel(i,j, color);
                Red[i][j] = ScaleValue(0, 255, -1, 1, color[0]);
                Green[i][j] = ScaleValue(0, 255, -1, 1, color[1]);
                Blue[i][j] = ScaleValue(0, 255, -1, 1, color[2]);
            }
        }

        InverseWaveletTransform(Red, iterations);
        InverseWaveletTransform(Green, iterations);
        InverseWaveletTransform(Blue, iterations);

        Color c;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                c = new Color((int)ScaleValue(-1, 1, 0, 255, Red[i][j]),
                        (int)ScaleValue(-1, 1, 0, 255, Green[i][j]),
                        (int)ScaleValue(-1, 1, 0, 255, Blue[i][j]));

                outImage.setRGB(i, j, c.getRGB());
            }
        }
    }

    public static void ApplyInverseDWTonHSB(float[][][] data, int iterations, int width, int height){


        float[][] Hue = new float[width][height];
        float[][] Saturation = new float[width][height];
        float[][] Bright = new float[width][height];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                Hue[i][j] = ScaleValue(0, 360, 0, 1, data[i][j][0]);
                Saturation[i][j] = data[i][j][1];
                Bright[i][j] = data[i][j][2];
            }
        }



        InverseWaveletTransform(Hue, iterations);
        InverseWaveletTransform(Saturation, iterations);
        InverseWaveletTransform(Bright, iterations);

        Color c;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                data[i][j][0] = ScaleValue(0, 1, 0, 360, Hue[i][j]);
                data[i][j][1] = Saturation[i][j];
                data[i][j][2] = Bright[i][j];

            }
        }
    }
}
