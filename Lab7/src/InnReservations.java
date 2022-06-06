import java.sql.*;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;

import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class InnReservations {


    public static void main(String[] args) throws SQLException {
        String test = System.getenv("HP_JDBC_URL");
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
                    reservationChange(myScan, conn);

                }
            }
        }
    }


    public static void printOptions() {
        System.out.println("1. Rooms and Rates");
        System.out.println("2. Reservations");
        System.out.println("3. Reservation Change");
        System.out.println("4. Reservation Cancellation");
        System.out.println("5. Detailed Reservation Information");
        System.out.println("6. Revenue");
        System.out.println("7. Quit");
    }

    public static void reservationChange(Scanner scanner, Connection conn) {

        int reservationCode;
        String newFirstName;
        String newLastName;
        String newCheckIn;
        String newCheckOut;
        String newNumOfChildren;
        String newNumOfAdults;


        System.out.println("Enter the reservation code");
        reservationCode = scanner.nextInt();
        scanner.nextLine(); // get rid of the buffer
        System.out.println("Enter the new first name, N/A if unchanged");
        newFirstName = scanner.nextLine();
        System.out.println("Enter the new last name, N/A if unchanged");
        newLastName = scanner.nextLine();

        System.out.println("Enter the new checkin date, N/A if unchanged");
        newCheckIn = scanner.nextLine();
        System.out.println("Enter the new checkout date, N/A if unchanged");
        newCheckOut = scanner.nextLine();

        System.out.println("Enter the new number of children, N/A if unchanged");
        newNumOfChildren = scanner.nextLine();
        System.out.println("Enter the new number of adults, N/A if unchanged");
        newNumOfAdults = scanner.nextLine();
        //System.out.println(newFirstName);

        if (!newFirstName.equals("N/A")) {
            String updateSql = "UPDATE lab7_reservations set firstname=? where code = ?;";
            UpdatingContents(conn, reservationCode, newFirstName, updateSql, "Name");
        }

        if (!newLastName.equals("N/A")) {
            String updateSql = "UPDATE lab7_reservations set lastname=? where code = ?;";
            UpdatingContents(conn, reservationCode, newLastName, updateSql, "Name");
        }

        if (!newCheckIn.equals("N/A")) {
            String updateSql = "UPDATE lab7_reservations set CheckIn=? where code = ?;";
            UpdatingContents(conn, reservationCode, newCheckIn, updateSql, "Date");
        }

        if (!newCheckOut.equals("N/A")) {
            String updateSql = "UPDATE lab7_reservations set CheckOut=? where code = ?;";
            UpdatingContents(conn, reservationCode, newCheckIn, updateSql, "Date");
        }
        if (!newNumOfChildren.equals("N/A")) {
            String updateSql = "UPDATE lab7_reservations set Kids=? where code = ?;";
            UpdatingContents(conn, reservationCode, newNumOfChildren, updateSql, "NumberChange");
        }
        if (!newNumOfAdults.equals("N/A")) {
            String updateSql = "UPDATE lab7_reservations set Adults=? where code = ?;";
            UpdatingContents(conn, reservationCode, newNumOfAdults, updateSql, "NumberChange");
        }
    }

    private static void UpdatingContents(Connection conn, int reservationCode, String changeToBeMade, String sqlStatement, String TypeOfChange) {
        if (TypeOfChange.equals("Name")) {
            try (PreparedStatement pstmt = conn.prepareStatement(sqlStatement)) {
                pstmt.setString(1, changeToBeMade);
                pstmt.setInt(2, reservationCode);
                pstmt.executeUpdate();
                conn.commit();
                System.out.println("Success updating " + changeToBeMade);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error updating " + changeToBeMade);
            }
        }
        if (TypeOfChange.equals("Date")) {
            try (PreparedStatement pstmt = conn.prepareStatement(sqlStatement)) {
                pstmt.setDate(1, java.sql.Date.valueOf(changeToBeMade));
                pstmt.setInt(2, reservationCode);
                pstmt.executeUpdate();
                conn.commit();
                System.out.println("Success updating " + changeToBeMade);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error updating " + changeToBeMade);
            }
        }
        if (TypeOfChange.equals("NumberChange")) {
            try (PreparedStatement pstmt = conn.prepareStatement(sqlStatement)) {
                pstmt.setInt(1, Integer.parseInt(changeToBeMade));
                pstmt.setInt(2, reservationCode);
                pstmt.executeUpdate();
                conn.commit();
                System.out.println("Success updating " + changeToBeMade);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error updating " + changeToBeMade);
            }
        }
    }
}






