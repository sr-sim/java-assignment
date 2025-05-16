/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
public abstract class User{
    protected String userId;
    protected String username;
    protected String password;
    protected String fullname;
    protected String email;
    protected String contact;
    protected Role role;
    protected boolean active;

    public User(String userID, String username, String password, String fullname, String email, String contact, Role role, boolean isActive) {
        this.userId = userID;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.contact = contact;
        this.role = role;
        this.active = isActive;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserID(String userID) {
        this.userId = userID;
    }

    public String getUsername() {
        return username;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }
}
