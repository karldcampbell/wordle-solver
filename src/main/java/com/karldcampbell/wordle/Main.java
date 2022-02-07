package com.karldcampbell.wordle;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    private static final Scanner reader = new Scanner(System.in);

    public static void main(String[] args) {
        runGame(args[0]);
    }

    private static void runGame(String dictPath) {
        Set<String> allWords;
        try {
            var dictionaryPath = Paths.get(Main.class.getClassLoader().getResource(dictPath).toURI());
            allWords = Files.lines(dictionaryPath)
                    .filter(Objects::nonNull).filter(s -> !s.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var solver = new HardModeSolver(allWords);
        // var firstGuess = solver.bestGuesses().findFirst().orElse("<void>");
        // System.out.println("hint: try " + firstGuess);
        while (true) {
            var guess = inputGuess();
            if (guess.result().equals("ggggg")) {
                System.out.println("You Won!");
                break;
            }

            solver = solver.makeGuess(guess);

            List<String> bestGuesses = solver.bestGuesses().limit(25).collect(Collectors.toList());
            if (!gameOver(bestGuesses)) {
                printNextGuesses(bestGuesses);
            } else {
               break;
            }
        }
    }

    private static boolean gameOver(List<String> bestGuesses) {
        if (bestGuesses.size() > 1) {
            return false;
        }  else if (bestGuesses.size() == 1) {
            System.out.println("Solution found: " + bestGuesses.get(0));
            return true;
        } else {
            System.out.println("No valid guesses; are you sure you entered things correctly?");
            return true;
        }
    }

    private static void printNextGuesses(List<String> bestGuesses) {
        System.out.println("Next best guesses:");
        for (int i = 0; i< bestGuesses.size(); i++) {
            System.out.print(bestGuesses.get(i));
            if (i % 5 == 4) {
                System.out.println();
            } else {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    private static GuessResult inputGuess() {
        while (true) {
            try {
                System.out.println("Enter Guess: <guess> <result>");
                System.out.print(">");
                String[] line = reader.nextLine().split(" ");
                var result = new GuessResult(line[0], line[1]);
                return result;
            } catch (Exception e) {
                System.out.println("Error: " + e.toString());
            }
        }
    }
}
