package com.karldcampbell.wordle;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solver {

    private final Set<String> validWords;
    private final Set<Character> allContained;
    private final Set<Character> notContained;
    private final int wordLength;

    public Solver(Set<String> validWords, int wordLength) {
        this(validWords, wordLength, Collections.emptySet(), Collections.emptySet());
    }

    private Solver(Set<String> validWords, int wordLength, Set<Character> allContained, Set<Character> notContained) {
        this.validWords = validWords;
        this.wordLength = wordLength;
        this.allContained = allContained;
        this.notContained = notContained;
    }

    public Solver makeGuess(GuessResult result) {

        var allContainedAfterGuess = new HashSet<>(allContained);
        var notContainedAfterGuess = new HashSet<>(notContained);
        Pattern p = processResult(result, allContainedAfterGuess, notContainedAfterGuess);

        var validWordsAfterGuess = possibleSolutions(allContainedAfterGuess, notContainedAfterGuess, p)
                .collect(Collectors.toSet());

        return new Solver(validWordsAfterGuess, wordLength, allContainedAfterGuess, notContainedAfterGuess);
    }

    private Pattern processResult(GuessResult result, HashSet<Character> allContainedAfterGuess, HashSet<Character> notContainedAfterGuess) {
        var sb = new StringBuilder();
        var guessChars = result.guess().toCharArray();
        var resultChars = result.result().toCharArray();

        for (int i=0; i<wordLength; i++) {
            var guessChar = guessChars[i];
            var resultChar = resultChars[i];

            if (resultChar == 'g' || resultChar == 'y') {
                allContainedAfterGuess.add(guessChar);
            } else {
                notContainedAfterGuess.add(guessChar);
            }

            if (resultChar == 'g') {
                sb.append(guessChar);
            } else {
                sb.append('.');
            }
        }
        notContainedAfterGuess.removeAll(allContainedAfterGuess);
        return Pattern.compile(sb.toString());
    }

    public Stream<String> bestGuesses() {
        var scores = validWords
                .parallelStream().map(guess -> {
                        long totalScore = 0;
                        for (String target : validWords) {
                            totalScore += calcScore(guess, target);
                        }
                        return new AbstractMap.SimpleImmutableEntry<>(totalScore, guess);
                    })
                .collect(Collectors.toList());
        scores.sort((a, b) -> b.getKey().compareTo(a.getKey()));

       return scores.stream().map(AbstractMap.SimpleImmutableEntry::getValue);
    }

    private static int calcScore(String guess, String target) {
        var guessSet = toSet(guess);
        int score = 0;
        for (int i=0; i<target.length(); i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                score += 2;
            } else if (guessSet.contains(target.charAt(i))) {
                score++;
            }
        }
        return score;
    }

    private Stream<String> possibleSolutions(Set<Character> allContained, Set<Character> notContained, Pattern matches) {

        return validWords.parallelStream()
                .filter(w -> everyCharInTargetString(w, allContained))
                .filter(w -> noCharInTargetString(w, notContained))
                .filter(matches.asMatchPredicate());
    }

    private static Set<Character> toSet(String s) {
        HashSet<Character> set = new HashSet<>();
        for (Character c : s.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    private static boolean everyCharInTargetString(String target, Set<Character> chars) {
        var charsNotInTarget = new HashSet<>(chars);
        charsNotInTarget.removeAll(toSet(target));
        for (Character c : target.toCharArray()) {
            charsNotInTarget.remove(c);
        }
        return charsNotInTarget.isEmpty();
    }

    private static boolean noCharInTargetString(String target, Set<Character> chars) {
        var charSet = new HashSet<>(chars);
        for (Character c : target.toCharArray()) {
            if (charSet.contains(c)) return false;
        }
        return true;
    }
}
