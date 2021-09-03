package blazing.chain.demo;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LZIntEncoding {
    public static int[] compressToInts(String uncompressedStr) {
        if (uncompressedStr == null) return null;
        if (uncompressedStr.isEmpty()) return new int[0];
        final int bitsPerChar = 32;
        int i, value;
        HashMap<String, Integer> context_dictionary = new HashMap<>(256, 0.5f);
        HashSet<String> context_dictionaryToCreate = new HashSet<>(256, 0.5f);
        String context_c;
        String context_wc;
        String context_w = "";
        int context_enlargeIn = 2; // Compensate for the first entry which should not count
        int context_dictSize = 3;
        int context_numBits = 2;
        IntArray context_data = new IntArray(uncompressedStr.length() >>> 1);
        int context_data_val = 0;
        int context_data_position = 0;
        int ii;

        char[] uncompressed = uncompressedStr.toCharArray();
        for (ii = 0; ii < uncompressed.length; ii++) {
            context_c = String.valueOf(uncompressed[ii]);
            if (!context_dictionary.containsKey(context_c)) {
                context_dictionary.put(context_c, context_dictSize++);
                context_dictionaryToCreate.add(context_c);
            }

            context_wc = context_w + context_c;
            if (context_dictionary.containsKey(context_wc)) {
                context_w = context_wc;
            } else {
                if (context_dictionaryToCreate.contains(context_w)) {
                    if ((value = context_w.charAt(0)) < 256) {
                        for (i = 0; i < context_numBits; i++) {
                            context_data_val = (context_data_val << 1);
                            if (context_data_position == bitsPerChar - 1) {
                                context_data_position = 0;
                                context_data.add(context_data_val);
                                context_data_val = 0;
                            } else {
                                context_data_position++;
                            }
                        }
                        for (i = 0; i < 8; i++) {
                            context_data_val = (context_data_val << 1) | (value & 1);
                            if (context_data_position == bitsPerChar - 1) {
                                context_data_position = 0;
                                context_data.add(context_data_val);
                                context_data_val = 0;
                            } else {
                                context_data_position++;
                            }
                            value >>>= 1;
                        }
                    } else {
                        value = 1;
                        for (i = 0; i < context_numBits; i++) {
                            context_data_val = (context_data_val << 1) | value;
                            if (context_data_position == bitsPerChar - 1) {
                                context_data_position = 0;
                                context_data.add(context_data_val);
                                context_data_val = 0;
                            } else {
                                context_data_position++;
                            }
                            value = 0;
                        }
                        value = context_w.charAt(0);
                        for (i = 0; i < 16; i++) {
                            context_data_val = (context_data_val << 1) | (value & 1);
                            if (context_data_position == bitsPerChar - 1) {
                                context_data_position = 0;
                                context_data.add(context_data_val);
                                context_data_val = 0;
                            } else {
                                context_data_position++;
                            }
                            value >>>= 1;
                        }
                    }
                    context_enlargeIn--;
                    if (context_enlargeIn == 0) {
                        context_enlargeIn = 1 << context_numBits++;
                    }
                    context_dictionaryToCreate.remove(context_w);
                } else {
                    value = context_dictionary.get(context_w);
                    for (i = 0; i < context_numBits; i++) {
                        context_data_val = (context_data_val << 1) | (value & 1);
                        if (context_data_position == bitsPerChar - 1) {
                            context_data_position = 0;
                            context_data.add(context_data_val);
                            context_data_val = 0;
                        } else {
                            context_data_position++;
                        }
                        value >>>= 1;
                    }

                }
                context_enlargeIn--;
                if (context_enlargeIn == 0) {
                    context_enlargeIn = 1 << context_numBits++;
                }
                // Add wc to the dictionary.
                context_dictionary.put(context_wc, context_dictSize++);
                context_w = context_c;
            }
        }

        // Output the code for w.
        if (!context_w.isEmpty()) {
            if (context_dictionaryToCreate.contains(context_w)) {
                if (context_w.charAt(0) < 256) {
                    for (i = 0; i < context_numBits; i++) {
                        context_data_val = (context_data_val << 1);
                        if (context_data_position == bitsPerChar - 1) {
                            context_data_position = 0;
                            context_data.add(context_data_val);
                            context_data_val = 0;
                        } else {
                            context_data_position++;
                        }
                    }
                    value = context_w.charAt(0);
                    for (i = 0; i < 8; i++) {
                        context_data_val = (context_data_val << 1) | (value & 1);
                        if (context_data_position == bitsPerChar - 1) {
                            context_data_position = 0;
                            context_data.add(context_data_val);
                            context_data_val = 0;
                        } else {
                            context_data_position++;
                        }
                        value >>>= 1;
                    }
                } else {
                    value = 1;
                    for (i = 0; i < context_numBits; i++) {
                        context_data_val = (context_data_val << 1) | value;
                        if (context_data_position == bitsPerChar - 1) {
                            context_data_position = 0;
                            context_data.add(context_data_val);
                            context_data_val = 0;
                        } else {
                            context_data_position++;
                        }
                        value = 0;
                    }
                    value = context_w.charAt(0);
                    for (i = 0; i < 16; i++) {
                        context_data_val = (context_data_val << 1) | (value & 1);
                        if (context_data_position == bitsPerChar - 1) {
                            context_data_position = 0;
                            context_data.add(context_data_val);
                            context_data_val = 0;
                        } else {
                            context_data_position++;
                        }
                        value >>>= 1;
                    }
                }

                context_dictionaryToCreate.remove(context_w);
            } else {
                value = context_dictionary.get(context_w);
                for (i = 0; i < context_numBits; i++) {
                    context_data_val = (context_data_val << 1) | (value & 1);
                    if (context_data_position == bitsPerChar - 1) {
                        context_data_position = 0;
                        context_data.add(context_data_val);
                        context_data_val = 0;
                    } else {
                        context_data_position++;
                    }
                    value >>>= 1;
                }

            }
        }

        // Mark the end of the stream
        value = 2;
        for (i = 0; i < context_numBits; i++) {
            context_data_val = (context_data_val << 1) | (value & 1);
            if (context_data_position == bitsPerChar - 1) {
                context_data_position = 0;
                context_data.add(context_data_val);
                context_data_val = 0;
            } else {
                context_data_position++;
            }
            value >>>= 1;
        }

        // Flush the last char
        while (true) {
            context_data_val = (context_data_val << 1);
            if (context_data_position == bitsPerChar - 1) {
                context_data.add(context_data_val);
                break;
            } else
                context_data_position++;
        }
        return context_data.shrink();
    }
    public static String decompressFromInts(int[] getNextValue) {
        if(getNextValue == null)
            return null;
        final int length = getNextValue.length;
        if(length == 0)
            return "";
        final int resetValue = 0x80000000;
        ArrayList<String> dictionary = new ArrayList<>(length >>> 1);
        int enlargeIn = 4, dictSize = 4, numBits = 3, position = resetValue, index = 1, resb, maxpower, power;
        String entry, w, c;
        StringBuilder res = new StringBuilder(length);
        char bits;
        int val = getNextValue[0];

        for (char i = 0; i < 3; i++) {
            dictionary.add(String.valueOf(i));
        }

        bits = 0;
        maxpower = 2;
        power = 0;
        while (power != maxpower) {
            resb = val & position;
            position >>>= 1;
            if (position == 0) {
                position = resetValue;
                val = getNextValue[index++];
            }
            bits |= (resb != 0 ? 1 : 0) << power++;
        }

        switch (bits) {
            case 0:
                bits = 0;
                maxpower = 8;
                power = 0;
                while (power != maxpower) {
                    resb = val & position;
                    position >>>= 1;
                    if (position == 0) {
                        position = resetValue;
                        val = getNextValue[index++];
                    }
                    bits |= (resb != 0 ? 1 : 0) << power++;
                }
                c = String.valueOf(bits);
                break;
            case 1:
                bits = 0;
                maxpower = 16;
                power = 0;
                while (power != maxpower) {
                    resb = val & position;
                    position >>>= 1;
                    if (position == 0) {
                        position = resetValue;
                        val = getNextValue[index++];
                    }
                    bits |= (resb != 0 ? 1 : 0) << power++;
                }
                c = String.valueOf(bits);
                break;
            default:
                return "";
        }
        dictionary.add(c);
        w = c;
        res.append(w);
        while (true) {
            if (index > length) {
                return "";
            }
            int cc = 0;
            maxpower = numBits;
            power = 0;
            while (power != maxpower) {
                resb = val & position;
                position >>>= 1;
                if (position == 0) {
                    position = resetValue;
                    val = getNextValue[index++];
                }
                cc |= (resb != 0 ? 1 : 0) << power++;
            }
            switch (cc) {
                case 0:
                    bits = 0;
                    maxpower = 8;
                    power = 0;
                    while (power != maxpower) {
                        resb = val & position;
                        position >>>= 1;
                        if (position == 0) {
                            position = resetValue;
                            val = getNextValue[index++];
                        }
                        bits |= (resb != 0 ? 1 : 0) << power++;
                    }

                    dictionary.add(String.valueOf(bits));
                    cc = dictSize++;
                    enlargeIn--;
                    break;
                case 1:
                    bits = 0;
                    maxpower = 16;
                    power = 0;
                    while (power != maxpower) {
                        resb = val & position;
                        position >>>= 1;
                        if (position == 0) {
                            position = resetValue;
                            val = getNextValue[index++];
                        }
                        bits |= (resb != 0 ? 1 : 0) << power++;
                    }
                    dictionary.add(String.valueOf(bits));
                    cc = dictSize++;
                    enlargeIn--;
                    break;
                case 2:
                    return res.toString();
            }

            if (enlargeIn == 0) {
                enlargeIn = 1 << numBits;
                numBits++;
            }

            if (cc < dictionary.size() && dictionary.get(cc) != null) {
                entry = dictionary.get(cc);
            } else {
                if (cc == dictSize) {
                    entry = w + w.charAt(0);
                } else {
                    return "";
                }
            }
            res.append(entry);

            // Add w+entry[0] to the dictionary.
            dictionary.add(w + entry.charAt(0));
            dictSize++;
            enlargeIn--;

            w = entry;

            if (enlargeIn == 0) {
                enlargeIn = 1 << numBits;
                numBits++;
            }

        }
    }

    public static String join(int... elements) {
        if (elements == null || elements.length == 0) return "";
        StringBuilder sb = new StringBuilder(64);
        sb.append(elements[0]);
        for (int i = 1; i < elements.length; i++) {
            sb.append(',').append(elements[i]);
        }
        return sb.toString();
    }

    private static final int[] hexCodes = new int[]
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                    0, 1, 2, 3, 4, 5, 6, 7, 8, 9,-1,-1,-1,-1,-1,-1,
                    -1,10,11,12,13,14,15,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                    -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                    -1,10,11,12,13,14,15};

    /**
     * Reads in a CharSequence containing only decimal digits (0-9) with an optional sign at the start and returns the
     * int they represent, reading at most 10 characters (11 if there is a sign) and returning the result if valid, or 0
     * if nothing could be read. The leading sign can be '+' or '-' if present. This can technically be used to handle
     * unsigned integers in decimal format, but it isn't the intended purpose. If you do use it for handling unsigned
     * ints, 2147483647 is normally the highest positive int and -2147483648 the lowest negative one, but if you give
     * this a number between 2147483647 and {@code 2147483647 + 2147483648}, it will interpret it as a negative number
     * that fits in bounds using the normal rules for converting between signed and unsigned numbers. If end is negative
     * (as is the case for {@link String#indexOf(int)} when nothing more was found), this treats the end of the
     * CharSequence as the {@code end} parameter here.
     * <br>
     * Should be fairly close to the JDK's Integer.parseInt method, but this also supports CharSequence data instead of
     * just String data, and allows specifying a start and end. This doesn't throw on invalid input, either, instead
     * returning 0 if the first char is not a decimal digit, or stopping the parse process early if a non-decimal-digit
     * char is read before end is reached. If the parse is stopped early, this behaves as you would expect for a number
     * with less digits, and simply doesn't fill the larger places.
     * @param cs a CharSequence, such as a String, containing only digits 0-9 with an optional sign
     * @param start the (inclusive) first character position in cs to read
     * @param end the (exclusive) last character position in cs to read (this stops after 10 or 11 characters if end is too large, depending on sign)
     * @return the int that cs represents
     */
    public static int intFromDecimal(final CharSequence cs, final int start, int end)
    {
        int len, h, lim = 10;
        if(cs == null) return 0;
        len = cs.length();
        if(end < 0) end = len;
        if(start < 0 || end - start <= 0
                || len - start <= 0 || end > len)
            return 0;
        char c = cs.charAt(start);
        if(c == '-')
        {
            len = -1;
            lim = 11;
            h = 0;
        }
        else if(c == '+')
        {
            len = 1;
            lim = 11;
            h = 0;
        }
        else if(c > 102 || (h = hexCodes[c]) < 0 || h > 9)
            return 0;
        else
        {
            len = 1;
        }
        int data = h;
        for (int i = start + 1; i < end && i < start + lim; i++) {
            if((c = cs.charAt(i)) > 102 || (h = hexCodes[c]) < 0 || h > 9)
                return data * len;
            data = data * 10 + h;
        }
        return data * len;
    }

    public static int[] readJoined(String source) {
        if(source == null) return null;
        final int length = source.length();
        if(length == 0) return new int[0];
        int amount = 1, idx = -1;
        while ((idx = source.indexOf(',', idx+1)) >= 0)
            ++amount;
        int[] res = new int[amount];
        idx = 0;
        for (int i = 0; i < amount; i++) {
            res[i] = intFromDecimal(source, idx, idx = source.indexOf(',', idx+1));
        }
        return res;
    }

}
