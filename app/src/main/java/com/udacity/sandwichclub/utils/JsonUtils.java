package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private static final String KEY_NAME = "name";
    private static final String KEY_MAIN_NAME = "mainName";
    private static final String KEY_ALSO_KNOWN_AS = "alsoKnownAs";

    private static final String KEY_PLACE_OF_ORIGIN = "placeOfOrigin";

    private static final String KEY_DESCRIPTION = "description";

    private static final String KEY_IMAGE = "image";

    private static final String KEY_INGREDIENTS = "ingredients";

    public static Sandwich parseSandwichJson(String json) {
        try {
            JSONObject sandwichObject = new JSONObject(json);

            JSONObject nameObject = sandwichObject.getJSONObject(KEY_NAME);
            String mainName = nameObject.getString(KEY_MAIN_NAME);
            List<String> alsoKnownAs = getStringListFromJsonArray(nameObject.getJSONArray(KEY_ALSO_KNOWN_AS));

            String placeOfOrigin = sandwichObject.getString(KEY_PLACE_OF_ORIGIN);

            String description = sandwichObject.getString(KEY_DESCRIPTION);

            String image = sandwichObject.getString(KEY_IMAGE);

            List<String> ingredients = getStringListFromJsonArray(sandwichObject.getJSONArray(KEY_INGREDIENTS));

            Sandwich sandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
            Log.d(TAG, "parseSandwichJson: " + sandwich.toString());

            return sandwich;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> getStringListFromJsonArray(JSONArray jsonArray) throws JSONException {
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            stringList.add(jsonArray.getString(i));
        }
        return stringList;
    }
}