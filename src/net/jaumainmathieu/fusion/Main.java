package net.jaumainmathieu.fusion;

import net.jaumainmathieu.fusion.methods.AverageFusion;
import net.jaumainmathieu.fusion.methods.BroveyFusion;
import net.jaumainmathieu.fusion.methods.MultiplicativeFusion;
import net.jaumainmathieu.fusion.methods.SelectMaximumFusion;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by inv on 17/06/14.
 */
public class Main {

    public static final int METHOD_AVERAGE = 0;
    public static final int METHOD_SELECT_MAX = 1;
    public static final int METHOD_DWT = 2;
    public static final int METHOD_MULTIPLICATIVE = 3;
    public static final int METHOD_BROVEY = 4;

    public static  void main(String[] args){

        BufferedImage multiSpectra = null;
        BufferedImage panchro = null;

        String image1Path;
        String image2Path;
        int fusionMethod;


        if(args.length != 3){

            System.out.println("Usage : ImageFusion <pathToImage1> <pathToImage2> <FusionMethod>\n" +
                               "Method :\n 0 = Average fusion");
            return;
        }

        image1Path = args[0];
        image2Path = args[1];
        fusionMethod = Integer.parseInt(args[2]);
        File file1 = new File(image1Path);
        File file2 = new File(image2Path);

        try {
            multiSpectra = ImageIO.read(file1);
            panchro = ImageIO.read(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        IImageFusion fusion;

        switch(fusionMethod){

            case METHOD_AVERAGE:
                fusion = new AverageFusion();
                break;

            case METHOD_SELECT_MAX:
                fusion = new SelectMaximumFusion();
                break;

            case METHOD_MULTIPLICATIVE:
                fusion = new MultiplicativeFusion();
                break;

            case METHOD_BROVEY:
                fusion = new BroveyFusion();
                break;

            default:
                fusion = null;
                break;
        }
        BufferedImage result = null;
        if(fusion != null){
            result = fusion.Fuse(multiSpectra, panchro);
        }
        /*
        BufferedImage result = ImagingUtils.toGreyScale(panchro);

        File out = new File("grey 1.png");
        try {
            ImageIO.write(result, "png", out);
        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }


        result = ImagingUtils.toGreyScale(result);

        out = new File("grey 2.png");
        try {
            ImageIO.write(result, "png", out);
        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }*/


        File out = new File("fused.png");
        try {
            ImageIO.write(result, "png", out);
        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }


    }
}
