package com.polarbear.plutus.domain;

import java.util.ArrayList;

import com.polarbear.plutus.technical.BankDatabaseManager;
import com.polarbear.plutus.technical.BankDatabaseSchema;

import android.content.Context;
import android.database.Cursor;

public class User 
{
	private String userName;
	private String email;
	private String phone;
	private String passwd;
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
	private String[] tempUserInfo = null;
	
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
			temp.trnsId = tableCrs.getInt(columnIndex);
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
		passwd = pwd;
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
		uid = bdm.getUserid(un, pwd);
		bdm.close();
	}
	
	public void Resume()
	{
		bdm.openReadMode();
	}
	
	public void Pause()
	{
		bdm.close();
	}
	
	public boolean AccountInfoChanged()
	{
		if(tempUserInfo == null)
			return false;
		double sThrsh = 0.0;
		double cThrsh = 0.0;
		try
		{	//Need to skip the dollar sign (assume it's first symbol)
			sThrsh = Double.parseDouble(tempUserInfo[3].substring(1));
			cThrsh = Double.parseDouble(tempUserInfo[4].substring(1));
		}
		catch(NumberFormatException e)
		{
			//User input a bad double
		}
		return (tempUserInfo[0].compareTo(email) != 0 || tempUserInfo[1].compareTo(phone) != 0 || tempUserInfo[2].compareTo(passwd) != 0 || Math.abs(sThrsh - savAcntThresh) >= 0.01 || Math.abs(cThrsh - chkAcntThresh) >= 0.01); 
	}
	
	public void UpdateAccountInfo(String[] uInf)
	{
		tempUserInfo = uInf;
	}
	
	public String GetUsername()
	{
		return userName;
	}
	
	public void SetPassword(String pwd)
	{
		passwd = pwd;
	}
	
	public String GetPassword()
	{
		return passwd;
	}
	
	public void SetEmail(String eml)
	{
		email = eml;
	}
	
	
	public String GetEmail()
	{
		return email;
	}
	
	public void SetPhone(String phn)
	{
		phone = phn;
	}
	
	public String GetPhone()
	{
		return phone;
	}
	
	public double GetSavingBal()
	{
		return savAcntBal;
	}
	
	public void SetSavingThresh(double thrsh)
	{
		savAcntThresh = thrsh;
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
	
	public void SetCheckThresh(double thrsh)
	{
		chkAcntThresh = thrsh;
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
	
	public boolean SavingAlert()
	{
		return ((savAcntBal - savAcntThresh) < 0);
	}
	
	public boolean CheckingAlert()
	{
		return ((chkAcntBal - chkAcntThresh) < 0);
	}
	
	public void WriteChanges()
	{
		if(!AccountInfoChanged())
			return;
		email = tempUserInfo[0];
		phone = tempUserInfo[1];
		passwd = tempUserInfo[2];
		bdm.updateUser(uid, passwd, userName, phone, email);
		try
		{	//Assume $ is the first character
			savAcntThresh = Double.parseDouble(tempUserInfo[3].substring(1));
			chkAcntThresh = Double.parseDouble(tempUserInfo[4].substring(1));
		}
		catch(NumberFormatException e)
		{
			//User input a bad double
		}
		//Get the user's balance information
		Cursor tableCrs = bdm.getBankAccountTableCursor(uid);
		int savAcntNum = 0;
		int chkAcntNum = 0;
		int columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE);
		String acntType = tableCrs.getString(columnIndex);
		columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount._ID);
		//Find out which account this row is for
		if(acntType.compareTo("saving") == 0)
			savAcntNum = tableCrs.getInt(columnIndex);
		else
			chkAcntNum = tableCrs.getInt(columnIndex);
		tableCrs.moveToNext();
		//Move to the next and do the same
		columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE);
		acntType = tableCrs.getString(columnIndex);
		columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount._ID);
		if(acntType.compareTo("saving") == 0)
			savAcntNum = tableCrs.getInt(columnIndex);
		else
			chkAcntNum = tableCrs.getInt(columnIndex);
		bdm.updateAccountThreshold(savAcntNum, savAcntThresh);
		bdm.updateAccountThreshold(chkAcntNum, chkAcntThresh);

	}
	
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
