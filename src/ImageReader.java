import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import static java.lang.Math.abs;

public class ImageReader extends JPanel {

    public BufferedImage readImage(String path){
        BufferedImage img = null;
        try {
            File imageFile = new File(path);
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public Raster getRasterDataForImage(BufferedImage img){
        Raster imageData = img.getData();
        return imageData;
    }

    public float getAmplitudeValueForPixel(int x, int y, Raster imageRaster) {
        float sum = 0;
        int numOfBands = imageRaster.getNumBands();
        for (int i = 0; i < numOfBands; i++) {
            sum += (imageRaster.getSampleFloat(x,y,i));
        }
        return abs(sum/3 - 255);
    }

    public float checkAndReturnPixelValue(int x, int y,Raster imageRaster,Hashtable paddingValues){
        int xPadding = (int) paddingValues.get("xPadding");
        int yPadding = (int) paddingValues.get("yPadding");
        return  (x - xPadding) < 0 ||
                (y - yPadding) < 0 ||
                 x >= xPadding + imageRaster.getWidth() ||
                 y >= yPadding + imageRaster.getHeight()
                 ? -1 : getAmplitudeValueForPixel(x - xPadding, y - yPadding, imageRaster);
    }

    public Hashtable getXAndYPaddingValues(Raster imageRaster){
        Hashtable paddingValues = new Hashtable();
        int maxValue = getImageBounds(imageRaster);
        paddingValues.put("xPadding",(maxValue - imageRaster.getWidth()) / 2);
        paddingValues.put("yPadding",(maxValue - imageRaster.getHeight()) /2);
        return paddingValues;
    }

    public int getImageBounds(Raster imageRaster){
        Properties imageSize = new Properties();
        int maxValue = Math.max(imageRaster.getHeight(),imageRaster.getWidth());
        int boundSize = 2;
        do{
            boundSize *=2;
        }while(boundSize < maxValue);
        return boundSize;
    }


//    public get

}
