package com.web.note.util;

import java.util.Random;

public class StringUtil {
    public static final char[] basicCharacters =
            {
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
            };


    public static boolean empty(String s) {
        return s == null || s.length() == 0;
    }

    public static String randomString(int length) {
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int i1 = random.nextInt(basicCharacters.length);
            stringBuilder.append(basicCharacters[i1]);
        }

        return stringBuilder.toString();
    }

    public static String noEmptyString(String target) {
        return target != null && !target.isEmpty() ? target : null;
    }

}
