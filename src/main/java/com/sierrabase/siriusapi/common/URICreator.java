package com.sierrabase.siriusapi.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class URICreator {
    public static String pathToString(String... segments) {
        if (segments.length == 0) {
            return "";
        }

        Path path = Paths.get(segments[0], Arrays.copyOfRange(segments, 1, segments.length));
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
