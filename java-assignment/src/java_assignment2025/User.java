/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
public class User {
    private String userId;
    private String username;
    private String password;
    private String contact;
    private String role;
    
    public User(String userId, String username, String password, String contact, String role){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.contact = contact;
        this.role = role;
    }
    public User() {
    }
    
    public String getUsername(){
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getContact(){
        return contact;
    }
    
    public String setContact(){
        return contact;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
