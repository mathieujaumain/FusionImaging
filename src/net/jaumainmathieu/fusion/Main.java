package net.jaumainmathieu.fusion;

import net.jaumainmathieu.fusion.methods.*;

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
    public static final int METHOD_STANDARD_IHS = 5;
    public static final int METHOD_IHS_DWT = 6;
    public static final int METHOD_DWT_2  = 7;

    public static  void main(String[] args){

        BufferedImage multiSpectra = null;
        BufferedImage panchro = null;

        String image1Path;
        String image2Path;
        String resultPath;
        int fusionMethod;


        if(args.length != 4){

            System.out.println("Usage : FusionProgram.jar <pathToMultiChroma> <pathToPanChroma> <OutputImage> <FusionMethod>\n" +
                                "Methods : \n"+
                                " 0 = Average method\n" +
                                " 1 = Select Max method\n" +
                                " 2 = Discreet Wavelet Transform + averaging method\n" +
                                " 3 = Multiplicative method\n" +
                                " 4 = Brovey method\n" +
                                " 5 = Standard IHS method\n" +
                                " 6 = IHS + DWT method\n");
            return;
        }

        image1Path = args[0];
        image2Path = args[1];
        resultPath = args[2];
        fusionMethod = Integer.parseInt(args[3]);
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

            case METHOD_DWT:
                fusion = new DWTFusion();
                break;

            case METHOD_BROVEY:
                fusion = new BroveyFusion();
                break;

            case METHOD_STANDARD_IHS:
                fusion = new StandardIHSFusion();
                break;

            case METHOD_IHS_DWT:
                fusion = new IHS_DWT_Fusion();
                break;

            case METHOD_DWT_2:
                fusion = new DWT2Fusion();
                break;

            default:
                fusion = null;
                break;
        }
        BufferedImage result = null;
        if(fusion != null){
            result = fusion.Fuse(multiSpectra, panchro);
        }

        File out = new File(resultPath);
        try {
            ImageIO.write(result, "png", out);
        } catch (IOException e) {
            System.out.println("Error writing file.");
            e.printStackTrace();
        }
    }
}
