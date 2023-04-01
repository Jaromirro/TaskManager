package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET email = ?, username = ?, password = ? WHERE id = ?;";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";
    private static final String FALL = "SELECT * FROM users";
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

    public static void delete(int userId) {
        try (PreparedStatement statement =
                     DbUtil.connect().prepareStatement(DELETE_USER_QUERY)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static User[] findAll(){

        try (PreparedStatement statement = DbUtil.connect().prepareStatement(FALL);
             ResultSet resultSet = statement.executeQuery();) {
            User[] users = new User[0];
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setEmail(resultSet.getString(2));
                user.setUserName(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));
                users = addToArray(user, users);

            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;



    }
    private static User[] addToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.
        return tmpUsers; // Zwracamy nową tablicę.
    }

}
