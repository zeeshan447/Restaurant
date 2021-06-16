package com.example.restaurant;

import java.util.Random;

public class FoodSupplyDetails {
    public String Dish,Price,Description,ImageURL,RandomUID,RestaurantId;

    public FoodSupplyDetails(String dish, String price, String description, String imageURL, String randomUID, String restaurantId) {
        Dish = dish;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID = randomUID;
        RestaurantId = restaurantId;
    }
    public FoodSupplyDetails(String randomUID){
        RandomUID = randomUID;
    }


}
