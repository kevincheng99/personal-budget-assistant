package com.example.plutus;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserMainActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;
  private TextToSpeech tts;
  private Intent umaIntent = null;
  private Bank bank = new Bank();
  private String txtToSpeek = "";
  // Arrayadapter list for the naviagtion options
  private ArrayAdapter<String> arrAdpt = null;
  private ArrayList<String> listElems = null;
  // Text view for the account summary information
  private ListView optLv = null;
  private TextView totFunds = null;
  private TextView totSpend = null;
  private TextView alertTv = null;
  private RelativeLayout acntSumRl = null;

  // Initialize the bank database with the bank database manager.
  private BankDatabaseManager plutusDbManager = new BankDatabaseManager(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_main);

    // Find all the views
    alertTv = (TextView) findViewById(R.id.menu_alert_tv);
    totFunds = (TextView) findViewById(R.id.menu_amnt_tv);
    totSpend = (TextView) findViewById(R.id.menu_expns_tv);
    optLv = (ListView) findViewById(R.id.um_lv);
    acntSumRl = (RelativeLayout) findViewById(R.id.acnt_sum_rl);
    // Make the Account Summary clickable
    acntSumRl.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Initialize the intent to view user account balance activity.
        Intent userViewAccountBalanceIntent = new Intent(UserMainActivity.this,
            ViewAccountBalanceActivity.class);
        // Build the intent with the user id.
        // Start the view-account-balance activity
        startActivity(userViewAccountBalanceIntent);
      }
    });
    // Populate the list view for the menu options
    listElems = new ArrayList<String>();
    String[] values = new String[] { "\tAccount Statistics", "\tUpdate Account" };
    for (int i = 0; i < values.length; ++i)
      listElems.add(values[i]);
    arrAdpt = new ArrayAdapter<String>(getApplicationContext(),
        R.layout.menu_opt_li, R.id.menu_item_tv, listElems);
    optLv.setAdapter(arrAdpt);
    // Make the items clickable
    optLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, final View view,
          int position, long id) {
        switch (position) {
        case 0:
          // Initialize the intent to view user account balance activity.
          Intent userViewAccountStatisticsIntent = new Intent(
              UserMainActivity.this, ViewAccountStatisticsActivity.class);
          // Build the intent with the user id.
          // Start the view-account-statistics activity.
          startActivity(userViewAccountStatisticsIntent);
          break;
        case 1:
          // Initialize the intent to view user account balance activity.
          Intent userUpdateAccountStatisticsIntent = new Intent(
              UserMainActivity.this, UpdateAccountSettingActivity.class);
          // Build the intent with the user id.
          // Start the update-account-settings activity.
          startActivity(userUpdateAccountStatisticsIntent);
          break;
        default:

          break;
        }
      }
    });
    // Receive an intent from the login Activity.
    umaIntent = getIntent();
    // Extract the user id.
    userid = umaIntent.getIntExtra("uid", -1);
    if (-1 == userid) {
      // TODO: Handle -1
    }
    // Retreive the user's total account balance (savings + checking)
    double acntBalance = bank.GetUserBalance(userid);
    double acntSpent = bank.GetUserSpending(userid);
    int numAlert = bank.NumAccountBelowThresh(userid);
    String userName = bank.GetUserName(userid);
    // If the user has an alert set the alert text to red, otherwise it is green
    if (numAlert == 0)
      alertTv.setTextColor(getResources().getColor(R.color.grn_txt));
    else
      alertTv.setTextColor(getResources().getColor(R.color.red_txt));
    // Set the fragment's text to display the user's balance
    totFunds.setText(String.format("$%,.2f", acntBalance));
    totSpend.setText(String.format("$%,.2f", acntSpent));
    alertTv.setText(numAlert + " Alerts");
    /**
     * Check if any account is over-budget and send an alert notification in the
     * form of text, email or pop-up messages.
     */
     
    //If savedInstanceState == null blah blah blah?? 
    
    // Text to speech to welcome the user
    txtToSpeek = "Welcome " + userName + ". You have " + numAlert + " alerts.";
    tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
      @Override
      public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
          tts.setLanguage(Locale.US);
          // TODO: Put user name here
          Toast.makeText(getApplicationContext(), txtToSpeek,
              Toast.LENGTH_SHORT).show();
          tts.speak(txtToSpeek, TextToSpeech.QUEUE_FLUSH, null);
        }
      }
    });
  }

  @Override
  public void onDestroy() { // Perform necessary cleanup for the text to speech
    tts.shutdown();
    super.onDestroy();
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
    getMenuInflater().inflate(R.menu.user_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.

    // Get the item id from the option menu.
    int id = item.getItemId();

    // Select the help event, logout event or default event.
    switch (id) {
    case R.id.action_help:
      // Display a help documentation to describe each main activity.

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
