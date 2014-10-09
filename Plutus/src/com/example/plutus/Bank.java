package com.example.plutus;

public class Bank 
{
	public Bank() {}
	
	public int GetUserIndex(String un, String pwd) { return 1; }
	
	public double GetUserBalance(int uid) { return 1234.56; }
	
	public double GetUserThreshold(int uid) { return 500.00; }
	
	public double GetUserSpending(int uid) { return 5432.21; }
	
	public int NumAccountBelowThresh(int uid) { return 0; }
	
	public String GetUserName(int uid) { return "Nicholas"; }
	

}
