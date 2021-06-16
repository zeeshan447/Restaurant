package com.example.restaurant;

public class User {
    String Role, UserID;
    public User(String role,String userID)
    {
        Role=role;
        UserID = userID;
    }

public User (){

}
    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
