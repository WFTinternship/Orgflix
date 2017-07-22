package am.aca.orgflix.entity.valodik;

import javax.persistence.*;

/**
 * Created by karin on 7/22/2017.
 */
@Entity
@Table(name = "casts", schema = "orgflix", catalog = "")
public class CastsEntity {
    private int id;
    private String actorName;
    private byte hasOscar;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ACTOR_NAME")
    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    @Basic
    @Column(name = "HAS_OSCAR")
    public byte getHasOscar() {
        return hasOscar;
    }

    public void setHasOscar(byte hasOscar) {
        this.hasOscar = hasOscar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CastsEntity that = (CastsEntity) o;

        if (id != that.id) return false;
        if (hasOscar != that.hasOscar) return false;
        if (actorName != null ? !actorName.equals(that.actorName) : that.actorName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (actorName != null ? actorName.hashCode() : 0);
        result = 31 * result + (int) hasOscar;
        return result;
    }
}
