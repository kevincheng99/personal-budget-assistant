package com.example.plutus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class UpdateAccountSettingActivity extends ActionBarActivity 
{
  // Initialize the user id.
  private int userid = -1;
  private EditText et1 = null;
  private EditText et2 = null;
  private EditText et3 = null;
  private EditText et4 = null;
  private Bank bank = new Bank();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_account_setting);
    //Find the views
    et1 = (EditText) findViewById(R.id.ua_email_tv);
    et2 = (EditText) findViewById(R.id.ua_phone_tv);
    et3 = (EditText) findViewById(R.id.ua_pwd_tv);
    et4 = (EditText) findViewById(R.id.ua_thresh_tv);

  }


  @Override
  public void onDestroy()
  {	  //Write any changes back to the bank database
	  bank.WriteChanges(et1.getText().toString(), et2.getText().toString(), et3.getText().toString(), et4.getText().toString());
	  super.onDestroy();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  { // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.update_account_setting, menu);
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
        // Display a help documentation to describe the activity of update account settings.

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

