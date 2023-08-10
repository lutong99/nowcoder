package org.example.nowcoder.util;

public class NowcoderUtils {

    public static Integer parseInt(String value, Integer defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
