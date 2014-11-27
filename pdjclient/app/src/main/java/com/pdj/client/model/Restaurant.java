package com.pdj.client.model;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by fengqin on 14/11/13.
 */
@ParseClassName("Restaurant")
public class Restaurant extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    public String getAddress() {
        return getString("address");
    }

    public void setAddress(String address) {
        put("address",address);
    }

    public String getPostalCode() {
        return getString("postalCode");
    }

    public void setPostalCode(String postalCode) {
        put("postalCode",postalCode);
    }

    public String getCity() {
        return getString("city");
    }

    public void setCity(String city) {
        put("city",city);
    }

    public String getTelephone() {
        return getString("telephone");
    }

    public void setTelephone(String telephone) {
        put("telephone",telephone);
    }

    public static Restaurant newRestaurantWithoutData(final String idResto){
        return (Restaurant)ParseObject.createWithoutData("Restaurant",idResto);
    }

    public static ParseQuery<Restaurant> getQuery() {
        return ParseQuery.getQuery(Restaurant.class);
    }


}
