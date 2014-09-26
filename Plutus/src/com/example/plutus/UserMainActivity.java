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

public class UserMainActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_main);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment())
          .commit();
    }

    /**
     * We may start our implementations here.
     */
    // Receive an intent from the login Activity.

    // Extract the user id.

    /**
     * Check if any account is over-budget and send an alert notificaiton in the form of text, email
     * or pop-up messages.
     */

  }

  /**
   * Start the View Account Balance Activity.
   */
  public void viewUserAccountBalanceActivity(View view) {
    // Initialize the intent to view user account balance activity.
    Intent userViewAccountBalanceIntent = new Intent(this, ViewAccountBalanceActivity.class);

    // Build the intent with the user id.

    // Start the view-account-balance activity
    startActivity(userViewAccountBalanceIntent);
  }

  /**
   * Start the View Account Statistics Activity.
   */
  public void viewUserAccountStatisticActivity(View view) {
    // Initialize the intent to view user account balance activity.
    Intent userViewAccountStatisticsIntent = new Intent(this, ViewAccountStatisticsActivity.class);

    // Build the intent with the user id.

    // Start the view-account-statistics activity.
    startActivity(userViewAccountStatisticsIntent);
  }

  /**
   * Start the Update Account Settings Activity.
   */
  public void updateUserAccountSettingActivity(View view) {
    // Initialize the intent to view user account balance activity.
    Intent userUpdateAccountStatisticsIntent = new Intent(this, UpdateAccountSettingActivity.class);

    // Build the intent with the user id.

    // Start the update-account-settings activity.
    startActivity(userUpdateAccountStatisticsIntent);
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

        // Clear the top activities in the task stack and go back to log in activity.
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
      View rootView = inflater.inflate(R.layout.fragment_user_main, container, false);
      return rootView;
    }
  }

}