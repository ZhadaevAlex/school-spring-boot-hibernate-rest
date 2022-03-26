package ru.zhadaev;

import java.util.*;

public class GroupCreator {
    public Set<Group> createGroups(int numberGroups) {
        Set<Group> groups = new HashSet<>();

        RandomWords randomWords = new RandomWords();

        while (groups.size() < numberGroups) {
            StringBuilder name = new StringBuilder();
            name.append(randomWords.getRandomWord(2, LetterCase.UPPERCASE))
                    .append("-")
                    .append(randomWords.getRandomNumber(10, 99));

            groups.add(new Group(name.toString()));
        }

        return groups;
    }
}
