package DAO;

import SQL.PostgreSQLJDBC;
import models.Coincubator;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import services.*;
import java.util.Calendar;
import java.util.List;


public class CoincubatorDAO {
    StudentDAO studentDAO = new StudentDAO();
    StudentService studentService = new StudentService();
    PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public void addCoincubator(Coincubator coincubator) {
        String sqlQuery = "INSERT INTO coincubators (name, description, current_donation, target_donation, is_active) VALUES(?, ?, ?, ?, ?) ";
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(sqlQuery);

            preparedStatement.setString(1, coincubator.getName());
            preparedStatement.setString(2, coincubator.getDescription());
            preparedStatement.setInt(3, coincubator.getCurrentDonation());
            preparedStatement.setInt(4, coincubator.getTargetDonation());
            preparedStatement.setBoolean(5, coincubator.isActive());


            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAllCoincubatorsFromDb(){
        String orderToSql = "SELECT * FROM coincubators " +
                "WHERE is_active = true;";
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            resultSet = preparedStatement.executeQuery();
            //preparedStatement.executeQuery();
        }catch (SQLException e) {
            System.out.println(e);

        }
        return  resultSet;
    }

    public Coincubator isCoincubatorWithIdInDB(int id){
        Coincubator coincubator = null;

        String orderToSql = "SELECT * FROM coincubators " +
                "WHERE id = ?;";

        try {

            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int currentDonation = resultSet.getInt("current_donation");
                int targetDonation = resultSet.getInt("target_donation");
                boolean isActive = resultSet.getBoolean("is_active");
                coincubator = new Coincubator(id,name,description,currentDonation, targetDonation,isActive);

            }
            preparedStatement.executeQuery();
        }catch (SQLException e) {
            System.out.println(e);
        }
        return  coincubator;
    }

    public void ediCoincubator(Coincubator coincubator) {
        PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
        String orderForSql = ("UPDATE coincubators SET name = ?, description = ?, target_donation = ? WHERE id = ?");

        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderForSql);
            preparedStatement.setString(1, coincubator.getName());
            preparedStatement.setString(2, coincubator.getDescription());
            preparedStatement.setInt(3, coincubator.getTargetDonation());
            preparedStatement.setInt(4, coincubator.getId());


            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void deleteCoincubator(Coincubator coincubator) {
        PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
        String orderForSql = ("UPDATE coincubators SET is_active = ? WHERE id = ?");

        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderForSql);
            preparedStatement.setBoolean(1, coincubator.isActive());
            preparedStatement.setInt(2, coincubator.getId());


            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public ResultSet payCoinsToCoincubator(){
        try {
            PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
            String orderToSql ="UPDATE coincubators SET current_donation = ? WHERE id = ?";
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }return resultSet;

    }
    public void payCoinsToCoincubator2(int coincubatorId, int coinAmount) {
        try {
            int newCurrentDonation = resultSet.getInt("current_donation") + coinAmount;
            preparedStatement.setInt(1, newCurrentDonation);
            preparedStatement.setInt(2, coincubatorId);
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void payCoinsToCoincubator(int coincubatorId, int coinAmount) {
        PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
        String orderToSql ="UPDATE coincubators SET current_donation = ? WHERE id = ?";
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            int newCurrentDonation = resultSet.getInt("current_donation") + coinAmount;
            preparedStatement.setInt(1, newCurrentDonation);
            preparedStatement.setInt(2, coincubatorId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public void donateToCoincubatorDb(int studentId, int coincubatorId, int coinAmount) {
        PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
        String orderToSql = "SELECT current_donation, target_donation FROM coincubators WHERE id = ?";
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            preparedStatement.setInt(1, coincubatorId);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int currentDonation = resultSet.getInt("current_donation");
                int targetDonation = resultSet.getInt("target_donation");
                int coinsToPay = coinAmount;
                //if user gave too many coins
                if ((currentDonation+coinAmount)>targetDonation){
                    coinsToPay = coinAmount - (currentDonation + coinAmount - targetDonation);
                }
                payCoinsToCoincubator(coincubatorId, coinsToPay);
                studentDAO.takeCoinsFromStudent(studentId, coinsToPay);
                addDonor(studentId, coincubatorId, coinsToPay);
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Date getCurrentDate() {
        return new Date(Calendar.getInstance().getTime().getTime());
    }

    public void addDonor(int userId, int coincubatorId, int donation){
        String sqlQuery = "INSERT INTO donators (coincubator_id, user_id, donation, donation_date) VALUES(?, ?, ?, ?)";
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(sqlQuery);
            preparedStatement.setInt(1, coincubatorId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, donation);
            preparedStatement.setDate(4, getCurrentDate());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
