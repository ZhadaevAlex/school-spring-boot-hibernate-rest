package ru.zhadaev;

import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        Map<String, String> subjects = new HashMap<>();
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

        List<String> firstNames = new ArrayList<>();
        Collections.addAll(firstNames,
                "Oliver", "Jack", "Harry", "Jacob", "Charlie",
                "Thomas", "Oscar", "William", "James", "George",
                "Amelia", "Olivia", "Emily", "Ava", "Jessica",
                "Isabella", "Sophie", "Mia", "Ruby", "Lily");

        List<String> lastNames = new ArrayList<>();
        Collections.addAll(lastNames,
                "Anderson", "Thomas", "Jackson", "White", "Harris",
                "Martin", "Thompson", "Wood", "Lewis", "Scott",
                "Cooper", "King", "Green",  "Walker", "Edwards",
                "Turner", "Morgan", "Baker", "Hill", "Phillips");

        List<Student> students = new StudentsCreator().createStudents(200, firstNames, lastNames);
        School school = new SchoolCreator(10, 10, 30,
        subjects, 1, 3).createSchool(students);

        Group groupIn1 = new Group("RR-16");
        GroupRepository gr = new GroupRepository();
        Group groupOut1 = gr.save(groupIn1);

        Group groupIn2 = new Group("RR-17");
        Group groupOut2 = gr.save(groupIn2);

        int bb = 2;
    }
}
