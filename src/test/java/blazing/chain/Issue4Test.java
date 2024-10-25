package blazing.chain;

public class Issue4Test {
    public static void main(String[] args) {
        System.out.println("START:");
        String dec1 = "[{\"f\":\"crb:language\",\"v\":\"Englids\",\"b\":\"-\"}]";
        System.out.println(dec1);
        System.out.println("ENCODING:");
        String enc1 = LZSEncoding.compressToEncodedURIComponent(dec1);
        System.out.println(enc1);
        System.out.println("EVERY ENCODED CHAR:");
        for (int i = 0; i < enc1.length(); i++) {
            System.out.print((int)enc1.charAt(i) + " ");
        }
        System.out.println();
        System.out.println("DECODING:");
        String dec2 = LZSEncoding.decompressFromEncodedURIComponent(enc1);
        System.out.println(dec2);
    }
}
