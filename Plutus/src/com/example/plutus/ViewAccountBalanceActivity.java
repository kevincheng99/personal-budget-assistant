package com.example.plutus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewAccountBalanceActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;
  // Amount, spending, and progress bar for savings
  private TextView acntSpendTv = null;
  private TextView acntAmntTv = null;
  private ProgressBar acntPb = null;
  // Amount, spending, and progress bar for checking
  private TextView acntSpendTv2 = null;
  private TextView acntAmntTv2 = null;
  private ProgressBar acntPb2 = null;
  // Bank object for accessing bank's database
  private Bank bank = new Bank();
  private Intent vabIntent = null;
  private double userSavBal = 0.0;
  private double userSavThresh = 0.0;
  private double userSavSpend = 0.0;
  private double userCheckBal = 0.0;
  private double userCheckThresh = 0.0;
  private double userCheckSpend = 0.0;

  // Initialize the bank database with the bank database manager.
  private BankDatabaseManager plutusDbManager = new BankDatabaseManager(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_account_balance);
    // Get the intent that started the activity
    vabIntent = getIntent();
    // Get the user's id
    userid = vabIntent.getIntExtra("uid", -1);
    if (userid == -1) {
      // TODO handle -1
    }
    // Get the user's account info
    // TODO replace these stub functions with the actually account values
    userSavBal = bank.GetUserBalance(userid);
    userSavThresh = bank.GetUserThreshold(userid);
    userSavSpend = bank.GetUserSpending(userid);
    userCheckBal = bank.GetUserBalance(userid) * 2;
    userCheckThresh = bank.GetUserThreshold(userid) * 2;
    userCheckSpend = bank.GetUserSpending(userid) * 2;
    // Find views for savings account
    acntSpendTv = (TextView) findViewById(R.id.acnt_expns_tv);
    acntAmntTv = (TextView) findViewById(R.id.acnt_amnt_tv);
    acntPb = (ProgressBar) findViewById(R.id.acnt_thresh_pb);
    // Find views for checking
    acntSpendTv2 = (TextView) findViewById(R.id.acnt_expns_tv2);
    acntAmntTv2 = (TextView) findViewById(R.id.acnt_amnt_tv2);
    acntPb2 = (ProgressBar) findViewById(R.id.acnt_thresh_pb2);
    // Set the text views and progress bars appropriately
    acntSpendTv.setText(String.format("$%,.2f", userSavSpend));
    acntAmntTv.setText(String.format("$%,.2f", userSavBal));
    // TODO need formula for progress bar
    acntPb.setProgress(Math.min(100, (int) (userSavBal - userSavThresh)));
    acntSpendTv2.setText(String.format("$%,.2f", userCheckSpend));
    acntAmntTv2.setText(String.format("$%,.2f", userCheckBal));
    acntPb2.setProgress(Math.min(100, (int) (userCheckBal - userCheckThresh)));

  }

  // When resume/running/visible to the user, open the database for the read
  // and write.
  @Override
  protected void onResume() {
    plutusDbManager.openReadMode();
    super.onResume();
  }

  // When pause, close any open database.
  @Override
  protected void onPause() {
    plutusDbManager.close();
    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
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
      // Display a help documentation to describe the activity of view account
      // balance.

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

    // Here are the original Android codes.
    // if (id == R.id.action_settings) {
    // return true;
    // }
    // return super.onOptionsItemSelected(item);

  }

  public void loadSavingTransHistory(View view) {
    // Initialize the intent to user's main activity
    Intent userTrnsIntent = new Intent(this, TransactionHistoryActivity.class);
    userTrnsIntent.putExtra("uid", userid);
    userTrnsIntent.putExtra("acntType", 0);
    startActivity(userTrnsIntent);
  }

  public void loadCheckTransHistory(View view) {
    // Initialize the intent to user's main activity
    Intent userTrnsIntent = new Intent(this, TransactionHistoryActivity.class);
    userTrnsIntent.putExtra("uid", userid);
    userTrnsIntent.putExtra("acntType", 1);
    startActivity(userTrnsIntent);
  }
}
