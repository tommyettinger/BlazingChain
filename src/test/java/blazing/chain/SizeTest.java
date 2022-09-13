package blazing.chain;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class SizeTest {
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        Object[] testData = new Object[]{new BigDecimal("2345.287272"),
                Arrays.asList(new BigDecimal("2345.287272"), new BigDecimal("23452.87272"),
                        new BigDecimal("234528.7272"), new BigDecimal("2345287.272")),
                "Arrays.asList(new BigDecimal(\"2345.287272\"), new BigDecimal(\"23452.87272\"), new BigDecimal(\"234528.7272\"), new BigDecimal(\"2345287.272\"))"
        };
        for (Object object : testData) {
            System.out.println("Checking data: " + object);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
            objectOutput.writeObject(object);
            objectOutput.flush();
            objectOutput.close();
            byteArrayOutputStream.flush();

            String serializedObjectStr = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.ISO_8859_1);

            String lzsEncodedBase64 = LZSEncoding.compressToBase64(serializedObjectStr);
            String base64Encoded = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

            // Why is Base64 Encoder more efficient sometimes?
            System.out.println(serializedObjectStr + "\nHas length " + serializedObjectStr.length() + "\n"
                    + lzsEncodedBase64 + "\nHas length " + lzsEncodedBase64.length() + "\n"
                    + base64Encoded + "\nHas length " + base64Encoded.length());

            // verify Base64 Encoder
            byte[] bytes = serializedObjectStr.getBytes(StandardCharsets.ISO_8859_1);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Assert.assertEquals(object, objectInputStream.readObject());
            objectInputStream.close();
            byteArrayInputStream.close();

            // verify LZSEncoder
            bytes = LZSEncoding.decompressFromBase64(lzsEncodedBase64).getBytes(StandardCharsets.ISO_8859_1);
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Assert.assertEquals(object, objectInputStream.readObject());
            objectInputStream.close();
            byteArrayInputStream.close();
        }
    }
}
