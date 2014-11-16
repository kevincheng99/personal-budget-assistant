package com.polarbear.plutus.technical;


import android.os.AsyncTask;


public class AlertSystem 
{
	public AlertSystem()
	{
		
	}
	
	public void SendEmailAlert(String name, String email, String account) throws Exception
	{
		final String un = name;
		final String eml = email;
		final String acnt = account;
        new AsyncTask<Void, Void, Void>() 
        {
        	GMailSender sender = null;
            @Override public Void doInBackground(Void... arg) 
            {
            	sender = new GMailSender("plutus.polar.bear@gmail.com", "polarbearwrestler4lyfe");
                try 
                {   
                	sender.sendMail("Plutus Alert", "Hi " + un + ",\n\nYour " + acnt + " account is below threshold!\n\nPlutus", "plutus.polar.bear@gmail.com", eml);   
                } 
                catch (Exception e) 
                {   
                	e.printStackTrace();    
                }
				return null; 
            }
        }.execute();
	}
 
}