package com.example.plutus;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewAccountBalanceActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_account_balance);
    AcntMenuFragment amf = new AcntMenuFragment(3);
    if (savedInstanceState == null) 
    {
      getSupportFragmentManager().beginTransaction().add(R.id.container, amf).commit();
    }

    /**
     * We may start our implementations here.
     */

    /**
     * Retrieve the user id.
     */
    // Receive an intent from the login Activity.

    // Extract the user id.

    /**
     * Retrieve account balances.
     */

    /**
     * Display account balances.
     */

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

    // Here are the original Android codes.
    // if (id == R.id.action_settings) {
    // return true;
    // }
    // return super.onOptionsItemSelected(item);

  }

  /**
   * A fragment for the list adapter that holds the main menu
   */
  public class AcntMenuFragment extends Fragment 
  {

	private MenuItemAdapter arrAdpt = null;
	private ArrayList<AccountMenuItem> listElems = null;
	private ListView lv = null;
	private TextView menuTv2 = null;
	private ProgressBar[] pbs = null; 
	private String tv2Text = "";
	private int[] tempPbProg = null;
	public AcntMenuFragment(int numAcnts) 
	{
		pbs = new ProgressBar[numAcnts];
		tempPbProg = new int[numAcnts];
	}
	
	public void SetTv2(String txt)
	{
		if(menuTv2 == null)
			tv2Text = txt;
		else
			menuTv2.setText(txt);
	}
	
	public void SetPb(int prog, int ind)
	{
		if(menuTv2 == null)
			tempPbProg[ind] = prog;
		else
			pbs[ind].setProgress(prog);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	//When the fragment is created instantiate the list on the UI
		View rootView = inflater.inflate(R.layout.fragment_view_account_balance, container, false);

		//Populate the list view items for the UI
		listElems = new ArrayList<AccountMenuItem>();
		String[] values = {"Checking", "Savings", "Cash"};
		for(int i = 0; i < 3; ++i)
			listElems.add(new AccountMenuItem((i + 1) * 20, "..................... " + values[i], String.format("$%.2f", (i + 100) * Math.PI), String.format("$%.2f above threshold", Math.E * Math.PI * (i + 1)), "Account #" + (i + 1), "foo@foo.com", "555-5555-5555"));
		lv = (ListView) rootView.findViewById(R.id.ab_lv);
		arrAdpt = new MenuItemAdapter(getActivity().getApplicationContext(), listElems);
		lv.setAdapter(arrAdpt);


		return rootView;
	}
  }

}