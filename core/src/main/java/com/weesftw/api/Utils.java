package com.weesftw.api;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public static String capitalize(String str) {
        char[] array = str.toCharArray();
        if (array.length > 0) {
            array[0] = Character.toUpperCase(array[0]);
        }
        return new String(array);
    }
}
