import java.sql.*;

public class JdbcDemo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        //ładowanie sterownika bazy
        Class.forName("org.h2.Driver").newInstance();
        //tworzenie połączenia do bazy przy pomocy DriverManagera
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        //wyłączamy auto commit
        connection.setAutoCommit(false);
        System.out.println(connection.getAutoCommit());
        //Tworzenie obiektu polecenia SQL
        Statement statement = connection.createStatement();

        String createTable = "CREATE TABLE user(" +
                "id int primary key, " +
                "login varchar(20), " +
                "pass varchar(20)" +
                ")";

        statement.execute(createTable);
        //początek transakcji jako savepoint
        connection.setSavepoint();
        statement.executeUpdate("INSERT INTO user VALUES (1, 'kazik', '1234')");
        //przed rollback dane są w bazie
        System.out.println("Dane w bazie przed rollback");
        ResultSet set = statement.executeQuery("SELECT * FROM user");
        while(set.next()){
            System.out.println(set.getString("login"));
        }
        //odwołujemy zmiany po savepoint
        connection.rollback();
        //po rollback danych już nie ma
        System.out.println("Dane w bazie po rollback");
        set = statement.executeQuery("SELECT * FROM user");
        while(set.next()){
            System.out.println(set.getString("login"));
        }
        connection.close();
    }
}
