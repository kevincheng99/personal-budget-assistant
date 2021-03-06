package com.polarbear.plutus.ui;

import com.example.plutus.R;
import com.polarbear.plutus.domain.User;
import com.polarbear.plutus.technical.BankDatabaseManager;
import com.polarbear.plutus.technical.BankDatabaseSimulationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {
  /**
   * Define the key for the intent's extra content using a public constant here
   * or in strings.xml, which can be used in the code as R.string.indent_key;
   */
  // This is the edit text for the user name field
  private EditText unameEt = null;

  // This is the edit text for the password field
  private EditText pwdEt = null;

  // Initialize the bank database with the bank database manager.
  private BankDatabaseManager plutusDbManager = new BankDatabaseManager(this);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    unameEt = (EditText) findViewById(R.id.edit_uername);
    pwdEt = (EditText) findViewById(R.id.edit_password);
  }

  /**
   * Upon login, the System direct the user to his or her main activity.
   */
  public void loginUserMainActivity(View view) 
  {
    // Initialize the intent to user's main activity
    Intent userMainIntent = new Intent(this, UserMainActivity.class);

    // Get the user name.
    String uname = unameEt.getText().toString();

    // Get the password.
    String pwd = pwdEt.getText().toString();

    if(!User.UserExists(uname, pwd, getApplicationContext())) 
    { // The user credentials were invalid, do nothing
      Toast.makeText(getApplicationContext(), "Invalid credentials.", Toast.LENGTH_SHORT).show();
    } 
    else 
    { // The credentials were valid, open main menu, build the intent with the user id.
    	//TODO encrypt the data first?
      userMainIntent.putExtra("un", uname);
      userMainIntent.putExtra("pwd", pwd);
      // Start the user's main activity.
      startActivity(userMainIntent);
    }
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
  public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu; this adds
                                                  // items to the action
                                                  // bar if it is present.
    getMenuInflater().inflate(R.menu.login, menu);
    return true;
  }

  /**
   * When the bank database becomes remote, we will remove the menu option for
   * the DB administrator. The Bank database simulation activity is used to
   * simulate the database by dropping the all the tables and recreate database
   * entries.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // Select the help event, logout event or default event.
    switch (id) {
    case R.id.action_help:
      // Display a help documentation for the login activity.

      // When successfully handling a menu item, return true.
      return true;
    case R.id.action_db_admin_login:
      // Initialize the intent to the login activity.
      Intent dbSimulationIntent = new Intent(this,
          BankDatabaseSimulationActivity.class);

      // Clear the top activities in the activity stack. We don't have to do it,
      // since we can only login as DB admin at the login activity. After login,
      // there would be only bank database simulation activity. Once the
      // simulation is finished, we can go back to the login activity.

      // dbSimulationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      // Redirect the bank database admin to the db simulation activity.
      startActivity(dbSimulationIntent);

      // When successfully handling a menu item, return true.
      return true;
    case R.id.action_logout:
      // Initialize the intent to the login activity.
      Intent loginIntent = new Intent(this, LoginActivity.class);

      // Clear the top activities in the task stack and loop back to login
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
