package am.aca.entity;

import am.aca.dao.DirectorDao;
import am.aca.dao.DirectorDaoJdbc;

/**
 * Created by David on 5/31/2017.
 */
public class Director {
    private int id;
    private String name;
    private boolean hasOscar;

    public Director() {
        this.id = -1;
        this.hasOscar = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasOscar() {
        return hasOscar;
    }

    public void setHasOscar(boolean hasOscar) {
        this.hasOscar = hasOscar;
    }

    public static void main(String[] args) {
        Director director = new Director();
        director.setName("Tarantino");

        DirectorDao directorDao = new DirectorDaoJdbc();

    }
}
