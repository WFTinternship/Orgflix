package am.aca.orgflix.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * Actor entity class
 */
@Entity(name = "CASTS")
@Table(name = "CASTS")
public class Cast {
    //    @Id
//    @GeneratedValue
//    @Column(name = "ID")
    private int id;

    //    @Column(name = "ACTOR_NAME", columnDefinition = "VARCHAR(250)", nullable = false)
    private String name;

    //    @Column(name = "HAS_OSCAR", columnDefinition = "BOOLEAN", nullable = false)
//    @ColumnDefault("false")
    private boolean hasOscar;

    //    @ManyToMany(mappedBy = "casts", cascade = CascadeType.REFRESH)
//    private List<Film> films;

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

    @Id
    @GeneratedValue
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "ACTOR_NAME", columnDefinition = "VARCHAR(250)", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "HAS_OSCAR", columnDefinition = "BOOLEAN", nullable = false)
    @ColumnDefault("false")
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
