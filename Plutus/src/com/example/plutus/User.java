package com.example.plutus;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public class User 
{
	private String userName;
	private String email;
	private String phone;
	private double savAcntBal = 0.0;
	private double savAcntThresh = 0.0;
	private double savAcntSpend = 0.0;
	private double chkAcntBal = 0.0;
	private double chkAcntThresh = 0.0;
	private double chkAcntSpend = 0.0;
	private ArrayList<Transaction> savAcntTrans = null;
	private ArrayList<Transaction> chkAcntTrans = null;
	private int uid = 0;
	private BankDatabaseManager bdm = null;
	
	public User(String un, String pwd, Context context)
	{
		bdm = new BankDatabaseManager(context);
		uid = bdm.getUserid(un, pwd);
		if(uid == -1)
			return;
		
		//Get the user's transaction history
		Cursor tableCrs = bdm.getTransactionTableCursor(uid);
		Transaction temp = null;
		int columnIndex = 0;
		savAcntTrans = new ArrayList<Transaction>();
		chkAcntTrans = new ArrayList<Transaction>();
		while(!tableCrs.isAfterLast())
		{
			temp = new Transaction();
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord._ID);
			temp.trnsTitle = "Transaction " + Integer.toString(tableCrs.getInt(columnIndex));
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord.COLUMN_NAME_TYPE);
			temp.trnsType = tableCrs.getString(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord.COLUMN_NAME_AMOUNT);
			temp.trnsTotal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord.COLUMN_NAME_DATE);
			temp.trnsDate = tableCrs.getString(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE);
			String bankAccountType = tableCrs.getString(columnIndex);
			tableCrs.moveToNext();
			
			//TODO decide if transaction is from checking or savings
			
			/**
			 * Below is an example of fetching the type of bank account from the transaction table for a particular user.
			 * The type of bank account is either checking or saving.
			 *  
			 * columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE);
			 * String bankAccountType = tableCrs.getString(columnIndex);
			 * 
			 * Added by Kevin
			 */
			 			 
			if(bankAccountType.compareTo("checking") == 0) //If true add to savings
				chkAcntTrans.add(temp);
			else
				savAcntTrans.add(temp);
			
		}
		tableCrs.close();
		for(int i = 0; i < savAcntTrans.size(); ++i)
			savAcntSpend += savAcntTrans.get(i).trnsTotal;
		for(int i = 0; i < chkAcntTrans.size(); ++i)
			chkAcntSpend += chkAcntTrans.get(i).trnsTotal;
		
		//Get the user's balance information
		tableCrs = bdm.getBankAccountTableCursor(uid);
		//TODO decide if balance is for checking or savings
		columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE);
		String acntType = tableCrs.getString(columnIndex);
		if(acntType.compareTo("saving") == 0)
		{
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE);
			savAcntBal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD);
			savAcntThresh = tableCrs.getDouble(columnIndex);
			tableCrs.moveToNext();
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE);
			chkAcntBal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD);
			chkAcntThresh = tableCrs.getDouble(columnIndex);
		}
		else
		{
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE);
			chkAcntBal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD);
			chkAcntThresh = tableCrs.getDouble(columnIndex);
			tableCrs.moveToNext();
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE);
			savAcntBal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD);
			savAcntThresh = tableCrs.getDouble(columnIndex);
		}
		tableCrs.close();
		//Get the user's name, email, and phone
		tableCrs = bdm.getUserInfo(uid);
		columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.User.COLUMN_NAME_FULL_NAME);
		userName = tableCrs.getString(columnIndex);
		columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.User.COLUMN_NAME_PHONE);
		phone = tableCrs.getString(columnIndex);
		columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.User.COLUMN_NAME_EMAIL);
		email = tableCrs.getString(columnIndex);
		tableCrs.close();
		
	}
	
	//Used for just querying if a user exists
	private User(String un, String pwd, Context context, int foo)
	{
		bdm = new BankDatabaseManager(context);
		BankDatabaseSimulation bds = new BankDatabaseSimulation(bdm);
		bds.simulate();
		uid = bdm.getUserid(un, pwd);		
	}
	
	public String GetUsername()
	{
		return userName;
	}
	
	public String GetEmail()
	{
		return email;
	}
	
	public String GetPhone()
	{
		return phone;
	}
	
	public double GetSavingBal()
	{
		return savAcntBal;
	}
	
	public double GetSavingThresh()
	{
		return savAcntThresh;
	}
	
	public double GetSavingSpend()
	{
		//TODO compute how much the user has spent
		return savAcntSpend;
	}
	
	public double GetCheckBal()
	{
		return chkAcntBal;
	}
	
	public double GetCheckThresh()
	{
		return chkAcntThresh;
	}
	
	public double GetCheckSpend()
	{
		//TODO compute how much the user has spent
		return chkAcntSpend;
	}
	
	public ArrayList<Transaction> GetSavingTrans()
	{
		return savAcntTrans;
	}
	
	public ArrayList<Transaction> GetCheckTrans()
	{
		return chkAcntTrans;
	}
	
	public int GetNumAlerts()
	{
		int numAlerts = 0;
		if((savAcntBal - savAcntThresh) < 0)
			numAlerts++;
		if((chkAcntBal - chkAcntThresh) < 0)
			numAlerts++;
		return numAlerts;
	}
	
	//TODO Link user to the data base

	public static boolean UserExists(String un, String pwd, Context context)
	{	
		User temp = new User(un, pwd, context, 0);
		if(temp.uid == -1)
			return false;
		return true;
	}
    // Example of how to retrieve the userid from the Bank database.
    // userId = plutusDbManager.getUserid(uname, pwd);

	
	public static User GetUser(String un, String pwd, Context context)
	{
		User temp = new User(un, pwd, context);
		if(temp.uid == -1)
			return null;
		return temp;
	}
	
}
