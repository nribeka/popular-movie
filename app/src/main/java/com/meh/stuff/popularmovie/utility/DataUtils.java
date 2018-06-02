package com.meh.stuff.popularmovie.utility;

import android.util.Log;

import com.meh.stuff.popularmovie.data.Cast;
import com.meh.stuff.popularmovie.data.Config;
import com.meh.stuff.popularmovie.data.Movie;
import com.meh.stuff.popularmovie.data.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class DataUtils {

    private static final String TAG = DataUtils.class.getSimpleName();
    private static final DateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private DataUtils() {
        // utility class, don't instantiate.
    }

    public static Person processPersonData(String personJsonString) throws JSONException {
        Person person = new Person();

        JSONObject personJson = new JSONObject(personJsonString);
        person.setId(personJson.getString(Person.PEOPLE_ID_KEY));
        person.setName(personJson.getString(Person.PEOPLE_NAME_KEY));
        person.setBiography(personJson.getString(Person.PEOPLE_BIOGRAPHY_KEY));
        person.setProfilePath(personJson.getString(Person.PEOPLE_PROFILE_PATH_KEY));

        Set<String> aliases = new TreeSet<>();
        JSONArray aliasesJson = personJson.getJSONArray(Person.PEOPLE_ALIASES_PATH_KEY);
        for (int i = 0; i < aliasesJson.length(); i++) {
            String alias = aliasesJson.getString(i);
            aliases.add(alias);
        }
        person.setAliases(aliases);

        person.setDob(safeParseDate(personJson.getString(Person.PEOPLE_DATE_OF_BIRTH_KEY)));
        person.setPlaceOfBirth(personJson.getString(Person.PEOPLE_PLACE_OF_BIRTH_KEY));

        return person;
    }

    private static Cast processCast(JSONObject castJson) throws JSONException {
        Cast cast = new Cast();

        cast.setOrder(castJson.getInt(Cast.CAST_ORDER_KEY));
        cast.setCreditId(castJson.getString(Cast.CAST_CREDIT_ID_KEY));
        cast.setCharacter(castJson.getString(Cast.CAST_CHARACTER_KEY));

        cast.setPersonId(castJson.getString(Cast.CAST_PERSON_ID_KEY));
        cast.setPersonName(castJson.getString(Cast.CAST_PERSON_NAME_KEY));
        cast.setProfilePath(castJson.getString(Cast.CAST_PROFILE_PATH_KEY));

        return cast;
    }

    public static List<Cast> processCreditsData(String creditsJsonString) throws JSONException {
        JSONObject creditsJson = new JSONObject(creditsJsonString);
        JSONArray castsJson = creditsJson.getJSONArray(Cast.BASE_CAST_KEY);

        List<Cast> casts = new ArrayList<>();
        for (int i = 0; i < castsJson.length(); i++) {
            JSONObject castJson = castsJson.getJSONObject(i);
            Cast cast = processCast(castJson);
            casts.add(cast);
        }

        return casts;
    }

    private static Movie processMovie(JSONObject movieJson) throws JSONException {
        Movie movie = new Movie();
        movie.setId(movieJson.getString(Movie.MOVIE_ID_KEY));
        movie.setTitle(movieJson.getString(Movie.MOVIE_TITLE_KEY));
        movie.setSynopsis(movieJson.getString(Movie.MOVIE_OVERVIEW_KEY));
        movie.setRating(movieJson.getString(Movie.MOVIE_VOTE_AVERAGE_KEY));
        movie.setPosterPath(movieJson.getString(Movie.MOVIE_POSTER_PATH_KEY));
        movie.setBackdropPath(movieJson.getString(Movie.MOVIE_BACKDROP_PATH_KEY));
        movie.setReleaseDate(safeParseDate(movieJson.getString(Movie.MOVIE_RELEASE_DATE_KEY)));
        return movie;
    }

    public static List<Movie> processMovieData(String moviesJsonString) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(moviesJsonString);
        JSONArray movieResults = moviesJson.getJSONArray(Movie.BASE_MOVIE_KEY);
        for (int i = 0; i < movieResults.length(); i++) {
            JSONObject movieResult = movieResults.getJSONObject(i);
            Movie movie = processMovie(movieResult);
            movies.add(movie);
        }

        return movies;
    }

    public static Config processConfigData(String configJsonString) throws JSONException {
        Config config = new Config();
        JSONObject configJson = new JSONObject(configJsonString);

        JSONObject imagesConfig = configJson.getJSONObject(Config.BASE_CONFIG_KEY);
        config.setBaseUrl(imagesConfig.getString(Config.IMAGE_BASE_URL));
        config.setSecureBaseUrl(imagesConfig.getString(Config.IMAGE_SECURE_BASE_URL));

        return config;
    }

    private static Date safeParseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, "Unable to parse date data.", e);
            return null;
        }
    }
}
