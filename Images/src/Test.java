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
       // PHash image1 = new PHash("https://scopely.widen.net/content/exv3fkkeh0");
       PHash image1 = new PHash(Path.of("Images/Output/out305DE.png"));

       String str1= image1.DCT();
        System.out.println("Phash  " +str1);
        System.out.println("Phash  " +str1.length());
        System.out.println(image1.getHashHex());
        System.out.println(image1.getHashHex().length());
       // System.out.println("Phash binary length : " +str1.length());

        PHash image2 = new PHash(Path.of("Images/Output/out305IT.png"));
       // String url = "https://www.facebook.com/ads/image/?d=AQLHlGYHuMlyp_HjLGf150gOA3pfKMGA2WxBlBXzoCmUSZiJ6aayR1jo7Yu3Xae29e5g3ew6QXuU-LPatKUy15dLLT5pKbk3DFvU4iOj2wKwaQkIwW1-ouWsa6ZCM2PWbzK_CHYEcgpCZcMTrbDEzWoa";
        //PHash image2 = new PHash(url);
        //BufferedImage imageURLReduced = image2.resize(imageURL.getImage(),32,32);
        //BufferedImage greyImageURL = image2.changeColor(imageURLReduced);
        String str2= image2.DCT();
        System.out.println("Phash : " +str2);
        System.out.println("Phash  " +str2.length());
        System.out.println(image2.getHashHex().length());
        System.out.println(image2.getHashHex());
        //System.out.println("Phash binary length : " +str1.length());
        MathAux math = new MathAux();
        int distance = math.hammingDist(str1,str2);

        System.out.println("Hamming distance " +distance);



    }
}
