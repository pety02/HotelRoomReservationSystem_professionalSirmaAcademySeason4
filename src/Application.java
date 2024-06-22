import java.util.Scanner;

public class Application {
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
            // TODO: to write down the user in a file and to hash the password.
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

    private static boolean login(String[] credentials) {
        String username = credentials[0], email = credentials[1],
                password = credentials[2], reEnteredPassword = credentials[3];
        if(UserCredentialsValidator.isValidUsername(username)
                && UserCredentialsValidator.isValidPassword(password)) {
            // TODO: to check if the user is existing in a file and if it so to return initialize it and return true, else to return false
            return true;
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
                }
            } else if(command.equals("Login")) {
                userCredentials = initLoginForm();
                boolean isLoggedIn = login(userCredentials);
                if(isLoggedIn) {
                    // TODO: to print out user friendly message and to allow the user to view the menu from its profile
                } else {
                    // TODO: to print out an user friendly message and to allow the user to re-log in yourself
                }
            }
        } while (!command.equals("Register") && !command.equals("Login"));

    }
}