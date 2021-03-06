/**
 * 
 */
package com.example.plutusbank;

import android.database.Cursor;
import android.util.Log;

/**
 * @author kevin
 * 
 */
public class TestUserTable {
    // Initialize a debug tag while testing the user table.
    private static final String DEBUG_TAG = "DebugUserTable";

    // Declare a BankDatabaseManager.
    private BankDatabaseManager myDatabaseManager = null;

    public TestUserTable(BankDatabaseManager inputDatabaseManager) {
        myDatabaseManager = inputDatabaseManager;
    }

    // The function prints the User table.
    public void printUserTable() {
        // Retrieve the user table.
        Cursor userTableCursor = myDatabaseManager.getAllUser();

        // Print the user table.
        while (!userTableCursor.isAfterLast()) {
            // Initialize a row string for the display purpose.
            String userData = "";

            // Get the column index for the userid.
            int columnIndex = userTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.User._ID);

            // Get the username.
            userData = userData + userTableCursor.getString(columnIndex) + "|";

            // Get the column index for the userid.
            columnIndex = userTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.User.COLUMN_NAME_USERNAME);

            // Get the username.
            userData = userData + userTableCursor.getString(columnIndex) + "|";

            // Get the column index for the password.
            columnIndex = userTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.User.COLUMN_NAME_PASSWORD);

            // Get the password.
            userData = userData + userTableCursor.getString(columnIndex) + "|";

            // Get the column index for the full name.
            columnIndex = userTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.User.COLUMN_NAME_FULL_NAME);

            // Get the full name.
            userData = userData + userTableCursor.getString(columnIndex) + "|";

            // Get the column index for the phone number.
            columnIndex = userTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.User.COLUMN_NAME_PHONE);

            // Get the phone number.
            userData = userData + userTableCursor.getString(columnIndex) + "|";

            // Get the column index for the email.
            columnIndex = userTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.User.COLUMN_NAME_EMAIL);

            // Get the email.
            userData = userData + userTableCursor.getString(columnIndex) + "|";

            // Display the user data.
            Log.d(DEBUG_TAG, "User: " + userData);

            // Move to the next entry.
            userTableCursor.moveToNext();
        }

        // Close the cursor to release related resources.
        userTableCursor.close();
    }

    // The function test the user table: insert one record.
    public void testUserTable1() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: insert two record.
    public void testUserTable2() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Define the second record.
        username = "lidia";
        password = "cpsc462";
        fullName = "lidia morrison";
        phoneNumber = "(890)123-4567";
        email = "lmorrison@fullerton.edu";

        // Insert the second user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: invalid password.
    public void testUserTable3() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information
        myDatabaseManager.updateUser(1, "", "kevin zu", "", "");

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: insert two record.
    public void testUserTable4() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Define the second record.
        username = "lidia";
        password = "cpsc462";
        fullName = "lidia morrison";
        phoneNumber = "(890)123-4567";
        email = "lmorrison@fullerton.edu";

        // Insert the second user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Define the third record.
        username = "chuck";
        password = "split";
        fullName = "chuck norris";
        phoneNumber = "";
        email = "";

        // Insert the third record.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: invalid usrname.
    public void testUserTable5() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: invalid password.
    public void testUserTable7() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: invalid full name.
    public void testUserTable8() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "22345";
        String fullName = "";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: invalid usrname.
    public void testUserTable9() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = null;
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: invalid password.
    public void testUserTable10() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = null;
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: invalid full name.
    public void testUserTable11() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "22345";
        String fullName = null;
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an invalid password.
    public void testUserTable12() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information
        myDatabaseManager.updateUser(1, null, "kevin yu", "000",
                "kyu@fullerton.edu");

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an invalid password.
    public void testUserTable13() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information
        // Update user information
        myDatabaseManager.updateUser(1, "", "kevin yu", "000",
                "kyu@fullerton.edu");

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an invalid full name.
    public void testUserTable14() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information..
        // Update user information
        myDatabaseManager.updateUser(1, "8", "", "000", "kyu@fullerton.edu");

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an invalid full name..
    public void testUserTable15() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information
        myDatabaseManager.updateUser(1, "8", null, "000", "kyu@fullerton.edu");

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an invalid phone number.
    public void testUserTable16() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information..
        myDatabaseManager.updateUser(1, "8", "kevin yu", "",
                "kyu@fullerton.edu");

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an invalid phone number.
    public void testUserTable17() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information
        myDatabaseManager.updateUser(1, "8", "kevin yu", null,
                "kyu@fullerton.edu");

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an invalid email address.
    public void testUserTable18() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information..
        myDatabaseManager.updateUser(1, "8", "kevin yu", "000", "");

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an invalid email address.
    public void testUserTable19() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information
        myDatabaseManager.updateUser(1, "8", "kevin yu", "000", null);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an empty arguments
    public void testUserTable20() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information
        myDatabaseManager.updateUser(1, "", "", "", null);

        // Display the user tables
        printUserTable();
    }

    // The function test the user table: update with an empty arguments
    public void testUserTable21() {
        // Define an user data, including username, password, full name, phone
        // number and email address.
        String username = "kevin";
        String password = "12345";
        String fullName = "kevin cheng";
        String phoneNumber = "(123)456-7890";
        String email = "kevincheng99@csu.fullerton.edu";

        // Insert the new user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Update user information
        myDatabaseManager.updateUser(1, null, null, null, null);

        // Display the user tables
        printUserTable();
    }

} // End of TestUserTable

