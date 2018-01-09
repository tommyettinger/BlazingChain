package blazing.chain;

import static blazing.chain.LZSEncoding.*;

/**
 * Basic test.
 * Created by Tommy Ettinger on 9/13/2016.
 */
public class LZSEncodingTest {
    public static void main(String[] args) {
        String compressed;
        String[] inputs = new String[]{
                "hello1hello2hello3hello4hello5hello6hello7hello8hello9helloAhelloBhelloChelloDhelloEhelloF",
                "hello1hello2hello3hello4hello5hello6hello7hello8hello9helloAhelloBhelloChelloDhelloEhelloFF",
                "hello1hello2hello3hello4hello5hello6hello7hello8hello9helloAhelloBhelloChelloDhelloEhelloFFF",
                "hello1hello2hello3hello4hello5hello6hello7hello8hello9helloAhelloBhelloChelloDhelloEhelloFFFF",
                "The Quick Brown Fox Jumps Over The Lazy Dog. The Quick Brown Fox Jumps Over The Lazy Dog. The Quick Brown Fox Jumps Over The Lazy Dog.",
                "Dorothy lived in the midst of the great Kansas prairies, with Uncle Henry, who was a \n" +
                        "farmer, and Aunt Em, who was the farmer's wife. Their house was small, for the \n" +
                        "lumber to build it had to be carried by wagon many miles. There were four walls, \n" +
                        "a floor and a roof, which made one room; and this room contained a rusty looking \n" +
                        "cookstove, a cupboard for the dishes, a table, three or four chairs, and the beds. \n" +
                        "Uncle Henry and Aunt Em had a big bed in one corner, and Dorothy a little bed in \n" +
                        "another corner. There was no garret at all, and no cellar–except a small hole dug \n" +
                        "in the ground, called a cyclone cellar, where the family could go in case one of \n" +
                        "those great whirlwinds arose, mighty enough to crush any building in its path. It \n" +
                        "was reached by a trap door in the middle of the floor, from which a ladder led \n" +
                        "down into the small, dark hole.",
                "Бъгрудажок кёла бё ат сёб сюф ат аджус Нудъскё трасконё, рег Брыф Нымобъб, суд цюз фарыр \n" +
                        "фйброск, цюф Лыцур Рэцодна, суд цюз ат фйброск'дез реп. Уск вы цюз род, гаж ат \n" +
                        "рерроск шюф пйл удар тяс шюф ви фэлыха деж зэнып быняк годжанё. Атроск грен сюр вугё, \n" +
                        "фарыр копроск цюф фарыр глус, рорар грерова дач зырян; цюф дасё зырян урафова фарыр госкыбйз вагдапаск \n" +
                        "щылев, фарыр езад гаж ат едё, фарыр луп, дет дан сюр фысадё, цюф ат пыё. \n" +
                        "Брыф Нымобъб цюф Лыцур Рэцодна тяс фарыр ска пы бё дач лъроск, цюф Бъгрудажок фарыр нът пы бё \n" +
                        "элыскроск лъроск. Атроск цюз цюн йвял ерас вип, цюф цюн лук–габ фарыр род ыск ал \n" +
                        "бё ат выгрир, дада фарыр лыних лук, лифроск ат грёнэкеф роб ыб бё бол дач сюф \n" +
                        "грер аджус цофаскё худъв, бушод ыскбоф шюф пйс сявюд пйлпаск бё лъс тидодка. Удар \n" +
                        "цюз пйскада деж фарыр ко ботроск бё ат луск сюф ат копроск, выгодна рорар фарыр сюд зыс \n" +
                        "юн ыскшюф ат род, рах ыск."
        };
        for (String input : inputs) {
            System.out.println(input + "\n... has length " + input.length() + " (uncompressed)");
            System.out.println(compressed = compress(input));
            System.out.println("Is correct? " + (decompress(compressed).equals(input) ? "Yes" : "NO NO NO\nNO\n\nNO") +
                    ", with length " + compressed.length() + " (normal compression)");
            System.out.println(compressed = compressToBase64(input));
            System.out.println("Is correct? " + (decompressFromBase64(compressed).equals(input) ? "Yes" : "NO NO NO\nNO\n\nNO") +
                    ", with length " + compressed.length() + " (Base64 compression)");
            System.out.println(compressed = compressToUTF16(input));
            System.out.println("Is correct? " + (decompressFromUTF16(compressed).equals(input) ? "Yes" : "NO NO NO\nNO\n\nNO") +
                    ", with length " + compressed.length() + " (UTF-16 compression)");
            System.out.println(compressed = compressToEncodedURIComponent(input));
            System.out.println("Is correct? " + (decompressFromEncodedURIComponent(compressed).equals(input) ? "Yes" : "NO NO NO\nNO\n\nNO") +
                    ", with length " + compressed.length() + " (URI Component compression)");
        }
    }
}
