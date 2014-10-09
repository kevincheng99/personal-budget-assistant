package com.example.plutus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class ViewAccountStatisticsActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_account_statistics);

    /**
     * We may start our implementations here.
     */

    /**
     * Retreive the user id.
     */
    // Receive an intent from the login Activity.

    // Extract the user id.

    /**
     * Retrieve transaction records.
     */

    /**
     * Display daily, weekly and bi-weekly expense summary in a table based on the user's choice. Or
     * the default can be weekly expense summary in a table.
     */

    /**
     * Display daily, weekly and bi-weekly expense summary in a pie chart based on the user's
     * choice. Or the default can be weekly expense summary in a pie chart.
     */

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

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
        // Display a help documentation to describe the activity of view account statistics.

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
