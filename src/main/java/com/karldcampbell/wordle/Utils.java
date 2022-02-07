package com.karldcampbell.wordle;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
    public static <T> Set<T> intersection(List<Set<T>> sets) {
        var result = new HashSet<>(sets.get(0));

        for (int i=1; i<sets.size(); i++) {
            result.retainAll(sets.get(i));
        }
        return result;
    }

    public static <T> Set<T> sum(List<Set<T>> sets) {
        var result = new HashSet<>(sets.get(0));

        for (int i=1; i<sets.size(); i++) {
            result.addAll(sets.get(i));
        }
        return result;
    }

}
