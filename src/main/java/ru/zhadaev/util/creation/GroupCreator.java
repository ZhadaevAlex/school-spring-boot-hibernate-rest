package ru.zhadaev.util.creation;

import ru.zhadaev.dao.entitie.Group;

import java.util.*;

public class GroupCreator {
    public List<Group> createGroups(int numberGroups) {
        Set<Group> groups = new LinkedHashSet<>();

        RandomWords randomWords = new RandomWords();

        while (groups.size() < numberGroups) {
            StringBuilder name = new StringBuilder();
            name.append(randomWords.getRandomWord(2, LetterCase.UPPERCASE))
                    .append("-")
                    .append(randomWords.getRandomNumber(10, 99));

            Group group = new Group(name.toString());
            groups.add(group);
        }

        return new ArrayList<>(groups);
    }
}
