package db;

import model.Subject;

import java.sql.SQLException;
import java.util.List;

public interface SubjectRepo {
    List<Subject> getSubjects() throws SQLException;
    Subject getSubject(int id) throws SQLException;
    Subject getSubject(String name) throws SQLException;
    void create(Subject item) throws SQLException;
    void update(Subject item) throws SQLException;
    void delete(Subject item) throws SQLException;
}
