import controllers.UserController;
import models.User;
import java.util.Scanner;

public class Application {
    private static final UserController userController = new UserController();

    private static String[] initRegistrationForm() {
        String[] credentials = new String[4];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hotel models.Room models.Reservation System");
        System.out.println("-----------------------------");
        System.out.println("Registration Form:");
        System.out.print("*Username: ");
        credentials[0] = scanner.nextLine();
        System.out.print("*Email: ");
        credentials[1] = scanner.nextLine();
        System.out.print("*Password: ");
        credentials[2] = scanner.nextLine();
        System.out.print("*Re-Entered password: ");
        credentials[3] = scanner.nextLine();

        return credentials;
    }

    private static String[] initLoginForm() {
        String[] credentials = new String[2];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hotel models.Room models.Reservation System");
        System.out.println("-----------------------------");
        System.out.println("Login Form:");
        System.out.print("*Username: ");
        credentials[0] = scanner.nextLine();
        System.out.print("*Password: ");
        credentials[1] = scanner.nextLine();

        return credentials;
    }

    private static void initMenu() {
        System.out.println("Hotel models.Room models.Reservation System");
        System.out.println("-----------------------------");
        System.out.println("MENU:");
        System.out.println("1. View Rooms");
        System.out.println("2. Book a models.Room");
        System.out.println("3. Cancel Booking");
        System.out.println("4. Log Out");
    }

    private static void viewRooms() {
        // TODO: to implement it
    }

    private static void bookRoom(String roomId) {
        // TODO: to implement it
    }

    private static void cancelBooking(String bookingId) {
        // TODO: to implement it
    }

    private static void executeCommand(String command, User currentUser) {
        Scanner scanner = new Scanner(System.in);
        switch (command) {
            case "View Rooms" -> {
                viewRooms();
            }
            case "Book a models.Room" -> {
                System.out.println("Enter room id:");
                String roomId = scanner.nextLine();
                bookRoom(roomId);
            }
            case "Cancel Booking" -> {
                userController.showAllBookings(currentUser);
                System.out.println("Enter booking id:");
                String bookingId = scanner.nextLine();
                cancelBooking(bookingId);
            }
            case "Log Out" -> {
                System.out.println("Goodbye... You had been successfully logged out!");
                System.exit(0);
                return;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Hotel models.Room models.Reservation System!");
        System.out.println("If you are registered, please enter \"Login\"," +
                "\nif you are not registered, please enter \"Register\"!");

        Scanner scanner = new Scanner(System.in);
        String command;
        String[] userCredentials;
        do {
            System.out.print("Enter command: ");
            command = scanner.nextLine();
            if (command.equals("Register")) {
                userCredentials = initRegistrationForm();
                boolean isRegistered = userController.register(userCredentials);
                if(isRegistered) {
                    userCredentials = initLoginForm();
                } else {
                    System.out.println("Sorry, incorrect registration data! Please try to register yourself again!");
                }
            } else if(command.equals("Login")) {
                userCredentials = initLoginForm();
                User loggedIn = new User();
                boolean isLoggedIn = userController.login(userCredentials, loggedIn);
                if(isLoggedIn) {
                    userController.loadProfile(loggedIn);
                    System.out.printf("Welcome, %s!%n", loggedIn.getUsername());
                    initMenu();
                    String option;
                    do {
                        do {
                            System.out.print("Enter command: ");
                            option = scanner.nextLine();
                        } while (!option.equals("View Rooms") &&
                                !option.equals("Book a models.Room") && !option.equals("Cancel Booking") &&
                                !option.equals("Log Out") && !option.equals("END"));
                        if(option.equals("END")) {
                            break;
                        }
                        executeCommand(option, loggedIn);
                    } while (true);
                } else {
                    System.out.println("Sorry, you mistake your credentials, so try to log-in again!");
                }
            }
        } while (!command.equals("Register") && !command.equals("Login"));
    }
}