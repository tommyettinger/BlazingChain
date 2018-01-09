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
        args = new String[]{"pom.xml"};
        if (args != null && args.length > 0) {
            String outArg = "compressed.txt";
            if(args.length > 1)
                outArg = args[1];
            Writer writer = null;
            String text = "";
            try {
                text = new String(Files.readAllBytes(Paths.get(args[0])), "UTF-8");
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(outArg), "UTF-8"));
                text = LZSEncoding.compressToUTF16(text);
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
