package ru.zhadaev.service;

import java.util.*;

public class SchoolInitData {
    private final Map<String, String> subjects = new HashMap<>();
    private final List<String> firstNames = new ArrayList<>();
    private final List<String> lastNames = new ArrayList<>();
    public static final Integer NUMBER_STUDENTS = 200;
    public static final Integer NUMBER_GROUPS = 10;
    public static final Integer MIN_STUDENTS_IN_GROUP = 10;
    public static final Integer MAX_STUDENTS_IN_GROUP = 30;
    public static final Integer MIN_COURSES = 1;
    public static final Integer MAX_COURSES = 3;

    public SchoolInitData() {
        subjects.put("Literature", "Subject Literature");
        subjects.put("Astronomy", "Subject Astronomy");
        subjects.put("Biology", "Subject Biology");
        subjects.put("Music", "Subject Music");
        subjects.put("Botany", "Subject Botany");
        subjects.put("Chemistry", "Subject Chemistry");
        subjects.put("Computer science", "Subject Computer science");
        subjects.put("Economics", "Subject Economics");
        subjects.put("Math", "Subject Math");
        subjects.put("History", "Subject History");

        Collections.addAll(firstNames,
                "Oliver", "Jack", "Harry", "Jacob", "Charlie",
                "Thomas", "Oscar", "William", "James", "George",
                "Amelia", "Olivia", "Emily", "Ava", "Jessica",
                "Isabella", "Sophie", "Mia", "Ruby", "Lily");

        Collections.addAll(lastNames,
                "Anderson", "Thomas", "Jackson", "White", "Harris",
                "Martin", "Thompson", "Wood", "Lewis", "Scott",
                "Cooper", "King", "Green", "Walker", "Edwards",
                "Turner", "Morgan", "Baker", "Hill", "Phillips");
    }

    public Map<String, String> getSubjects() {
        return subjects;
    }

    public List<String> getFirstNames() {
        return firstNames;
    }

    public List<String> getLastNames() {
        return lastNames;
    }
}
