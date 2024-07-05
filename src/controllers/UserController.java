package controllers;

import interfaces.IUserManageable;
import models.DebitCard;
import models.Reservation;
import models.User;
import readersWriters.DebitCardReaderWriter;
import readersWriters.ReservationReaderWriter;
import readersWriters.UserReaderWriter;
import validators.UserCredentialsValidator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Map;

/**
 * UserController class implements IUserManageable interface and provides
 * functionalities for user management such as registration, login, logout,
 * showing all bookings, and loading user profile.
 */
public class UserController implements IUserManageable {
    private final static String usersFilename = "users.txt";
    private final static String reservationsFilename = "reservations.txt";
    private final static String debitCardsFilename = "debitCards.txt";

    /**
     * Registers a new user with the provided credentials.
     *
     * @param credentials An array of strings containing username, email,
     *                    password, re-entered password, and balance.
     * @return true if the registration is successful, false otherwise.
     */
    @Override
    public boolean register(String[] credentials) {
        String username = credentials[0], email = credentials[1],
                password = credentials[2], reEnteredPassword = credentials[3], balanceString = credentials[4];
        double balance = Double.parseDouble(balanceString);

        // Validate credentials
        if(username.matches("^[a-zA-Z0-9]{6,20}$")
                && UserCredentialsValidator.isValidEmail(email)
                && UserCredentialsValidator.isValidPassword(password)
                && UserCredentialsValidator.isValidPassword(reEnteredPassword)
                && 0.0 < balance) {

            // Create a new user and debit card
            User registeredUser = new User(username, email, password, new ArrayList<>(), null);
            DebitCard debitCard = new DebitCard(balance, registeredUser.getId());
            registeredUser.setDebitCard(Map.entry(debitCard.getId(), debitCard.getBalance()));

            // Write user and debit card to their respective files
            UserReaderWriter uw = new UserReaderWriter();
            uw.write(registeredUser, UserController.usersFilename);

            DebitCardReaderWriter dw = new DebitCardReaderWriter();
            dw.write(debitCard, UserController.debitCardsFilename);

            return true;
        }
        return false;
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param credentials An array of strings containing username and password.
     * @param loggedIn The user object to store logged-in user's details.
     * @return true if the login is successful, false otherwise.
     */
    @Override
    public boolean login(String[] credentials, User loggedIn) {
        String username = credentials[0], password = credentials[1];

        // Validate credentials
        if(username.matches("^[a-zA-Z0-9]{6,20}$")
                && UserCredentialsValidator.isValidPassword(password)) {

            UserReaderWriter urw = new UserReaderWriter();
            ArrayList<User> readUsers;
            File file = new File(UserController.usersFilename);

            try (FileReader fr = new FileReader(file)) {
                readUsers = urw.read(fr, file);
                if (readUsers.isEmpty()) {
                    System.out.println("There is no read users.");
                } else {
                    // Check each user for matching username and password
                    for (User usr : readUsers) {
                        if (usr.getUsername().equals(username) && usr.getPassword().equals(password)) {
                            // Set the logged in user's details
                            loggedIn.setId(usr.getId());
                            loggedIn.setUsername(usr.getUsername());
                            loggedIn.setEmail(usr.getEmail());
                            loggedIn.setPassword(usr.getPassword(), false);
                            loggedIn.setReservations(usr.getReservations());
                            loggedIn.setDebitCard(usr.getDebitCard());
                            return true;
                        }
                    }
                }
            } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException ex) {
                ex.fillInStackTrace();
            }
        }
        return false;
    }

    /**
     * Logs out the current user.
     */
    @Override
    public void logout() {
        System.out.println("Goodbye... You had been successfully logged out!");
        System.exit(0);
    }

    /**
     * Shows all bookings of the current user.
     *
     * @param currentUser The current user whose bookings are to be displayed.
     */
    @Override
    public void showAllBookings(User currentUser) {
        System.out.println("All reservations:");
        System.out.println("------------------");
        System.out.println();

        ReservationReaderWriter rrw = new ReservationReaderWriter();
        File file = new File(UserController.reservationsFilename);
        ArrayList<Reservation> readReservations;

        try (FileReader fr = new FileReader(file)) {
            readReservations = rrw.read(fr, file);
            if (readReservations.isEmpty()) {
                System.out.println("There is no read reservations.");
            } else {
                // Display each reservation of the current user
                for (Reservation reservation : readReservations) {
                    if (reservation.getBookedById() == currentUser.getId()) {
                        System.out.printf("ID: %d | From: %s | To: %s | Total price: %.2f$ | Status: %s%n",
                                reservation.getId(), reservation.getFromDate(), reservation.getToDate(),
                                reservation.getTotalPrice(), reservation.isCancelled() ? "cancelled" : "active");
                    }
                }
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }
    }

    /**
     * Loads the profile of the current user and displays all bookings.
     *
     * @param currentUser The current user whose profile is to be loaded.
     */
    @Override
    public void loadProfile(User currentUser) {
        System.out.printf("%s | %s%n", currentUser.getUsername(), currentUser.getEmail());
        showAllBookings(currentUser);
    }
}