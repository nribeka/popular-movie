package com.meh.stuff.popularmovie.utility;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppUtils {

    private static final String TAG = AppUtils.class.getSimpleName();
    private static final String APPLICATION_PROPERTIES = "local.properties";

    private AppUtils() {
        // utility class, don't instantiate.
    }

    public static Properties loadApplicationProperties(Context context) {
        Properties properties = new Properties();

        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(APPLICATION_PROPERTIES);
            properties.load(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "Unable to load application properties file.", e);
        } finally {
            quietlyClose(inputStream);
        }
        return properties;
    }

    private static void quietlyClose(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            // silently ignore the failure to close the stream.
        }
    }
}
