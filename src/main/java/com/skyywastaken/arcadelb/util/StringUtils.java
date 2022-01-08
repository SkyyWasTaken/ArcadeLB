package com.skyywastaken.arcadelb.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringUtils {
    public static List<String> getSortedPartialMatches(String partialString, Iterable<String> originalStrings) {
        ArrayList<String> matches = new ArrayList<>();
        for (String possibleMatch : originalStrings) {
            if (possibleMatch.toLowerCase().startsWith(partialString.toLowerCase())) {
                matches.add(possibleMatch);
            }
        }
        Collections.sort(matches);
        return matches;
    }
}
