package com.example.plutus;

import java.util.ArrayList;

public class User 
{
	private String userName;
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
	
	public User()
	{
		userName = "Foo Bar";
		savAcntBal = savAcntThresh = chkAcntBal = chkAcntThresh = 0.0;
		savAcntTrans = new ArrayList<Transaction>();
		chkAcntTrans = new ArrayList<Transaction>();
		String[] cats = {"Gas", "Food", "Drugs", "Bills"};
		for(int i = 0; i < 20; ++i)
		{
			savAcntTrans.add(new Transaction("Transaction " + (i + 1), "10/" + (i + 1) + "/14", String.format("%,.2f", Math.PI * (i + 5)), cats[i % cats.length]));
			chkAcntTrans.add(new Transaction("Transaction " + (i + 1), "10/" + (i + 1) + "/14", String.format("%,.2f", Math.PI * (i + 5)), cats[i % cats.length]));
		}
		uid = 0;
	}
	
	public User(String un, double savB, double chkB)
	{
		userName = un;
		savAcntBal = savB;
		chkAcntBal = chkB;
		chkAcntThresh = savAcntThresh = 0.0;
		savAcntTrans = new ArrayList<Transaction>();
		chkAcntTrans = new ArrayList<Transaction>();
		//Fill the transactions with junk data
		//TODO actually get the users transaction info
		String[] cats = {"Gas", "Food", "Drugs", "Bills"};
		for(int i = 0; i < 20; ++i)
		{
			savAcntTrans.add(new Transaction("Transaction " + (i + 1), "10/" + (i + 1) + "/14", String.format("%,.2f", Math.PI * (i + 5)), cats[i % cats.length]));
			chkAcntTrans.add(new Transaction("Transaction " + (i + 1), "10/" + (i + 1) + "/14", String.format("%,.2f", Math.PI * (i + 5)), cats[i % cats.length]));
		}
		uid = 0;
	}
	
	public User(String un, String pwd, Context context)
	{
		bdm = new BankDatabaseManager(context);
		uid = bdm.getUserId(un, pwd);
		Cursor tableCrs = //TODO
		Transaction temp = new Transaction();
		int columnIndex = 0;
		while(!tableCrs.isAfterLast())
		{
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord._ID);
			temp.trnsTitle = "Transaction " + Integer.toString(tableCrs.getInt(columnIndex));
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord.COLUMN_NAME_TYPE);
			temp.trnsType = tableCrs.getString(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord.COLUMN_NAME_AMOUNT);
			temp.trnsTotal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.TransactionRecord.COLUMN_NAME_DATE);
			temp.trnsDate = tableCrs.getDouble(columnIndex);
			tableCrs.moveToNext();
			//TODO decide if transaction is from checking or savings
			if( ) //If true add to savings
				savAcntTrans.add(temp);
			else
				chkAcntTrans.add(temp);
			
		}
		tableCrs.close();
		for(int i = 0; i < savAcntTrans.size(); ++i)
		{
			savAcntSpend += savAcntTrans.get(i).trnsTotal;
			chkAcntSpend += chkAcntTrans.get(i).trnsTotal;
		}
		
		tableCrs = getBankAccountTableCursor(uid);
		//TODO decide if balance is for checking or savings
		columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_TYPE);
		String acntType = tableCrs.getString(columnIndex);
		if(acntType.compareTo("saving"))
		{
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE);
			savAcntBal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrown(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD);
			savAcntBal = tableCrs.getDouble(columnIndex);
			tableCrs.moveToNext();
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE);
			chkAcntBal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrown(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD);
			chkAcntBal = tableCrs.getDouble(columnIndex);
		}
		else
		{
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE);
			chkAcntBal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrown(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD);
			chkAcntBal = tableCrs.getDouble(columnIndex);
			tableCrs.moveToNext();
			columnIndex = tableCrs.getColumnIndexOrThrow(BankDatabaseSchema.BankAccount.COLUMN_NAME_BALANCE);
			savAcntBal = tableCrs.getDouble(columnIndex);
			columnIndex = tableCrs.getColumnIndexOrThrown(BankDatabaseSchema.BankAccount.COLUMN_NAME_THRESHOLD);
			savAcntBal = tableCrs.getDouble(columnIndex);
		}
		
		tableCrs.close();
		
	}
	
	public String GetUsername()
	{
		return userName;
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
	
	//TODO Link user to the data base

	public static boolean UserExists(String un, String pwd)
	{	
		//TODO query if a user exists
		return true;
	}
    // Example of how to retrieve the userid from the Bank database.
    // userId = plutusDbManager.getUserid(uname, pwd);

	
	public static User GetUser(String un, String pwd, Context context)
	{
		//TODO Query the database and try to get a user object else return null
		User temp = new User(un, pwd, context);
		if(temp.uid == -1)
			return null;
		else
			return temp;
	}
	
	//TODO query the database and fill the list of transaction objects
	
}
