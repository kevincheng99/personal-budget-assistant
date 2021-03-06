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
public class TestPurchaseTable {
    // Initialize a debug tag while testing the user-bank-account table.
    private static final String DEBUG_TAG = "DebugPurchaseTable";

    // Declare a BankDatabaseManager.
    private BankDatabaseManager myDatabaseManager = null;

    public TestPurchaseTable(BankDatabaseManager inputDatabaseManager) {
        myDatabaseManager = inputDatabaseManager;
    }

    /**
     * Print a table.
     */
    // The function prints the BankAccount table.
    public void printPurchaseTable() {
        // Retrieve the purchase table.
        Cursor purchaseTableCursor = myDatabaseManager.getAllPurchaseRecord();

        // Print the purchase table.
        while (!purchaseTableCursor.isAfterLast()) {
            // Initialize a row string for the display purpose.
            String purchaseData = "";

            // Get the column index for the entry id.
            int columnIndex = purchaseTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.Purchase._ID);

            // Get the entry id.
            purchaseData = purchaseData
                    + purchaseTableCursor.getString(columnIndex) + "|";

            // Get the column index for the userid.
            columnIndex = purchaseTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.Purchase.COLUMN_NAME_USERID);

            // Get the user id.
            purchaseData = purchaseData
                    + purchaseTableCursor.getString(columnIndex) + "|";

            // Get the column index for the account number.
            columnIndex = purchaseTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.Purchase.COLUMN_NAME_ACCOUNT_NUMBER);

            // Get the account number.
            purchaseData = purchaseData
                    + purchaseTableCursor.getString(columnIndex) + "|";

            // Get the column index for the transaction id.
            columnIndex = purchaseTableCursor
                    .getColumnIndexOrThrow(BankDatabaseSchema.Purchase.COLUMN_NAME_TRASACTION_ID);

            // Get the account number.
            purchaseData = purchaseData
                    + purchaseTableCursor.getString(columnIndex) + "|";

            Log.d(DEBUG_TAG, "Purchase record: " + purchaseData);

            // Move to the next entry.
            purchaseTableCursor.moveToNext();
        }

        // Close the cursor to release related resources.
        purchaseTableCursor.close();
    }

    // The function test the purchase table: insert two records.
    public void testPurchaseTable1() {
        // Define the purchase record
        int userid = 1;
        int accountNumber = 2;
        int transactionId = 3;

        // Insert the new data.
        myDatabaseManager.addPurchaseRecord(userid, accountNumber,
                transactionId);

        // Define the second user-bank-account entry.
        userid = 4;
        accountNumber = 5;
        transactionId = 6;

        // Insert the new data.
        myDatabaseManager.addPurchaseRecord(userid, accountNumber,
                transactionId);

        // print the user-bank-account table.
        printPurchaseTable();
    }

}
