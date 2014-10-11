package com.example.plutus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class ViewAccountStatisticsActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;
  private WebView wv1 = null;
  private WebView wv2 = null;

  // Initialize the bank database with the bank database manager.
  private BankDatabaseManager plutusDbManager = new BankDatabaseManager(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_account_statistics);
    wv1 = (WebView) findViewById(R.id.webView1);
    wv2 = (WebView) findViewById(R.id.webView2);
    wv1.getSettings().setJavaScriptEnabled(true);
    wv2.getSettings().setJavaScriptEnabled(true);
    
    wv1.loadUrl("file:///android_asset/piechart.html");
    wv2.loadUrl("file:///android_asset/piechart2.html");
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
