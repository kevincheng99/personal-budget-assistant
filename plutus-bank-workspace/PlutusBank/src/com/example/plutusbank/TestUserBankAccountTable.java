/**
 * 
 */
package com.example.plutusbank;

import android.database.Cursor;
import android.util.Log;

/**
 * @author Kevin Cheng
 * 
 */
public class TestUserBankAccountTable {
    // Initialize a debug tag while testing the user-bank-account table.
    private static final String DEBUG_TAG = "DebugUserBankAccountTable";

    // Declare a BankDatabaseManager.
    private BankDatabaseManager myDatabaseManager = null;

    public TestUserBankAccountTable(BankDatabaseManager inputDatabaseManager) {
        myDatabaseManager = inputDatabaseManager;
    }

    /**
     * Print a table.
     */
    // The function prints the BankAccount table.
    public void printUserBankAccountTable() {
        // Retrieve the transaction table.
        Cursor userBankAccountTableCursor = myDatabaseManager
                .getAllUserBankAccountRecord();

        // Print the transaction table.
        while (!userBankAccountTableCursor.isAfterLast()) {
            // Initialize a row string for the display purpose.
            String userBankAccountData = "";

            // Get the column index for the entry id.
            int columnIndex = userBankAccountTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.UserBankAccount._ID);

            // Get the entry id.
            userBankAccountData = userBankAccountData
                    + userBankAccountTableCursor.getString(columnIndex) + "|";

            // Get the column index for the userid.
            columnIndex = userBankAccountTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.UserBankAccount.COLUMN_NAME_USERID);

            // Get the user id.
            userBankAccountData = userBankAccountData
                    + userBankAccountTableCursor.getString(columnIndex) + "|";

            // Get the column index for the account number.
            columnIndex = userBankAccountTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.UserBankAccount.COLUMN_NAME_ACCOUNT_NUMBER);

            // Get the account number.
            userBankAccountData = userBankAccountData
                    + userBankAccountTableCursor.getString(columnIndex) + "|";

            Log.d(DEBUG_TAG, "User-Bank Account: " + userBankAccountData);

            // Move to the next entry.
            userBankAccountTableCursor.moveToNext();
        }

        // Close the cursor to release related resources.
        userBankAccountTableCursor.close();
    }

    // The function test the user-bank-account table: insert two records.
    public void testUserBankAccountTable1() {
        // Define the user-bank-account entry.
        int userid = 1;
        int accountNumber = 1;

        // Insert the new data.
        myDatabaseManager.addUserToBankAccount(userid, accountNumber);

        // Define the second user-bank-account entry.
        accountNumber = 2;
        
        // Insert the new data.
        myDatabaseManager.addUserToBankAccount(userid, accountNumber);

        // print the user-bank-account table.
        printUserBankAccountTable();
    }

} // end of UserBankAccountTable.
