package com.example.plutus;

public class Transaction 
{
	public String trnsTitle = "";
	public String trnsDate = "";
	public double trnsTotal = 0.0;
	public String trnsType = "";

	public Transaction()
	{
		
	}
	
	public Transaction(String title, String date, double total, String type) 
	{
		trnsTitle = title;
		trnsDate = date;
		trnsTotal = total;
		trnsType = type;
	}

}
