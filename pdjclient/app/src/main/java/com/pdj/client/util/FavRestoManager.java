package com.pdj.client.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengqin on 14/11/30.
 */
public class FavRestoManager {

    private static final String FAVORITES="favorites";

    private static FavRestoManager favRestoManager = new FavRestoManager();
    private FavRestoManager(){
        super();
    }

    private SharedPreferences settings;

    public static FavRestoManager getInstance(Context context){
        favRestoManager.settings = context.getSharedPreferences("FavoriteRestos", 0);
        return favRestoManager;
    }

    public void addFavResto(String restoId){
        JSONObject jsonObject = getFavorites();
        try {
            JSONArray favorites = jsonObject.getJSONArray(FAVORITES);
            favorites.put(restoId);
            editCommit(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void editCommit(String result){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("json", result.toString());
        editor.commit();
    }

    private JSONObject getFavorites(){
        final String json = settings.getString("json", "{}");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray favorites;
            if(jsonObject.isNull(FAVORITES)){
                favorites = new JSONArray();
                jsonObject.put(FAVORITES,favorites);
            }
        } catch (JSONException e) {
            Log.d(FAVORITES, e.toString());
        }
        return jsonObject;
    }

    public void removeFavResto(String restoId){
        JSONObject jsonObject = getFavorites();
        try {
            JSONArray favorites = jsonObject.getJSONArray(FAVORITES);
            for(int i=0; i<favorites.length(); i++){
                if(favorites.get(i).equals(restoId)){
                    favorites = remove(i,favorites);
                    jsonObject.put(FAVORITES,favorites);
                    break;
                }
            }
            editCommit(jsonObject.toString());
        } catch (JSONException e) {
            Log.d(FAVORITES, e.toString());
        }
    }

    public boolean isRestoFav(String restoId){
       JSONObject jsonObject = getFavorites();
        try {
            JSONArray favorites = jsonObject.getJSONArray(FAVORITES);
            for(int i=0; i<favorites.length(); i++){
                if(favorites.get(i).equals(restoId)){
                   return true;
                }
            }
            return false;
        } catch (JSONException e) {
            Log.d(FAVORITES, e.toString());
        }
        return false;
    }


    public static JSONArray remove(final int idx, final JSONArray from) {
        final List<String> objs = asList(from);
        objs.remove(idx);

        final JSONArray ja = new JSONArray();
        for (final String obj : objs) {
            ja.put(obj);
        }

        return ja;
    }

    public List<String> getFavoriteRestoIds(){
        try {
            return asList(getFavorites().getJSONArray(FAVORITES));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    public static List<String> asList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<String> result = new ArrayList<String>(len);
        for (int i = 0; i < len; i++) {
            String obj=null;
            try {
                obj = ja.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }
}
