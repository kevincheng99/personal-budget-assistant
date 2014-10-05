package com.example.plutus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity 
{
  /**
   * Define the key for the intent's extra content using a public constant here or in strings.xml,
   * which can be used in the code as R.string.indent_key;
   */

	private LoginFragment lf = null;
	private Bank bnk = new Bank();
	
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    lf = new LoginFragment();
    if (savedInstanceState == null) 
    	getSupportFragmentManager().beginTransaction().add(R.id.container, lf).commit();
  }


  /**
   * Upon login, the System direct the user to his or her main activity.
   */
  public void loginUserMainActivity(View view) 
  {
    // Initialize the intent to user's main activity
    Intent userMainIntent = new Intent(this, UserMainActivity.class);
    int userId = -1;
    // Get the user name.
    String uname = lf.GetUsernameText();
    // Get the password.
    String pwd = lf.GetPwdText();
    // Retrieve the user id from the Bank database.
    userId = bnk.GetUserIndex(uname, pwd);
    if(userId < 0)
    {	//The user credentials were invalid, do nothing
    	Toast.makeText(getApplicationContext(), "Invalid credentials.", Toast.LENGTH_SHORT).show();
    }
    else
    {	//The credentials were valid, open main menu
        // Build the intent with the user id.
        userMainIntent.putExtra("uid", userId);
        // Start the user's main activity.
        startActivity(userMainIntent);
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  { // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.login, menu);
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
        // Display a help documentation for the login activity.

        // When successfully handling a menu item, return true.
        return true;
      case R.id.action_logout:
        // Initialize the intent to the login activity.
        Intent loginIntent = new Intent(this, LoginActivity.class);

        // Clear the top activities in the task stack and loop back to login activity.
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

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class LoginFragment extends Fragment 
  {
	  //This is the edit text for the user name field
	  private EditText unameEt = null;
	  //This is the edit text for the password field
	  private EditText pwdEt = null;
	  public LoginFragment() {}

	  //Function for getting the username text from the fragment
	  public String GetUsernameText()
	  {
		  return unameEt.getText().toString();
	  }
	  
	  //Function for getting the password text from the fragment
	  public String GetPwdText()
	  {
		  return pwdEt.getText().toString();
	  }
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	  {	  //Inflate the fragment
		  View rootView = inflater.inflate(R.layout.fragment_login, container, false);
		  //Get the EditText views from the inflated fragment
		  unameEt = (EditText) rootView.findViewById(R.id.edit_uername);
		  pwdEt = (EditText) rootView.findViewById(R.id.edit_password);
		  return rootView;
	  }
  }

}
