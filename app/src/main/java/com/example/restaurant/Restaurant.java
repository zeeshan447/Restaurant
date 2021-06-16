package com.example.restaurant;

public class Restaurant {
    private String EmailID,Name,Phone,Password, Address, Capacity,ImageURL,RestaurantId,RandomUID;

    public Restaurant(String emailID, String name, String mobile, String password, String address, String capcity, String restaurantId, String randomUID,String imageURL) {
        EmailID = emailID;
        Name = name;
        Phone = mobile;
        Password = password;
        Address = address;
        Capacity = capcity;
        RestaurantId = restaurantId;
        RandomUID = randomUID;
        ImageURL = imageURL;

    }


    public Restaurant(String randomUId, String restaurantId, String imageURL) {
        ImageURL = imageURL;
        RestaurantId = restaurantId;
        RandomUID = randomUId;
    }


    public String getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        RestaurantId = restaurantId;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public Restaurant(){

    }

    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String mobile) {
        Phone = mobile;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAddress(){return Address;}
    public void setAddress(String address) {Address = address; }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
