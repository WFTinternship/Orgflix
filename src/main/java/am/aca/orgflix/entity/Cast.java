package am.aca.orgflix.entity;

/**
 * Actor entity class
 */
public class Cast {
    private int id;
    private String name;
    private boolean hasOscar;

    public Cast() {
        this.id = -1;
        this.hasOscar = false;
    }

    public Cast(String name, boolean hasOscar) {
        this.name = name;
        this.hasOscar = hasOscar;
    }

    public Cast(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cast)) return false;

        Cast cast = (Cast) o;

        return id == cast.id && hasOscar == cast.hasOscar && name.equals(cast.name);
    }
}
