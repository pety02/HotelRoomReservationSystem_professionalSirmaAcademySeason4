package controllers;

import interfaces.IUserManageable;
import models.DebitCard;
import models.Reservation;
import models.User;
import readersWriters.ReservationReaderWriter;
import readersWriters.UserReaderWriter;
import validators.UserCredentialsValidator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

public class UserController implements IUserManageable {
    private final static String usersFilename = "users.txt";
    private final static String reservationsFilename = "reservations.txt";

    @Override
    public boolean register(String[] credentials) {
        String username = credentials[0], email = credentials[1],
                password = credentials[2], reEnteredPassword = credentials[3];
        if(username.matches("^[a-zA-Z+0-9]{6,20}$")
                && UserCredentialsValidator.isValidEmail(email)
                && UserCredentialsValidator.isValidPassword(password)
                && UserCredentialsValidator.isValidPassword(reEnteredPassword)) {
            User registeredUser = new User(username, email, password, new ArrayList<>(), null);
            DebitCard userDebitCard = new DebitCard();
            userDebitCard.setOwner(registeredUser.getId());
            registeredUser.setDebitCard(Map.entry(userDebitCard.getId(), userDebitCard.getBalance()));
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
            ArrayList<User> readUsers;
            File file = new File(UserController.usersFilename);
            try(FileReader fr = new FileReader(file)) {
                readUsers = urw.read(fr, file);
                if (readUsers == null) {
                    System.out.println("There is no read users.");
                    return false;
                } else {
                    for(User usr : readUsers) {
                        if (usr.getUsername().equals(username) && usr.getPassword().equals(password)) {
                            loggedIn.setId(usr.getId());
                            loggedIn.setUsername(usr.getUsername());
                            loggedIn.setEmail(usr.getEmail());
                            loggedIn.setReservations(usr.getReservations());
                            loggedIn.setDebitCard(usr.getDebitCard());
                            return true;
                        }
                    }

                    return false;
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
        System.out.println("All reservations:");
        System.out.println("------------------");
        System.out.println();
        ArrayList<Reservation> allReservations = new ArrayList<>();
        ReservationReaderWriter rrw = new ReservationReaderWriter();

        File file = new File(UserController.reservationsFilename);
        ArrayList<Reservation> readReservations = new ArrayList<>();
        try(FileReader fr = new FileReader(file)) {
            readReservations = rrw.read(fr, file);
            if (readReservations == null) {
                System.out.println("There is no read reservations.");
            } else {
                for(Reservation reservation : readReservations) {
                    if (reservation.getBookedBy() == currentUser.getId()) {
                        readReservations.add(reservation);
                    }
                }
            }
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }

        for(Reservation currentReservation : readReservations) {
            System.out.println(currentReservation);
        }
    }

    @Override
    public void loadProfile(User currentUser) {
        System.out.printf("%s | %s%n", currentUser.getUsername(), currentUser.getEmail());
        showAllBookings(currentUser);
    }
}
