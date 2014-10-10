package com.example.plutus;

import java.util.ArrayList;

public class Bank 
{
	public Bank() {}
	
	public int GetUserIndex(String un, String pwd) { return 1; }
	
	public double GetUserBalance(int uid) { return 1234.56; }
	
	public double GetUserThreshold(int uid) { return 500.00; }
	
	public double GetUserSpending(int uid) { return 5432.21; }
	
	public int NumAccountBelowThresh(int uid) { return 0; }
	
	public String GetUserName(int uid) { return "Nicholas"; }
	
	public void WriteChanges(String email, String phone, String pwd, String threshold) { }
	
	public ArrayList<Transaction> GetTransactions(int uid) 
	{
		ArrayList<Transaction> transLst = new ArrayList<Transaction>();
		for(int i = 0; i < 20; ++i)
			transLst.add(new Transaction("Transaction " + (i + 1), "10/" + (i + 1) + "/14", String.format("$%,.2f", Math.PI * (i + 5)), "Junk"));
		return transLst;
	}
	

}
