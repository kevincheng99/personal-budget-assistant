package com.example.plutusbank;

import android.database.Cursor;
import android.util.Log;

public class TestBankAccountTable {
    // Initialize a debug tag while testing the bank account table.
    private static final String DEBUG_TAG = "DebugBankAccountTable";

    // Declare a BankDatabaseManager.
    private BankDatabaseManager myDatabaseManager = null;

    public TestBankAccountTable(BankDatabaseManager inputDatabaseManager) {
        myDatabaseManager = inputDatabaseManager;
    }

    // The function prints the BankAccount table.
    public void printBankAccountTable() {
        // Retrieve the bank account table.
        Cursor bankAccountTableCursor = myDatabaseManager.getAllBankAccount();

        // Print the bank account table.
        while (!bankAccountTableCursor.isAfterLast()) {
            // Initialize a row string for the display purpose.
            String bankAccountData = "";

            // Get the column index for the account number.
            int columnIndex = bankAccountTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.BankAccount._ID);

            // Get the account number.
            bankAccountData = bankAccountData
                    + bankAccountTableCursor.getString(columnIndex) + "|";

            // Get the column index for the account type..
            columnIndex = bankAccountTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE);

            // Get the account type.
            bankAccountData = bankAccountData
                    + bankAccountTableCursor.getString(columnIndex) + "|";

            // Get the column index for the account balance.
            columnIndex = bankAccountTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE);

            // Get the account balance.
            bankAccountData = bankAccountData
                    + bankAccountTableCursor.getString(columnIndex) + "|";

            // Get the column index for the account threshold.
            columnIndex = bankAccountTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD);

            // Get the account threshold.
            bankAccountData = bankAccountData
                    + bankAccountTableCursor.getString(columnIndex) + "|";

            // Display the user data.
            Log.d(DEBUG_TAG, "Bank Account: " + bankAccountData);

            // Move to the next entry.
            bankAccountTableCursor.moveToNext();
        }

        // Close the cursor to release related resources.
        bankAccountTableCursor.close();
    }

    // The function test the bank account table: insert checking and saving
    // account.
    public void testBankAccountTable1() {
        // Define the first bank account.
        String type = "checking";
        double balance = 100.0;
        double threshold = 20.0;

        // Insert the first bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Define the second bank account.
        type = "saving";
        balance = 300.0;
        threshold = 50.0;

        // Insert the second bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Update the account threshold.
        // plutusDbManager.updateAccountThreshold(2, 1000);

        // Display bank accounts.
        printBankAccountTable();
    }

    // The function test the bank account table: insert checking and saving
    // account.
    public void testBankAccountTable2() {
        // Define the first bank account.
        String type = "checking";
        double balance = -100.0;
        double threshold = 20.0;

        // Insert the first bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Define the second bank account.
        type = "saving";
        balance = 300.0;
        threshold = 50.0;

        // Insert the second bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Display bank accounts.
        printBankAccountTable();
    }

    // The function test the bank account table: insert checking and saving
    // account.
    public void testBankAccountTable3() {
        // Define the first bank account.
        String type = "checking";
        double balance = 100.0;
        double threshold = -20.0;

        // Insert the first bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Define the second bank account.
        type = "saving";
        balance = 300.0;
        threshold = 50.0;

        // Insert the second bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Display bank accounts.
        printBankAccountTable();
    }

    // The function test the bank account table: insert an null type.
    public void testBankAccountTable4() {
        // Define the first bank account.
        String type = null;
        double balance = 100.0;
        double threshold = -20.0;

        // Insert the first bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Define the second bank account.
        type = "saving";
        balance = 300.0;
        threshold = 50.0;

        // Insert the second bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Display bank accounts.
        printBankAccountTable();
    }

    // The function test the bank account table: insert an empty type.
    public void testBankAccountTable5() {
        // Define the first bank account.
        String type = "";
        double balance = 100.0;
        double threshold = -20.0;

        // Insert the first bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Define the second bank account.
        type = "saving";
        balance = 300.0;
        threshold = 50.0;

        // Insert the second bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Display bank accounts.
        printBankAccountTable();
    }

    // The function test the function of update account threshold.
    public void testBankAccountTable6() {
        // Define the first bank account.
        String type = "checking";
        double balance = 100.0;
        double threshold = 20.0;

        // Insert the first bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Define the second bank account.
        type = "saving";
        balance = 300.0;
        threshold = 50.0;

        // Insert the second bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Update the account threshold.
        myDatabaseManager.updateAccountThreshold(2, 1000);

        // Display bank accounts.
        printBankAccountTable();
    }

    // The function test the function of update account with an negative
    // threshold.
    public void testBankAccountTable7() {
        // Define the first bank account.
        String type = "checking";
        double balance = 100.0;
        double threshold = 20.0;

        // Insert the first bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Define the second bank account.
        type = "saving";
        balance = 300.0;
        threshold = 50.0;

        // Insert the second bank account.
        myDatabaseManager.addBankAccount(type, balance, threshold);

        // Update the account threshold.
        myDatabaseManager.updateAccountThreshold(2, -100);

        // Display bank accounts.
        printBankAccountTable();
    }

} // end of TestBankAccountTable
