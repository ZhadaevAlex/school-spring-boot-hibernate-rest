package ru.zhadaev.util.creation;

import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor
public class RandomWords {
    private final Random random;

    String getRandomWord(int length, LetterCase letterCase) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(rndLetter(letterCase));
        }

        return result.toString();
    }

    int getRandomNumber(int from, int to) {
        if (to < from) {
            int tmp = from;
            from = to;
            to = tmp;
        }

        if (from == to) {
            return from;
        }

        return from + random.nextInt((to - from) + 1);
    }

    private char rndLetter(LetterCase letterCase) {
        char result;

        if (letterCase == LetterCase.UPPERCASE) {
            result = (char)(random.nextInt(26) + 'A');
        } else {
            result = (char)(random.nextInt(26) + 'a');
        }

       return result;
    }
}
