package blazing.chain;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Run with {@code java -jar Consumer.jar input.txt output.txt}, where input.txt is a text file to decompress and
 * output.txt is an optional file to write the decompressed contents to (if not given, this will only print the output).
 * <br>
 * Created by Tommy Ettinger on 1/8/2018.
 */
public class Consumer {
    public static void main(String... args) {
        String arg = "compressed.txt";
        if(args == null)
            args = new String[0];
        if (args.length > 0) {
            arg = args[0];
        }
        String outArg = null;
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
        try {
            String text = new String(Files.readAllBytes(Paths.get(arg)), mode <= 1 ? "UTF-16" : "UTF-8");
            //System.out.println("COMPRESSED:\n");
            //System.out.println(text);
            switch (mode)
            {
                case 2:
                    text = LZSEncoding.decompressFromBase64(text);
                    break;
                case 3:
                    text = LZSEncoding.decompressFromEncodedURIComponent(text);
                    break;
                default:
                    text = LZSEncoding.decompressFromUTF16(text);
                    break;
            }
            System.out.println("\nDECOMPRESSED:\n");
            System.out.println(text);
            if (outArg != null) {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(outArg), "UTF-8"));
                writer.write(text);
            }
        }catch (Exception e) {
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
