package am.aca.orgflix.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User entity class
 */
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private int id = -1;
    @Column(name = "NICK", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String nick = "";
    @Column(name = "USER_NAME", columnDefinition = "VARCHAR(250)")
    private String userName = "";
    @Column(name = "USER_PASS", columnDefinition = "VARCHAR(50)", nullable = false)
    private String email = "";
    @Column(name = "EMAIL", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String pass = "";

    public User(String nick, String userName, String email, String pass) {
        this.nick = nick;
        this.userName = userName;
        this.email = email;
        this.pass = pass;
    }

    public User() {
    }

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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > 0) this.id = id;
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
