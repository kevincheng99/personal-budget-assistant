/**
 * 
 */
package com.example.plutusbank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.database.Cursor;
import android.util.Log;

/**
 * BankDatabaseSimulation creates a simple databases. It has a pre-defined three
 * users. Each user has a checking and saving account, which may have 0 to 1000
 * dollars initially. The initial account threshold is 50. We have six
 * transaction types: food, clothing, housing, transportation, entertainment and
 * others. Each transaction has a date and amount from 1 to 500. The date is in
 * the MM-DD-YYYY format. For simplicity, the month is either October and
 * November. For simplicity, the day is from 1 to 30. For simplicity, the year
 * is 2014.
 * 
 * @author Kevin Cheng
 * 
 */
public class BankDatabaseSimulation {
    // Define the debug tag in the logcat.
    private static final String DEBUG_TAG = "DebugBankDatabaseSimulation";

    // Define the multiplication factor of account balance, generated by a
    // random number generator. [0, 1.0) * 3000
    private static final double ACCOUNT_BALANCE_FACTOR = 3000.0;

    // Define the threshold of checking account.
    private static final double CHECK_ACCOUNT_THRESHOLD = 50.0;

    // Define the threshold of saving account.
    private static final double SAVING_ACCOUNT_THRESHOLD = 50.0;

    // Define the number of users.
    private static final int NUMBER_USER = 3;

    // Define the shopping categories.
    private static final String[] SHOPPING_CATEGORY = new String[] { "food",
            "clothing", "housing", "transportation", "entertainment", "others" };

    // Define the multiplication factor for the item price, generated by a
    // random number generator. [0, 1.0) * 500
    private static final double ITEM_PRICE_FACTOR = 500;

    // Define the number of transaction per user.
    private static final int NUMBER_TRANSACTION_PER_USER = 10;

    // Initialize a BankDatabaseManager to null.
    private BankDatabaseManager myDatabaseManager = null;

    /**
     * Dummy constructor of doing nothing.
     */
    public BankDatabaseSimulation() {
    }

    /**
     * Construct the instances with the an input database manager.
     * 
     * @param inputDatabaseManager
     */
    public BankDatabaseSimulation(BankDatabaseManager inputDatabaseManager) {
        myDatabaseManager = inputDatabaseManager;
    }

    /**
     * The function simulates the bank database.
     */
    public void simulate() {
        // Populate the user table.
        populateUserTable();

        // Populate the bank account table.
        populateBankAccountTable();

        // Populate the transaction table.
        populateTransactionTable();

        // Populate the user-bankAccount table.
        populateUserBankAccountTable();

        // Populate the purchase table.
        populatePurchaseTable();
    }

    /**
     * The function populates the User table.
     */
    public void populateUserTable() {
        // Define the first user: username, password, full name, phone number
        // and
        // email.
        String username = "chuck";
        String password = "wrestle";
        String fullName = "chuck norris";
        String phoneNumber = "411";
        String email = "cnorris@wrestle.polarbear.com";

        // Insert the first user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Define the second user.
        username = "jvcd";
        password = "split";
        fullName = "jean-claude van damme";
        phoneNumber = "511";
        email = "jdamme@space.split.com";

        // Insert the second user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);

        // Define the third user.
        username = "bear";
        password = "comeon";
        fullName = "polar bear";
        phoneNumber = "911";
        email = "pbear@wrestle.chucknorris.com";

        // Insert the third user data.
        myDatabaseManager.addUser(username, password, fullName, phoneNumber,
                email);
    }

    /**
     * The function populates the bank account table. We have three users. Each
     * has a checking or saving account.
     */
    public void populateBankAccountTable() {
        // For each of three users
        for (int iUser = 0; iUser < NUMBER_USER; ++(iUser)) {
            // Initialize a random number generator.
            Random myRandomNumberGenerator = new Random();

            // Define a checking account: type, balance and threshold.
            String type = "checking";
            double threshold = CHECK_ACCOUNT_THRESHOLD;
            double accountBalance = myRandomNumberGenerator.nextDouble()
                    * ACCOUNT_BALANCE_FACTOR;

            // Round the account balance to two decimal places.
            accountBalance = Math.round(accountBalance * 100) / 100.0d;

            // Insert the checking account.
            myDatabaseManager.addBankAccount(type, accountBalance, threshold);

            // Define a saving account: type, balance and threshold.
            type = "saving";
            threshold = SAVING_ACCOUNT_THRESHOLD;
            accountBalance = myRandomNumberGenerator.nextDouble()
                    * ACCOUNT_BALANCE_FACTOR;

            // Round the account balance to two decimal places.
            accountBalance = Math.round(accountBalance * 100) / 100.0d;

            // Insert the saving account.
            myDatabaseManager.addBankAccount(type, accountBalance, threshold);
        }
    }

    /**
     * The function populates the transaction table.
     */
    public void populateTransactionTable() {
        // Compute the total number of transactions.
        int nTransaction = NUMBER_USER * NUMBER_TRANSACTION_PER_USER;

        // For each transaction
        for (int iTransaction = 0; iTransaction < nTransaction; ++(iTransaction)) {
            // Initialize a random number generator.
            Random myRandomNumberGenerator = new Random();

            // Get the number of shopping category.
            int nCategory = SHOPPING_CATEGORY.length;

            // Generate a random index.
            int randomIndex = myRandomNumberGenerator.nextInt(nCategory);

            // Randomly select a shopping type.
            String shoppingType = SHOPPING_CATEGORY[randomIndex];

            // Randomly generated an item price.
            double itemPrice = myRandomNumberGenerator.nextDouble()
                    * ITEM_PRICE_FACTOR;

            // Round the item price to two decimal places.
            itemPrice = Math.round(itemPrice * 100) / 100.0d;

            // Initialize the month, day and year.
            int month = 10;
            int day = -1;
            int year = 2014;

            // Randomly select a month between October (10) and November (11).
            if (myRandomNumberGenerator.nextBoolean()) {
                month = 11;
            }

            // Randomly select a day from 1 to 30.
            day = myRandomNumberGenerator.nextInt(30) + 1;

            // Initialize an empty data string.
            String date = "";

            // Construct a date string. To convert date string to integer:
            // String.split. Integer.parseInt
            if (day < 10) {
                date = month + "-" + "0" + day + "-" + year;
            } else {
                date = month + "-" + day + "-" + year;
            }

            // Insert the transaction record.
            myDatabaseManager.addTransactionRecord(shoppingType, itemPrice,
                    date);
        }
    }

    /**
     * The function populates user-bank-accounts table. Since we have three
     * users and each has 1 checking and 1 saving account, we can assign the
     * accounts in checking-saving order for each user.
     */
    public void populateUserBankAccountTable() {
        // Initialize the account number.
        int accountNumber = 0;

        // For each user, they get the next two accounts.
        for (int userid = 1; userid <= NUMBER_USER; ++(userid)) {
            // Increment the account number.
            ++(accountNumber);

            // Insert user-bankaccount record.
            myDatabaseManager.addUserToBankAccount(userid, accountNumber);

            // Increment the account number.
            ++(accountNumber);

            // Insert user-bankaccount record.
            myDatabaseManager.addUserToBankAccount(userid, accountNumber);
        }
    }

    /**
     * The function populates purchase table.
     */
    public void populatePurchaseTable() {
        // Compute the total number of transactions.
        int nTransaction = NUMBER_USER * NUMBER_TRANSACTION_PER_USER;

        // Initialize an list to contain transaction id in an random order.
        ArrayList<Integer> transactionIdList = new ArrayList<Integer>();

        // Fill the list with transaction id.
        for (int transactionId = 1; transactionId <= nTransaction; ++(transactionId)) {
            transactionIdList.add(Integer.valueOf(transactionId));
        }

        // Shuffle the transaction IDs.
        Collections.shuffle(transactionIdList);

        // for each user, get n transactions or transaction IDs.
        for (int iUser = 0; iUser < NUMBER_USER; ++(iUser)) {

            // for each of n transaction ids, we associated it with userid and
            // account number.
            for (int jTransaction = 0; jTransaction < NUMBER_TRANSACTION_PER_USER; ++(jTransaction)) {
                // Initialize a random number generator.
                Random myRandomNumberGenerator = new Random();

                // Initialize the account number.
                int accountNumber = 2 * iUser + 1;

                // Choose an account number
                if (myRandomNumberGenerator.nextBoolean()) {
                    ++(accountNumber);
                }

                // Pop the transaction id from the beginning.
                Integer transactionId = transactionIdList.remove(0);

                // Insert the purchase record.
                myDatabaseManager.addPurchaseRecord(iUser + 1, accountNumber,
                        transactionId.intValue());
            }
        }
    }

    /**
     * The function prints the BankAccount table.
     */
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

    /**
     * The function prints the BankAccount table.
     */
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

    /**
     * The function prints the BankAccount table.
     */
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

    /**
     * The function prints the BankAccount table.
     */
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

    /**
     * The function prints the User table.
     */
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

} // end of BankDatabaseSimulation
