package com.example.plutus;

public class AccountMenuItem 
{
	public int progress = 0;
	public String acntTitle = "";
	public String acntName = "";
	public String acntTotal = "";
	public String threshText = "";
	public String emailTxt = "";
	public String phoneTxt = "";
	public AccountMenuItem() 
	{
		progress = 0;
		acntName = "";
		acntTotal = "";
	}
	
	public AccountMenuItem(int p, String an, String at, String threshT, String title, String emlTxt, String phnTxt) 
	{
		progress = p;
		acntName = an;
		acntTotal = at;
		threshText = threshT;
		acntTitle = title;
		emailTxt = emlTxt;
		phoneTxt = phnTxt;
	}

}
