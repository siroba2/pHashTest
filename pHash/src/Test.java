import java.io.IOException;

public class Test {

    public static void main(String args[]) throws IOException  //static method
    {
        PHash pHash = new PHash("TestImages/Cat2.jpeg", 8 ,4 );
        pHash.reduceSize(pHash.getImage(),8,8);
        pHash.changeColor(pHash.getImage());
    }
}
