package com.pherux.skyquest.utils;

import android.os.Build;

class Utils {

    private static String deviceName;

    static String getDeviceName() {
        if (deviceName != null) {
            return deviceName;
        } else {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.startsWith(manufacturer)) {
                deviceName = capitalize(model);
                return deviceName;
            } else {
                deviceName = capitalize(manufacturer) + " " + model;
            }
            return deviceName;
        }

    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
