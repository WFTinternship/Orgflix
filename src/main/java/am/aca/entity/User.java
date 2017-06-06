package am.aca.entity;

import am.aca.dao.impljdbc.UserDaoJdbc;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by David on 5/26/2017
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

    public boolean checkFileds(){
        return nick != null && nick.length()!=0 &&
                email != null && email.length() !=0 &&
                pass != null && pass.length() != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!getNick().equals(user.getNick())) return false;
        return getEmail().equals(user.getEmail());
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
