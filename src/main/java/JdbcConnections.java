import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public enum JdbcConnections {
    //odpowiednik JdbcConnections H2 = new JbdcConnections("org.h2.Driver","jdbc:h2:mem:testdb","sa","");
    H2("org.h2.Driver","jdbc:h2:mem:testdb","sa","");

    private String url;
    private String user;
    private String pass;
    private Connection connection;

    JdbcConnections(String driver, String url, String user, String pass)  {
        this.url = url;
        this.user = user;
        this.pass = pass;
        try {
            Class.forName(driver).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection get() throws SQLException {
        if (connection == null){
            connection = DriverManager.getConnection(url, user, pass);
        }
        return connection;
    }

    public void init() throws SQLException {
        String createTable = "CREATE TABLE user(" +
                "id int primary key, " +
                "login varchar(20), " +
                "pass varchar(20)" +
                ")";
        Statement st = get().createStatement();
        st.execute(createTable);
        int count = 0;
        count += st.executeUpdate("INSERT INTO user VALUES (1, 'kazik', '1234')");
        count += st.executeUpdate("INSERT INTO user VALUES (2, 'ada', '12345')");
        count += st.executeUpdate("INSERT INTO user VALUES (3, 'karol', 'ABCD')");
        if(count == 3){
            System.out.println("Wszystkie wiersze dodane!");
        }
    }
}
