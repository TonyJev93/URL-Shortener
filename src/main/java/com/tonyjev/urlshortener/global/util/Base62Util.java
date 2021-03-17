package com.tonyjev.urlshortener.global.util;

public class Base62Util {
    final static int RADIX = 62;
    final static String CODEC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encoding(long inputNumber) {
        StringBuffer encodedString = new StringBuffer();
        while (inputNumber > 0) {
            encodedString.append(CODEC.charAt((int) (inputNumber % RADIX)));
            inputNumber /= RADIX;
        }
        return encodedString.toString();
    }

    public static long decoding(String inputString) {
        long decodedNumber = 0;
        long power = 1;
        for (int i = 0; i < inputString.length(); i++) {
            decodedNumber += CODEC.indexOf(inputString.charAt(i)) * power;
            power *= RADIX;
        }
        return decodedNumber;
    }
}
