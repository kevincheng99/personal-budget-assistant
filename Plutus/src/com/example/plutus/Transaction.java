package com.example.plutus;

public class Transaction 
{
	public String trnsTitle = "";
	public String trnsDate = "";
	public String trnsTotal = "";
	public String trnsType = "";

	public Transaction(String title, String date, String total, String type) 
	{
		trnsTitle = title;
		trnsDate = date;
		trnsTotal = total;
		trnsType = type;
	}

}
