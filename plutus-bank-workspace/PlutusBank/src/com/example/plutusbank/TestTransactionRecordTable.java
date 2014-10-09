package com.example.plutusbank;

import android.database.Cursor;
import android.util.Log;

public class TestTransactionRecordTable {
    // Initialize a debug tag while testing the bank account table.
    private static final String DEBUG_TAG = "DebugTransactionRecordTable";

    // Declare a BankDatabaseManager.
    private BankDatabaseManager myDatabaseManager = null;

    public TestTransactionRecordTable(BankDatabaseManager inputDatabaseManager) {
        myDatabaseManager = inputDatabaseManager;
    }

    /**
     * Print a table.
     */
    // The function prints the BankAccount table.
    public void printTransactionTable() {
        // Retrieve the transaction table.
        Cursor transactionTableCursor = myDatabaseManager
                .getAllTransactionRecord();

        // Print the transaction table.
        while (!transactionTableCursor.isAfterLast()) {
            // Initialize a row string for the display purpose.
            String transactionData = "";

            // Get the column index for the transaction id.
            int columnIndex = transactionTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord._ID);

            // Get the transaction id.
            transactionData = transactionData
                    + transactionTableCursor.getString(columnIndex) + "|";

            // Get the column index for the transaction type..
            columnIndex = transactionTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord.COLUMN_NAME_TYPE);

            // Get the transaction type.
            transactionData = transactionData
                    + transactionTableCursor.getString(columnIndex) + "|";

            // Get the column index for the amount of transaction
            columnIndex = transactionTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord.COLUMN_NAME_AMOUNT);

            // Get the amount of transaction.
            transactionData = transactionData
                    + transactionTableCursor.getString(columnIndex) + "|";

            // Get the column index for the transaction date.
            columnIndex = transactionTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord.COLUMN_NAME_DATE);

            // Get the transaction date.
            transactionData = transactionData
                    + transactionTableCursor.getString(columnIndex) + "|";

            // Display the user data.
            Log.d(DEBUG_TAG, "Transaction: " + transactionData);

            // Move to the next entry.
            transactionTableCursor.moveToNext();
        }

        // Close the cursor to release related resources.
        transactionTableCursor.close();
    }

    // The function test the bank account table.
    public void testTransactionTable1() {
        // Define the first transaction.
        String type = "food";
        double amount = 100.0;
        String date = "10-05-2014";

        // Insert the transaction.
        myDatabaseManager.addTransactionRecord(type, amount, date);

        // Define the second transaction.
        type = "clothing";
        amount = 300.0;
        date = "";

        // Insert the second transaction
        myDatabaseManager.addTransactionRecord(type, amount, date);

        // Display bank accounts.
        printTransactionTable();
    }

    // The function test the transaction table: empty type.
    public void testTransactionTable2() {
        // Define the first transaction.
        String type = "";
        double amount = 100.0;
        String date = "10-05-2014";

        // Insert the transaction.
        myDatabaseManager.addTransactionRecord(type, amount, date);

        // Display bank accounts.
        printTransactionTable();
    }

    // The function test the transaction table: null type.
    public void testTransactionTable3() {
        // Define the first transaction.
        String type = null;
        double amount = 100.0;
        String date = "10-05-2014";

        // Insert the transaction.
        myDatabaseManager.addTransactionRecord(type, amount, date);

        // Display bank accounts.
        printTransactionTable();
    }

    // The function test the transaction table: negative amount.
    public void testTransactionTable4() {
        // Define the first transaction.
        String type = "food";
        double amount = -100.0;
        String date = "10-05-2014";

        // Insert the transaction.
        myDatabaseManager.addTransactionRecord(type, amount, date);

        // Display bank accounts.
        printTransactionTable();
    }

    // The function test the transaction table: empty date.
    public void testTransactionTable5() {
        // Define the first transaction.
        String type = "food";
        double amount = 100.0;
        String date = "";

        // Insert the transaction.
        myDatabaseManager.addTransactionRecord(type, amount, date);

        // Display bank accounts.
        printTransactionTable();
    }

    // The function test the transaction table: empty date.
    public void testTransactionTable6() {
        // Define the first transaction.
        String type = "food";
        double amount = 100.0;
        String date = null;

        // Insert the transaction.
        myDatabaseManager.addTransactionRecord(type, amount, date);

        // Display bank accounts.
        printTransactionTable();
    }

} // end of TestTransactionRecordTable
