package com.meh.stuff.popularmovie.data;

import java.util.Date;
import java.util.Set;

public class Person {

    public static final String PEOPLE_ID_KEY = "id";
    public static final String PEOPLE_NAME_KEY = "name";
    public static final String PEOPLE_BIOGRAPHY_KEY = "biography";
    public static final String PEOPLE_PROFILE_PATH_KEY = "profile_path";
    public static final String PEOPLE_ALIASES_PATH_KEY = "also_known_as";
    public static final String PEOPLE_DATE_OF_BIRTH_KEY = "birthday";
    public static final String PEOPLE_PLACE_OF_BIRTH_KEY = "place_of_birth";

    private String id;
    private String name;
    private String biography;
    private String profilePath;
    private Set<String> aliases;

    private Date dob;
    private String placeOfBirth;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }
}
