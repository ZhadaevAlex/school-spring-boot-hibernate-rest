package ru.zhadaev.dao.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.entitie.Course;
import ru.zhadaev.dao.entitie.Group;
import ru.zhadaev.dao.entitie.Student;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.exception.DAOException;

import java.sql.*;
import java.util.*;

public class StudentRepository implements CrudRepository<Student, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(StudentRepository.class);
    private static final String STUDENT_ID = "student_id";
    private static final String GROUP_ID = "group_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String GROUP_NAME = "group_name";
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_DESCRIPTION = "course_description";
    private static final String CLOSE_ERROR_MSG = "ResultSet close error";
    private static final String CREATE_QUERY = "insert into school.students (group_id, first_name, last_name) values (?, ?, ?)";
    private static final String CREATE_STUDENTS_COURSES_QUERY = "insert into school.students_courses (student_id, course_id) values (?, ?)";
    private static final String FIND_BY_ID_QUERY = "select s.*, g.group_name, c.* from school.students s\n" +
            "left join school.groups g on g.group_id = s.group_id\n" +
            "left join school.students_courses sc on s.student_id = sc.student_id\n" +
            "left join school.courses c on sc.course_id = c.course_id\n" +
            "where s.student_id = ?";
    private static final String FIND_ALL_QUERY = "select s.*, g.group_name, c.* from school.students s\n" +
            "left join school.groups g on g.group_id = s.group_id\n" +
            "left join school.students_courses sc on s.student_id = sc.student_id\n" +
            "left join school.courses c on sc.course_id = c.course_id";
    private static final String COUNT_QUERY = "select count(*) from school.students";
    private static final String DELETE_BY_ID_QUERY = "delete from school.students where student_id = ?";
    private static final String DELETE_ALL = "delete from school.students";

    private final ConnectionManager connectionManager;

    public StudentRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Student save(Student entity) throws DAOException {
        Student student;
        Group group = null;
        Set<Course> courses = new HashSet<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        GroupRepository groupRepository = new GroupRepository(this.connectionManager);
        if (entity.getGroup() != null) {
            boolean existsGroup = groupRepository.existsById(entity.getGroup().getId());
            if (!existsGroup) {
                group = groupRepository.save(entity.getGroup());
            } else {
                group = entity.getGroup();
            }
        }

        CourseRepository courseRepository = new CourseRepository(this.connectionManager);
        for (Course course : entity.getCourses()) {
            boolean existsCourse = courseRepository.existsById(course.getId());
            if (!existsCourse) {
                courses.add(courseRepository.save(course));
            } else {
                courses.add(course);
            }
        }

        try (PreparedStatement preStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            if (entity.getGroup() == null) {
                preStatement.setNull(1, Types.INTEGER);
            } else {
                preStatement.setInt(1, entity.getGroup().getId());
            }
            preStatement.setString(2, entity.getFirstName());
            preStatement.setString(3, entity.getLastName());
            preStatement.execute();

            resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            student = new Student(entity.getFirstName(), entity.getLastName());
            student.setId(resultSet.getInt(STUDENT_ID));
            student.setGroup(group);
            student.setCourses(courses);
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot save the student", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        saveStudentsCourses(student);

        return student;
    }

    @Override
    public Optional<Student> findById(Integer integer) throws DAOException {
        Student student = null;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(FIND_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, integer);
            resultSet = preStatement.executeQuery();

            Set<Course> courses = new HashSet<>();
            boolean firstLine = false;
            while (resultSet.next()) {
                if (!firstLine) {
                    student = new Student(resultSet.getString(FIRST_NAME), resultSet.getString(LAST_NAME));
                    student.setId(resultSet.getInt(STUDENT_ID));

                    Group group = new Group();
                    group.setId(resultSet.getInt(GROUP_ID));
                    group.setName(resultSet.getString(GROUP_NAME));
                    student.setGroup(group);

                    firstLine = true;
                }

                Course course = new Course(resultSet.getString(COURSE_NAME));
                course.setId(resultSet.getInt(COURSE_ID));
                course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                courses.add(course);
            }

            if (student != null) {
                student.setCourses(courses);
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("cannot be found by id in the students", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return Optional.ofNullable(student);
    }

    @Override
    public boolean existsById(Integer integer) throws DAOException {
        Optional<Student> optStudent = this.findById(integer);

        return optStudent.isPresent();
    }

    @Override
    public List<Student> findAll() throws DAOException {
        List<Student> students = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(FIND_ALL_QUERY);

            int prevId = 0;
            Student student = new Student("", "");
            Set<Course> courses = new HashSet<>();
            boolean firstStudent = true;

            while (resultSet.next()) {
                int currentId = resultSet.getInt(STUDENT_ID);
                if (currentId != prevId) {

                    if (!firstStudent) {
                        student.setCourses(courses);
                        students.add(student);
                    }

                    firstStudent = false;

                    student = new Student(resultSet.getString(FIRST_NAME), resultSet.getString(LAST_NAME));
                    student.setId(currentId);

                    Group group = new Group();
                    group.setId(resultSet.getInt(GROUP_ID));
                    group.setName(resultSet.getString(GROUP_NAME));
                    student.setGroup(group);
                    courses = new HashSet<>();
                    prevId = currentId;
                }

                Course course = new Course(resultSet.getString(COURSE_NAME));
                course.setId(resultSet.getInt(COURSE_ID));
                course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                courses.add(course);

                if (resultSet.isLast()) {
                    student.setCourses(courses);
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be found all in the students", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return students;
    }

    @Override
    public long count() throws DAOException {
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(COUNT_QUERY);

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new DAOException("Cannot to count a students");
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot to count a students", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void deleteById(Integer integer) throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, integer);
            preStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted by id in the students", e);
        }
    }

    @Override
    public void delete(Student entity) throws DAOException {
        deleteById(entity.getId());
    }

    @Override
    public void deleteAll() throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute(DELETE_ALL);
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted all in the students", e);
        }
    }

    private void saveStudentsCourses(Student entity) throws DAOException {
        Connection connection = connectionManager.getConnection();

        for (Course course : entity.getCourses()) {
            try (PreparedStatement preStatement = connection.prepareStatement(CREATE_STUDENTS_COURSES_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preStatement.setInt(1, entity.getId());
                preStatement.setInt(2, course.getId());
                preStatement.execute();
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
                throw new DAOException("Cannot save the student_courses", e);
            }
        }
    }
}
