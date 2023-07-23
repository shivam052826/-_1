import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CentralDatabase centralDatabase = new CentralDatabase();
        LoginForm loginForm = new LoginForm();
        ReservationSystem reservationSystem = new ReservationSystem(centralDatabase);
        CancellationForm cancellationForm = new CancellationForm(centralDatabase);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Online Reservation System!");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (loginForm.isValidUser(username, password)) {
            System.out.println("Login Successful!\n");
            while (true) {
                System.out.println("Select an option:");
                System.out.println("1. Make a reservation");
                System.out.println("2. Cancel a reservation");
                System.out.println("3. Exit");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (option) {
                    case 1 -> {
                        System.out.print("Enter train number: ");
                        String trainNumber = scanner.nextLine();
                        System.out.print("Enter class type: ");
                        String classType = scanner.nextLine();
                        System.out.print("Enter date of journey: ");
                        String dateOfJourney = scanner.nextLine();
                        System.out.print("Enter from (place): ");
                        String from = scanner.nextLine();
                        System.out.print("Enter to (destination): ");
                        String to = scanner.nextLine();
                        reservationSystem.makeReservation(username, trainNumber, classType, dateOfJourney, from, to);
                    }
                    case 2 -> {
                        System.out.print("Enter PNR number to cancel the reservation: ");
                        String pnrNumber = scanner.nextLine();
                        cancellationForm.cancelReservation(pnrNumber);
                    }
                    case 3 -> {
                        System.out.println("Thank you for using the Online Reservation System!");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            }
        } else {
            System.out.println("Invalid username or password. Login failed.");
        }
    }
}
class LoginForm {
    private HashMap<String, String> users;

    public LoginForm() {
        users = new HashMap<>();
        users.put("user1", "password1");
        users.put("user2", "password2");
        users.put("user3", "password3");
    }

    public boolean isValidUser(String username, String password) {

        return users.containsKey(username) && users.get(username).equals(password);
    }
}
class ReservationSystem {
    private int reservationCounter;
    private CentralDatabase database;

    public ReservationSystem(CentralDatabase database) {
        this.database = database;
        reservationCounter = 100; // Starting reservation number
    }

    public void makeReservation(String username, String trainNumber, String classType, String dateOfJourney, String from, String to) {

        String pnrNumber = "PNR" + reservationCounter++;


        database.saveReservation(username, pnrNumber, trainNumber, classType, dateOfJourney, from, to);

        // Display reservation confirmation to the user
        System.out.println("Reservation Successful!");
        System.out.println("PNR Number: " + pnrNumber);
    }
}

class CancellationForm {
    private CentralDatabase database;

    public CancellationForm(CentralDatabase database) {
        this.database = database;
    }

    public void cancelReservation(String pnrNumber) {
        ReservationDetails reservation = database.getReservationDetails(pnrNumber);

        if (reservation != null) {
            System.out.println("Reservation Details:");
            System.out.println("PNR Number: " + reservation.getPnrNumber());
            System.out.println("Train Number: " + reservation.getTrainNumber());
            System.out.println("Class Type: " + reservation.getClassType());
            System.out.println("Date of Journey: " + reservation.getDateOfJourney());
            System.out.println("From: " + reservation.getFrom());
            System.out.println("To: " + reservation.getTo());
            System.out.println("Confirm Cancellation? Press OK to proceed.");
            database.deleteReservation(pnrNumber);
            System.out.println("Reservation Canceled Successfully!");
        } else {
            System.out.println("Invalid PNR Number. No reservation found.");
        }
    }
}
class CentralDatabase {
    private HashMap<String, ReservationDetails> reservations;

    public CentralDatabase() {
        reservations = new HashMap<>();
    }

    public void saveReservation(String username, String pnrNumber, String trainNumber, String classType, String dateOfJourney, String from, String to) {
        ReservationDetails reservation = new ReservationDetails(username, pnrNumber, trainNumber, classType, dateOfJourney, from, to);
        reservations.put(pnrNumber, reservation);
    }

    public ReservationDetails getReservationDetails(String pnrNumber) {
        return reservations.get(pnrNumber);
    }

    public void deleteReservation(String pnrNumber) {
        reservations.remove(pnrNumber);
    }
}

class ReservationDetails {
    private String username;
    private String pnrNumber;
    private String trainNumber;
    private String classType;
    private String dateOfJourney;
    private String from;
    private String to;

    public ReservationDetails(String username, String pnrNumber, String trainNumber, String classType, String dateOfJourney, String from, String to) {
        this.username = username;
        this.pnrNumber = pnrNumber;
        this.trainNumber = trainNumber;
        this.classType = classType;
        this.dateOfJourney = dateOfJourney;
        this.from = from;
        this.to = to;
    }

    public String getUsername() {
        return username;
    }

    public String getPnrNumber() {
        return pnrNumber;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public String getClassType() {
        return classType;
    }

    public String getDateOfJourney() {
        return dateOfJourney;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
