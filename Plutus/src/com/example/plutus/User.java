package com.example.plutus;

import java.util.ArrayList;

public class User 
{
	private String userName;
	private double savAcntBal = 0.0;
	private double savAcntThresh = 0.0;
	private double chkAcntBal = 0.0;
	private double chkAcntThresh = 0.0;
	private ArrayList<Transaction> savAcntTrans = null;
	private ArrayList<Transaction> chkAcntTrans = null;
	private int uid = 0;
	
	public User()
	{
		userName = "Foo Bar";
		savAcntBal = savAcntThresh = chkAcntBal = chkAcntThresh = 0.0;
		savAcntTrans = new ArrayList<Transaction>();
		chkAcntTrans = new ArrayList<Transaction>();
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
		uid = 0;
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
		return savAcntBal;
	}
	
	public double GetSavingSpend()
	{
		//TODO compute how much the user has spent
		return 123.45;
		
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
		return 321.98;
	}
	
	//TODO Link user to the data base

	public static boolean UserExists(String un, String pwd)
	{	
		//TODO query if a user exists
		return true;
	}
    // Example of how to retrieve the userid from the Bank database.
    // userId = plutusDbManager.getUserid(uname, pwd);

	
	public static User GetUser(String un, String pwd)
	{
		//TODO Query the database and try to get a user object else return null
		
		return new User("Nicholas", 1234.56, 5321.12);
	}
	
	//TODO query the database and fill the list of transaction objects
	
}
