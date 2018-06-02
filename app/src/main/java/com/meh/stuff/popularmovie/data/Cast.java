package com.meh.stuff.popularmovie.data;

public class Cast {

    public static final String BASE_CAST_KEY = "cast";

    public static final String CAST_ORDER_KEY = "order";
    public static final String CAST_CREDIT_ID_KEY = "credit_id";
    public static final String CAST_CHARACTER_KEY = "character";


    public static final String CAST_PERSON_ID_KEY = "id";
    public static final String CAST_PERSON_NAME_KEY = "name";
    public static final String CAST_PROFILE_PATH_KEY = "profile_path";

    private int order;
    private String creditId;
    private String character;

    private String personId;
    private String personName;
    private String profilePath;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
