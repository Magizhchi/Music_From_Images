import javax.swing.*;
import java.awt.image.Raster;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ConvertImageToSingleDimension {
    public static void main(String[] args) throws IOException, InterruptedException {
        List<Float> imageValues = new ArrayList<Float>();
        List<Double> imageValuesDouble = new ArrayList<>();
        String path = "";
        System.out.println("Please Choose a valid jpg file");
        JFileChooser fileChooser = new JFileChooser();
        if ((fileChooser.showOpenDialog(null)) == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            System.out.println("Unable to select the given File... Now Exiting");
            Thread.sleep(1000);
            System.exit(1);
        }

        System.out.println("Reading the input image....");
        ImageReader imageReader = new ImageReader();
        HilbertCurveGenerator hilbertUtil = new HilbertCurveGenerator();
        Interpolation interpolation = new Interpolation();

        Raster imageRaster = imageReader.getRasterDataForImage(imageReader.readImage(path));
        int boundValue = imageReader.getImageBounds(imageRaster);
        Hashtable paddingValues = imageReader.getXAndYPaddingValues(imageRaster);

        System.out.println("Running A Hilbert Curve and gathering the pixel values.... ");
        for (int i = 0; i < boundValue*boundValue ; i++) {
            ArrayList<Integer> pixelLocation = hilbertUtil.d2xy(boundValue,i);
            float pixelValue = imageReader.checkAndReturnPixelValue(pixelLocation.get(0),pixelLocation.get(1),imageRaster,paddingValues);
            if(pixelValue != -1)
                imageValues.add(pixelValue);
        }
        imageValues.stream()
                   .mapToDouble(n -> n.doubleValue())
                   .forEach(imageValuesDouble::add);

        System.out.println("Interpolation the input image to 800 points...");
        List<Double> interpolatedImageValues = interpolation.interpolateAllValues(imageValuesDouble,interpolation.getDataPointsForInterpolation(Double.valueOf(imageValues.size()),800));

        FileWriter fileWriter = new FileWriter("imageValues.csv");
        fileWriter.write(interpolatedImageValues.toString()
                                                .replace("[", "")
                                                .replace("]", "")
                                                .replace(", ",","));
        fileWriter.close();
//        MatlabProxyFactory matlabProxyFactory = new MatlabProxyFactory();
//        try {
//            MatlabProxy proxy = matlabProxyFactory.getProxy();
//            proxy.feval("RepeatEvery8");
//            proxy.disconnect();
//        } catch (MatlabConnectionException e) {
//            e.printStackTrace();
//        } catch (MatlabInvocationException e) {
//            e.printStackTrace();
//        }
        System.out.println("Image details have been succesfully stored in the file 'imageValues.csv'");
   }
}
