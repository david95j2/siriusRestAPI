package com.sierrabase.siriusapi.common;

public class StringToDouble {
    public static double convertExposureTimeToDouble(String exposureTimeStr) {
        try {
            String[] parts = exposureTimeStr.split("/");
            if (parts.length == 2) {
                double numerator = Double.parseDouble(parts[0]);
                double denominator = Double.parseDouble(parts[1]);
                return numerator / denominator;
            } else {
                return Double.parseDouble(exposureTimeStr);
            }
        } catch (NumberFormatException e) {
            System.err.println("Failed to convert exposure time to double: " + exposureTimeStr);
            return 0;
        }
    }

}
