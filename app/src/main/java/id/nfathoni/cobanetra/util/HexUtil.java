package id.nfathoni.cobanetra.util;

import android.util.Log;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class HexUtil {

    private static final String TAG = "HexUtil";

    public static String stringToHex(String str) {
        Log.d(TAG, "Str length: " + str.length());

//        StringBuilder builder = new StringBuilder();
//
//        char[] chars = str.toCharArray();
//        Log.d(TAG, "Chars length: " + str.length());
//
//        for (char c : chars) {
//            String charHex = Integer.toHexString(c);
//            builder.append(charHex);
//        }
//
//        return builder.toString();

//        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
//
//        Log.d(TAG, "bytes length: " + strBytes.length);
//
//        for (byte strByte : strBytes) {
//            builder.append(String.format("%x", strByte));
//            Log.d(TAG, "stringToHex: " + String.format("%x", strByte));
//        }
//
//        return builder.toString();

        return String.format("%x", new BigInteger(1, str.getBytes(StandardCharsets.UTF_8)));
    }

    public static String hexToString(String hex) {
//        StringBuilder builder = new StringBuilder();
//
//        for (int i = 0; i < hex.length(); i += 2) {
//            builder.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
//        }
//
//        return builder.toString();

        return new String(new BigInteger(hex, 16).toByteArray());
    }
}
