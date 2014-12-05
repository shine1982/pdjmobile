package com.pdj.client.model;

import java.io.Serializable;

/**
 * Created by fengqin on 14/12/5.
 */
public class RestaurantBO implements Serializable{

    private String id;
    private String name;
    private String address;
    private String postalCode;
    private String city;
    private String telephone;

    public RestaurantBO(final Restaurant restaurant){
        this.id=restaurant.getObjectId();
        this.name=restaurant.getName();
        this.address=restaurant.getAddress();
        this.city=restaurant.getCity();
        this.postalCode = restaurant.getPostalCode();
        this.telephone=restaurant.getTelephone();
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getCompleteAddress(){
        return getAddress()+","+getPostalCode()+","+getCity()+",France";
    }
}
