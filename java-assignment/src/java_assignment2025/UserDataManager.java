/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ASUS
 */
public class UserDataManager {
    private final List<User> users;
    private int currentIndex;
    private static final String FILEPATH = "src/java_assignment2025/user.txt";

    public UserDataManager() {
        this.users = loadAllUsers();
    }

    private List<User> loadAllUsers(){
        List<User> userList = new ArrayList<>();
        for(String line : TextFile.readFile(FILEPATH)){
            String[] parts = line.split("`", 8);
            if(parts.length == 8){
                userList.add(createUserFromData(parts));
                System.out.println(parts.length == 0 ? "Nothing here" : parts[1]);
            }
        }
        this.currentIndex = userList.isEmpty() ? 0 : 
                        extractNumberFromID(userList.get(userList.size() - 1).getUserId());
        return userList;
    }
    
    private void writeAllUsers(List<User> users) {
        TextFile.writeToFile(FILEPATH, users, user -> 
            user.getUserId() + "`" +
            user.getUsername() + "`" +
            user.getPassword() + "`" +
            user.getFullname() + "`" +
            user.getEmail() + "`" +
            user.getContact() + "`" +
            user.getRole().getRoleAbbr() + "`" +
            (user.isActive() ? "active" : "deleted")
        );
    }
    
    public List<User> getAllActiveUsers(){
        return users.stream().filter(User::isActive).collect(Collectors.toList());
    }
    
    public List<User> getAllDeletedUsers(List<User> users){
        return users.stream().filter(user -> !user.isActive()).collect(Collectors.toList());
    }
    
    private User createUserFromData(String[] data) {
        boolean isActive = data[7].equalsIgnoreCase("active");
        Role role = Role.convertFromData(data[6]);
        return switch (role) {
            case ADMIN -> new Administrator(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
            case SALES_MANAGER -> new SalesManager(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
            case FINANCE_MANAGER -> new FinanceManager(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
            case INVENTORY_MANAGER -> new InventoryManager(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
            case PURCHASE_MANAGER -> new PurchaseManager(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
        };
    }
    
    //before using this, remember to do validation on GUI side first
    public void addUser(List<User> users, String userName, String password, String fullname, String email, String contact, Role role) {
        switch (role) {
            case ADMIN -> users.add(new Administrator(assignNewIndex(), userName, password, fullname, email, contact));
            case SALES_MANAGER -> users.add(new SalesManager(assignNewIndex(), userName, password, fullname, email, contact));
            case FINANCE_MANAGER -> users.add(new FinanceManager(assignNewIndex(), userName, password, fullname, email, contact));
            case INVENTORY_MANAGER -> users.add(new InventoryManager(assignNewIndex(), userName, password, fullname, email, contact));
            case PURCHASE_MANAGER -> users.add(new PurchaseManager(assignNewIndex(), userName, password, fullname, email, contact));
        }
        writeAllUsers(users);
    }
    
    //remember to perform validation checks on GUI side first
    public void editUser(List<User> users, String userID, String userName, String password, String fullname, String email, String contact){
        for (User user : users){
            if (userID.equals(user.getUserId())){
                user.setUsername(userName);
                user.setPassword(password);
                user.setFullname(fullname);
                user.setEmail(email);
                user.setContact(contact);
                break;
            }
        }
        writeAllUsers(users);
    }
    
    public void deleteUser(List<User> users, User user){
        user.setUsername("N/A");
        user.setPassword("N/A");
        user.setActive(false);
        writeAllUsers(users);
        users.remove(user);
    }
    
    private int extractNumberFromID(String id) {
        try {
            return Integer.parseInt(id.substring(3));
        } catch (Exception e) {
            System.err.println("innvalid user ID format: " + id);
            return 0;
        }
    }
    
    public List<User> getUsers() {
        return users;
    }
        
    public int getCurrentIndex() {
        return currentIndex;
    }
    
    private void incrementIndex() {
        currentIndex++;
    }
    
    private String assignNewIndex() {
        incrementIndex();
        return String.format("UID%03d", currentIndex);
    }
    
    //testting dont touch
//    public static void main(String[] args) {
//        UserDataManager test = new UserDataManager();
//        for (User a : test.getUsers()){
//            System.out.println(a.getFullname() + " " + a.getRole().getRoleFullName());
//        }
//        System.out.println(test.getCurrentIndex());
//      
////        test.getUsers().stream().filter(fuck -> fuck.getRole().equals("Sales Manager")).collect(Collectors.toList);
//    }
    
//    public User findUserByID(List<User> users, String userID){
//        for (User user : users){
//            if (user.getUserID().equals(userID)){
//                return user;
//            }
//        }
//        return null;
//    }
    
//    public User findUserByID(String userID){
//        for (User user : userDataLoader.readData()){
//            if (user.getUserID().equals(userID)){
//                return user;
//            }
//        }
//        return null;
//    }
    
}
