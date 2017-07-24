package am.aca.orgflix.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * Actor entity class
 */
@Entity
@Table(name = "CASTS")
public class Cast {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;
    @Column(name = "ACTOR_NAME", nullable = false)
    private String name;
    @Column (name = "HAS_OSCAR")
    private boolean hasOscar;

    public Cast() {
//        this.id = -1;
//        this.hasOscar = false;
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
}
