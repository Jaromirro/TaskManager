package pl.coderslab;

import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static String inputS(){
        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextLine()) {
            scanner.next();
        }
        String result = scanner.nextLine();

        return result;

    }
    public static int input(){
        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextLine()) {
            scanner.next();
        }
        int number = scanner.nextInt();

        return number;
    }

    public static void main(String[] args) throws SQLException {
        try(Connection connect = DbUtil.connect()) {
            System.out.println("Successfully connected to database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        boolean exit = false;
        while (!exit) {
            System.out.println("Co chcesz zrobic?");
            System.out.println("n - nowy użytkownik, " +
                    "w - wczytaj id, e - edycja, u - usun, f - findAll, x - koniec programu");
            String action = inputS();
            switch (action) {

                case "n":
                    System.out.println("Podaj maila: ");
                    User.setEmail(inputS());
                    System.out.println("Podaj nazwe uzytkownika: ");
                    User.setUserName(inputS());
                    System.out.println("Podaj hasło: ");
                    User.setPassword(UserDao.hashPassword(inputS()));
                    System.out.println(User.getPassword());
                    User user = new User(User.getEmail(),User.getUserName(), User.getPassword());
                    UserDao.create(user);


                    break;


                case "w":
                    System.out.println("Podaj id wiersza: ");
                    UserDao.read(User.setId(input()));
                    System.out.println(User.getId());



                    break;

                case "e":
                    System.out.println("Podaj id wiersza: ");
                    User.setId(input());
                    System.out.println("Podaj nową nazwę: ");
                    User.setUserName(inputS());
                    System.out.println("Podaj nowego maila: ");
                    User.setEmail(inputS());
                    System.out.println("Podaj nowe hasło:");
                    User.setPassword(UserDao.hashPassword(inputS()));
                    User user2 = new User(User.getEmail(),User.getUserName(), User.getPassword());
                    UserDao.update(user2);



                    break;
                case "u":
                    System.out.println("Podaj id wiersza: ");
                    int id1 = input();
                    System.out.println("Czy checesz usunac wiersz o id: " + id1 + " T/N?");
                    String app = inputS();
//
//                    if("T".equals(app)){
//                        UserDao..remove(connection,"cinemas", id1);
//                    }else if("N".equals(app)){
//                        System.out.println("STOP");
//                    }
//                    outputTable(connection);
//                    break;
                case "x":
                    exit = true;
                    break;
                default:
                    System.out.println("Brak wyboru");
                    break;
            }
        }
}


    }
