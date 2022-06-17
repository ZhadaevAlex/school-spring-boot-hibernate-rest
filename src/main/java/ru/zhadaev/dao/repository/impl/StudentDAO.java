package ru.zhadaev.dao.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.zhadaev.config.ConnectionManager;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.exception.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StudentDAO implements CrudRepository<Student, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(StudentDAO.class);
    private static final String STUDENT_ID = "student_id";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_DESCRIPTION = "course_description";
    private static final String CREATE_QUERY = "insert into school.students (first_name, last_name, group_id) values (?, ?, ?)";
    private static final String UPDATE_QUERY = "update school.students set (first_name, last_name, group_id) = (?, ?, ?) where student_id = ?";
    private static final String FIND_BY_ID_QUERY = "select s.*, g.group_name, c.* from school.students s\n" +
            "left join school.groups g on g.group_id = s.group_id\n" +
            "left join school.students_courses sc on s.student_id = sc.student_id\n" +
            "left join school.courses c on sc.course_id = c.course_id\n" +
            "where s.student_id = ?";
    private static final String FIND_QUERY = "select s.*, g.group_name, c.* from school.students s\n" +
            "left join school.groups g on g.group_id = s.group_id\n" +
            "left join school.students_courses sc on s.student_id = sc.student_id\n" +
            "left join school.courses c on sc.course_id = c.course_id where" +
            " s.first_name = ? AND" +
            " s.last_name = ?";
    private static final String FIND_ALL_QUERY = "select s.*, g.group_name, c.* from school.students s\n" +
            "left join school.groups g on g.group_id = s.group_id\n" +
            "left join school.students_courses sc on s.student_id = sc.student_id\n" +
            "left join school.courses c on sc.course_id = c.course_id";
    private static final String COUNT_QUERY = "select count(*) from school.students";
    private static final String DELETE_BY_ID_QUERY = "delete from school.students where student_id = ?";
    private static final String DELETE_QUERY = "delete from school.students where" +
            " school.students.first_name = ? AND" +
            " school.students.last_name = ?";
    private static final String DELETE_ALL_QUERY = "delete from school.students";
    private static final String CREATE_STUDENTS_COURSES_QUERY = "insert into school.students_courses (student_id, course_id) values (?, ?)";
    private static final String DELETE_STUDENTS_COURSES_QUERY = "delete from school.students_courses where" +
            " student_id = ? AND" +
            " course_id = ?";
    private static final String CLOSE_ERROR_MSG = "ResultSet close error";

    private final ConnectionManager connectionManager;

    @Autowired
    public StudentDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Student save(Student student) throws DAOException {
        Student studentDb = new Student();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, student.getFirstName());
            preStatement.setString(2, student.getLastName());
            if (student.getGroup() == null) {
                preStatement.setNull(3, Types.INTEGER);
            } else {
                preStatement.setInt(3, student.getGroup().getId());
            }
            preStatement.execute();

            resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            studentDb.setId(resultSet.getInt(STUDENT_ID));
            studentDb.setFirstName(student.getFirstName());
            studentDb.setLastName(student.getLastName());
            studentDb.setGroup(student.getGroup());
            studentDb.setCourses(student.getCourses());
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

        return studentDb;
    }

    @Override
    public Student update(Student student) throws SQLException {
        Student studentDb = new Student();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(UPDATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, student.getFirstName());
            preStatement.setString(2, student.getLastName());
            if (student.getGroup() == null) {
                preStatement.setNull(3, Types.INTEGER);
            } else {
                preStatement.setInt(3, student.getGroup().getId());
            }
            preStatement.execute();

            resultSet = preStatement.getGeneratedKeys();
            resultSet.next();

            studentDb.setId(resultSet.getInt(STUDENT_ID));
            studentDb.setFirstName(student.getFirstName());
            studentDb.setLastName(student.getLastName());
            studentDb.setGroup(student.getGroup());
            studentDb.setCourses(student.getCourses());
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot update the student", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return studentDb;
    }

    @Override
    public Optional findById(Integer id) throws DAOException {
        Student studentDb = null;
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(FIND_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, id);
            resultSet = preStatement.executeQuery();

            List<Course> courses = new ArrayList<>();
            boolean firstLine = false;
            while (resultSet.next()) {
                if (!firstLine) {
                    studentDb = new Student();
                    studentDb.setId(resultSet.getInt(STUDENT_ID));
                    studentDb.setFirstName(resultSet.getString(FIRST_NAME));
                    studentDb.setLastName(resultSet.getString(LAST_NAME));

                    Group group = new Group();
                    group.setId(resultSet.getInt(GROUP_ID));
                    group.setName(resultSet.getString(GROUP_NAME));
                    studentDb.setGroup(group);

                    firstLine = true;
                }

                Course course = new Course();
                course.setId(resultSet.getInt(COURSE_ID));
                course.setName(resultSet.getString(COURSE_NAME));
                course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                courses.add(course);
            }

            if (studentDb != null) {
                studentDb.setCourses(courses);
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

        return Optional.ofNullable(studentDb);
    }

    @Override
    public Optional<List<Student>> find(Student student) throws DAOException {
        List<Student> studentsDb = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (PreparedStatement preStatement = connection.prepareStatement(FIND_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, student.getFirstName());
            preStatement.setString(2, student.getLastName());
            preStatement.getGeneratedKeys();
            preStatement.execute();

            resultSet = preStatement.getResultSet();

            int prevId = 0;
            List<Course> courses = new ArrayList<>();
            Student studentDb = null;
            while (resultSet.next()) {
                int id = resultSet.getInt(STUDENT_ID);
                if (id != prevId) {
                    if (studentDb != null) {
                        studentsDb.add(studentDb);
                    }

                    studentDb = new Student();
                    studentDb.setId(id);
                    studentDb.setFirstName(resultSet.getString(FIRST_NAME));
                    studentDb.setLastName(resultSet.getString(LAST_NAME));

                    Group group = new Group();
                    group.setId(resultSet.getInt(GROUP_ID));
                    group.setName(resultSet.getString(GROUP_NAME));                   studentDb.setGroup(group);
                    courses = new ArrayList<>();
                }

                Course course = new Course();
                course.setId(resultSet.getInt(COURSE_ID));
                course.setName(resultSet.getString(COURSE_NAME));
                course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                courses.add(course);

                if (studentDb != null) {
                    studentDb.setCourses(courses);
                }

                prevId = id;

                if (resultSet.isLast()) {
                    studentsDb.add(studentDb);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("The students cannot be found in students", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                logger.error(CLOSE_ERROR_MSG, e.getLocalizedMessage());
            }
        }

        return Optional.ofNullable(studentsDb);
    }

    @Override
    public List<Student> findAll() throws DAOException {
        List<Student> studentsDb = new ArrayList<>();
        Connection connection = connectionManager.getConnection();
        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(FIND_ALL_QUERY);

            int prevId = 0;
            List<Course> courses = new ArrayList<>();
            Student studentDb = null;
            while (resultSet.next()) {
                int id = resultSet.getInt(STUDENT_ID);
                if (id != prevId) {
                    if (studentDb != null) {
                        studentsDb.add(studentDb);
                    }

                    studentDb = new Student();
                    studentDb.setId(id);
                    studentDb.setFirstName(resultSet.getString(FIRST_NAME));
                    studentDb.setLastName(resultSet.getString(LAST_NAME));

                    Group group = new Group();
                    group.setId(resultSet.getInt(GROUP_ID));
                    group.setName(resultSet.getString(GROUP_NAME));
                    studentDb.setGroup(group);
                    courses = new ArrayList<>();
                }

                Course course = new Course();
                course.setId(resultSet.getInt(COURSE_ID));
                course.setName(resultSet.getString(COURSE_NAME));
                course.setDescription(resultSet.getString(COURSE_DESCRIPTION));
                courses.add(course);

                if (studentDb != null) {
                    studentDb.setCourses(courses);
                }

                prevId = id;

                if (resultSet.isLast()) {
                    studentsDb.add(studentDb);
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

        int a = 2;
        return studentsDb;
    }

    @Override
    public boolean existsById(Integer id) throws DAOException {
        Optional<Student> optStudent = this.findById(id);

        return optStudent.isPresent();
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
    public void deleteById(Integer id) throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_BY_ID_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, id);
            preStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted by id in the students", e);
        }
    }

    @Override
    public void delete(Student student) throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setString(1, student.getFirstName());
            preStatement.setString(2, student.getLastName());
            preStatement.setInt(3, student.getGroup().getId());
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted in the students", e);
        }
    }

    @Override
    public void deleteAll() throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (Statement statement = connection.createStatement()) {
            statement.execute(DELETE_ALL_QUERY);
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot be deleted all in the students", e);
        }
    }

    public void signOnCourses(Student student, List<Course> courses) throws DAOException {
        Connection connection = connectionManager.getConnection();

        for (Course course : courses) {
            try (PreparedStatement preStatement = connection.prepareStatement(CREATE_STUDENTS_COURSES_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                preStatement.setInt(1, student.getId());
                preStatement.setInt(2, course.getId());
                preStatement.execute();
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
                throw new DAOException("Cannot save the student_courses", e);
            }
        }
    }

    public void removeFromCourse(Student student, Course course) throws DAOException {
        Connection connection = connectionManager.getConnection();

        try (PreparedStatement preStatement = connection.prepareStatement(DELETE_STUDENTS_COURSES_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            preStatement.setInt(1, student.getId());
            preStatement.setInt(2, course.getId());
            preStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
            throw new DAOException("Cannot delete from the student_courses", e);
        }
    }
}
