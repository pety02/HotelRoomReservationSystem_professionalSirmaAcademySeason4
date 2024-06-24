package validators;

import models.User;
import readersWriters.UserReaderWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class UserCredentialsValidator {
    private static final String usersFilename = "users.txt";
    public static boolean isValidUsername(String username) {
        boolean isValid = username.matches("^[a-zA-Z+0-9]{6,20}$");
        UserReaderWriter urw = new UserReaderWriter();
        try {
            int index = 0;
            File file = new File(UserCredentialsValidator.usersFilename);
            FileReader fr = new FileReader(file);
            User readUser = new User();
            do {
                readUser = urw.read(fr, file);
                if(readUser == null || readUser.getUsername().equals(username)) {
                    return false;
                }
            } while (file.canRead());
        } catch (FileNotFoundException ex) {
            ex.fillInStackTrace();
        }
        return isValid;
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9]{5,10}@(abv.bg|gmail.com)$");
    }

    public static boolean isValidPassword(String password) {
        return password.matches("^[a-zA-Z+0-9]{8,16}$");
    }
}