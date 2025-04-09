/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

/**
 *
 * @author User
 */
public class Administrator extends User{
    public Administrator(String userId, String username, String password) {
        super(userId, username, password, "Administrator");
    }
    public void manageuser(){
        System.out.println("maange user lo");
    }
}
