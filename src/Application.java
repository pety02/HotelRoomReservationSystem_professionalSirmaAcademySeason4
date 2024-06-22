import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private static final String fileName = "users.txt";
    private static String[] initRegistrationForm() {
        String[] credentials = new String[4];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hotel Room Reservation System");
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

    private static boolean register(String[] credentials) {
        String username = credentials[0], email = credentials[1],
                password = credentials[2], reEnteredPassword = credentials[3];
        if(UserCredentialsValidator.isValidUsername(username)
                && UserCredentialsValidator.isValidEmail(email)
                && UserCredentialsValidator.isValidPassword(password)
                && UserCredentialsValidator.isValidPassword(reEnteredPassword)) {
            User registeredUser = new User(username, email, password);
            UserReaderWriter uw = new UserReaderWriter();

            uw.write(registeredUser, Application.fileName);
            return true;
        }
        return false;
    }

    private static String[] initLoginForm() {
        String[] credentials = new String[2];
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hotel Room Reservation System");
        System.out.println("-----------------------------");
        System.out.println("Login Form:");
        System.out.print("*Username: ");
        credentials[0] = scanner.nextLine();
        System.out.print("*Password: ");
        credentials[1] = scanner.nextLine();

        return credentials;
    }

    private static boolean login(String[] credentials, User loggedIn) {
        String username = credentials[0], password = credentials[1];
        if(UserCredentialsValidator.isValidUsername(username)
                && UserCredentialsValidator.isValidPassword(password)) {
            // TODO: to check if the user is existing in a file and if it so to return initialize it and return true, else to return false
            UserReaderWriter urw = new UserReaderWriter();
            ArrayList<User> readUsers = urw.read(Application.fileName);
            for(User currentUser : readUsers) {

                if(currentUser.getUsername().equals(username) && currentUser.getPassword().equals(password)) {
                    loggedIn = currentUser;
                    return true;
                }

                return false;
            }
        }
        return false;
    }

    private static void initMenu() {
        System.out.println("Hotel Room Reservation System");
        System.out.println("-----------------------------");
        System.out.println("MENU:");
        System.out.println("1. View Rooms");
        System.out.println("2. Book a Room");
        System.out.println("3. Cancel Booking");
        System.out.println("4. Log Out");
    }
    public static void main(String[] args) {
        System.out.println("Welcome to Hotel Room Reservation System!");
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
                boolean isRegistered = register(userCredentials);
                if(isRegistered) {
                    userCredentials = initLoginForm();
                } else {
                    // TODO: to print user friendly message and to allow user to re-register yourself
                    System.out.println("Sorry, incorrect registration data! Please try to register yourself again!");
                }
            } else if(command.equals("Login")) {
                userCredentials = initLoginForm();
                User loggedIn = new User();
                boolean isLoggedIn = login(userCredentials, loggedIn);
                if(isLoggedIn) {
                    // TODO: to print out user friendly message and to allow the user to view the menu from its profile
                    System.out.printf("Welcome, %s!", userCredentials[0]);
                    initMenu();
                } else {
                    // TODO: to print out an user friendly message and to allow the user to re-log in yourself
                    System.out.println("Sorry, you mistake your credentials, so try to log-in again!");
                }
            }
        } while (!command.equals("Register") && !command.equals("Login"));

    }
}