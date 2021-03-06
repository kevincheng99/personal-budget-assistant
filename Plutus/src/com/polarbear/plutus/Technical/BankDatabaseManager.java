/**
 * 
 */
package com.polarbear.plutus.technical;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * BankDataBaseManager create and maintain the Plutus SQLite database. The
 * functionality may includes the following or more: create tables, delete
 * tables, insert and update.
 * 
 * NOTE: Inner class, BankDatabaseHelper, helps to manage database creation and
 * version management. It is located at the very bottom of the implementation.
 * 
 * @author Kevin Cheng
 * 
 */
public class BankDatabaseManager {
  // Define a debug tag.
  private static final String DEBUG_TAG = "DebugBankDatabaseManager";

  /**
   * SQLite storage class.
   */
  private static final String TEXT_TYPE = " TEXT";
  private static final String INTEGER_TYPE = " INTEGER";
  private static final String REAL_TYPE = " REAL";

  /**
   * SQLite configuration phrase.
   */
  private static final String COMMA = ",";

  /**
   * SQLite statement: create table.
   */
  // Create User table statement.
  private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS "
      + BankDatabaseSchema.User.TABLE_NAME + " (" + BankDatabaseSchema.User._ID
      + " INTEGER PRIMARY KEY," + BankDatabaseSchema.User.COLUMN_NAME_USERNAME
      + TEXT_TYPE + COMMA + BankDatabaseSchema.User.COLUMN_NAME_PASSWORD
      + TEXT_TYPE + COMMA + BankDatabaseSchema.User.COLUMN_NAME_FULL_NAME
      + TEXT_TYPE + COMMA + BankDatabaseSchema.User.COLUMN_NAME_PHONE
      + TEXT_TYPE + COMMA + BankDatabaseSchema.User.COLUMN_NAME_EMAIL
      + TEXT_TYPE + " )";

  // Create BankAccount table statement.
  private static final String CREATE_BANK_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS "
      + BankDatabaseSchema.BankAccount.TABLE_NAME
      + " ("
      + BankDatabaseSchema.BankAccount._ID
      + " INTEGER PRIMARY KEY,"
      + BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE
      + TEXT_TYPE
      + COMMA
      + BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE
      + REAL_TYPE
      + COMMA
      + BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD + REAL_TYPE + " )";

  // Create Transaction table statement.
  private static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE IF NOT EXISTS "
      + BankDatabaseSchema.TransactionRecord.TABLE_NAME
      + " ("
      + BankDatabaseSchema.TransactionRecord._ID
      + " INTEGER PRIMARY KEY,"
      + BankDatabaseSchema.TransactionRecord.COLUMN_NAME_TYPE
      + TEXT_TYPE
      + COMMA
      + BankDatabaseSchema.TransactionRecord.COLUMN_NAME_AMOUNT
      + REAL_TYPE
      + COMMA
      + BankDatabaseSchema.TransactionRecord.COLUMN_NAME_DATE
      + TEXT_TYPE
      + " )";

  // Create UserBankAccount table statement.
  private static final String CREATE_USER_BANK_ACCOUNT_TABLE = "CREATE TABLE IF NOT EXISTS "
      + BankDatabaseSchema.UserBankAccount.TABLE_NAME
      + " ("
      + BankDatabaseSchema.UserBankAccount._ID
      + " INTEGER PRIMARY KEY,"
      + BankDatabaseSchema.UserBankAccount.COLUMN_NAME_USERID
      + INTEGER_TYPE
      + COMMA
      + BankDatabaseSchema.UserBankAccount.COLUMN_NAME_ACCOUNT_NUMBER
      + INTEGER_TYPE + " )";

  // Create Purchase table statement.
  private static final String CREATE_PURCHASE_TABLE = "CREATE TABLE IF NOT EXISTS "
      + BankDatabaseSchema.Purchase.TABLE_NAME
      + " ("
      + BankDatabaseSchema.Purchase._ID
      + " INTEGER PRIMARY KEY,"
      + BankDatabaseSchema.Purchase.COLUMN_NAME_USERID
      + INTEGER_TYPE
      + COMMA
      + BankDatabaseSchema.Purchase.COLUMN_NAME_ACCOUNT_NUMBER
      + INTEGER_TYPE
      + COMMA
      + BankDatabaseSchema.Purchase.COLUMN_NAME_TRASACTION_ID
      + INTEGER_TYPE + " )";

  /**
   * SQLite Statement: delete table.
   */
  // Delete User table statement.
  private static final String DELETE_USER_TABLE = "DROP TABLE IF EXISTS "
      + BankDatabaseSchema.User.TABLE_NAME;

  // Delete BankAccount table statement.
  private static final String DELETE_BANK_ACCOUNT_TABLE = "DROP TABLE IF EXISTS "
      + BankDatabaseSchema.BankAccount.TABLE_NAME;

  // Delete Transaction table statement.
  private static final String DELETE_TRANSACTION_TABLE = "DROP TABLE IF EXISTS "
      + BankDatabaseSchema.TransactionRecord.TABLE_NAME;

  // Delete UserBankAccount table statement.
  private static final String DELETE_USER_BANK_ACCOUNT_TABLE = "DROP TABLE IF EXISTS "
      + BankDatabaseSchema.UserBankAccount.TABLE_NAME;

  // Delete Purchase table statement.
  private static final String DELETE_PURCHASE_TABLE = "DROP TABLE IF EXISTS "
      + BankDatabaseSchema.Purchase.TABLE_NAME;

  // Declare a SQLite database helper.
  private BankDatabaseHelper bankDatabaseHelper = null;

  // Declare a SQLite database object.
  private SQLiteDatabase bankDatabase = null;

  /**
   * Constructor to initialize/initiate the bank database thru
   * BankDatabaseHelper.
   * 
   * @param myContext
   */
  public BankDatabaseManager(Context myContext) {
    bankDatabaseHelper = new BankDatabaseHelper(myContext);
  }

  /**
   * Open the database in the read and write mode. Throw the exception when the
   * database cannot be opened.
   * 
   * @throws SQLException
   */
  public synchronized void openReadWriteMode() throws SQLException {
    bankDatabase = bankDatabaseHelper.getWritableDatabase();
  }

  /**
   * Open the database in the read mode. Throw the exception when the database
   * cannot be opened. For the read mode, we may not need to do the
   * synchrnoization.
   * 
   * @return SQLiteDatabase object.
   * @throws SQLException
   */
  public SQLiteDatabase openReadMode() throws SQLException {
    bankDatabase = bankDatabaseHelper.getReadableDatabase();

    // Return the reference to the bank database object, so we can query the
    // database from other activities. This is the read-only database
    // object.
    return bankDatabase;
  }

  /**
   * Close any opened database object.
   */
  public void close() {
    bankDatabaseHelper.close();
  }

  /**
   * DANGER: DROP every table in the bank database.
   * 
   * @throws SQLException
   */
  public void dropAllTable() throws SQLException {
    // Just to open the database so we can start to drop tables.
    openReadMode();

    /**
     * The current implementation of upgrade is to drop all tables and re-create
     * everyone of them. Since we do not change the Database schema, so we can
     * just use the current database version. Thus, we can use onUpgrade.
     */
    bankDatabaseHelper.onUpgrade(bankDatabase,
        BankDatabaseHelper.DATABASE_VERSION,
        BankDatabaseHelper.DATABASE_VERSION);

    // Close the database connection.
    close();
  }

  /**
   * SQLite Insert.
   */

  /**
   * The function add a user entries
   * 
   * @param username
   * @param password
   * @param fullName
   * @param phoneNumber
   * @param email
   * @return the new user ID, or -1 if an error occurred.
   */
  public long addUser(String username, String password, String fullName,
      String phoneNumber, String email) {
    // Open the database in the read and write mode.
    openReadWriteMode();

    // Initialize a container storing values, which the content resolver can
    // process.
    ContentValues userData = new ContentValues();

    // If the username is not null and not empty, it's a valid username.
    if (username == null || username == "") {
      throw new IllegalArgumentException(
          "The username is NOT null and NOT empty!");
    } else {
      // Create a map of username and its column names as the key.
      userData.put(BankDatabaseSchema.User.COLUMN_NAME_USERNAME, username);
    }

    // If the new password is not null and not empty, it's a valid password.
    if (password == null || password == "") {
      throw new IllegalArgumentException(
          "The password is NOT null and NOT empty!");
    } else {
      // Create a map of password and its column names as the key.
      userData.put(BankDatabaseSchema.User.COLUMN_NAME_PASSWORD, password);
    }

    // If the full name is not null and not empty, it's a valid full name.
    if (fullName == null || fullName == "") {
      throw new IllegalArgumentException(
          "The full name is NOT null and NOT empty!");
    } else {
      // Create a map of full name and its column names as the key.
      userData.put(BankDatabaseSchema.User.COLUMN_NAME_FULL_NAME, fullName);
    }

    // Create a map of phone number and its column names as the key.
    userData.put(BankDatabaseSchema.User.COLUMN_NAME_PHONE, phoneNumber);

    // Create a map of email and its column names as the key.
    userData.put(BankDatabaseSchema.User.COLUMN_NAME_EMAIL, email);

    // Insert the new user data.
    long newUserid = bankDatabase.insert(BankDatabaseSchema.User.TABLE_NAME,
        null, userData);

    // Close the database connection.
    close();

    // return the new userid.
    return newUserid;
  }

  /**
   * The function add a bank account entry.
   * 
   * @param type
   * @param balance
   * @param threshold
   * @return the new bank account number, or -1 if an error occurred.
   */
  public long addBankAccount(String type, double balance, double threshold) {
    // Open the database in the read and write mode.
    openReadWriteMode();

    // Initialize a container storing values, which the content resolver can
    // process.
    ContentValues bankAccountData = new ContentValues();

    // If the type of bank account is not null and not empty, it's a valid
    // type.
    if (type == null || type == "") {
      throw new IllegalArgumentException(
          "The type of bank account is NOT null and NOT empty!");
    } else {
      // Create a map of username and its column names as the key.
      bankAccountData
          .put(BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE, type);
    }

    // Create a map of balance and its column names as the key. Negative
    // balance means debt.
    bankAccountData.put(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE,
        balance);

    // The minimum threshold is 0.
    if (threshold < 0) {
      threshold = 0;
    }

    // Create a map of threshold and its column names as the key.
    bankAccountData.put(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD,
        threshold);

    // Insert the new bank account data.
    long newBankAccountNumber = bankDatabase.insert(
        BankDatabaseSchema.BankAccount.TABLE_NAME, null, bankAccountData);

    // Close the database connection.
    close();

    // Return the new bank account number.
    return newBankAccountNumber;
  }

  /**
   * The function add a transaction entry.
   * 
   * @param type
   * @param amount
   * @param date
   * @return the new transaction id, or -1 if an error occurred.
   */
  public long addTransactionRecord(String type, double amount, String date) {
    // Open the database in the read and write mode.
    openReadWriteMode();

    // Initialize a container storing values
    ContentValues transactionData = new ContentValues();

    // If the type of transaction is not null and not empty, it's a valid
    // type.
    if (type == null || type == "") {
      throw new IllegalArgumentException(
          "The type of transaction is NOT null and NOT empty!");
    } else {
      // Create a map of type and its column names as the key.
      transactionData.put(
          BankDatabaseSchema.TransactionRecord.COLUMN_NAME_TYPE, type);
    }

    // The amount of transaction is greater than or equal to 0.
    if (amount < 0) {
      throw new IllegalArgumentException(
          "Amount of transaction is non-negative.");
    } else {
      // Create a map of transaction amount and its column names as the
      // key.
      transactionData.put(
          BankDatabaseSchema.TransactionRecord.COLUMN_NAME_AMOUNT, amount);
    }

    // If the type of transaction date is not null and not empty, it's a
    // valid transaction date.
    if (date == null || date == "") {
      throw new IllegalArgumentException(
          "The transaction date is NOT null and NOT empty!");
    } else {
      // Create a map of transaction date and its column names as the key.
      transactionData.put(
          BankDatabaseSchema.TransactionRecord.COLUMN_NAME_DATE, date);
    }

    // Insert the transaction data.
    long newTransactionId = bankDatabase.insert(
        BankDatabaseSchema.TransactionRecord.TABLE_NAME, null, transactionData);

    // Close the database connection.
    close();

    // Return the new transaction id.
    return newTransactionId;
  }

  /**
   * The function add a user-bankAccount entry.
   * 
   * @param userid
   * @param accountNumber
   * @return the new entry id or -1 if an error occurred.
   */
  public long addUserToBankAccount(int userid, int accountNumber) {
    // Open the database in the read and write mode.
    openReadWriteMode();

    // Initialize a container storing values
    ContentValues userBankAccountData = new ContentValues();

    // Create a map of userid with the column name as the key
    userBankAccountData.put(
        BankDatabaseSchema.UserBankAccount.COLUMN_NAME_USERID, userid);

    // Create a map of account number with the column name as the key
    userBankAccountData.put(
        BankDatabaseSchema.UserBankAccount.COLUMN_NAME_ACCOUNT_NUMBER,
        accountNumber);

    // Insert the transaction data.
    long newRowId = bankDatabase.insert(
        BankDatabaseSchema.UserBankAccount.TABLE_NAME, null,
        userBankAccountData);

    // Close the database connection.
    close();

    // Return the new entry ID.
    return newRowId;
  }

  /**
   * The function add a purchase record.
   * 
   * @param userid
   * @param accountNumber
   * @param transactionId
   * @return the purchase id or -1 if an error occurred.
   */
  public long addPurchaseRecord(int userid, int accountNumber, int transactionId) {
    // Open the database in the read and write mode.
    openReadWriteMode();

    // Initialize a container storing values
    ContentValues purchaseData = new ContentValues();

    // Create a map of userid with the column name as the key
    purchaseData.put(BankDatabaseSchema.Purchase.COLUMN_NAME_USERID, userid);

    // Create a map of account number with the column name as the key
    purchaseData.put(BankDatabaseSchema.Purchase.COLUMN_NAME_ACCOUNT_NUMBER,
        accountNumber);

    // Create a map of transaction id with the column name as the key
    purchaseData.put(BankDatabaseSchema.Purchase.COLUMN_NAME_TRASACTION_ID,
        transactionId);

    // Insert the purchase data.
    long newPurchaseId = bankDatabase.insert(
        BankDatabaseSchema.Purchase.TABLE_NAME, null, purchaseData);

    // Close the database connection.
    close();

    return newPurchaseId;
  }

  /**
   * SQLite: Update.
   */

  /**
   * The function update the user information in the User table.
   * 
   * @param userid
   * @param newPassword
   * @param newFullName
   * @param newPhoneNumber
   * @param newEmail
   * @return the number of rows affected by the userid.
   */
  public int updateUser(int userid, String newPassword, String newFullName,
      String newPhoneNumber, String newEmail) {
    // Open the database in the read and write mode.
    openReadWriteMode();

    // Initialize a boolean flag to indicate if all the input arguments are
    // either empty or null.
    boolean hasData = false;

    // Initialize a container storing values.
    ContentValues newUserData = new ContentValues();

    // If the new password is not null and not empty, it's a valid password.
    if (newPassword != null && newPassword != "") {
      // Create a map of new password and its column names as the key.
      newUserData
          .put(BankDatabaseSchema.User.COLUMN_NAME_PASSWORD, newPassword);

      // Set the flag to true.
      hasData = true;
    }

    // If the new full name is not null and not empty, it's a valid full
    // name.
    if (newFullName != null && newFullName != "") {
      // Create a map of new password and its column names as the key.
      newUserData.put(BankDatabaseSchema.User.COLUMN_NAME_FULL_NAME,
          newFullName);

      // Set the flag to true.
      hasData = true;
    }

    // If the new email is not null and not empty, it's a valid email.
    if (newPhoneNumber != null && newPhoneNumber != "") {
      // Create a map of new phone number and its column names as the key.
      newUserData
          .put(BankDatabaseSchema.User.COLUMN_NAME_PHONE, newPhoneNumber);

      // Set the flag to true.
      hasData = true;
    }

    // If the new email is not null and not empty, it's a valid email.
    if (newEmail != null && newEmail != "") {
      // Create a map of new email and its column names as the key.
      newUserData.put(BankDatabaseSchema.User.COLUMN_NAME_EMAIL, newEmail);

      // Set the flag to true.
      hasData = true;
    }

    // Initialize the number of rows associated with the specified userid.
    int nEffectRow = 0;

    if (hasData) {
      // Construct the where-clause to select based on userid.
      String whereClause = BankDatabaseSchema.User._ID + " == ?";

      // Provide the user id corresponding to the '?'.
      String[] whereClauseArgumentArray = { String.valueOf(userid) };

      // Update the user information.
      nEffectRow = bankDatabase.update(BankDatabaseSchema.User.TABLE_NAME,
          newUserData, whereClause, whereClauseArgumentArray);
    }

    // Close the database connection.
    close();

    // Return the number of effected rows associated with the userid.
    return nEffectRow;
  }

  /**
   * The function update the account threshold in the Bank Account table.
   * 
   * @param accountNumber
   * @param newThreshold
   * @return the number of rows affected by the account number.
   */
  public int updateAccountThreshold(int accountNumber, double newThreshold) {
    // Open the database in the read and write mode.
    openReadWriteMode();

    // Initialize a container storing values.
    ContentValues newBankAccountData = new ContentValues();

    // if the new threshold is less than 0, set it as 0.
    if (newThreshold < 0) {
      newThreshold = 0;
    }

    // Create a map of threshold and its column names as the key.
    newBankAccountData.put(
        BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD, newThreshold);

    // Construct the where-clause to select based on account number.
    String whereClause = BankDatabaseSchema.BankAccount._ID + " == ?";

    // Provide the account number corresponding to "?".
    String[] whereClauseArgumentArray = { String.valueOf(accountNumber) };

    // Update the bank account
    int nEffectRow = bankDatabase.update(
        BankDatabaseSchema.BankAccount.TABLE_NAME, newBankAccountData,
        whereClause, whereClauseArgumentArray);

    // Close the database connection.
    close();

    // Return the number of effected rows associated with the account
    // number.
    return nEffectRow;
  }

  /**
   * SQLite: query userid, bank account cursor and transactions cursor.
   */

  /**
   * The function retrieve the userid based on the username and password.
   * 
   * @param username
   * @param password
   * @return the userid or -1 if an error occurs during the retrieval.
   */
  public int getUserid(String username, String password) {
    // Open the database in the read mode.
    openReadMode();

    /**
     * Set up the query
     */
    // SELECT clause.
    String[] columnList = new String[] { BankDatabaseSchema.User._ID };

    // WHERE clause.
    String whereClause = BankDatabaseSchema.User.COLUMN_NAME_USERNAME + " = ? "
        + " AND " + BankDatabaseSchema.User.COLUMN_NAME_PASSWORD + " = ?";

    // WHERE clause arguments: ? in the where clause.
    String[] whereClauseArgumentList = new String[] { username, password };

    // Specify the rest of clause.
    String groupBy = null;
    String having = null;
    String orderBy = null;

    // Initialize the userid.
    int userid = -1;

    /**
     * Query the database.
     */
    // Get the table cursor.
    Cursor useridCursor = bankDatabase.query(
        BankDatabaseSchema.User.TABLE_NAME, columnList, whereClause,
        whereClauseArgumentList, groupBy, having, orderBy);

    // Move the cursor to the first entry.
    if (useridCursor.moveToFirst()) {
      // Get the column index userid.
      int useridIndex = useridCursor
          .getColumnIndexOrThrow(BankDatabaseSchema.User._ID);

      // Get the userid.
      userid = useridCursor.getInt(useridIndex);
    } else {
      Log.e(DEBUG_TAG, "ERROR: username and password do not match.");
    }

    // Close the cursor to release resources.
    useridCursor.close();

    // Close the database.
    close();

    // Retrun the retrieved userid.
    return userid;
  }

  /**
   * The function retrieves the user info based on the userid.
   * 
   * @param userid
   * @return
   */
  public Cursor getUserInfo(int userid) {
    // Open the database in the read mode.
    openReadMode();

    // Define components of SQLite query.
    // http://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
    String[] columnArray = null;
    String whereClause = BankDatabaseSchema.User.TABLE_NAME + "."
        + BankDatabaseSchema.User._ID + " = ?";
    String[] whereClauseArgumentArray = new String[] { Integer.toString(userid) };
    String groupBy = null;
    String having = null;
    String orderBy = null;

    // Retrieve the user info.
    Cursor userInfoCursor = bankDatabase.query(
        BankDatabaseSchema.User.TABLE_NAME, columnArray, whereClause,
        whereClauseArgumentArray, groupBy, having, orderBy);

    // Move the cursor to the first entry.
    if (!userInfoCursor.moveToFirst()) {
      Log.e(DEBUG_TAG,
          "ERROR: user table cursor is failed to move to the first entry.");
    }

    // Close the database connection.
    close();

    // Return the cursor of use info.
    return userInfoCursor;
  }


  /**
   * The function retrieve the cursor of bank account table associated with the
   * userid.
   * 
   * @param userid
   * @return the cursor of bank account table associated with the userid.
   */
  public Cursor getBankAccountTableCursor(int userid) {
    // Open the database in the read mode.
    openReadMode();

    // /**
    // * Set up query: method 1.
    // */
    // String queryString = "SELECT bankAccount.* "
    // +
    // "FROM user INNER JOIN userBankAccount on user._id = userBankAccount.userid "
    // +
    // "INNER JOIN bankAccount on userBankAccount.accountNumber = bankAccount._id "
    // + "WHERE user._id = ?";
    //
    // // Specify the where clause arguments.
    // String[] whereClauseArgument = new String[] {
    // Integer.toString(userid) };
    //
    // /**
    // * Query the database by the method 1.
    // */
    // // Retrieve the records for the method 1.
    // Cursor bankAccountCursor = bankDatabase.rawQuery(queryString,
    // whereClauseArgument);

    /**
     * Set up the query: method 1.
     */
    // SELECT clause.
    String[] columnList = new String[] { BankDatabaseSchema.BankAccount.TABLE_NAME
        + ".*" };

    // FROM clause.
    String table = BankDatabaseSchema.User.TABLE_NAME + " INNER JOIN "
        + BankDatabaseSchema.UserBankAccount.TABLE_NAME + " on "
        + BankDatabaseSchema.User.TABLE_NAME + "."
        + BankDatabaseSchema.User._ID + " = "
        + BankDatabaseSchema.UserBankAccount.TABLE_NAME + "."
        + BankDatabaseSchema.UserBankAccount.COLUMN_NAME_USERID
        + " INNER JOIN " + BankDatabaseSchema.BankAccount.TABLE_NAME + " on "
        + BankDatabaseSchema.UserBankAccount.TABLE_NAME + "."
        + BankDatabaseSchema.UserBankAccount.COLUMN_NAME_ACCOUNT_NUMBER + " = "
        + BankDatabaseSchema.BankAccount.TABLE_NAME + "."
        + BankDatabaseSchema.BankAccount._ID;

    // WHERE cluase
    String whereClause = BankDatabaseSchema.User.TABLE_NAME + "."
        + BankDatabaseSchema.User._ID + " = ?";

    // Specify the arguments (?) in the where clause: username and password.
    String[] whereClauseArgumentList = new String[] { Integer.toString(userid) };

    // Specify the rest of clause.
    String groupBy = null;
    String having = null;
    String orderBy = null;

    /**
     * Query the database by the method 2.
     */
    Cursor bankAccountCursor = bankDatabase.query(table, columnList,
        whereClause, whereClauseArgumentList, groupBy, having, orderBy);

    /**
     * Move the cursor to the first entry.
     */
    if (!bankAccountCursor.moveToFirst()) {
      Log.e(DEBUG_TAG,
          "ERROR: bank account table associated with the userid cannot be retrieved.");
    }

    // Close the database.
    close();

    // Return the cursor of bank account table associated with the userid.
    return bankAccountCursor;
  }

  /**
   * The function retrieves the cursor of transaction table, order by date and
   * associated with the userid.
   * 
   * @param userid
   * @return the cursor of transaction table associated with the userid.
   */
  public Cursor getTransactionTableCursor(int userid) {
    // Open database in the read mode.
    openReadMode();

    // /**
    // * Set up the query: method 1.
    // */
    // String queryString = "SELECT transactionRecord.* "
    // + "FROM user INNER JOIN purchase on user._id = purchase.userid "
    // +
    // "INNER JOIN transactionRecord on purchase.transactionID = transactionRecord._id "
    // + "WHERE user._id = ? " + "ORDER BY transactionRecord.date";
    //
    // // Specify the where clause arguments.
    // String[] whereClauseArgument = new String[] {
    // Integer.toString(userid) };
    //
    // /**
    // * Query the database by the method 1.
    // */
    // // Retrieve the records.
    // Cursor transactionTableCursor = bankDatabase.rawQuery(queryString,
    // whereClauseArgument);

    /**
     * Set up the query: method 2.
     */
    // SELECT: Specify columns of interests.
    String[] columnList = new String[] { BankDatabaseSchema.TransactionRecord.TABLE_NAME
        + ".*"
        + " , "
        + BankDatabaseSchema.BankAccount.TABLE_NAME
        + "."
        + BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE };

    // String[] columnList = new String[] { "transactionRecord.*",
    // "sum(amount)"};

    // FROM: Specify the table.
    String table = BankDatabaseSchema.User.TABLE_NAME + " INNER JOIN "
        + BankDatabaseSchema.Purchase.TABLE_NAME + " on "
        + BankDatabaseSchema.User.TABLE_NAME + "."
        + BankDatabaseSchema.User._ID + " = "
        + BankDatabaseSchema.Purchase.TABLE_NAME + "."
        + BankDatabaseSchema.Purchase.COLUMN_NAME_USERID + " INNER JOIN "
        + BankDatabaseSchema.TransactionRecord.TABLE_NAME + " on "
        + BankDatabaseSchema.Purchase.TABLE_NAME + "."
        + BankDatabaseSchema.Purchase.COLUMN_NAME_TRASACTION_ID + " = "
        + BankDatabaseSchema.TransactionRecord.TABLE_NAME + "."
        + BankDatabaseSchema.TransactionRecord._ID + " INNER JOIN "
        + BankDatabaseSchema.BankAccount.TABLE_NAME + " ON "
        + BankDatabaseSchema.BankAccount.TABLE_NAME + "."
        + BankDatabaseSchema.BankAccount._ID + " = "
        + BankDatabaseSchema.Purchase.TABLE_NAME + "."
        + BankDatabaseSchema.Purchase.COLUMN_NAME_ACCOUNT_NUMBER;

    // WHERE: Specify the where clause.
    String whereClause = BankDatabaseSchema.User.TABLE_NAME + "."
        + BankDatabaseSchema.User._ID + " = ?";

    // Specify the arguments (?) in the where clause: username and
    // password.
    String[] whereClauseArgumentList = new String[] { Integer.toString(userid) };

    // Specify the rest of clause.
    String groupBy = null;
    String having = null;
    String orderBy = BankDatabaseSchema.TransactionRecord.COLUMN_NAME_DATE;

    /**
     * Query the database by the method 2.
     */
    Cursor transactionTableCursor = bankDatabase.query(table, columnList,
        whereClause, whereClauseArgumentList, groupBy, having, orderBy);

    /**
     * Move the cursor to the first entry.
     */
    // Move the cursor to the first entry.
    if (!transactionTableCursor.moveToFirst()) {
      Log.e(DEBUG_TAG,
          "ERROR: transaction table assoicated with the userid cannot be retrieved.");
    }

    // Close the database connection.
    close();

    // Return the cursor of transaction table associated with the userid.
    return transactionTableCursor;
  }

  /**
   * SQLite: query all entries in a table.
   */

  /**
   * The function get all the user information from the User table.
   * 
   * @return the cursor to the user table.
   */
  public Cursor getAllUser() {
    // Open the database in the read mode.
    openReadMode();

    // Define components of SQLite query.
    // http://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
    String[] columnArray = null;
    String whereClause = null;
    String[] whereClauseArgumentArray = null;
    String groupBy = null;
    String having = null;
    String orderBy = null;

    // Retrieve all the enters in the User table.
    Cursor userTableCursor = bankDatabase.query(
        BankDatabaseSchema.User.TABLE_NAME, columnArray, whereClause,
        whereClauseArgumentArray, groupBy, having, orderBy);

    // Move the cursor to the first entry.
    if (!userTableCursor.moveToFirst()) {
      Log.e(DEBUG_TAG,
          "ERROR: user table cursor is failed to move to the first entry.");
    }

    // Close the database connection.
    close();

    // Return the cursor of use table.
    return userTableCursor;
  }

  /**
   * The function retrieves all the bank account information from the
   * BankAccount table.
   * 
   * @return the cursor of bank account table.
   */
  public Cursor getAllBankAccount() {
    // Open the database in the read mode.
    openReadMode();

    // Define components of SQLite query.
    // http://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
    String[] columnArray = null;
    String whereClause = null;
    String[] whereClauseArgumentArray = null;
    String groupBy = null;
    String having = null;
    String orderBy = null;

    // Retrieve all the enters in the User table.
    Cursor bankAccountTableCursor = bankDatabase.query(
        BankDatabaseSchema.BankAccount.TABLE_NAME, columnArray, whereClause,
        whereClauseArgumentArray, groupBy, having, orderBy);

    // Move the cursor to the first entry.
    if (!bankAccountTableCursor.moveToFirst()) {
      Log.e(DEBUG_TAG,
          "ERROR: bank account table cursor is failed to move to the first entry.");
    }

    // Close the database connection.
    close();

    // Return the cursor of bank account table.
    return bankAccountTableCursor;
  }

  /**
   * The function retrieves all the transaction information from the Transaction
   * table.
   * 
   * @return the cursor to the transaction table.
   */
  public Cursor getAllTransactionRecord() {
    // Open the database in the read mode.
    openReadMode();

    // Define components of SQLite query.
    // http://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
    String[] columnArray = null;
    String whereClause = null;
    String[] whereClauseArgumentArray = null;
    String groupBy = null;
    String having = null;
    String orderBy = null;

    // Retrieve all the enters in the User table.
    Cursor transactionTableCursor = bankDatabase.query(
        BankDatabaseSchema.TransactionRecord.TABLE_NAME, columnArray,
        whereClause, whereClauseArgumentArray, groupBy, having, orderBy);

    // Move the cursor to the first entry.
    if (!transactionTableCursor.moveToFirst()) {
      Log.e(DEBUG_TAG,
          "ERROR: transaction table cursor is failed to move to the first entry.");
    }

    // Close the database connection.
    close();

    // Return the cursor of transaction table.
    return transactionTableCursor;
  }

  /**
   * The function get all the user-to-bank-accounts entries from the
   * User-BankAccount table.
   * 
   * @return the cursor of User-BankAccount table.
   */
  public Cursor getAllUserBankAccountRecord() {
    // Open the database in the read mode.
    openReadMode();

    // Define components of SQLite query.
    // http://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
    String[] columnArray = null;
    String whereClause = null;
    String[] whereClauseArgumentArray = null;
    String groupBy = null;
    String having = null;
    String orderBy = null;

    // Retrieve all the enters in the User table.
    Cursor userBankAccountTableCursor = bankDatabase.query(
        BankDatabaseSchema.UserBankAccount.TABLE_NAME, columnArray,
        whereClause, whereClauseArgumentArray, groupBy, having, orderBy);

    // Move the cursor to the first entry.
    if (!userBankAccountTableCursor.moveToFirst()) {
      Log.e(DEBUG_TAG,
          "ERROR: user-bankaccount table cursor is failed to move to the first entry.");
    }

    // Close the database connection.
    close();

    // Return the cursor of user-bankaccount table.
    return userBankAccountTableCursor;
  }

  /**
   * The function get all the purchase records from the Purchase table.
   * 
   * @return the cursor of purchase table.
   */
  public Cursor getAllPurchaseRecord() {
    // Open the database in the read mode.
    openReadMode();

    // Define components of SQLite query.
    // http://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
    String[] columnArray = null;
    String whereClause = null;
    String[] whereClauseArgumentArray = null;
    String groupBy = null;
    String having = null;
    String orderBy = null;

    // Retrieve all the enters in the User table.
    Cursor purchaseTableCursor = bankDatabase.query(
        BankDatabaseSchema.Purchase.TABLE_NAME, columnArray, whereClause,
        whereClauseArgumentArray, groupBy, having, orderBy);

    // Move the cursor to the first entry.
    if (!purchaseTableCursor.moveToFirst()) {
      Log.e(DEBUG_TAG,
          "ERROR: purchase table cursor is failed to move to the first entry.");
    }

    // Close the database connection.
    close();

    // Return the cursor of purchase table.
    return purchaseTableCursor;
  }

  /**
   * SQLiteOpenHelper class helps to manage database creation and version
   * management.
   */
  public class BankDatabaseHelper extends SQLiteOpenHelper {
    // If we change the database schema, we need to update the database
    // version.
    public static final int DATABASE_VERSION = 1;

    // Define the database name
    public static final String DATABASE_NAME = "PlutusBankDatabase";

    /**
     * Default constructor calls SQLiteHelper to initialize the SQLite database
     * with the specified database name and database version.
     */
    public BankDatabaseHelper(Context myContext) {
      super(myContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase myBankDatabase) {
      // While the creation of user table, we can catch the exception by
      // try-catch statements.

      // Create User table.
      myBankDatabase.execSQL(BankDatabaseManager.CREATE_USER_TABLE);

      // Create BankAccount table.
      myBankDatabase.execSQL(BankDatabaseManager.CREATE_BANK_ACCOUNT_TABLE);

      // Create Transaction table.
      myBankDatabase.execSQL(BankDatabaseManager.CREATE_TRANSACTION_TABLE);

      // Create UserBankAccount table.
      myBankDatabase
          .execSQL(BankDatabaseManager.CREATE_USER_BANK_ACCOUNT_TABLE);

      // Create Purchase table.
      myBankDatabase.execSQL(BankDatabaseManager.CREATE_PURCHASE_TABLE);
    }

    /**
     * onUpgrade is called when we need to update the database. The
     * implementation should use onUpgrade to add tables, drop tables and
     * upgrade new schema version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase myBankDatabase, int oldVersion,
        int newVersion) {
      // Delete User table.
      myBankDatabase.execSQL(BankDatabaseManager.DELETE_USER_TABLE);

      // Delete BankAccount table.
      myBankDatabase.execSQL(BankDatabaseManager.DELETE_BANK_ACCOUNT_TABLE);

      // Delete Transaction table.
      myBankDatabase.execSQL(BankDatabaseManager.DELETE_TRANSACTION_TABLE);

      // Delete UserBankAccount table.
      myBankDatabase
          .execSQL(BankDatabaseManager.DELETE_USER_BANK_ACCOUNT_TABLE);

      // Delete Purchase table.
      myBankDatabase.execSQL(BankDatabaseManager.DELETE_PURCHASE_TABLE);

      // create the SQLite database with the newer version.
      onCreate(myBankDatabase);
    }

    /**
     * onDowngrade is called when the database needs to be downgraded. It is
     * called when the current database version is newer than the requested one.
     * It is not necessary to implement this function.
     */
    @Override
    public void onDowngrade(SQLiteDatabase myBankDatabase, int oldVersion,
        int newVersion) {
      // Call onUpgrade to update the database version and tables.
      onUpgrade(myBankDatabase, oldVersion, newVersion);
    }

  } // end of BankDatabaseHelper

} // end of BankDatabaseManager

// // SELECT: Specify columns of interests.
// String[] columnList = new String[] {
// BankDatabaseSchema.TransactionRecord.TABLE_NAME
// + ".*" };
//
// // FROM: Specify the table.
// String table = BankDatabaseSchema.User.TABLE_NAME + " INNER JOIN "
// + BankDatabaseSchema.Purchase.TABLE_NAME + " on "
// + BankDatabaseSchema.User.TABLE_NAME + "."
// + BankDatabaseSchema.User._ID + " = "
// + BankDatabaseSchema.Purchase.TABLE_NAME + "."
// + BankDatabaseSchema.Purchase.COLUMN_NAME_USERID + " INNER JOIN "
// + BankDatabaseSchema.TransactionRecord.TABLE_NAME + " on "
// + BankDatabaseSchema.Purchase.TABLE_NAME + "."
// + BankDatabaseSchema.Purchase.COLUMN_NAME_TRASACTION_ID + " = "
// + BankDatabaseSchema.TransactionRecord.TABLE_NAME + "."
// + BankDatabaseSchema.TransactionRecord._ID;
