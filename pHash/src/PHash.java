import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PHash {

    //Define attributes of the class
    private BufferedImage image = null;
    private String image_path = " ";
    private int hash_size = 8;
    private int highfreq_factor = 4;

    //Constructor
    public PHash(String image_path , int hash_size, int highfreq_factor) throws IOException {
        this.image_path= image_path;
        this.hash_size= hash_size;
        this.highfreq_factor = highfreq_factor;
        try {
            this.image = ImageIO.read(new File(image_path));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //
    public BufferedImage getImage(){
        return this.image;
    }

    //Reduce size of the image --> method from https://www.baeldung.com/java-resize-image

    public BufferedImage reduceSize (BufferedImage image , int witdh , int height){

        BufferedImage resizedImage = new BufferedImage(witdh,height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, witdh, height, null);
        graphics2D.dispose();
        return resizedImage;
    }


    // Change the RGB components of the image to a gray scale
    public File changeColor (BufferedImage image) throws  IOException{
        File grey_image = null;
        for(int j=0 ; j< image.getHeight(); j++){
            for (int i = 0; i< image.getWidth() ; i++){

                //Get pixels values and extract its RGB values
                int pixel = image.getRGB(i,j); // i == x ; j == y
                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                //Find average and set new pixel value

                int avg  = (red + blue + green) / 3;
                pixel = (alpha <<24) | (avg << 16) | (avg << 8) | avg;
                image.setRGB(i , j , pixel);

            }
        }

        //Write the grey image

        grey_image = new File("TestImages/GreyImage.jpeg");
        try {
            ImageIO.write(image,"jpg",grey_image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return grey_image;

    }

     //Apply the discrete cosine transform
    public void DCT (BufferedImage image){


    }













}
