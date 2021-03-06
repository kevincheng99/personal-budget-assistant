package com.polarbear.plutus.technical;

import java.util.Date;

import com.example.plutus.R;
import com.polarbear.plutus.ui.LoginActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class BankDatabaseSimulationActivity extends ActionBarActivity {
  // Define the debugging tag.
  private static final String DEBUG_TAG = "DebugDbSimulationActivity";

  // Initialize the bank database with the bank database manager.
  private BankDatabaseManager plutusDbManager = new BankDatabaseManager(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bank_database_simulation);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, new PlaceholderFragment()).commit();
    }
  }

  public void startSimulation(View view) {
    // Start the timer.
    Date startTime = new Date();

    // Instantiate a database simulator.
    BankDatabaseSimulation databaseSimulator = new BankDatabaseSimulation(
        plutusDbManager);

    // Drop all the tables.
    plutusDbManager.dropAllTable();

    // Simulate tables.
    databaseSimulator.simulate();

    // Stop the timer.
    Date endTime = new Date();

    // Display the running time.
    Log.d(DEBUG_TAG,
        "Simulation Time: " + (endTime.getTime() - startTime.getTime())
            / 1000.0d);

    /**
     * Print tables for the visual inspection.
     */
    // // Print the user table.
    // Cursor userTableCursor = plutusDbManager.getAllUser();
    // printUserTable(userTableCursor);
    //
    // // Print the bank account table.
    // Cursor bankAccountTableCursor = plutusDbManager.getAllBankAccount();
    // printBankAccountTable(bankAccountTableCursor);
    //
    // // Print the transaction table.
    // Cursor transactionTableCursor =
    // plutusDbManager.getAllTransactionRecord();
    // printTransactionTable(transactionTableCursor);
    //
    // // Print the user-bankaccount table.
    // Cursor userBankAccountTableCursor = plutusDbManager
    // .getAllUserBankAccountRecord();
    // printUserBankAccountTable(userBankAccountTableCursor);
    //
    // // Print the purchase table.
    // Cursor purchaseTableCursor = plutusDbManager.getAllPurchaseRecord();
    // printPurchaseTable(purchaseTableCursor);
  }

  /**
   * The function test the update user data and update account threshold.
   * 
   * @param view
   */
  public void testUpdate(View view) {
    // Update first user's password to !@#$%
    plutusDbManager.updateUser(1, "!@#$%", null, null, null);

    // Update the threshold to 1000 for the account number as 2.
    plutusDbManager.updateAccountThreshold(2, 1000);

    /**
     * Display user table and bank account table.
     */
    // Print the user table.
    Cursor userTableCursor = plutusDbManager.getAllUser();
    printUserTable(userTableCursor);

    // Print the bank account table.
    Cursor bankAccountTableCursor = plutusDbManager.getAllBankAccount();
    printBankAccountTable(bankAccountTableCursor);
  }

  /**
   * The function test the retrieval of data. Please be cautious when using this
   * function after the testUpdate, since testUpdate update the password and
   * account threshold.
   * 
   * @param view
   */
  public void testRetrieval(View view) {
    // Get the userid for Chuck Norris.
    int userid = plutusDbManager.getUserid("chuck", "wrestle");

    // Test the userid.
    if (userid != 1) {
      Log.e(DEBUG_TAG, "ERROR: WRONG USERID: " + userid);
    } else {
      Log.d(DEBUG_TAG, "Right userid: " + userid);
    }

    // Get the cursor of the bank account table associated with the userid 1.
    Cursor bankAccountCursor = plutusDbManager.getBankAccountTableCursor(1);

    // Print the bank account table associated with the userid 1.
    printBankAccountTable(bankAccountCursor);

    // Get the cursor of the bank account table associated with the userid.
    Cursor transactionTableCursor = plutusDbManager
        .getTransactionTableCursor(userid);

    // print the transaction table associated with the userid.
    printTransactionTable(transactionTableCursor);
  }

  /**
   * The function prints the Purchase table.
   */
  public void printPurchaseTable(Cursor purchaseTableCursor) {
    // Retrieve the purchase table.
    // Cursor purchaseTableCursor = myDatabaseManager.getAllPurchaseRecord();

    // Print the purchase table.
    while (!purchaseTableCursor.isAfterLast()) {
      // Initialize a row string for the display purpose.
      String purchaseData = "";

      // Get the column index for the entry id.
      int columnIndex = purchaseTableCursor
          .getColumnIndexOrThrow(BankDatabaseSchema.Purchase._ID);

      // Get the entry id.
      purchaseData = purchaseData + purchaseTableCursor.getString(columnIndex)
          + "|";

      // Get the column index for the userid.
      columnIndex = purchaseTableCursor
          .getColumnIndexOrThrow(BankDatabaseSchema.Purchase.COLUMN_NAME_USERID);

      // Get the user id.
      purchaseData = purchaseData + purchaseTableCursor.getString(columnIndex)
          + "|";

      // Get the column index for the account number.
      columnIndex = purchaseTableCursor
          .getColumnIndexOrThrow(BankDatabaseSchema.Purchase.COLUMN_NAME_ACCOUNT_NUMBER);

      // Get the account number.
      purchaseData = purchaseData + purchaseTableCursor.getString(columnIndex)
          + "|";

      // Get the column index for the transaction id.
      columnIndex = purchaseTableCursor
          .getColumnIndexOrThrow(BankDatabaseSchema.Purchase.COLUMN_NAME_TRASACTION_ID);

      // Get the account number.
      purchaseData = purchaseData + purchaseTableCursor.getString(columnIndex)
          + "|";

      Log.d(DEBUG_TAG, "Purchase record: " + purchaseData);

      // Move to the next entry.
      purchaseTableCursor.moveToNext();
    }

    // Close the cursor to release related resources.
    purchaseTableCursor.close();
  }

  /**
   * The function prints the User-BankAccount table.
   */
  public void printUserBankAccountTable(Cursor userBankAccountTableCursor) {
    // Retrieve the transaction table.
    // Cursor userBankAccountTableCursor = myDatabaseManager
    // .getAllUserBankAccountRecord();

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
   * The function prints the Transaction table.
   */
  public void printTransactionTable(Cursor transactionTableCursor) {
    // Retrieve the transaction table.
    // Cursor transactionTableCursor =
    // myDatabaseManager.getAllTransactionRecord();

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
  public void printBankAccountTable(Cursor bankAccountTableCursor) {
    // Retrieve the bank account table.
    // Cursor bankAccountTableCursor = myDatabaseManager.getAllBankAccount();

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
  public void printUserTable(Cursor userTableCursor) {
    // Retrieve the user table.
    // Cursor userTableCursor = myDatabaseManager.getAllUser();

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

  // When resume/running/visible to the user, open the database for the read
  // and write.
  @Override
  protected void onResume() {
    plutusDbManager.openReadWriteMode();
    super.onResume();
  }

  // When pause, close any open database.
  @Override
  protected void onPause() {
    plutusDbManager.close();
    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.bank_database_simulation, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // Select the help event, logout event or default event.
    switch (id) {
    case R.id.action_help:
      // Display a help documentation to describe the activity of view account
      // balance.

      // When successfully handling a menu item, return true.
      return true;
    case R.id.action_logout:
      // Initialize the intent to the login activity.
      Intent loginIntent = new Intent(this, LoginActivity.class);

      // Clear the top activities in the task stack and go back to log in
      // activity.
      loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      // Redirect the user the login activity.
      startActivity(loginIntent);

      // When successfully handling a menu item, return true.
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
      View rootView = inflater.inflate(
          R.layout.fragment_bank_database_simulation, container, false);
      return rootView;
    }
  }

}

// public void testBankAccountCursor(int userid) {
// // Get the cursor of the bank account table associated with the userid.
// Cursor bankAccountCursor = plutusDbManager
// .getBankAccountTableCursor(userid);
//
// // print the bank account table associated with the userid.
// // the cursor is closed in the print function.
// printBankAccountTable(bankAccountCursor);
// }

// public void testTransactionCursor(int userid) {
// // Get the cursor of the bank account table associated with the userid.
// Cursor transactionTableCursor = plutusDbManager
// .getTransactionTableCursor(userid);
//
// // print the bank account table associated with the userid.
// // the cursor is closed in the print function.
// this.printTransactionTable(transactionTableCursor);
//
// // Close the cursor to release resources.
// // bankAccountCursor.close();
// }