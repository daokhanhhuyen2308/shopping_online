package com.dao.shopping.utils;

import java.util.Date;

public class SlugUtils {
    public static String generateSlug(String input) {
        return input.toLowerCase().replaceAll("\\s+", "-") + "-" + new Date().getTime();
    }
}
