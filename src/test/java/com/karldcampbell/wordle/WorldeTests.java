package com.karldcampbell.wordle;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class WorldeTests {

    @Test
    public void write5letterWords() throws IOException {
        var lines = Files.lines(Paths.get("C:\\Users\\karld\\IdeaProjects\\wordle-solver\\src\\main\\resources\\words_alpha.txt"));

        Iterable<String> fiveLetterWords = lines.filter(w -> w.length() == 5)::iterator;
        Files.write(Paths.get("five_letter_words.txt"), fiveLetterWords, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

}
