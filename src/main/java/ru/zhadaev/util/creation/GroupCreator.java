package ru.zhadaev.util.creation;

import lombok.RequiredArgsConstructor;
import ru.zhadaev.dao.entities.Group;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class GroupCreator {
    private static final int LENGTH = 2;
    private static final LetterCase LETTER_CASE = LetterCase.UPPERCASE;
    private static final int FROM = 10;
    private static final int TO = 99;

    private final RandomWords randomWords;

    public List<Group> createGroups(int numberGroups) {
        Set<Group> groups = new LinkedHashSet<>();

        while (groups.size() < numberGroups) {
            StringBuilder name = new StringBuilder();
            name.append(randomWords.getRandomWord(LENGTH, LETTER_CASE))
                    .append("-")
                    .append(randomWords.getRandomNumber(FROM, TO));

            Group group = new Group();
            group.setName(name.toString());
            groups.add(group);
        }

        return new ArrayList<>(groups);
    }
}