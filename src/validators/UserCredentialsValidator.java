package validators;

import models.User;
import readersWriters.UserReaderWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Utility class for validating user credentials such as username, email, password, and secret admin code.
 */
public class UserCredentialsValidator {
    private static final String usersFilename = "users.txt";

    /**
     * Validates if a username meets the criteria.
     *
     * @param username The username to validate.
     * @return true if the username is valid; false otherwise.
     */
    public static boolean isValidUsername(String username) {
        boolean isValid = username.matches("^[a-zA-Z+0-9]{6,20}$");
        UserReaderWriter urw = new UserReaderWriter();
        try {
            File file = new File(UserCredentialsValidator.usersFilename);
            FileReader fr = new FileReader(file);
            ArrayList<User> readUsers;
            do {
                readUsers = urw.read(fr, file);
                for (User usr : readUsers) {
                    if (usr == null || username.equals(usr.getUsername())) {
                        return false;
                    }
                }
            } while (file.canRead());
        } catch (FileNotFoundException ex) {
            ex.fillInStackTrace();
        }
        return isValid;
    }

    /**
     * Validates if an email meets the criteria.
     *
     * @param email The email to validate.
     * @return true if the email is valid; false otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9]{5,10}@(abv.bg|gmail.com)$");
    }

    /**
     * Validates if a password meets the criteria.
     *
     * @param password The password to validate.
     * @return true if the password is valid; false otherwise.
     */
    public static boolean isValidPassword(String password) {
        return password.matches("^[a-zA-Z+0-9]{8,16}$");
    }

    /**
     * Validates if a secret admin code matches the predefined value.
     *
     * @param secretCode The secret admin code to validate.
     * @return true if the secret code is valid; false otherwise.
     */
    public static boolean isValidSecretAdminCode(String secretCode) {
        return secretCode.equals("#HRRS_admin");
    }
}