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
       // PHash image1 = new PHash("https://scopely.widen.net/content/j5p955e5g1");
       PHash image1 = new PHash(Path.of("Images/TestImages/White.png"));

        String str1= image1.DCT();
        System.out.println("Phash  " +str1);
        System.out.println("Phash  " +str1.length());
        System.out.println(image1.getHashHex());
        System.out.println(image1.getHashHex().length());
       // System.out.println("Phash binary length : " +str1.length());

        PHash image2 = new PHash(Path.of("Images/TestImages/Black.png"));
        //PHash image2 = new PHash("https://www.facebook.com/ads/image/?d=AQJAiEmGn3081cz4k-Ux96aDt7WgUkZrmNcscvryGdte-hdvQMlXiDSJocj_5NTv5FkPbMjo9Mvz6EEv-wQUicHEM_tbOlQOD9Gnl9Vp8hr3E0srnnLD2dNtSMQehFq9T5gfo6m75I1BGoUq-vlxsw-t");
        //BufferedImage imageURLReduced = image2.resize(imageURL.getImage(),32,32);
        //BufferedImage greyImageURL = image2.changeColor(imageURLReduced);
        String str2= image2.DCT();
        System.out.println("Phash : " +str2);
        System.out.println("Phash  " +str2.length());
        System.out.println(image2.getHashHex().length());
        System.out.println(image2.getHashHex());
        //System.out.println("Phash binary length : " +str1.length());
        int distance = image1.hammingDist(str1,str2);

        System.out.println("Hamming distance " +distance);



    }
}
