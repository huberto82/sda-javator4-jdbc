public class User {
    private int id;
    private String login;
    private String pass;

    public User(int id, String login, String pass) {
        this.id = id;
        this.login = login;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
