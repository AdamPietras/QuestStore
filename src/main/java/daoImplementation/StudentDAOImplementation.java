package daoImplementation;

import SQL.PostgreSQLJDBC;
import interfaces.StudentDAO;
import models.Coincubator;
import models.Item;
import models.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class StudentDAOImplementation implements StudentDAO {

    PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    Scanner scanner = new Scanner(System.in);

    @Override
    public List<Item> showItems() {
        String orderToSql = "SELECT * FROM items";
        List<Item> itemList = new ArrayList<>();
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int price = resultSet.getInt("price");
                String description = resultSet.getString("description");
                boolean isActive = resultSet.getBoolean("is_active");
                Item item = new Item(id, name, price, description, isActive);
                itemList.add(item);
                System.out.println(id+ "| " + name+ "| " + price+ "| " + description+ "| " + isActive);

            }
            preparedStatement.executeQuery();
        }catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }return itemList;
    }


    @Override
    public List<Coincubator> showCoincubatos() {
        String orderToSql = "SELECT * FROM coincubators";
        List<Coincubator> coincubatorsList = new ArrayList<>();
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                int currentDonation = resultSet.getInt("current_donation");
                int targetDonation = resultSet.getInt("target_donation");
                boolean isActive = resultSet.getBoolean("is_active");
                Coincubator coincubator = new Coincubator(id, name, description, currentDonation, targetDonation, isActive);
                coincubatorsList.add(coincubator);
                System.out.println(id+ "| " + name+ "| " + description+ "| " + currentDonation + "| " + targetDonation+ "| " + isActive);
            }
            preparedStatement.executeQuery();
        }catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }return coincubatorsList;
    }

    @Override
    public void showUserItems() {
        Student student = new Student(3);
        String orderToSql = "SELECT * FROM user_items JOIN items ON user_items.item_id = items.id WHERE user_items.id = ?";
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            preparedStatement.setInt(1, student.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                boolean isAvailable = resultSet.getBoolean("is_available");
                Date boughtDate = resultSet.getDate("bought_date");
                Date usedDate = resultSet.getDate("used_date");
                String itemName = resultSet.getString("name");
                String description = resultSet.getString("description");
                System.out.println("Your items:\n" +
                        "Item name  |   Description     |   Bought date     |   Used date   |   Is Available    \n" +
                        itemName + " | " + description + " | " + boughtDate + " | " + usedDate + " | " + isAvailable);
            }
            preparedStatement.executeQuery();

        }catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }
    }



    @Override
    public void showUserCoins() {
        String orderToSql = "SELECT coins FROM students WHERE id = ?";
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            preparedStatement.setInt(1, 1);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int coins = resultSet.getInt("coins");
                System.out.println("Your coins amount is: " + coins);
            }
            preparedStatement.executeQuery();
        }catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }
    }

    public Student isStudentWithIdInDB(int id) {
        Student student = null;

        String orderToSql = "SELECT * FROM users " +
                "join user_details " +
                "on users.user_details_id = user_details.id WHERE users.user_type_id = ?";

        try {

            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){

                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                int userTypeId = resultSet.getInt("user_type_id");
                boolean isActive = resultSet.getBoolean("is_active");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                student = new Student(id, login, password, userTypeId, isActive, firstName, lastName);

            }
            preparedStatement.executeQuery();
        }catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }
        return  student;
    }


    @Override
    public void showUserQuests() {

    }

    public void editStudent(Student student, int userDetailsId) {
        PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
        String orderForSql = ("UPDATE user_details SET login = ?, password = ?, first_name = ?, last_name = ? WHERE id = ?");

        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderForSql);
            preparedStatement.setString(1, student.getLogin());
            preparedStatement.setString(2, student.getPassword());
            preparedStatement.setString(3, student.getFirstname());
            preparedStatement.setString(4, student.getLastname());
            preparedStatement.setInt(5, userDetailsId);


            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public int getUserDetailsId(Student student) {
        String orderToSql = "SELECT * FROM users WHERE id = ?";
        try {
            preparedStatement = postgreSQLJDBC.connect().prepareStatement(orderToSql);
            preparedStatement.setInt(1, student.getId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int userDetailId = resultSet.getInt("user_details_id");
                return  userDetailId;
            }
            preparedStatement.executeQuery();
        }catch (SQLException e) {
            System.out.println(e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }
        return 0;
    }
}
//
//
