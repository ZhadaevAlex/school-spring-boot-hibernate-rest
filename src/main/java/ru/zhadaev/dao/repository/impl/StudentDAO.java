package ru.zhadaev.dao.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.zhadaev.dao.entities.Course;
import ru.zhadaev.dao.entities.Group;
import ru.zhadaev.dao.entities.Student;
import ru.zhadaev.dao.repository.CrudRepository;
import ru.zhadaev.exception.DAOException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
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
            "left join school.courses c on sc.course_id = c.course_id order by student_id";
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
    private static final String FIND_BY_ID_STUDENT_COURSE_QUERY = "select count(*) from school.students_courses where " +
            "student_id = ? AND " +
            "course_Id = ?";
    private static final Integer INTEGER = 4;
    private static final Integer VARCHAR = 12;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Student save(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(CREATE_QUERY, new String[]{STUDENT_ID});
                    ps.setString(1, student.getFirstName());
                    ps.setString(2, student.getLastName());
                    if (student.getGroup() == null) {
                        ps.setNull(3, Types.INTEGER);
                    } else {
                        ps.setInt(3, student.getGroup().getId());
                    }
                    return ps;
                }, keyHolder);
        int id = extractId(keyHolder);
        student.setId(id);
        return student;
    }

    @Override
    public Student update(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(UPDATE_QUERY, new String[]{STUDENT_ID});
                    ps.setString(1, student.getFirstName());
                    ps.setString(2, student.getLastName());
                    if (student.getGroup() == null) {
                        ps.setNull(3, Types.INTEGER);
                    } else {
                        ps.setInt(3, student.getGroup().getId());
                    }
                    ps.setInt(4, student.getId());
                    return ps;
                }, keyHolder);
        int id = extractId(keyHolder);
        student.setId(id);
        return student;
    }

    @Override
    public Optional<Student> findById(Integer id) {
        return Optional.ofNullable(jdbcTemplate.query(FIND_BY_ID_QUERY, new Object[]{id}, new int[]{INTEGER}, rs -> {
            Student studentDb = null;
            List<Course> courses = new ArrayList<>();
            boolean firstLine = false;
            try {
                while (rs.next()) {
                    if (!firstLine) {
                        studentDb = new Student();
                        studentDb.setId(rs.getInt(STUDENT_ID));
                        studentDb.setFirstName(rs.getString(FIRST_NAME));
                        studentDb.setLastName(rs.getString(LAST_NAME));

                        Group group = new Group();
                        group.setId(rs.getInt(GROUP_ID));
                        group.setName(rs.getString(GROUP_NAME));
                        studentDb.setGroup(group);

                        firstLine = true;
                    }

                    Course course = new Course();
                    course.setId(rs.getInt(COURSE_ID));
                    course.setName(rs.getString(COURSE_NAME));
                    course.setDescription(rs.getString(COURSE_DESCRIPTION));
                    courses.add(course);
                }

                if (studentDb != null) {
                    studentDb.setCourses(courses);
                }
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
                throw new DAOException("Cannot be found by id in the students", e);
            }

            return studentDb;
        }));
    }

    @Override
    public Optional<List<Student>> find(Student student) {
        return Optional.ofNullable(jdbcTemplate.query(FIND_QUERY, new Object[]{student.getFirstName(), student.getLastName()},
                new int[]{VARCHAR, VARCHAR}, rs -> {
                    List<Student> studentsDb = new ArrayList<>();
                    try {
                        int prevId = 0;
                        List<Course> courses = new ArrayList<>();
                        Student studentDb = null;
                        while (rs.next()) {
                            int id = rs.getInt(STUDENT_ID);
                            if (id != prevId) {
                                if (studentDb != null) {
                                    studentsDb.add(studentDb);
                                }

                                studentDb = new Student();
                                studentDb.setId(id);
                                studentDb.setFirstName(rs.getString(FIRST_NAME));
                                studentDb.setLastName(rs.getString(LAST_NAME));

                                Group group = new Group();
                                group.setId(rs.getInt(GROUP_ID));
                                group.setName(rs.getString(GROUP_NAME));
                                studentDb.setGroup(group);
                                courses = new ArrayList<>();
                            }

                            Course course = new Course();
                            course.setId(rs.getInt(COURSE_ID));
                            course.setName(rs.getString(COURSE_NAME));
                            course.setDescription(rs.getString(COURSE_DESCRIPTION));
                            courses.add(course);

                            if (studentDb != null) {
                                studentDb.setCourses(courses);
                            }

                            prevId = id;

                            if (rs.isLast()) {
                                studentsDb.add(studentDb);
                            }
                        }
                    } catch (SQLException e) {
                        logger.error(e.getLocalizedMessage());
                        throw new DAOException("The students cannot be found in students", e);
                    }

                    return studentsDb;
                }));
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, rs -> {
            List<Student> studentsDb = new ArrayList<>();
            try {
                int prevId = 0;
                List<Course> courses = new ArrayList<>();
                Student studentDb = null;
                while (rs.next()) {
                    int id = rs.getInt(STUDENT_ID);
                    if (id != prevId) {
                        if (studentDb != null) {
                            studentsDb.add(studentDb);
                        }

                        studentDb = new Student();
                        studentDb.setId(id);
                        studentDb.setFirstName(rs.getString(FIRST_NAME));
                        studentDb.setLastName(rs.getString(LAST_NAME));

                        Group group = new Group();
                        group.setId(rs.getInt(GROUP_ID));
                        group.setName(rs.getString(GROUP_NAME));
                        studentDb.setGroup(group);
                        courses = new ArrayList<>();
                    }

                    Course course = new Course();
                    course.setId(rs.getInt(COURSE_ID));
                    course.setName(rs.getString(COURSE_NAME));
                    course.setDescription(rs.getString(COURSE_DESCRIPTION));
                    courses.add(course);

                    if (studentDb != null) {
                        studentDb.setCourses(courses);
                    }

                    prevId = id;

                    if (rs.isLast()) {
                        studentsDb.add(studentDb);
                    }
                }
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
                throw new DAOException("Cannot be found all in the students", e);
            }

            return studentsDb;
        });
    }

    @Override
    public boolean existsById(Integer id) {
        Optional<Student> optStudent = this.findById(id);

        return optStudent.isPresent();
    }

    @Override
    public long count() {
        Integer count = jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class);
        return count == null ? 0 : count;
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update(DELETE_BY_ID_QUERY, id);
    }

    @Override
    public void delete(Student student) {
        jdbcTemplate.update(DELETE_QUERY, student.getFirstName(), student.getLastName(), student.getGroup().getId());
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(DELETE_ALL_QUERY);
    }

    public void signOnCourse(Integer studentId, Integer courseId) {
        jdbcTemplate.update(CREATE_STUDENTS_COURSES_QUERY, studentId, courseId);
    }

    public void removeFromCourse(Integer studentId, Integer courseId) {
        jdbcTemplate.update(DELETE_STUDENTS_COURSES_QUERY, studentId, courseId);
    }

    public boolean studentCourseIsExist(Integer studentId, Integer courseId) {
        Integer rows = jdbcTemplate.queryForObject(FIND_BY_ID_STUDENT_COURSE_QUERY,
                new Object[]{studentId, courseId}, new int[]{INTEGER, INTEGER}, Integer.class);
        return rows != null && rows > 0;
    }
}
