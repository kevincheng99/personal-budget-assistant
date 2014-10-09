package com.example.plutus;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class TransactionHistoryActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;
  private ArrayList<TransMenuItem> listElems = null;
  private ListView lv = null;
  private TransItemAdapter lstAdpt = null;
  private Intent thaIntent = null;
  private int acntType = -1;
  private TextView titleTv = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.transaction_act);
    thaIntent = getIntent();
    //Get the user ID and account type (savings 0 or checking 1)
    userid = thaIntent.getIntExtra("uid", -1);
    acntType = thaIntent.getIntExtra("acntType", -1);
    //Get the text view
    titleTv = (TextView) findViewById(R.id.trns_title_tv);
    //Set the title to savings or checking
    if(acntType == 0)
    	titleTv.setText("Savings:");
    else
    	titleTv.setText("Checking:");
    //TODO get the transaction list for the user
    //Populate transaction list
	listElems = new ArrayList<TransMenuItem>();
	for(int i = 0; i < 20; ++i)
		listElems.add(new TransMenuItem("Transaction " + (i + 1), "10/" + (i + 1) + "/14", String.format("$%,.2f", Math.PI * (i + 5)), "Junk"));
	lv = (ListView) findViewById(R.id.trns_lv);
	lstAdpt = new TransItemAdapter(getApplicationContext(), listElems);
	lv.setAdapter(lstAdpt);


  }
 
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    getMenuInflater().inflate(R.menu.view_account_balance, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // Select the help event, logout event or default event.
    switch (id) {
      case R.id.action_help:
        // Display a help documentation to describe the activity of view account balance.

        // When successfully handling a menu item, return true.
        return true;
      case R.id.action_logout:
        // Initialize the intent to the login activity.
        Intent loginIntent = new Intent(this, LoginActivity.class);

        // Clear the top activities in the task stack and go back to log in activity.
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