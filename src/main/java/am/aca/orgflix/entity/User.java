package am.aca.orgflix.entity;


import javax.persistence.*;
import java.util.Set;

/**
 * User entity class
 */
@Entity(name = "USERS")
@Table(name = "USERS")
public class User {
    //    @Id
//    @GeneratedValue
//    @Column(name = "ID", nullable = false)
    private int id = -1;

    //        @Column(name = "NICK", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String nick = "";

    //    @Column(name = "USER_NAME", columnDefinition = "VARCHAR(250)")
    private String userName = "";

    //    @Column(name = "USER_PASS", columnDefinition = "VARCHAR(50)", nullable = false)
    private String email = "";

    //    @Column(name = "EMAIL", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String pass = "";

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "?????", cascade=CascadeType.ALL)
//    private java.util.List<List> lists;

    private Set<List> lists;

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

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > 0) this.id = id;
    }

    @Column(name = "NICK", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Column(name = "USER_NAME", columnDefinition = "VARCHAR(250)")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "USER_PASS", columnDefinition = "VARCHAR(50)", nullable = false)
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Column(name = "EMAIL", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "key.user", cascade = CascadeType.ALL)
    public Set<List> getLists() {
        return lists;
    }

    public void setLists(Set<List> lists) {
        this.lists = lists;
    }
}
