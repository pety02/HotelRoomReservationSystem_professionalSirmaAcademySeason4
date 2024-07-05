package validators;

import models.User;
import readersWriters.UserReaderWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 */
public class UserCredentialsValidator {
    private static final String usersFilename = "users.txt";

    /**
     *
     * @param username
     * @return
     */
    public static boolean isValidUsername(String username) {
        boolean isValid = username.matches("^[a-zA-Z+0-9]{6,20}$");
        UserReaderWriter urw = new UserReaderWriter();
        try {
            int index = 0;
            File file = new File(UserCredentialsValidator.usersFilename);
            FileReader fr = new FileReader(file);
            ArrayList<User> readUsers;
            do {
                readUsers = urw.read(fr, file);
                for(User usr : readUsers) {
                    if(usr == null || username.equals(usr.getUsername())) {
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
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9]{5,10}@(abv.bg|gmail.com)$");
    }

    /**
     *
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
        return password.matches("^[a-zA-Z+0-9]{8,16}$");
    }

    /**
     *
     * @param secretCode
     * @return
     */
    public static boolean isValidSecretAdminCode (String secretCode) {
        return secretCode.equals("#HRRS_admin");
    }
}