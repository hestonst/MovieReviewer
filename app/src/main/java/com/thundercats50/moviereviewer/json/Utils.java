package com.thundercats50.moviereviewer.json;

import org.json.JSONObject;

/**
 * Created by Tom on 2/24/2016.
 */
public class Utils {
    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }
}
