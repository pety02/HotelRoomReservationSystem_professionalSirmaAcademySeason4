public class UserCredentialsValidator {
    public static boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z+0-9]{6,20}$");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9]{5,10}@(abv.bg|gmail.com)$");
    }

    public static boolean isValidPassword(String password) {
        return password.matches("^[a-zA-Z+0-9]{8,16}$");
    }
}