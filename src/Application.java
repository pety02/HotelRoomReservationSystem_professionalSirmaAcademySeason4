import controllers.AdminController;
import controllers.HotelController;
import controllers.UserController;
import models.Hotel;
import models.Room;
import models.User;
import types.RoomType;
import validators.UserCredentialsValidator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

/*
Application class is start point of the application.
*/
public class Application {
    private static final UserController userController = new UserController();
    private static final HotelController hotelController = new HotelController();
    private static final AdminController adminController = new AdminController();

    /*
    Initializes the registration form for new users.
    return String array containing user input for registration.
    */
    private static String[] initRegistrationForm() {
        String[] credentials = new String[5];
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
        System.out.println("*Debit-Card Balance:");
        credentials[4] = scanner.nextLine();

        return credentials;
    }

    /*
    Initializes the login form for existing users.
    return String array containing user input for login.
    */
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

    /*
    Initializes the main menu for regular users.
    */
    private static void initMenu() {
        System.out.println("Hotel Room Reservation System");
        System.out.println("-----------------------------");
        System.out.println("MENU:");
        System.out.println("1. View Rooms");
        System.out.println("2. Book a Room");
        System.out.println("3. Cancel Booking");
        System.out.println("4. Log Out");
    }

    /*
    Initializes the admin menu for administrative tasks.
    */
    private static void initAdminMenu() {
        System.out.println("Hotel Room Reservation System / Admin Panel");
        System.out.println("-----------------------------");
        System.out.println("MENU:");
        System.out.println("1. View All Bookings");
        System.out.println("2. View Total Income");
        System.out.println("3. View Total Cancellation Fees");
        System.out.println("4. Add Room");
        System.out.println("5. Remove Room");
        System.out.println("6. Update Room Data");
        System.out.println("7. Log Out");
    }

    /*
    Initializes a sample hotel with rooms and amenities.
    return Initialized Hotel object.
    */
    private static Hotel initHotel() {
        // TODO: to read all this data.
        Hotel myHotel = new Hotel("Petya's Hotel", "Malina str. 8, Town-City", new ArrayList<>());
        Map<Boolean, ArrayList<LocalDateTime>> bookingAvailabilities = new HashMap<>(){};
        LocalDateTime startDate = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, Month.JANUARY, 1, 0, 0, 0);
        ArrayList<LocalDateTime> dates = new ArrayList<>();
        dates.add(startDate);
        dates.add(endDate);
        bookingAvailabilities.put(true, dates);
        bookingAvailabilities.put(false, new ArrayList<>());
        ArrayList<String> amenities = new ArrayList<>();
        amenities.add("TV");
        amenities.add("Air conditioning");
        amenities.add("Refrigerator");
        amenities.add("Balcony");

        Room room1 = new Room(1, myHotel.getId(), RoomType.SUITE, amenities, 2, 50.00, 100.00, false, bookingAvailabilities, new ArrayList<>()),
                room2 = new Room(2, myHotel.getId(), RoomType.DELUXE, amenities, 5, 85.00, 420.00, false, bookingAvailabilities, new ArrayList<>()),
                room3 = new Room(3, myHotel.getId(), RoomType.SINGLE, amenities, 1, 30.00, 30.00, false, bookingAvailabilities, new ArrayList<>()),
                room4 = new Room(4, myHotel.getId(), RoomType.DOUBLE, amenities, 2, 45.00, 90.00, false, bookingAvailabilities, new ArrayList<>()),
                room5 = new Room(5, myHotel.getId(), RoomType.SUITE, amenities, 2, 35.00, 70.00, false, bookingAvailabilities, new ArrayList<>());

        myHotel.getAllRoomsIds().add(room1.getId());
        myHotel.getAllRoomsIds().add(room2.getId());
        myHotel.getAllRoomsIds().add(room3.getId());
        myHotel.getAllRoomsIds().add(room4.getId());
        myHotel.getAllRoomsIds().add(room5.getId());

        /*RoomReaderWriter rrw = new RoomReaderWriter();
        File rfile = new File(Application.roomsFilename);
        try(FileWriter fw = new FileWriter(rfile)) {
            rrw.write(room1, Application.roomsFilename);
            rrw.write(room2, Application.roomsFilename);
            rrw.write(room3, Application.roomsFilename);
            rrw.write(room4, Application.roomsFilename);
            rrw.write(room5, Application.roomsFilename);
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }

        HotelReaderWriter hrw = new HotelReaderWriter();
        File hfile = new File(Application.hotelsFilename);
        try(FileWriter fw = new FileWriter(hfile)) {
            hrw.write(myHotel, Application.hotelsFilename);
        } catch (IOException ex) {
            ex.fillInStackTrace();
        }*/

        return myHotel;
    }

    /*
    Displays all rooms of a given hotel.
    param myHotel The hotel whose rooms are to be displayed.
    */
    private static void viewRooms(Hotel myHotel) {
        hotelController.viewAllRooms(myHotel);
    }

    /*
    Parses a date string into LocalDateTime object.
    param dateString Date string in "yyyy-MM-dd HH:mm" format.
    return LocalDateTime parsed from dateString.
    */
    private static LocalDateTime parseDate(String dateString) {
        int year = Integer.parseInt(dateString.substring(0,4));
        int month = Integer.parseInt(dateString.substring(5,7));
        int day = Integer.parseInt(dateString.substring(8,10));
        int hour = Integer.parseInt(dateString.substring(11,13));
        int minute = Integer.parseInt(dateString.substring(14,16));
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    /*
    Allows a user to book a room in the hotel.
    param hotel The hotel where the room is to be booked.
    param loggedIn The user who is logged in and making the booking.
    */
    private static void bookRoom(Hotel hotel, User loggedIn) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter from date: ");
        String fromDateString = scanner.nextLine();
        LocalDateTime fromDate = parseDate(fromDateString);

        System.out.print("Enter to date: ");
        String toDateString = scanner.nextLine();
        LocalDateTime toDate = parseDate(toDateString);
        double cancellationFees = 100.00;

        System.out.print("Enter guests count: ");
        int guestsCount = Integer.parseInt(scanner.nextLine());

        try {
            hotelController.recommendRooms(fromDate, toDate, guestsCount, hotel);
            System.out.print("Choose room and type its id: ");
            int roomId = Integer.parseInt(scanner.nextLine());

            hotelController.bookRoom(roomId, hotel, fromDate, toDate, loggedIn, cancellationFees);
        } catch (RuntimeException ex) {
            ex.fillInStackTrace();
            System.out.println("Oops! Something went wrong! Please try again later!");
        } catch (IOException e) {
            e.fillInStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /*
    Cancels a booking based on booking ID.
    param bookingId The ID of the booking to be canceled.
    param loggedIn The user who is logged in and canceling the booking.
    param currentHotel The hotel from which the booking is canceled.
    */
    private static void cancelBooking(int bookingId, User loggedIn, Hotel currentHotel) {
        hotelController.cancelReservation(bookingId, loggedIn, currentHotel);
    }

    /*
    Executes a user command based on the menu option chosen.
    param command The command chosen by the user.
    param hotel The hotel object relevant to the user session.
    param currentUser The user who is currently logged in.
    */
    private static void executeCommand(String command, Hotel hotel, User currentUser) {
        Scanner scanner = new Scanner(System.in);
        switch (command) {
            case "View Rooms" -> {
                viewRooms(hotel);
            }
            case "Book a Room" -> {
                bookRoom(hotel, currentUser);
            }
            case "Cancel Booking" -> {
                userController.showAllBookings(currentUser);
                System.out.println("If you really want to cancel this reservation, you must pay cancellation fees!");
                System.out.print("Enter booking id: ");
                int bookingId = Integer.parseInt(scanner.nextLine());
                cancelBooking(bookingId, currentUser, hotel);
            }
            case "Log Out", "END" -> {
                System.out.println("Goodbye... You had been successfully logged out!");
                System.exit(0);
            }
            default -> System.out.println("Invalid command!");
        }
    }

    /*
    Retrieves RoomType enum based on string input.
    param type String representation of room type.
    return Corresponding RoomType enum value.
    */
    private static RoomType getType(String type) {
        return switch (type) {
            case "Deluxe" -> RoomType.DELUXE;
            case "Suite" -> RoomType.SUITE;
            case "Single" -> RoomType.SINGLE;
            case "Double" -> RoomType.DOUBLE;
            default -> RoomType.UNKNOWN;
        };
    }

    /*
    Executes an admin command based on the admin menu option chosen.
    param adminCmd The command chosen by the admin user.
    */
    private static void executeAdminCommand(String adminCmd) {
        if(adminCmd.equals("Log Out") || adminCmd.equals("END")) {
            System.out.println("Goodbye... You had been successfully logged out!");
            System.exit(0);
            return;
        }
        Scanner scanner = new Scanner(System.in);
        adminController.showAllHotels();
        System.out.print("Enter hotel Id: ");
        int hotelId = Integer.parseInt(scanner.nextLine());

        switch (adminCmd) {
            case "View All Bookings" -> {
                adminController.viewAllBookings(hotelId);
            }
            case "View Total Income" -> {
                double currHotelIncomes = adminController.getTotalIncome(hotelId);
                System.out.printf("Hotel total incomes are %.2f$.%n", currHotelIncomes);
            }
            case "View Total Cancellation Fees" -> {
                double currHotelCancellationFees = adminController.getCancellationFees(hotelId);
                System.out.printf("Hotel total cancellation fees are %.2f$.%n", currHotelCancellationFees);
            }
            case "Add Room" -> {
                System.out.print("Enter room id: ");
                int id = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter room type: ");
                String typeString = scanner.nextLine();
                RoomType type = getType(typeString);
                System.out.print("Enter amenities, separated by a single space: ");
                String amenitiesString = scanner.nextLine();
                String[] amenities = amenitiesString.split(" ");
                ArrayList<String> amenitiesList = new ArrayList<>(Arrays.asList(amenities));
                System.out.print("Enter maximum occupancy count: ");
                int maxOccupancy = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter price per night per person: ");
                double pricePerNight = Double.parseDouble(scanner.nextLine());

                if(adminController.addRoom(id, hotelId, type, amenitiesList, maxOccupancy, pricePerNight)) {
                    System.out.printf("Successfully added new room in hotel with id %d!%n", hotelId);
                } else {
                    System.out.println("Sorry, but the room's creation not successful!");
                }
            }
            case "Remove Room" -> {
                adminController.showAllHotelRooms(hotelId);
                System.out.print("Enter room id: ");
                int roomId = Integer.parseInt(scanner.nextLine());

                if(adminController.removeRoom(roomId)) {
                    System.out.printf("Successfully removed room with id %d!%n", roomId);
                } else {
                    System.out.println("Sorry, but the room's deletion not successful!");
                }
            }
            case "Update Room Data" -> {
                adminController.showAllHotelRooms(hotelId);
                System.out.print("Enter room id: ");
                int roomId = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter room type: ");
                String typeString = scanner.nextLine();
                RoomType type = getType(typeString);
                System.out.print("Enter amenities, separated by a single space: ");
                String amenitiesString = scanner.nextLine();
                String[] amenities = amenitiesString.split(" ");
                ArrayList<String> amenitiesList = new ArrayList<>(Arrays.asList(amenities));
                System.out.print("Enter maximum occupancy count: ");
                int maxOccupancy = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter price per night per person: ");
                double pricePerNight = Double.parseDouble(scanner.nextLine());

                if(adminController.updateRoom(roomId, hotelId, type, amenitiesList, maxOccupancy, pricePerNight)) {
                    System.out.printf("Successfully updated room with id %d!%n", roomId);
                } else {
                    System.out.println("Sorry, but the room's update not successful!");
                }
            }
            default -> System.out.println("Invalid command!");
        }
    }

    /*
    Main method that runs the hotel reservation application.
    param args Command-line arguments (not used).
    */
    public static void main(String[] args) {
        Hotel hotel = initHotel();
        System.out.println("Welcome to Hotel Reservation System!");
        System.out.println("If you are registered, please enter \"Login\"," +
                "\nif you are not registered, please enter \"Register\"!");
        System.out.println("For Admin Panel write \"Go to Admin Panel\"");

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
                                !option.equals("Book a Room") && !option.equals("Cancel Booking") &&
                                !option.equals("Log Out") && !option.equals("END"));
                        executeCommand(option, hotel, loggedIn);
                    } while (!option.equals("END"));
                } else {
                    System.out.println("Sorry, you mistake your credentials, so try to log-in again!");
                }
            } else if (command.equals("Go to Admin Panel")) {
                String secretCode;
                do {
                    System.out.print("Enter secret password: ");
                    secretCode = scanner.nextLine();
                } while (!UserCredentialsValidator.isValidSecretAdminCode(secretCode));

                initAdminMenu();

                String option;
                do {
                    do {
                        System.out.print("Enter command: ");
                        option = scanner.nextLine();
                    } while (!option.equals("View All Bookings") &&
                            !option.equals("View Total Income") && !option.equals("View Total Cancellation Fees") &&
                            !option.equals("Add Room") && !option.equals("Remove Room") &&
                            !option.equals("Update Room Data") &&
                            !option.equals("Log Out") && !option.equals("END"));
                    executeAdminCommand(option);
                } while (!option.equals("END"));
            }
        } while (!command.equals("Register") && !command.equals("Login") && !command.equals("Go to Admin Panel"));
    }
}