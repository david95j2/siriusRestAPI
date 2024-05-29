package com.sierrabase.siriusapi.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class URICreator {
    public static String pathToString(String... segments) {
        if (segments.length == 0) {
            return "";
        }

        StringBuilder path = new StringBuilder(segments[0]);
        for (int i = 1; i < segments.length; i++) {
            if (!path.toString().endsWith("/")) {
                path.append("/");
            }
            path.append(segments[i].replaceFirst("^/+", ""));
        }

        return path.toString();
    }

    public static Path pathTopath(String... segments) {
        if (segments.length == 0) {
            return Paths.get("");
        }

        Path path = Paths.get(segments[0], Arrays.copyOfRange(segments, 1, segments.length));
        return path;
    }
}
