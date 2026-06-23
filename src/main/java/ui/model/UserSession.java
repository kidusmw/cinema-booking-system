package ui.model;

public class UserSession {
    private static int currentUserID;
    private static String currentUsername;
    private static String currentUserRole;
    private static String currentFullName;

    // ============= USER ID =============
    public static void setUserID(int id) {
        currentUserID = id;
        System.out.println("✅ UserSession: UserID set to " + id);
    }

    public static int getUserID() {  // ✅ Returns int, not String
        return currentUserID;
    }

    // ============= USERNAME =============
    public static void setUsername(String username) {
        currentUsername = username;
    }

    public static String getUsername() {
        return currentUsername;
    }

    // ============= ROLE =============
    public static void setRole(String role) {
        currentUserRole = role;
    }

    public static String getRole() {
        return currentUserRole;
    }

    // ============= FULL NAME =============
    public static void setFullName(String fullName) {
        currentFullName = fullName;
    }

    public static String getFullName() {
        return currentFullName;
    }

    // ============= UTILITY =============

    // Check if user is logged in
    public static boolean isLoggedIn() {
        return currentUserID > 0;
    }

    // Clear session (logout)
    public static void clear() {
        currentUserID = 0;
        currentUsername = null;
        currentUserRole = null;
        currentFullName = null;
        System.out.println("✅ UserSession: Cleared");
    }

    // Debug: print all session data
    public static void printSession() {
        System.out.println("=== USER SESSION ===");
        System.out.println("UserID: " + currentUserID);
        System.out.println("Username: " + currentUsername);
        System.out.println("Role: " + currentUserRole);
        System.out.println("Full Name: " + currentFullName);
        System.out.println("Is Logged In: " + isLoggedIn());
        System.out.println("====================");
    }
}
