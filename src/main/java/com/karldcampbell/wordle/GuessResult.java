package com.karldcampbell.wordle;

import java.util.Set;

public record GuessResult(String guess, String result) {
    private static int WORD_LENGTH = 5;

    public GuessResult(String guess, String result) {
        if (guess == null || guess.length() != WORD_LENGTH) {
            throw new IllegalArgumentException();
        } else {
            this.guess =guess;
        }

        if (result == null || result.length() != WORD_LENGTH || containsInvalidChars(result)) {
            throw new IllegalArgumentException();
        } else {
            this.result = result;
        }
    }

    private static final Set<Character> validChars = Set.of('.', 'y', 'g');

    public static void setLength(int wordLength) {
        WORD_LENGTH = wordLength;
    }

    private boolean containsInvalidChars(String result) {
        for (Character c : result.toCharArray()) {
            if (!validChars.contains(c)) {
                return true;
            }
        }
        return false;
    }


}
