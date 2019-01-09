import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseOperator {
    static String url = "jdbc:postgresql://localhost:5432/MitsubishiControllerData";
    static String user = "postgres";
    static String pass = "123";


    public static void userCreator(List<String> arguments) {

        //args: 0 - login, 1 - password, 2 - firstName, 3 - secondName, 4 - email, 5 - position
        String query = "INSERT INTO \"UsersData\"(\"Login\", \"Password\", \"Имя\", \"Фамилия\", \"Email\", \"Должность\") VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, arguments.get(0));
            pst.setString(2, arguments.get(1));
            pst.setString(3, arguments.get(2));
            pst.setString(4, arguments.get(3));
            pst.setString(5, arguments.get(4));
            pst.setString(6, arguments.get(5));
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static List<String> nameReader() {

        List<String> list = new ArrayList<String>();

        String query = "SELECT \"Login\" FROM \"UsersData\"";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                list.add(rs.getString(1));

            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return list;

    }

    public static List<String> usersDataReader(String Login) {
        List<String> data = new ArrayList<>();

        String query = "SELECT * FROM \"UsersData\" WHERE \"Login\" = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, Login);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                for (int k = 1; k < 7; k++) {
                    data.add(rs.getString(k));
                }
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return data;
    }

    public static void usersDataChanger(List<String> arguments) {

        //args: 0 - login, 1 - password, 2 - firstName, 3 - secondName, 4 - email, 5 - position
        String query = "UPDATE \"UsersData\" SET \"Password\" = ?, \"Имя\" = ?, \"Фамилия\" = ?, \"Email\" = ?, \"Должность\" = ? WHERE \"Login\" = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, arguments.get(1));
            pst.setString(2, arguments.get(2));
            pst.setString(3, arguments.get(3));
            pst.setString(4, arguments.get(4));
            pst.setString(5, arguments.get(5));
            pst.setString(6, arguments.get(0));
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void userRemover(String Login) {

        String query = "DELETE FROM \"UsersData\" WHERE \"Login\" = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, Login);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public static Boolean logChecker(List<String> arguments) {

        //args: 0 - login, 1 - password
        Boolean response;

        String realPassword = null;
        String query = "SELECT \"Password\" FROM \"UsersData\" WHERE \"Login\" = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, arguments.get(0));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                realPassword = rs.getString(1);
            }


        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        try {
            response = realPassword.equals(arguments.get(1));
        } catch (NullPointerException ex) {
            response = false;
        }

        return response;

    }

    public static void moduleCreator(List<String> arguments) {
        //args: 0 - name, 1 - serial, 2 - date, 3 - maxTemperature, 4 - maxFrequency, 5 - maxMemory, 6 - updateFrequency

        String query = "INSERT INTO \"Modules\"(\"Name\", \"Serial\", \"Date\", \"MaxTemperature\", \"MaxFrequency\", \"MaxMemory\", \"UpdateFrequency\") VALUES(?, ?, ?, ?, ?, ?, ?); " +
                "CREATE TABLE \"" + arguments.get(0) + "Applications\" (\"ID\" serial NOT NULL, \"Заявитель\" text, \"Статус\" text, \"Дата\" text, \"Замена\" boolean, \"Приоритет\" integer, \"Информация\" text, CONSTRAINT \"" + arguments.get(0) + "_pkey\" PRIMARY KEY (\"ID\")) WITH (OIDS=FALSE);" +
                "CREATE TABLE \"" + arguments.get(0) + "Info\" (\"Дата\" text, \"Температура\" integer, \"Частота\" integer, \"Память\" integer, CONSTRAINT \"" + arguments.get(0) + "_pkey1\" PRIMARY KEY (\"Дата\")) WITH (OIDS=FALSE)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, arguments.get(0));
            pst.setString(2, arguments.get(1));
            pst.setString(3, arguments.get(2));
            pst.setInt(4, Integer.parseInt(arguments.get(3)));
            pst.setInt(5, Integer.parseInt(arguments.get(4)));
            pst.setInt(6, Integer.parseInt(arguments.get(5)));
            pst.setString(7, arguments.get(6));
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        int updateFrequency = 0;
        switch (arguments.get(6)) {
            case "Каждые полчаса": {
                updateFrequency = 30;
            }
            break;

            case "Каждый час": {
                updateFrequency = 60;
            }
            break;

            case "Каждые два часа": {
                updateFrequency = 120;
            }
            break;

            case "Каждый день": {
                updateFrequency = 24 * 60;
            }
            break;

            case "Каждые два дня": {
                updateFrequency = 48 * 60;
            }
            break;

            case "Каждую неделю": {
                updateFrequency = 24 * 7 * 60;
            }
            break;
        }
        List<Object[]> generatedInfo = new ArrayList<>();

        Random rnd = new Random(System.currentTimeMillis());
        int numberOfRows = 15 + rnd.nextInt(25 - 15 + 1);
        Date d = Calendar.getInstance().getTime();
        for (int i = 0; i < numberOfRows; i++) {
            Object[] data = new Object[4];
            data[0] = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm").format(d);
            data[1] = 25 + rnd.nextInt(99 - 25 + 1); // temperature
            data[2] = 15 + rnd.nextInt(106 - 15 + 1); // frequency
            data[3] = 20 + rnd.nextInt(1000 - 20 + 1); // memory
            generatedInfo.add(data);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.MINUTE, updateFrequency);
            d = cal.getTime();
        }

        query = "INSERT INTO \"" + arguments.get(0) + "Info\"(\"Дата\", \"Температура\", \"Частота\", \"Память\") VALUES(?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            for (int i = 0; i < generatedInfo.size(); i++) {
                pst.setString(1, generatedInfo.get(i)[0].toString());
                pst.setInt(2, Integer.parseInt(generatedInfo.get(i)[1].toString()));
                pst.setInt(3, Integer.parseInt(generatedInfo.get(i)[2].toString()));
                pst.setInt(4, Integer.parseInt(generatedInfo.get(i)[3].toString()));
                pst.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static List<Object[]> modulesInfoReader(String name) {

        List<Object[]> data = new ArrayList<>();
        String query = "SELECT * FROM \"" + name + "Info\"";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] data1 = new Object[4];
                for (int k = 1; k < 5; k++) {
                    data1[k - 1] = rs.getObject(k);
                }
                data.add(data1);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return data;
    }

    public static List<Object[]> modulesReader() {

        List<Object[]> data = new ArrayList<>();
        String query = "SELECT * FROM \"Modules\"";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] data1 = new Object[8];
                for (int k = 1; k < 9; k++) {
                    data1[k - 1] = rs.getObject(k);
                }
                data.add(data1);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return data;
    }

    public static void moduleRemover(List<String> arguments) {

        //args: 0 - row, 1 - name
        String query = "DELETE FROM \"Modules\" WHERE \"ID\" = ?; DROP TABLE \"" + arguments.get(1) + "Applications\"; DROP TABLE \"" + arguments.get(1) + "Info\"";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, Integer.parseInt(arguments.get(0)));
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public static List<String> modulesNamesReader() {

        List<String> list = new ArrayList<String>();

        String query = "SELECT \"Name\" FROM \"Modules\"";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString(1));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return list;
    }

    public static List<Object[]> applicationsReader(String name) {
        List<Object[]> data = new ArrayList<>();
        String query = "SELECT * FROM \"" + name + "Applications\"";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] data1 = new Object[7];
                for (int k = 1; k < 7; k++) {
                    data1[k - 1] = rs.getObject(k);
                }
                data.add(data1);
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return data;
    }

    public static void applicationCreator(List<String> arguments) {
        //args: 0 - module, 1 - login, 2 - status, 3 - date, 4 - change, 5 - priority, 6 - info
        String query = "INSERT INTO \"" + arguments.get(0) + "Applications\"(\"Заявитель\", \"Статус\", \"Дата\", \"Замена\", \"Приоритет\", \"Информация\") VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, arguments.get(1));
            pst.setString(2, arguments.get(2));
            pst.setString(3, arguments.get(3));
            pst.setBoolean(4, Boolean.valueOf(arguments.get(4)));
            pst.setInt(5, Integer.parseInt(arguments.get(5)));
            pst.setString(6, arguments.get(6));
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void applicationRemover(List<String> arguments) {
        //args: 0 - module, 1 - ID

        String query = "DELETE FROM \"" + arguments.get(0) + "Applications\" WHERE \"ID\" = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, Integer.parseInt(arguments.get(1)));
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static String applicationInfoReader(List<String> arguments) {
        //args: 0 - module, 1 - ID

        String data = null;
        String query = "SELECT \"Информация\" FROM \"" + arguments.get(0) + "Applications\" WHERE \"ID\" = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, Integer.parseInt(arguments.get(1)));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                data = rs.getString(1);
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return data;
    }

    public static void applicationDataChanger(List<String> arguments) {
        //args: 0 - module, 1 - status, 2 - info, 3 - ID
        String query = "UPDATE \"" + arguments.get(0) + "Applications\" SET \"Статус\" = ?, \"Информация\" = ? WHERE \"ID\" = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, arguments.get(1));
            pst.setString(2, arguments.get(2));
            pst.setInt(3, Integer.parseInt(arguments.get(3)));
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseOperator.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}