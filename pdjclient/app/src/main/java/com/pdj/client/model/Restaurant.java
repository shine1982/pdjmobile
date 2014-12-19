package com.pdj.client.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by fengqin on 14/11/13.
 */
@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {

    public static final String NAME="name";

    public static final String ADDRESS="address";
    public static final String POSTAL_CODE="postalCode";
    public static final String CITY="city";
    public static final String TELEPHONE="telephone";
    public static final String LOCATION="location";



    public String getName() {
        return getString(NAME);
    }

    public void setName(String name) {
        put(NAME,name);
    }

    public String getAddress() {
        return getString(ADDRESS);
    }

    public void setAddress(String address) {
        put(ADDRESS,address);
    }

    public String getPostalCode() {
        return getString(POSTAL_CODE);
    }

    public void setPostalCode(String postalCode) {
        put(POSTAL_CODE,postalCode);
    }

    public String getCity() {
        return getString(CITY);
    }

    public void setCity(String city) {
        put(CITY,city);
    }

    public String getTelephone() {
        return getString(TELEPHONE);
    }

    public void setTelephone(String telephone) {
        put(TELEPHONE,telephone);
    }

    public double getDistance(ParseGeoPoint geoPoint){
        return getParseGeoPoint(LOCATION).distanceInKilometersTo(geoPoint);
    }

    public String getDistanceLabel(ParseGeoPoint geoPoint){
        double distance = getDistance(geoPoint);
        BigDecimal bd = new BigDecimal(distance);
        int decimalPlaces = 3;  // the scale for the decimal
        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        DecimalFormat decFormat = new DecimalFormat("#.00", new DecimalFormatSymbols(Locale.ENGLISH));
        double formatDecimal = new Double(decFormat.format(distance)).doubleValue();
        return formatDecimal+" km";
    }

    public static Restaurant newRestaurantWithoutData(final String idResto){
        return (Restaurant)ParseObject.createWithoutData("Restaurant",idResto);
    }

    public static ParseQuery<Restaurant> getQuery() {
        return ParseQuery.getQuery(Restaurant.class);
    }

    public static ParseQuery<Restaurant> getQueryFromStr(String str){

            ParseQuery<Restaurant> queryCodePostal = getQuery();
            queryCodePostal.whereMatches(POSTAL_CODE, "("+str+")", "i");

            ParseQuery<Restaurant> queryCity = getQuery();
            queryCity.whereMatches(CITY, "("+str+")", "i");

            ParseQuery<Restaurant> queryName = getQuery();
            queryName.whereMatches(NAME,"("+str+")", "i");

            final List<ParseQuery<Restaurant>> queries = new ArrayList<ParseQuery<Restaurant>>();
            queries.add(queryCodePostal);
            queries.add(queryCity);
            queries.add(queryName);
            return ParseQuery.or(queries);
    }

    public ParseGeoPoint getLocation(){
        return getParseGeoPoint(LOCATION);
    }




}
