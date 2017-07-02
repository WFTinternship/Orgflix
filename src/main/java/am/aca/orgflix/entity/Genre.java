package am.aca.orgflix.entity;

/**
 * Fixed enumeration for types of film genres
 */
@SuppressWarnings("unused")
public enum Genre {
    FAMILY(1, "Family"),
    HISTORY(2, "History"),
    MUSIC(3, "Music"),
    MYSTERY(4, "Mystery"),
    SCI_FI(5, "Sci-Fi"),
    THRILLER(6, "Thriller"),
    WESTERN(7, "Western"),
    ACTION(8, "Action"),
    ANIMATION(9, "Animation"),
    COMEDY(10, "Comedy"),
    DOCUMENTARY(11, "Documentary"),
    ADVENTURE(12, "Adventure"),
    BIOGRAPHY(13, "Biography"),
    CRIME(14, "Crime"),
    DRAMA(15, "Drama"),
    FANTASY(16, "Fantasy"),
    HORROR(17, "Horror"),
    MUSICAL(18, "Musical"),
    ROMANCE(19, "Romance"),
    SPORT(20, "Sport"),
    WAR(21, "War");

    private int value;
    private String title;

    Genre(final int value, final String title) {
        this.value = value;
        this.title = title;
    }

    public static Genre getByTitle(String title) {
        if ("Sci-Fi".equals(title))
            return valueOf("SCI_FI");
        else return valueOf(title.toUpperCase());
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }
}
