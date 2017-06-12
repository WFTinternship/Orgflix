package am.aca.entity;

/**
 * Created by David on 5/31/2017
 */
public class Cast {
    private int id;
    private String name;
    private boolean hasOscar;

    public Cast() {
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
}
