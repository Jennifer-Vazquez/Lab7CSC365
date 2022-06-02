

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class InnReservations {


    public static void main(String[] args) throws SQLException {
        String test = System.getenv("HP_JDBC_URL");
        System.out.print(test);
        try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
                System.getenv("HP_JDBC_USER"),
                System.getenv("HP_JDBC_PW"))) {


            Scanner myScan = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Enter number associated with option");
            String optionSelected = " ";

            while (!optionSelected.equals("7")) {
                printOptions();
                System.out.print("Input: ");
                optionSelected = myScan.nextLine();
                if (optionSelected.equals("3")) {
                    reservationChange(myScan);

                }
            }
        }
    }


    public static void printOptions(){
        System.out.println("1. Rooms and Rates");
        System.out.println("2. Reservations");
        System.out.println("3. Reservation Change");
        System.out.println("4. Reservation Cancellation");
        System.out.println("5. Detailed Reservation Information");
        System.out.println("6. Revenue");
        System.out.println("7. Quit");
    }

    public static void reservationChange(Scanner scanner){
        String newFirstName;
        String newLastName;

        String newNumOfChildren;
        String newNumOfAdults;

        System.out.println("Enter the new first name, N/A if unchanged");
        newFirstName = scanner.nextLine();
        System.out.println("Enter the new last name, N/A if unchanged");
        newLastName = scanner.nextLine();

        System.out.println("Enter the new number of children, N/A if unchanged");
        newNumOfChildren = scanner.nextLine();
        System.out.println("Enter the new number of adults, N/A if unchanged");
        newNumOfAdults = scanner.nextLine();
        //System.out.println(newFirstName);

    }
}
