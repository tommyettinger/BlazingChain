package blazing.chain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Run with {@code java -jar Producer.jar input.txt output.txt}, where input.txt is a text file to compress and
 * output.txt is the optional filename to write to (default is "compressed.txt").
 * <br>
 * Created by Tommy Ettinger on 1/8/2018.
 */
public class Producer {
    public static void main(String... args) {
        if (args != null && args.length > 0) {
            String outArg = "compressed.txt";
            if(args.length > 1)
                outArg = args[1];
            int mode = 1;
            if(args.length > 2)
            {
                if(args[2].contains("B") || args[2].contains("b")) //base64
                    mode = 2;
                else if(args[2].contains("UR") || args[2].contains("ur")) //uri encoding
                    mode = 3;
            }

            Writer writer = null;
            String text = "";
            try {
                text = new String(Files.readAllBytes(Paths.get(args[0])), "UTF-8");
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(outArg), mode <= 1 ? "UTF-16" : "UTF-8"));
                switch (mode)
                {
                    case 2:
                        text = LZSEncoding.compressToBase64(text);
                        break;
                    case 3:
                        text = LZSEncoding.compressToEncodedURIComponent(text);
                        break;
                    default:
                        text = LZSEncoding.compressToUTF16(text);
                        break;
                }
                writer.write(text);
                System.out.println("Wrote " + text.length() + " characters to " + outArg);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
