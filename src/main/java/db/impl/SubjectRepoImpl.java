package db.impl;

import db.SubjectRepo;
import h2.H2Context;
import model.Subject;
import res.SubjectType;
import res.WDay;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SubjectRepoImpl implements SubjectRepo {

    private H2Context h2base;

    public SubjectRepoImpl() {
        try {

            h2base = H2Context.getInstance();
            final Statement statement = createStatement();
            String createTableSqlStatement = "CREATE TABLE IF NOT EXISTS subjects " +
                    "(subject_id INTEGER not NULL," +
                    "subject_name VARCHAR(45)," +
                    "subject_link VARCHAR(255)," +
                    "tutor_name VARCHAR(45)," +
                    "PRIMARY KEY ( subject_id ));";

            statement.executeUpdate(createTableSqlStatement);

            statement.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

//    public Subject findByWeekDay(WDay weekDay) {
//        Connection conn = null;
//        Statement statement = null;
//
//        try {
//            Class.forName(JDBC_DRIVER);
//            System.out.println("Connecting to db.DB");
//            conn = DriverManager.getConnection(DB_URL, USERNAME, PASS);
//
//            statement = conn.createStatement();
//            String sql = "";
//
//            System.out.println();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        return new Subject();
//    }

    @Override
    public List<Subject> getSubjects() throws SQLException {
        String getSubjectsSqlStatement = "SELECT * FROM subjects;";
        final Statement statement = createStatement();
        final ResultSet resultSet = statement.executeQuery(getSubjectsSqlStatement);

        List<Subject> subjectList = new ArrayList<>();
        while(resultSet.next()) {

            subjectList.add(Subject.getByValues(
                    resultSet.getInt("subject_id"),
                    resultSet.getString("subject_name"),
                    resultSet.getString("subject_link"),
                    resultSet.getString("tutor_name")
            ));
        }
        resultSet.close();
        statement.close();

        return subjectList;
    }

    @Override
    public Subject getSubject(int id) throws SQLException {
        String getSubjectSqlStatement = "SELECT * from subjects WHERE subject_id = " + String.valueOf(id) + ";";
        final Statement statement = createStatement();
        final ResultSet resultSet = h2base.getConnection().createStatement().executeQuery(getSubjectSqlStatement);
        if(resultSet.first()) {
            final Subject byValues = Subject.getByValues(
                    resultSet.getInt("subject_id"),
                    resultSet.getString("subject_name"),
                    resultSet.getString("subject_link"),
                    resultSet.getString("tutor_name")
            );
            resultSet.close();
            statement.close();

            return byValues;
        } else throw new RuntimeException("Unable to find -> by Id");
    }

    @Override
    public Subject getSubject(String name) throws SQLException {
        String getSubjectSqlStatement = "SELECT * from subjects WHERE subject_name = " + name + ";";
        final Statement statement = createStatement();
        final ResultSet resultSet = statement.executeQuery(getSubjectSqlStatement);
        if(resultSet.first()) {
            final Subject byValues = Subject.getByValues(
                    resultSet.getInt("subject_id"),
                    resultSet.getString("subject_name"),
                    resultSet.getString("subject_link"),
                    resultSet.getString("tutor_name")
            );
            resultSet.close();
            statement.close();

            return byValues;
        } else throw new RuntimeException("Unable to find -> by Subject Name");
    }

    @Override
    public void create(Subject item) throws SQLException {
        final Statement statement = createStatement();
        String sqlStatement = "INSERT INTO subjects VALUES (" +
                + item.getId() + ", \'" +
                item.getName() + "\', \'" +
                item.getLink() + "\', \'" +
                item.getTutorName() + "\'" +
                ");";
        statement.executeUpdate(sqlStatement);
        statement.close();
    }

    @Override
    public void update(Subject item) throws SQLException {
        final Statement statement = createStatement();
        String sqlStatement = "UPDATE subjects" +
                " SET subject_name = \'" + item.getName() + "\', " +
                "subject_link = \'" + item.getLink() + "\', " +
                "tutor_name = \'" + item.getTutorName() + "\' " +
                "WHERE subject_id = " + item.getId() + ";";
        statement.executeUpdate(sqlStatement);
        statement.close();
    }

    @Override
    public void delete(int itemId) throws SQLException {
        final Statement statement = createStatement();
        String sqlStatement = "DELETE from subjects WHERE subject_id = " + itemId + ";";
        statement.executeUpdate(sqlStatement);
        statement.close();
    }

    // Private

    private Statement createStatement() throws SQLException {
        return h2base.getConnection().createStatement();
    }


}
