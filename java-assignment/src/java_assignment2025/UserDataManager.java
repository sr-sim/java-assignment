/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package java_assignment2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author ASUS
 */
public class UserDataManager extends DataManager{
    private final List<User> users;
    private int currentIndex;
    private final String FILEPATH = "src/java_assignment2025/user.txt";

    public UserDataManager() {
        super();
        this.users = loadAllUsers();
    }

    private List<User> loadAllUsers(){
        List<User> userList = new ArrayList<>();
        for(String line : TextFile.readFile(FILEPATH)){
            String[] parts = line.split("`", 8);
            if(parts.length == 8){
                userList.add(createUserFromData(parts));
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
        Role role = Role.convertFromAbbr(data[6]);
        return switch (role) {
            case ADMIN -> new Administrator(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
            case SALES_MANAGER -> new SalesManager(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
            case FINANCE_MANAGER -> new FinanceManager(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
            case INVENTORY_MANAGER -> new InventoryManager(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
            case PURCHASE_MANAGER -> new PurchaseManager(data[0], data[1], data[2], data[3], data[4], data[5], isActive);
        };
    }
    
    private User checkUsernameExist(String username){
        for (User user : getAllActiveUsers()){
            if (user.getUsername().equals(username)){return user;}
        }
        return null;
    }
    
    private User checkEmailExist(String email){
        for (User user : getAllActiveUsers()){
            if (user.getEmail().equals(email)){return user;}
        }
        return null;
    }
    
    private User checkContactExist(String contact){
        for (User user : getAllActiveUsers()){
            if (user.getContact().equals(contact)){return user;}
        }
        return null;
    }
    
    public String validateUsername(String username, User user) {
        if (username.isEmpty()) return "- Username cannot be empty.\n";
        if (username.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/? ].*")) {
            return "- Username should not contain special characters!\n";
        }
        if (checkUsernameExist(username) != null &&
                (user == null || !Objects.equals(username, user.getUsername()))) {
            return "- Username has been taken!\n";
        }
        return "";
    }

    public String validatePassword(String password) {
        if (password.isEmpty()) return "- Password cannot be empty.\n";
        if (password.length() < 8) return "- Password must be at least 8 characters long.\n";
        if (password.contains("`") || password.contains("\"") || password.contains("\\")) {
            return "- Password cannot contain special characters such as \", `, or \\.\n";
        }
        return "";
    }

    public String validateFullname(String fullname) {
        if (fullname.isEmpty()) return "- Fullname cannot be empty.\n";
        if (fullname.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*") || fullname.matches(".*\\d.*")) {
            return "- Fullname should not contain any numbers or special characters.\n";
        }
        return "";
    }

    public String validateEmail(String email, User user) {
        if (email.isEmpty()) return "- Email cannot be empty.\n";
        if (!email.contains("@") || (!email.endsWith(".com")
                && !email.endsWith(".net") && !email.endsWith(".biz") && !email.endsWith(".us")
                && !email.endsWith(".org") && !email.endsWith(".my") && !email.endsWith(".uk")
                && !email.endsWith(".info"))) {
            return "- Email format is invalid.\n";
        }
        if (checkEmailExist(email) != null &&
                (user == null || !Objects.equals(email, user.getEmail()))) {
            return "- Email has been used.\n";
        }
        return "";
    }

    public String validateContact(String contact, User user) {
        if (contact.isEmpty()) return "- Contact number cannot be empty.\n";
        int digitCount = 0, hyphenCount = 0, hyphenPosition = -1;
        for (int i = 0; i < contact.length(); i++) {
            char c = contact.charAt(i);
            if (Character.isDigit(c)) digitCount++;
            else if (c == '-') {
                hyphenCount++;
                hyphenPosition = i;
            }
        }
        if (!(contact.length() < 13 && digitCount <= 11 && hyphenCount == 1 && (hyphenPosition == 2 || hyphenPosition == 3))) {
            return "- Phone number format is invalid.\n";
        }
        if (checkContactExist(contact) != null &&
                (user == null || !Objects.equals(contact, user.getContact()))) {
            return "- Phone number has been used.\n";
        }
        return "";
    }

    public String validateRole(String role) {
        if (role == null || role.isEmpty()) return "- Role cannot be empty.\n";
        return "";
    }

    public String validateUserDataAdd(String username, String password, String fullname, String email, String contact, String role) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append(validateUsername(username, null));
        errorMsg.append(validatePassword(password));
        errorMsg.append(validateFullname(fullname));
        errorMsg.append(validateEmail(email, null));
        errorMsg.append(validateContact(contact, null));
        errorMsg.append(validateRole(role));
        return errorMsg.toString();
    }

    public String validateUserDataEdit(User user, String username, String password, String fullname, String email, String contact) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append(validateUsername(username, user));
        errorMsg.append(validatePassword(password));
        errorMsg.append(validateFullname(fullname));
        errorMsg.append(validateEmail(email, user));
        errorMsg.append(validateContact(contact, user));
        return errorMsg.toString();
    }
    
    //before using this, remember to do validation on GUI side first
    public void addUser(String userName, String password, String fullname, String email, String contact, Role role) {
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
    public void editUser(String userID, String userName, String password, String fullname, String email, String contact){
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
    
    public void deleteUser(User user){
        user.setUsername("N/A");
        user.setPassword("N/A");
        user.setActive(false);
        writeAllUsers(users);
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
    
    public User findUserByID(String userID){
        for (User user : users){
            if (user.getUserId().equals(userID)){
                return user;
            }
        }
        return null;
    }
    
}
