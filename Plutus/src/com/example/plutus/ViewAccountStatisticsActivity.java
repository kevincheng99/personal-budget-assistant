package com.example.plutus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewAccountStatisticsActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;
  private WebView grphWv = null;
  private TextView acntTv = null;
  private TextView weekTv = null;
  private TextView grphTv = null;
  private RelativeLayout acntRl = null;
  private RelativeLayout weekRl = null;
  private RelativeLayout grphRl = null;
  private String[] acntStrs = {"Savings Account", "Checking Account"};
  private String[] weekStrs = {"Weekly", "Bi-Weekly", "Monthly", "Yearly"};
  private String[] grphStrs = {"Pie", "Line", "Bar"};
  private int curAcnt = 0;
  private int curWeek = 0;
  private int curGrph = 0;
  private boolean oldBuild = true;
  

  // Initialize the bank database with the bank database manager.
  private BankDatabaseManager plutusDbManager = new BankDatabaseManager(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_account_statistics);
    //Find the web view for displaying the graphs
    grphWv = (WebView) findViewById(R.id.webView1);
    //Find the text views for each of the buttons
    acntTv = (TextView) findViewById(R.id.stat_acnt_txt);
    weekTv = (TextView) findViewById(R.id.stat_week_txt);
    grphTv = (TextView) findViewById(R.id.stat_grph_txt);
    //Make the buttons clickable
    acntRl = (RelativeLayout) findViewById(R.id.stat_acnt_btn);
    acntRl.setOnClickListener(new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
			curAcnt = (curAcnt + 1) % acntStrs.length;
			acntTv.setText(acntStrs[curAcnt]);
		    grphWv.loadUrl(GenerateChartUrl(curAcnt, curWeek, curGrph));
		    grphWv.setBackgroundColor(0x00000000);
		}
	});
    weekRl = (RelativeLayout) findViewById(R.id.stat_week_btn);
    weekRl.setOnClickListener(new View.OnClickListener() 
    {	
		@Override
		public void onClick(View v) 
		{
			curWeek = (curWeek + 1) % weekStrs.length;
			weekTv.setText(weekStrs[curWeek]);
		    grphWv.loadUrl(GenerateChartUrl(curAcnt, curWeek, curGrph));
		    grphWv.setBackgroundColor(0x00000000);
		}
	});
    grphRl = (RelativeLayout) findViewById(R.id.stat_grph_btn);
    grphRl.setOnClickListener(new View.OnClickListener() 
    {	
		@Override
		public void onClick(View v) 
		{
			curGrph = (curGrph + 1) % grphStrs.length;
			grphTv.setText(grphStrs[curGrph]);
		    grphWv.loadUrl(GenerateChartUrl(curAcnt, curWeek, curGrph));
		    grphWv.setBackgroundColor(0x00000000);
		}
	});
    acntTv.setText(acntStrs[0]);
    weekTv.setText(weekStrs[0]);
    grphTv.setText(grphStrs[0]);
	if(android.os.Build.VERSION.RELEASE.startsWith("1.") || android.os.Build.VERSION.RELEASE.startsWith("2."))
	{ //Use old google image chart API for android version 1.0 and 2.0
		oldBuild = true;
	}
	else
	{
		oldBuild = false;
		grphWv.getSettings().setJavaScriptEnabled(true);
	}
    //Load the chart
    grphWv.loadUrl(GenerateChartUrl(curAcnt, curWeek, curGrph));
    //Need to set transparency AFTER loading web page (if you set before it doesn't work)
    grphWv.setBackgroundColor(0x00000000);
  }

	private String GenerateChartUrl(int acntNum, int weekNum, int grphNum)
	{
		String url = "";
		String cat = "Food|Gas|Bills|Drugs";
		if(oldBuild)
		{ //Use old google image chart API for android version 1.0 and 2.0
			//Charts for the savings account
			url = "https://chart.googleapis.com/chart?chco=0000FF&cht=";
			if(grphNum == 0)
				url += "p3&";
			else if(grphNum == 1)
				url += "lc&";
			else 
				url += "bhs&";
			url += "chd=s:Uf9a&chs=300x150&chl=" + cat + "&chf=bg,s,65432100";
		}
		else
		{	//Use latest google chart api
			if(grphNum == 0)
				url = "file:///android_asset/piechart.html";
			else if(grphNum == 1)
				url = "file:///android_asset/linechart.html";
			else 
				url = "file:///android_asset/barchart.html";
		}
		return url;
	}
  
  // When resume/running/visible to the user, open the database for the read
  // and write.
  @Override
  protected void onResume() 
  {
    plutusDbManager.openReadMode();
    super.onResume();
  }

  // When pause, close any open database.
  @Override
  protected void onPause() 
  {
    plutusDbManager.close();
    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.view_account_statistics, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // Select the help event, logout event or default event.
    switch (id) {
    case R.id.action_help:
      // Display a help documentation to describe the activity of view account
      // statistics.

      // When successfully handling a menu item, return true.
      return true;
    case R.id.action_logout:
      // Initialize the intent to the login activity.
      Intent loginIntent = new Intent(this, LoginActivity.class);

      // Clear the top activities in the task stack and go back to log in
      // activity.
      loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      // Redirect the user the login activity.
      startActivity(loginIntent);

      // When successfully handling a menu item, return true.
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }
}
