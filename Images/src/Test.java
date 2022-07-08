import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

    public static void main(String args[]) throws IOException  //static method
    {
        //PHash image1 = new PHash("https://scopely.widen.net/content/exv3fkkeh0 ");
       PHash image1 = new PHash(Path.of("Images/TestImages/Test2.jpg"));

        String str1= image1.DCT();
        System.out.println("Phash  " +str1);
        System.out.println(image1.getHashHex());
       // System.out.println("Phash binary length : " +str1.length());

        PHash image2 = new PHash(Path.of("Images/TestImages/F3-2.png"));
        //PHash image2 = new PHash("https://www.facebook.com/ads/image/?d=AQLtvRJl8OP_7csLtC30M9_Uz6UeL02zfAFvbpXOCIw3JSo[…]mSjaCCU4_YFOcCMHkCnVnitVn9BNsCxnm-uVXldyCiV4yUy2II4zvvcgnm-C");
        //BufferedImage imageURLReduced = image2.resize(imageURL.getImage(),32,32);
        //BufferedImage greyImageURL = image2.changeColor(imageURLReduced);
        String str2= image2.DCT();
        System.out.println("Phash : " +str2);
        //System.out.println("Phash binary length : " +str1.length());
        int distance = image1.hammingDist(str1,str2);

        System.out.println("Hamming distance " +distance);



    }
}
