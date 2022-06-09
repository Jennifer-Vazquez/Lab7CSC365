
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;

import java.util.Scanner;
import java.time.LocalDate;

public class InnReservations {
    public static class PossibleEntries {
        public String RoomCode, RoomN;
        public int rate;

            // constructor
            public PossibleEntries(String RC, String RN, int Ra) {
                this.RoomCode = RC;
                this.RoomN = RN;
                this.rate = Ra;
            }
    }

    public static HashMap<String, String> LinksToInputs = new HashMap<String, String>();

    public static void main(String[] args) throws SQLException {

        LinksToInputs.put("A", "firstname");
        LinksToInputs.put("B", "lastname");
        LinksToInputs.put("D", "Room");
        LinksToInputs.put("E", "Code");

        String EnvURL = System.getenv("HP_JDBC_URL");
        System.out.println(EnvURL);
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
                if (optionSelected.equals("2")) {
                    reservationSearch(myScan, conn);
                }
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
                        if (arg1Option.equals("D")) {
                            sql_stm.append(" = '");
                            sql_stm.append(arg1);
                            sql_stm.append("'");
                            sql_stm.append(" and ");
                        } else {
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
                        if (arg2Option.equals("D")) {
                            sql_stm.append(" = '");
                            sql_stm.append(arg2);
                            sql_stm.append("'");
                        } else {
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
                        if (arg2Option.equals("D")) {
                            sql_stm.append(" = '");
                            sql_stm.append(arg2);
                            sql_stm.append("'");
                        } else {
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
                        if (arg1Option.equals("D")) {
                            sql_stm.append(" = '");
                            sql_stm.append(arg1);
                            sql_stm.append("'");
                        } else {
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
                        if (arg2Option.equals("D")) {
                            sql_stm.append(" = '");
                            sql_stm.append(arg2);
                            sql_stm.append("'");
                        } else {
                            sql_stm.append(" = ");
                            sql_stm.append(arg2);
                        }
                    }
                    sql_stm.append(LinksToInputs.get(arg2Option));
                    if (arg2Option.equals("D")) {
                        sql_stm.append(" = '");
                        sql_stm.append(arg2);
                        sql_stm.append("'");
                    } else {
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
                        if (arg1Option.equals("D")) {
                            sql_stm.append(" = '");
                            sql_stm.append(arg1);
                            sql_stm.append("'");
                        } else {
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
                    System.out.format("\n%-10s %-10s %-15s %-15s %-10s %-20s %-20s %-10s %-10s\n", codeForRoom, RoomString, CheckInString, CheckOutString, rateFloat, FirstN, LastN, kidsNum, adultsNum);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public static void reservationSearch(Scanner scanner, Connection conn) throws SQLException {
        //•First name
        //•Last name
        //•A room code to indicate the specific room desired (or “Any” to indicate no preference)
        //•A desired bed type (or “Any” to indicate no preference)
        //•Begin date of stay•End date of stay
        //•Number of children•Number of adults
        String firstN;
        String lastN;
        String roomC;
        String bedT;
        String startD;
        String endD;
        String numOfChildren;
        String numOfAdults;

        ArrayList<PossibleEntries> possibilites = new ArrayList<PossibleEntries>();
        //PossibleEntries[] possibilites = new PossibleEntries[20];

        System.out.println("Please enter your first name:");
        firstN = scanner.nextLine();
        System.out.println("Please enter your last name:");
        lastN = scanner.nextLine();
        System.out.println("Please enter a desired roomCode, type 'ANY' if you have no preference");
        roomC = scanner.nextLine();
        System.out.println("Please enter the desired bed type, type 'ANY' if you have no preference");
        bedT = scanner.nextLine();
        System.out.println("Please enter the desired start date");
        startD = scanner.nextLine();
        System.out.println("Please enter the desired end date");
        endD = scanner.nextLine();
        System.out.println("Please enter the number of children");
        numOfChildren = scanner.nextLine();
        System.out.println("Please enter the number of adults");
        numOfAdults = scanner.nextLine();

        //occupied reseerations
        // start before the startD and ends after the endD -> checkIn <= startD and checkOut >= endD
        String dateCheck1 = "CheckIn <= '" + startD + "' and CheckOut >= '" + endD + "'";
        //starts before and ends before the checkout -> checkIn <= startD and checkOut <= endD
        String dateCheck2 = "CheckIn <= '" + startD + "' and CheckOut <= '" + endD + "'";
        // starts after startD and before the endD -> checkIn >= startD and checkOut <= endD
        String dateCheck3 = "CheckIn >= '" + startD + "' and CheckOut <= '" + endD + "'";
        //starts after startD and end after the endD -> checkIn >= startD and checkOut >= endD
        String dateCheck4 = "CheckIn >= '" + startD + "' and CheckOut >= '" + endD + "'";


        StringBuilder sql_stm = new StringBuilder("with RoomOccupations2 as (select lab7_reservations.code, RoomName, Room, CheckIn, Checkout,");
        sql_stm.append("Case when " + dateCheck1 + " and " + dateCheck2 + " and " + dateCheck3 + " and " + dateCheck4 + " ");
        sql_stm.append("then 'Occupied' else 'Empty' ");
        sql_stm.append("end as Status from lab7_reservations ");
        sql_stm.append("join lab7_rooms on lab7_rooms.RoomCode = lab7_reservations.Room ");
        sql_stm.append("group by code, RoomName), ");
        sql_stm.append("OccupiedCodes2 as (select Room from RoomOccupations2 ");
        sql_stm.append("where Status = 'Occupied' order by Room desc), \n ");
        sql_stm.append("UnoccupiedOnRange as (select * from RoomOccupations2 ");
        sql_stm.append("where Room not in (select * from OccupiedCodes2)), \n");
        sql_stm.append("distinctRooms as (select distinct Room from UnoccupiedOnRange) \n"); // unoccupied rooms

        //System.out.printlnun(sql_stm);
        boolean doneAlready = false;

        //•A room code to indicate the specific room desired (or “Any” to indicate no preference)
        //•A desired bed type (or “Any” to indicate no preference)
        //•Begin date of stay•End date of stay
        //•Number of children•Number of adults
        int totalOcc = Integer.parseInt(numOfAdults) + Integer.parseInt(numOfChildren);
        String total = String.valueOf(totalOcc);
        StringBuilder allOptions = new StringBuilder(sql_stm);
        if (!roomC.equals("ANY") && !bedT.equals("ANY")) {
            allOptions.append("select * from lab7_rooms where RoomCode in (select * from distinctRooms) and ");
            allOptions.append("RoomCode = '" + roomC + "' and ");
            allOptions.append("bedType = '" + bedT + "' and ");
            allOptions.append("maxOcc >= " + total);

        }
        else if(roomC.equals("ANY") && !bedT.equals("ANY")){
            allOptions.append("select * from lab7_rooms where RoomCode in (select * from distinctRooms) and ");
            allOptions.append("bedType = '" + bedT + "' and ");
            allOptions.append("maxOcc >= " + total);
        }
        else if(!roomC.equals("ANY") && bedT.equals("ANY")){
            allOptions.append("select * from lab7_rooms where RoomCode in (select * from distinctRooms) and ");
            allOptions.append("RoomCode = '" + roomC + "' and ");
            allOptions.append("maxOcc >= " + total);
        }
        else {
            doneAlready = true;
            allOptions.append("select * from lab7_rooms where RoomCode in (select * from distinctRooms) and ");
            allOptions.append("maxOcc >= " + total);
        }

        String sql = allOptions.toString();

        //executing SQL statement.
        try (PreparedStatement prep_stm = conn.prepareStatement(sql)) {
            try (ResultSet res_set = prep_stm.executeQuery()) {
                System.out.format("\n%-15s %-30s %-15s %-15s %-15s %-20s %-20s\n", "Room Code", "Room Name", "Beds", "BedType", "maxOcc", "Base Price", "Decor");

                while (res_set.next()) {
                    String codeForRoom = res_set.getString("RoomCode");
                    String RoomString = res_set.getString("RoomName");
                    String Beds = res_set.getString("Beds");
                    String BedTy = res_set.getString("bedType");
                    int maxO = res_set.getInt("MaxOcc");
                    int basePrice  = res_set.getInt("basePrice");
                    String decor = res_set.getString("decor");
                    System.out.format("\n%-15s %-30s %-15s %-15s %-15s %-20s %-20s\n", codeForRoom, RoomString, Beds, BedTy, maxO, basePrice, decor);
                    PossibleEntries newP = new PossibleEntries(codeForRoom, RoomString, basePrice);
                    possibilites.add(newP);


                    //possibilites.add(new PossibleEntries)
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(!doneAlready){
            StringBuilder RoomAva = new StringBuilder(sql_stm);
            RoomAva.append("select * from lab7_rooms where RoomCode in (select * from distinctRooms) and ");
            RoomAva.append("maxOcc >= " + total);

            String minSql = RoomAva.toString();

            //executing SQL statement.
            try (PreparedStatement prep_stm = conn.prepareStatement(minSql)) {
                try (ResultSet res_set = prep_stm.executeQuery()) {
                    System.out.format("\n%-15s %-30s %-15s %-15s %-15s %-20s %-20s\n", "Room Code", "Room Name", "Beds", "BedType", "maxOcc", "Base Price", "Decor");

                    while (res_set.next()) {
                        String codeForRoom = res_set.getString("RoomCode");
                        String RoomString = res_set.getString("RoomName");
                        String Beds = res_set.getString("Beds");
                        String BedTy = res_set.getString("bedType");
                        int maxO = res_set.getInt("MaxOcc");
                        int basePrice = res_set.getInt("basePrice");
                        String decor = res_set.getString("decor");
                        System.out.format("\n%-15s %-30s %-15s %-15s %-15s %-20s %-20s\n", codeForRoom, RoomString, Beds, BedTy, maxO, basePrice, decor);
                        PossibleEntries newP = new PossibleEntries(codeForRoom, RoomString, basePrice);
                        possibilites.add(newP);

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}



