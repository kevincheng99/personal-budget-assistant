package com.polarbear.plutus.technical;

import android.provider.BaseColumns;

/**
 * BankDatabaseSchema defines the bank database tables for Plutus.
 * 
 * It is a contract class that defines names of columns in each table. The
 * contract class allows us to use the same constants across all the other
 * classes in the same package. This lets us change a column name in one place
 * and have it propagate throughout our code.
 * 
 * Each inner class is an abstract class defining the table name and column
 * names and implements the interface BaseColumns. The inner class inherits the
 * primary key field, _ID, that some Android classes such as cursor adaptors
 * will expect it to have.
 * 
 * Or we can also use interface class in our case.
 * 
 * @author Kevin Cheng
 * 
 */
public final class BankDatabaseSchema {
  // Define an empty constructor; in case, we accidentally instantiate the
  // contract class.
  public BankDatabaseSchema() {
  }

  /**
   * User table = (userid or _ID, username, passowrd, full name, phone number,
   * email)
   */
  public static abstract class User implements BaseColumns {
    public static final String TABLE_NAME = "user";
    // public static final String COLUMN_NAME_USERID = "userid"; // use _ID
    public static final String COLUMN_NAME_USERNAME = "username";
    public static final String COLUMN_NAME_PASSWORD = "password";
    public static final String COLUMN_NAME_FULL_NAME = "fullName";
    public static final String COLUMN_NAME_PHONE = "phone";
    public static final String COLUMN_NAME_EMAIL = "email";
  }

  /**
   * BankAccount table = (accountNumber of _ID, type, balance, threshold)
   */
  public static abstract class BankAccount implements BaseColumns {
    public static final String TABLE_NAME = "bankAccount";
    // public static final String COLUMN_NAME_ACCOUNT_NUMBER =
    // "accountNumber"; // use _ID
    public static final String COLUMN_NAME_TYPE = "bankAccountType";
    public static final String COLUMN_NAME_BALANCE = "balance";
    public static final String COLUMN_NAME_THRESHOLD = "threshold";
  }

  /**
   * Transaction table = (transactionID or _ID, type, amount, date)
   */
  public static abstract class TransactionRecord implements BaseColumns {
    public static final String TABLE_NAME = "transactionRecord";
    // public static final String COLUMN_NAME_TRASACTION_ID =
    // "transactionID"; // user _ID
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_AMOUNT = "amount";
    public static final String COLUMN_NAME_DATE = "date";
  }

  /**
   * User-BankAccount table = (entry id or _ID, userid, accountNumber)
   */
  public static abstract class UserBankAccount implements BaseColumns {
    public static final String TABLE_NAME = "userBankAccount";
    // public static final String COLUMN_NAME_ENTRY_ID = "entryID"; // use
    // _ID
    public static final String COLUMN_NAME_USERID = "userid";
    public static final String COLUMN_NAME_ACCOUNT_NUMBER = "accountNumber";
  }

  /**
   * Purchase table = (purchase id or _ID, userid, accountNumber, transactionID)
   */
  public static abstract class Purchase implements BaseColumns {
    public static final String TABLE_NAME = "purchase";
    // public static final String COLUMN_NAME_PURCHASE_ID = "purchaseID"; //
    // use _ID
    public static final String COLUMN_NAME_USERID = "userid";
    public static final String COLUMN_NAME_ACCOUNT_NUMBER = "accountNumber";
    public static final String COLUMN_NAME_TRASACTION_ID = "transactionID";
  }

  // Maybe put more table definitions below.

} // end of BankDatabaseSchema
