import static blazing.chain.LZSEncoding.*;

/**
 * Basic test.
 * Created by Tommy Ettinger on 9/13/2016.
 */
public class LZSEncodingTest {
    public static void main(String[] args) {
        String input, compressed;
        input = "hello1hello2hello3hello4hello5hello6hello7hello8hello9helloAhelloBhelloChelloDhelloEhelloF";
        System.out.println(input + "    with length " + input.length() + " (uncompressed)");
        compressed = compress(input);
        System.out.println(decompress(compressed) + "    with length " + compressed.length() + " (normal compression)");
        compressed = compressToBase64(input);
        System.out.println(decompressFromBase64(compressed) + "    with length " + compressed.length() + " (Base64 compression)");
        compressed = compressToUTF16(input);
        System.out.println(decompressFromUTF16(compressed) + "    with length " + compressed.length() + " (UTF-16 compression)");
        compressed = compressToEncodedURIComponent(input);
        System.out.println(decompressFromEncodedURIComponent(compressed) + "    with length " + compressed.length() + " (URI Component compression)");
    }

}
