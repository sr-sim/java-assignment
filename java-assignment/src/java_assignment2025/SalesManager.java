package java_assignment2025;

public class SalesManager extends User{
    public SalesManager(String userID, String userName, String password, String fullname, String email, String contact, boolean isActive) {
        super(userID, userName, password, fullname, email, contact, Role.SALES_MANAGER, isActive);
    }
    
    public SalesManager(String userID, String userName, String password, String fullname, String email, String contact) {
        super(userID, userName, password, fullname, email, contact, Role.SALES_MANAGER, true);
    }
    
    public SalesManager(User user) {
        super (user.getUserId(), user.getUsername(), user.getPassword(), user.getFullname(), user.getEmail(), user.getContact(), Role.SALES_MANAGER, true);
    }
}

