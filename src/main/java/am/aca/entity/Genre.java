package am.aca.entity;

/**
 * Created by karine on 6/3/2017
 */
public enum Genre {
    FAMILY (1, "Family"),
    HISTORY (2, "History"),
    MUSIC (3, "Music"),
    MYSTERY (4, "Mystery"),
    SCI_FI (5, "Sci-Fi"),
    THRILLER (6, "Thriller"),
    WESTERN (7, "Western"),
    ACTION (8, "Action"),
    ANIMATION (9, "Animation"),
    COMEDY (10, "Comedy"),
    DOCUMENTARY (11, "Documentary"),
    ADVENTURE (12, "Adventure"),
    BIOGRAPHY (13, "Biography"),
    CRIME (14, "Crime"),
    DRAMA (15, "Drama"),
    FANTASY (16, "Fantasy"),
    HORROR (17, "Horror"),
    MUSICAL (18, "Musical"),
    ROMANCE (19, "Romance"),
    SPORT (20, "Sport"),
    WAR (21, "War");

    Genre(final int value, final String title) {
        this.value = value;
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    private int value;
    private String title;
}
