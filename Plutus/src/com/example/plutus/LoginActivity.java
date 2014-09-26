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

public class LoginActivity extends ActionBarActivity {
  /**
   * Define the key for the intent's extra content using a public constant here or in strings.xml,
   * which can be used in the code as R.string.indent_key;
   */

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment())
          .commit();
    }
  }


  /**
   * Upon login, the System direct the user to his or her main activity.
   */
  public void loginUserMainActivity(View view) {
    // Initialize the intent to user's main activity
    Intent userMainIntent = new Intent(this, UserMainActivity.class);

    // Get the user name.
    
    // Get the password.

    // Retrieve the user id from the Bank database.

    // Build the intent with the user id.

    // Start the user's main activity.
    startActivity(userMainIntent);
  }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.login, menu);
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
  public static class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_login, container, false);
      return rootView;
    }
  }

}