package interfaces;

import models.User;

public interface IUserManageable {
    boolean register(String[] credentials);
    boolean login(String[] credentials, User loggedIn);
    void logout();
    void showAllBookings(User currentUser);
    void loadProfile(User currentUser);
}