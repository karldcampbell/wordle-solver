package com.karldcampbell.wordle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.karldcampbell.wordle.Utils.intersection;

public class GuessFinder {
    private final Set<String> allWords;
    private final Map<Character, Set<String>> wordsByChar = new HashMap<>();

    public GuessFinder(String fileName) throws URISyntaxException, IOException {
        var dictionaryPath = Paths.get(GuessFinder.class.getClassLoader().getResource(fileName).toURI());
        allWords = Files.lines(dictionaryPath).filter(Objects::nonNull).filter(s -> !s.isEmpty()).collect(Collectors.toSet());

        for (String word : allWords) {
            for (Character c : word.toCharArray()) {
                wordsByChar.computeIfAbsent(c, HashSet::new).add(word);
            }
        }
    }

    public String findBestFirstWord() {
        var sorted = wordsByChar.entrySet().stream()
                .sorted((a, b) -> b.getValue().size() - a.getValue().size())
                .collect(Collectors.toList());

        var bestSets = sorted.stream().limit(5).map(Map.Entry::getValue).collect(Collectors.toList());
        var secondBestSets = sorted.stream().skip(10).limit(5).map(Map.Entry::getValue).collect(Collectors.toList());

        var possibleBest = intersection(bestSets);
        var possibleSecondBest = intersection(secondBestSets);
        return "";
    }

    //best: arose
    //second best: until
    public static void main(String[] args) throws Exception {
        var gf = new GuessFinder("wordle_words.txt");
        System.out.println("done");
        gf.findBestFirstWord();
        assert true;
    }
}
