package controllers;

import interfaces.IUserManageable;
import models.DebitCard;
import models.User;
import readersWriters.UserReaderWriter;
import validators.UserCredentialsValidator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UserController implements IUserManageable {
    private final static String usersFilename = "users.txt";

    @Override
    public boolean register(String[] credentials) {
        String username = credentials[0], email = credentials[1],
                password = credentials[2], reEnteredPassword = credentials[3];
        if(UserCredentialsValidator.isValidUsername(username)
                && UserCredentialsValidator.isValidEmail(email)
                && UserCredentialsValidator.isValidPassword(password)
                && UserCredentialsValidator.isValidPassword(reEnteredPassword)) {
            User registeredUser = new User(username, email, password, new ArrayList<>(), new DebitCard());
            UserReaderWriter uw = new UserReaderWriter();

            uw.write(registeredUser, UserController.usersFilename);
            return true;
        }
        return false;
    }

    @Override
    public boolean login(String[] credentials, User loggedIn) {
        String username = credentials[0], password = credentials[1];
        if(username.matches("^[a-zA-Z+0-9]{6,20}$")
                && UserCredentialsValidator.isValidPassword(password)) {
            UserReaderWriter urw = new UserReaderWriter();
            User readUser;
            File file = new File(UserController.usersFilename);
            try(FileReader fr = new FileReader(file)) {
                readUser = urw.read(fr, file);
                if (readUser == null) {
                    System.out.println("There is no read users.");
                    return false;
                } else {
                    if (readUser.getUsername().equals(username) && readUser.getPassword().equals(password)) {
                        loggedIn = readUser;
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (IOException ex) {
                ex.fillInStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public void logout() {
        System.out.println("Goodbye... You had been successfully logged out!");
        System.exit(0);
    }

    @Override
    public void showAllBookings(User currentUser) {
        // TODO: to load all current user's room reservations
    }

    @Override
    public void loadProfile(User currentUser) {
        System.out.printf("%s | %s%n", currentUser.getUsername(), currentUser.getEmail());
        showAllBookings(currentUser);
    }
}
