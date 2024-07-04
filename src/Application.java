import controllers.HotelController;
import controllers.UserController;
import models.Hotel;
import models.Room;
import models.User;
import readersWriters.HotelReaderWriter;
import readersWriters.RoomReaderWriter;
import types.RoomType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public class Application {
    private static final String usersFilename = "users.txt", roomsFilename = "rooms.txt",
    reservationsFilename = "reservations.txt", hotelsFilename = "hotels.txt", debitCardsFilename = "debitCard.txt";
    private static final UserController userController = new UserController();
    private static final HotelController hotelController = new HotelController();

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

        System.out.println("Hotel Room Reservation System");
        System.out.println("-----------------------------");
        System.out.println("Login Form:");
        System.out.print("*Username: ");
        credentials[0] = scanner.nextLine();
        System.out.print("*Password: ");
        credentials[1] = scanner.nextLine();

        return credentials;
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

    private static Hotel initHotel() {
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

        myHotel.getAllRooms().add(room1.getId());
        myHotel.getAllRooms().add(room2.getId());
        myHotel.getAllRooms().add(room3.getId());
        myHotel.getAllRooms().add(room4.getId());
        myHotel.getAllRooms().add(room5.getId());

        RoomReaderWriter rrw = new RoomReaderWriter();
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
        }

        return myHotel;
    }

    private static void viewRooms(Hotel myHotel) {
        hotelController.viewAllRooms(myHotel);
    }

    private static LocalDateTime parseDate(String dateString) {
        int year = Integer.parseInt(dateString.substring(0,4));
        int month = Integer.parseInt(dateString.substring(5,7));
        int day = Integer.parseInt(dateString.substring(8,10));
        int hour = Integer.parseInt(dateString.substring(11,13));
        int minute = Integer.parseInt(dateString.substring(14,16));
        return LocalDateTime.of(year, month, day, hour, minute);
    }

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
        }
    }

    private static void cancelBooking(int bookingId, User loggedIn, Hotel currentHotel) {
        // TODO: to implement it
        hotelController.cancelReservation(bookingId, loggedIn, currentHotel);
    }

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
                System.out.println("Enter booking id:");
                int bookingId = Integer.parseInt(scanner.nextLine());
                cancelBooking(bookingId, currentUser, hotel);
            }
            case "Log Out", "END" -> {
                System.out.println("Goodbye... You had been successfully logged out!");
                System.exit(0);
            }
            default -> {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        Hotel hotel = initHotel();
        System.out.println("Welcome to Hotel Reservation System!");
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
                                !option.equals("Book a Room") && !option.equals("Cancel Booking") &&
                                !option.equals("Log Out") && !option.equals("END"));
                        executeCommand(option, hotel, loggedIn);
                    } while (!option.equals("END"));
                } else {
                    System.out.println("Sorry, you mistake your credentials, so try to log-in again!");
                }
            }
        } while (!command.equals("Register") && !command.equals("Login"));
    }
}