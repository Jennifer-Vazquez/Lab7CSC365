
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;

import java.util.Scanner;
import java.time.LocalDate;

public class InnReservations {

    public static HashMap<String, String> LinksToInputs = new HashMap<String, String>();

    public static void main(String[] args) throws SQLException {

        LinksToInputs.put("A", "firstname");
        LinksToInputs.put("B", "lastname");
        LinksToInputs.put("D", "Room");
        LinksToInputs.put("E", "Code");

        try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
                System.getenv("HP_JDBC_USER"),
                System.getenv("HP_JDBC_PW"))) {


            Scanner myScan = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Enter number associated with option");
            String optionSelected = " ";
            conn.setAutoCommit(false);

            while (!optionSelected.equals("7")) {
                printOptions();
                System.out.print("Input: ");
                optionSelected = myScan.nextLine();
                if (optionSelected.equals("3")) {
                    reservationChange(myScan, conn);
                }
                if (optionSelected.equals("4")) {
                    deleteReservation(myScan, conn);
                }
                if (optionSelected.equals("5")) {
                    detailedReservation(myScan, conn);
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


    public static void deleteReservation(Scanner scanner, Connection conn) {

        int reservationCode;
        String confirmation;


        System.out.println("Enter the reservation code");
        reservationCode = scanner.nextInt();
        scanner.nextLine(); // get rid of the buffer
        System.out.println("Please confirm that you would like to delete reservation, Y for Yes, N for No");
        confirmation = scanner.nextLine();

        if (confirmation.equals("Y")) {
            String updateSql = "DELETE from lab7_reservations where code = ?;";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, reservationCode);
                pstmt.execute();
                conn.commit();
                System.out.println("Success deleting reservation with code: " + reservationCode);

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error deleting reservation");
            }
        }
    }

    //First name•Last name•A range of dates•Room code•Reservation code
    public static void detailedReservation(Scanner scanner, Connection conn) throws SQLException {
        String arg1Option;
        String arg1 = "";
        String arg2Option;
        String arg2 = "";
        StringBuilder sql_stm = new StringBuilder("select * from lab7_reservations where ");

        String date1 = null;
        String date2 = null;

        System.out.println("Pick a pair from the following list: (A) First Name, (B) Last Name, (C) Range of Dates, (D) Room Code, (E) Reservation Code");
        System.out.println("Pick first option (type it how it is in the above list, enter 'ANY' for a blank entry");
        arg1Option = scanner.nextLine();
        if (arg1Option.equals("A")) {
            System.out.println("Enter First Name");
            arg1 = scanner.nextLine();
        }
        if (arg1Option.equals("B")) {
            System.out.println("Enter Last Name");
            arg1 = scanner.nextLine();
        }
        if (arg1Option.equals("C")) {
            System.out.println("Enter First Date (Format must be YYYY-MM-DD)");
            date1 = scanner.nextLine();
            System.out.println("Enter Second Date (Format must be YYYY-MM-DD)");
            date2 = scanner.nextLine();
        }
        if (arg1Option.equals("D")) {
            System.out.println("Enter Room Code");
            arg1 = scanner.nextLine();
        }
        if (arg1Option.equals("E")) {
            System.out.println("Enter Reservation Code");
            arg1 = scanner.nextLine();
        }
        if (arg1Option.equals("ANY")) {
            arg1 = "ANY";
        }

        while (arg2.equals("")) {
            System.out.println("Pick second option (type it how it is in the above list, enter 'ANY' for a blank entry");
            arg2Option = scanner.nextLine();
            if (arg2Option.equals("A")) {
                System.out.println("Enter First Name");
                arg2 = scanner.nextLine();
            }
            if (arg2Option.equals("B")) {
                System.out.println("Enter Last Name");
                arg2 = scanner.nextLine();
            }
            if (arg2Option.equals("C")) {
                arg2 = "SKIP";
                System.out.println("Enter First Date (Format must be YYYY-MM-DD)");
                date1 = scanner.nextLine();
                System.out.println("Enter Second Date (Format must be YYYY-MM-DD)");
                date2 = scanner.nextLine();
            }
            if (arg2Option.equals("D")) {
                System.out.println("Enter Room Code");
                arg2 = scanner.nextLine();
            }
            if (arg2Option.equals("E")) {
                System.out.println("Enter Reservation Code");
                arg2 = scanner.nextLine();
            }
            if (arg2Option.equals("ANY")) {
                arg2 = "ANY";
            }


            if (!arg1Option.equals("C") && !arg2Option.equals("C")) {
                if (!arg1.equals("ANY") && !arg2.equals("ANY")) {
                    if (arg1Option.equals("A") || arg1Option.equals("B")) {
                        sql_stm.append(LinksToInputs.get(arg1Option));
                        sql_stm.append(" LIKE '%");
                        sql_stm.append(arg1);
                        sql_stm.append("%' and ");
                    } else {
                        sql_stm.append(LinksToInputs.get(arg1Option));
                        if(arg1Option.equals("D")){
                            sql_stm.append(" = '");
                            sql_stm.append(arg1);
                            sql_stm.append("'");
                            sql_stm.append(" and ");
                        }
                        else {
                            sql_stm.append(" = ");
                            sql_stm.append(arg1);
                            sql_stm.append(" and ");
                        }
                    }
                    if (arg2Option.equals("A") || arg2Option.equals("B")) {
                        sql_stm.append(LinksToInputs.get(arg2Option));
                        sql_stm.append(" LIKE '%");
                        sql_stm.append(arg2);
                        sql_stm.append("%'");
                    } else {
                        sql_stm.append(LinksToInputs.get(arg2Option));
                        if(arg2Option.equals("D")){
                            sql_stm.append(" = '");
                            sql_stm.append(arg2);
                            sql_stm.append("'");
                        }
                        else {
                            sql_stm.append(" = ");
                            sql_stm.append(arg2);
                        }
                    }
                }
                if (arg1.equals("ANY")) {
                    if (arg2Option.equals("A") || arg2Option.equals("B")) {
                        sql_stm.append(LinksToInputs.get(arg2Option));
                        sql_stm.append(" LIKE '%");
                        sql_stm.append(arg2);
                        sql_stm.append("%'");
                    } else {
                        sql_stm.append(LinksToInputs.get(arg2Option));
                        if(arg2Option.equals("D")){
                            sql_stm.append(" = '");
                            sql_stm.append(arg2);
                            sql_stm.append("'");
                        }
                        else {
                            sql_stm.append(" = ");
                            sql_stm.append(arg2);
                        }
                    }
                }
                if (arg2.equals("ANY")) {
                    if (arg1Option.equals("A") || arg1Option.equals("B")) {
                        sql_stm.append(LinksToInputs.get(arg1Option));
                        sql_stm.append(" LIKE '%");
                        sql_stm.append(arg1);
                        sql_stm.append("%'");
                    } else {
                        sql_stm.append(LinksToInputs.get(arg1Option));
                        if(arg1Option.equals("D")){
                            sql_stm.append(" = '");
                            sql_stm.append(arg1);
                            sql_stm.append("'");
                        }else {
                            sql_stm.append(" = ");
                            sql_stm.append(arg1);
                        }
                    }
                }
            }
            if (arg1Option.equals("C")) {
                if (arg2.equals("ANY")) {
                    sql_stm.append("CheckIn >= ");
                    sql_stm.append(date1);
                    sql_stm.append(" and ");
                    sql_stm.append("Checkout <= ");
                    sql_stm.append(date2);
                }
                if (!arg2.equals("ANY")) {
                    sql_stm.append("CheckIn >= '");
                    sql_stm.append(date1);
                    sql_stm.append("' and ");
                    sql_stm.append("Checkout <= '");
                    sql_stm.append(date2);
                    sql_stm.append("' and ");
                    if (arg2Option.equals("A") || arg2Option.equals("B")) {
                        sql_stm.append(LinksToInputs.get(arg2Option));
                        sql_stm.append(" LIKE '%");
                        sql_stm.append(arg2);
                        sql_stm.append("%'");
                    } else {
                        sql_stm.append(LinksToInputs.get(arg2Option));
                        if(arg2Option.equals("D")){
                            sql_stm.append(" = '");
                            sql_stm.append(arg2);
                            sql_stm.append("'");
                        }
                        else {
                            sql_stm.append(" = ");
                            sql_stm.append(arg2);
                        }
                    }
                    sql_stm.append(LinksToInputs.get(arg2Option));
                    if(arg2Option.equals("D")){
                        sql_stm.append(" = '");
                        sql_stm.append(arg2);
                        sql_stm.append("'");
                    }
                    else {
                        sql_stm.append(" = ");
                        sql_stm.append(arg2);
                    }
                }
            }

            if (arg2Option.equals("C")) {
                if (arg1.equals("ANY")) {
                    sql_stm.append("CheckIn >= '");
                    sql_stm.append(date1);
                    sql_stm.append("' and ");
                    sql_stm.append("Checkout <= '");
                    sql_stm.append(date2);
                    sql_stm.append("'");
                }
                if (!arg1.equals("ANY")) {
                    sql_stm.append("CheckIn >= '");
                    sql_stm.append(date1);
                    sql_stm.append("' and ");
                    sql_stm.append("Checkout <= '");
                    sql_stm.append(date2);
                    sql_stm.append("' and ");
                    if (arg1Option.equals("A") || arg1Option.equals("B")) {
                        sql_stm.append(LinksToInputs.get(arg1Option));
                        sql_stm.append(" LIKE '%");
                        sql_stm.append(arg1);
                        sql_stm.append("%'");
                    } else {
                        sql_stm.append(LinksToInputs.get(arg1Option));
                        if(arg1Option.equals("D")){
                            sql_stm.append(" = '");
                            sql_stm.append(arg1);
                            sql_stm.append("'");
                        }
                        else {
                            sql_stm.append(" = ");
                            sql_stm.append(arg1);
                        }
                    }
                }
            }
        }

        System.out.println(sql_stm);
        String sql = sql_stm.toString();

        //executing SQL statement.
        try (PreparedStatement prep_stm = conn.prepareStatement(sql)) {
            try (ResultSet res_set = prep_stm.executeQuery()) {

                //Output of Statement.
                System.out.format("\n%-10s %-10s %-15s %-15s %-10s %-20s %-20s %-10s %-10s\n", "Code", "Room", "CheckIn", "CheckOut", "Rate", "First Name", "Last Name", "Kids", "Adults");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");

                //CODE	Room	CheckIn	Checkout	Rate	LastName	FirstName	Adults	Kids
                while (res_set.next()) {
                    int codeForRoom = res_set.getInt("Code");
                    String RoomString = res_set.getString("Room");
                    String CheckInString = res_set.getString("CheckIn");
                    String CheckOutString = res_set.getString("CheckOut");
                    float rateFloat = res_set.getFloat("Rate");
                    String LastN = res_set.getString("LastName");
                    String FirstN = res_set.getString("FirstName");
                    int kidsNum = res_set.getInt("Kids");
                    int adultsNum = res_set.getInt("Adults");
                    System.out.format("\n%-10s %-10s %-15s %-15s %-10s %-20s %-20s %-10s %-10s\n", codeForRoom,RoomString, CheckInString, CheckOutString, rateFloat, FirstN, LastN, kidsNum, adultsNum);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}

