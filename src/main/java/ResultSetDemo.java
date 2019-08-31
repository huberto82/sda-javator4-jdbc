import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResultSetDemo {
    public static void main(String[] args) throws SQLException {
        JdbcConnections.H2.init();
        Connection con = JdbcConnections.H2.get();
        //ustawiamy właściwości ResultSet: przewijanie w obu kierunkach i tylko do odczytu
        List<User> users = mapTableUserToUserList(con);
        con.createStatement().executeUpdate("INSERT INTO user VALUES (4, 'anonim', NULL)");
        System.out.println(users);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj id: ");
        int id = scanner.nextInt();
        System.out.println("Podaj login: ");
        String login = scanner.next();
        System.out.println("Podaj hasło: ");
        String pass = scanner.next();
        if (addNewUser(con, id, login, pass)){
            System.out.println("Zostałe pomyślnie dodany");
            users = mapTableUserToUserList(con);
            System.out.println(users);
        } else {
            System.out.println("Już taki użytkownik istnieje");
        }
        deleteUsersWithEmptyPass(con);
        System.out.println(mapTableUserToUserList(con));
    }

    private static void deleteUsersWithEmptyPass(Connection con) throws SQLException {
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet set = st.executeQuery("SELECT * FROM user");
        while(set.next()){
            String pass = set.getString("pass");
            //testujemy czy poprzednia wartość pobrana metodą get była w bazie wartością NULL
            if (set.wasNull()){
                System.out.println("W bazie był NULL");
                continue;
            }
            if (pass.isEmpty()){
                set.deleteRow();
            }
        }
        st.close();
    }
    private static boolean addNewUser(Connection con, int id, String login, String pass) throws SQLException {
        //ResultSet ze skrolowanie i modyfikacją
        Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet set = st.executeQuery("SELECT * FROM user");
        while(set.next()){
            if (id == set.getInt("id") || login.equals(set.getString("login"))){
                return false;
            }
        }
        set.beforeFirst();
        while(set.next()){
            //czy kursor wskazuje na pierwszy
            if (set.isFirst()) {
                //dodajemy nowy wiersz w ResultSet
                set.moveToInsertRow();
                //uatualniamy kolumny nowego wiersza
                set.updateInt(1, id);
                set.updateString(2, login);
                set.updateString(3, pass);
                //zapisanie nowego wiersza w bazie
                set.insertRow();
            }
        }
        return true;
    }
    private static List<User> mapTableUserToUserList(Connection con) throws SQLException {
        List<User> users = new ArrayList<>();
        Statement st = con.createStatement();
        ResultSet set = st.executeQuery("SELECT * FROM user");
        while(set.next()){
            int id = set.getInt(1);
            String login = set.getString(2);
            String pass = set.getString(3);
            User user = new User(id, login, pass);
            users.add(user);
        }
        return users;
    }
}
