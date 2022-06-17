package ru.zhadaev;

public class Main {

//    public static void main(String[] args) throws SQLException, NoSuchFileException, IsNotFileException {
//        System.out.println("1. Find all groups with less or equals student count");
//        System.out.println("2. Find all students related to course with given name");
//        System.out.println("3. Add new student");
//        System.out.println("4. Delete student by STUDENT_ID");
//        System.out.println("5. Add a student to the course (from a list)");
//        System.out.println("6. Remove the student from one of his or her courses");
//
//        SchoolInitData schoolInitData = new SchoolInitData();
//
//        List<Student> students = new StudentsCreator().createStudents(
//                SchoolInitData.NUMBER_STUDENTS,
//                schoolInitData.getFirstNames(),
//                schoolInitData.getLastNames());
//
//        School school = new SchoolCreator(
//                SchoolInitData.NUMBER_GROUPS,
//                SchoolInitData.MIN_STUDENTS_IN_GROUP,
//                SchoolInitData.MAX_STUDENTS_IN_GROUP,
//                schoolInitData.getSubjects(),
//                SchoolInitData.MIN_COURSES,
//                SchoolInitData.MAX_COURSES).createSchool(students);
//
//        SchoolInitializer schoolInitializer = new SchoolInitializer(connection);
//
//        schoolInitializer.initialize(school);
//
//        GroupDAO groupDAO = context.getBean(
//                "groupRepository", GroupDAO.class);
//        CourseDAO courseDAO = context.getBean(
//                "courseRepository", CourseDAO.class);
//        StudentDAO studentDAO = context.getBean(
//                "studentRepository", StudentDAO.class);
//        ISchoolManager manager = context.getBean(
//                "schoolManager", SchoolManager.class);
//
//        List<Group> result1 = manager.findGroupsByStudentsCount(25);
//
//        List<Student> result2 = manager.findStudentsByCourseName("Economics");
//
//        List<Group> groups3 = groupDAO.findAll();
//        Group group3 = new Group(groups3.get(0).getName());
//        List<Group> result3 = manager.findGroup(group3);
//
//        Course course4 = new Course("Music");
//        course4.setDescription("Subject Music");
//        List<Course> coursesDb4 = manager.findCourse(course4);
//
//        List<Student> students5 = studentDAO.findAll();
//        Student student5 = new Student(students5.get(0).getFirstName(), students5.get(0).getLastName());
//        List<Student> result5 = manager.findStudent(student5);
//
//        Student student6 = new Student("Ivan", "Ivanov");
//        Group group61 = new Group(school.getGroups().get(0).getName());
//        group61.setId(school.getGroups().get(0).getId());
//        student6.setGroup(group61);
//        List<Course> coursesAll = courseDAO.findAll();
//        List<Course> courses6 = new ArrayList<>();
//        Collections.addAll(courses6, coursesAll.get(0), coursesAll.get(1), coursesAll.get(2));
//        student6.setCourses(courses6);
//        Student studentDb6 = manager.addNewStudent(student6);
//
//        manager.deleteStudentById(201);
//
//        Student student7 = new Student("Petr1", "Petr1");
//        student7.setGroup(group61);
//        Collections.addAll(courses6, coursesAll.get(3), coursesAll.get(4), coursesAll.get(5));
//        student7.setCourses(courses6);
//        Student studentDb7 = manager.addNewStudent(student7);
//
//        manager.removeStudentFromCourse(studentDb7, coursesAll.get(0));
//    }
}
