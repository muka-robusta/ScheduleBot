package db.impl;

import db.SubjectRepo;
import model.Subject;
import res.SubjectType;
import res.WDay;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SubjectRepoImpl implements SubjectRepo {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:~/test";

    private static final String USERNAME = "sa";
    private static final String PASS = "";

    private Connection connection;
    private Statement statement;

    public SubjectRepoImpl() {
        try {

            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();

            String createTableSqlStatement = "CREATE TABLE IF NOT EXISTS subjects " +
                    "(subject_id INTEGER not NULL," +
                    "subject_name VARCHAR(45)," +
                    "subject_link VARCHAR(255)," +
                    "tutor_name VARCHAR(45)," +
                    "PRIMARY KEY ( subject_id ));";

            statement.executeUpdate(createTableSqlStatement);


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

        return subjectList;
    }

    @Override
    public Subject getSubject(int id) throws SQLException {
        String getSubjectSqlStatement = "SELECT * from subjects WHERE subject_id = " + String.valueOf(id) + ";";
        final ResultSet resultSet = statement.executeQuery(getSubjectSqlStatement);
        if(resultSet.first()) {
            return Subject.getByValues(
                    resultSet.getInt("subject_id"),
                    resultSet.getString("subject_name"),
                    resultSet.getString("subject_link"),
                    resultSet.getString("tutor_name")
            );
        } else throw new RuntimeException("Unable to find -> by Id");
    }

    @Override
    public Subject getSubject(String name) throws SQLException {
        String getSubjectSqlStatement = "SELECT * from subjects WHERE subject_name = " + name + ";";
        final ResultSet resultSet = statement.executeQuery(getSubjectSqlStatement);
        if(resultSet.first()) {
            return Subject.getByValues(
                    resultSet.getInt("subject_id"),
                    resultSet.getString("subject_name"),
                    resultSet.getString("subject_link"),
                    resultSet.getString("tutor_name")
            );
        } else throw new RuntimeException("Unable to find -> by Subject Name");
    }

    @Override
    public void create(Subject item) throws SQLException {
        String sqlStatement = "INSERT INTO subjects VALUES (" +
                + UUID.randomUUID().hashCode() + ", " +
                item.getName() + ", " +
                item.getLink() + ", " +
                item.getTutorName() +
                ");";
        statement.executeUpdate(sqlStatement);
    }

    @Override
    public void update(Subject item) throws SQLException {
        String sqlStatement = "UPDATE subjects" +
                " SET subject_name = " + item.getName() + ", " +
                "subject_link = " + item.getLink() + ", " +
                "tutor_name = " + item.getTutorName() +
                "WHERE subject_id = " + item.getId() + ");";
        statement.executeUpdate(sqlStatement);
    }

    @Override
    public void delete(Subject item) throws SQLException {
        String sqlStatement = "DELETE from subjects WHERE subject_id = " + item.getId() + ");";
        statement.executeUpdate(sqlStatement);
    }

    public void closeConnection() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
