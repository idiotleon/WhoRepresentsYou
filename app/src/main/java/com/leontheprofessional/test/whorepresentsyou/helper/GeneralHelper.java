package com.leontheprofessional.test.whorepresentsyou.helper;

/**
 * Created by Leon on 10/12/2015.
 */
public class GeneralHelper {

    public static boolean isZipCode(String zipcode) {
        if (zipcode != null && zipcode.length() == 5) {
            if (zipcode.matches("[0-9]+"))
                return true;
        }

        return false;
    }
}
