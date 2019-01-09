package com.example.miskra.paybook;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miskra on 09.01.2019.
 */

class JsonTypes {

    void init(SharedPreferences paymentTypes){

        String jsonTypes = paymentTypes.getString("types", "");

        List<String> ptypes = new ArrayList<>();
        if (!jsonTypes.isEmpty()) {
            try {
                Log.e("TAG", "Json jsonTypes not empty");
                JSONObject jsonObj = new JSONObject(jsonTypes);

                // Getting JSON Array node
                JSONArray JSONarrTypes = jsonObj.getJSONArray("paymentTypes");

                // looping through All Contacts
                for (int i = 0; i < JSONarrTypes.length(); i++) {
                    ptypes.add(JSONarrTypes.getString(i));
                }


            } catch (final JSONException ex) {
                Log.e("TAG", "Json parsing error: " + ex.getMessage());
                ptypes.add("Groceries");
                ptypes.add("Car");
                ptypes.add("House");
                ptypes.add("Clothes");
                ptypes.add("Gifts");
                ptypes.add("Playtime");
                ptypes.add("Tech");
                ptypes.add("Children");
                ptypes.add("Work");

                jsonTypes = "{\"paymentTypes\":[ \"" + ptypes.get(0)+"\"";
                Log.e("TAG", "registered jsontypes: "+ jsonTypes);
                for (int i = 1; i < ptypes.size(); i++) {
                    jsonTypes = ",\"" + ptypes.get(i) + "\"";
                }
                jsonTypes = jsonTypes + "]}";



                SharedPreferences.Editor editor = paymentTypes.edit();
                editor.putString("types", jsonTypes);
                //editor.commit();
                editor.apply();
            }
        } else {

            ptypes.add("Groceries");
            ptypes.add("Car");
            ptypes.add("House");
            ptypes.add("Groceries");
            ptypes.add("Car");
            ptypes.add("House");
            ptypes.add("Clothes");
            ptypes.add("Gifts");
            ptypes.add("Playtime");
            ptypes.add("Tech");
            ptypes.add("Children");
            ptypes.add("Work");

            jsonTypes = "{\"paymentTypes\":[ \"" + ptypes.get(0)+"\"";
            Log.e("TAG", "registered jsontypes: "+ jsonTypes);
            for (int i = 1; i < ptypes.size(); i++) {
                jsonTypes = ",\"" + ptypes.get(i) + "\"";
            }
            jsonTypes = jsonTypes + "]}";



            SharedPreferences.Editor editor = paymentTypes.edit();
            editor.putString("types", jsonTypes);
            //editor.commit();
            editor.apply();
        }
    }
}
