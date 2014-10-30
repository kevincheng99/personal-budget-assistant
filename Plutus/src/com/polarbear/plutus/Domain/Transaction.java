package com.polarbear.plutus.Domain;

public class Transaction 
{
	public int trnsId = 0;
	public String trnsDate = "";
	public double trnsTotal = 0.0;
	public String trnsType = "";

	public Transaction()
	{
		
	}
	
	public Transaction(int id, String date, double total, String type) 
	{
		trnsId = id;
		trnsDate = date;
		trnsTotal = total;
		trnsType = type;
	}
	
	public String toString()
	{
		return "[" + Integer.toString(trnsId) + ", " + trnsDate + ", " + Double.toString(trnsTotal) + ", " + trnsType +  "]";
	}

}
