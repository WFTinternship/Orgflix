package am.aca.entity;


/**
 * User entity class
 */
public class User {
    private int id = -1;
    private String nick = "";
    private String userName = "";
    private String email = "";
    private String pass = "";

    public User(String nick, String userName, String email, String pass) {
        this.nick = nick;
        this.userName = userName;
        this.email = email;
        this.pass = pass;
    }
    public User(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getNick().equals(user.getNick()) && getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getNick().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nick='" + nick + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getPass() { return pass; }

    public void setPass(String pass) { this.pass = pass; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(id > 0) this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
