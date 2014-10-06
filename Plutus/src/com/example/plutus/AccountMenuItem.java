package com.example.plutus;

public class AccountMenuItem 
{
	private int progress = 0;
	private String acntName = "";
	private String acntTotal = "";
	public AccountMenuItem() 
	{
		progress = 0;
		acntName = "";
		acntTotal = "";
	}
	
	public AccountMenuItem(int p, String an, String at) 
	{
		progress = p;
		acntName = an;
		acntTotal = at;
	}
	
	public int GetProg()
	{
		return progress;
	}
	
	public void SetProg(int p)
	{
		progress = 0;
	}
	
	public String GetAcntName()
	{
		return acntName;
	}
	
	public void SetAcntName(String an)
	{
		acntName = an;
	}
	
	public String GetAcntTotal()
	{
		return acntTotal;
	}
	
	public void setAcntTotal(String t)
	{
		acntTotal = t;
	}
}
