package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Scanner;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?;";
    private static final String DELETE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public static User create(User user) {
        try (Connection conn = DbUtil.connect()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            //Pobieramy wstawiony do bazy identyfikator, a następnie ustawiamy id obiektu user.
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User read(int userId) throws SQLException {
        PreparedStatement preStmt =
                DbUtil.connect().prepareStatement("SELECT * FROM users WHERE id = ?", PreparedStatement.RETURN_GENERATED_KEYS);
        preStmt.setString(1, String.valueOf(userId));
        ResultSet rs = preStmt.executeQuery();
        while (rs.next()) {
            String firstName = rs.getString("username");
            String email = rs.getString("email");
            String password = rs.getString("password");
            int id = rs.getInt("id");
            System.out.println(id + " " + firstName + " " + email + " " + password);
        }


        return null;
    }

    public static void update(User user){
        try (Connection connection = DbUtil.connect();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USER_QUERY);
        ) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User[] findAll(){
        return new User[0];
    }

}
