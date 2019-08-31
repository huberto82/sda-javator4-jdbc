import java.sql.*;
import java.util.Scanner;

public class SQLInjectionDemo {
    public static void main(String[] args) throws SQLException {
        JdbcConnections.H2.init();
        Connection con = JdbcConnections.H2.get();
        Statement st = con.createStatement();
        printAll(st);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Szukanie podatne na SQL injection");
        System.out.println("Podaj id szukanego user'a");
        System.out.println("Spróbuj wpisać zmiast liczby frazę: 1 or 1 = 1");
        printWithSqlInjection(con, scanner.nextLine());
        System.out.println("Bezpieczne szukanie user'a z użyciem prepared statement");
        System.out.println("Wpisz id user'a:");
        if (scanner.hasNextInt()) {
            int id = scanner.nextInt();
            print(con, id);
        } else {
            System.out.println("Wpisz poprawną wartość całkowitą!!!");
        }
        System.out.println("Szukanie na podstawie loginu. Możesz użyć symboli specjalnych: % i ?");
        System.out.println("Wpisz login:");
        if (scanner.hasNext()) {
            String login = scanner.next();
            printByLogin(con, login);
        } else {
            System.out.println("Wpisz login!!!");
        }
    }

    private static void printAll(Statement st) throws SQLException {
        ResultSet set = st.executeQuery("SELECT * FROM user");
        while (set.next()){
            System.out.println("id: " + set.getInt("id"));
            System.out.println("login: " + set.getString("login"));
            System.out.println("pass: " + set.getString("pass"));
        }
    }

    private static void print(Connection con, int id) throws SQLException {
        //w miejscu znaku zapytania należy wstawić parametr
        PreparedStatement st = con.prepareStatement("SELECT * FROM user where id = ?");
        //wstawienie parametru w miejscu pierwszego znaku zapytania
        st.setInt(1, id);
        ResultSet set = st.executeQuery();
        while (set.next()){
            System.out.println(set.getString("login"));
        }
    }

    private static void printWithSqlInjection(Connection con, String idStr) throws SQLException {
        //parametr zapytania doklejamy do łańcucha zapytania
        PreparedStatement st = con.prepareStatement("SELECT * FROM user where id = " + idStr);
        ResultSet set = st.executeQuery();
        while (set.next()){
            System.out.println(set.getString("login"));
        }
    }

    private static void printByLogin(Connection con, String login) throws SQLException {
        //w miejscu znaku zapytania należy wstawić parametr
        PreparedStatement st = con.prepareStatement("SELECT * FROM user WHERE login like ?");
        //wstawienie parametru w miejscu pierwszego znaku zapytania
        st.setString(1, login);
        ResultSet set = st.executeQuery();
        while (set.next()){
            System.out.println(set.getString("login"));
        }
    }
}
