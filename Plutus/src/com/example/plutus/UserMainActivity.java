package com.example.plutus;

import java.util.ArrayList;
import java.util.Locale;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import android.widget.Toast;

public class UserMainActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;
  private TextToSpeech tts;
  private Intent umaIntent = null;
  private MainMenuFragment mmf = null;
  private Bank bank = new Bank();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_main);
    mmf = new MainMenuFragment();
    if (savedInstanceState == null) 
    {
      getSupportFragmentManager().beginTransaction().add(R.id.container, mmf).commit();
    }
    /**
     * We may start our implementations here.
     */
    // Receive an intent from the login Activity.
    umaIntent = getIntent();
    // Extract the user id.
    userid = umaIntent.getIntExtra("uid", -1);
    if(-1 == userid)
    {
    	//TODO: Handle -1
    }
    double acntBalance = bank.GetUserBalance(userid);
    double acntThresh = bank.GetUserThreshold(userid);
    mmf.SetTv2(String.format("$%.2f ($%.2f above threshold)", acntBalance, acntBalance - acntThresh));
    mmf.SetPb1((int) (acntBalance - acntThresh) / 10);
    /**
     * Check if any account is over-budget and send an alert notification in the form of text, email
     * or pop-up messages.
     */
    
    //Text to speech to welcome the user
    tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() 
    {
    	@Override
    	public void onInit(int status) 
    	{
            if(status == TextToSpeech.SUCCESS)
            {
            	tts.setLanguage(Locale.US);
            	//TODO: Put user name here
            	String txtSpeech = "Welcome user";
            	Toast.makeText(getApplicationContext(), txtSpeech, Toast.LENGTH_SHORT).show();
            	tts.speak(txtSpeech, TextToSpeech.QUEUE_FLUSH, null);
            }				
    	}
    });
  }

  @Override
  public void onDestroy()
  {	 //Perform necessary cleanup for the text to speech
	 tts.shutdown();
	 super.onDestroy();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.user_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {
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
   * A fragment for the list adapter that holds the main menu
   */
  public class MainMenuFragment extends Fragment 
  {

	private ArrayAdapter<String> arrAdpt = null;
	private ArrayList<String> listElems = null;
	private ListView lv = null;
	private TextView menuTv2 = null;
	private ProgressBar menuPb1 = null; 
	private String tv2Text = "";
	private int pb1 = 0;
	public MainMenuFragment() {}
	
	public void SetTv2(String txt)
	{
		if(menuTv2 == null)
			tv2Text = txt;
		else
			menuTv2.setText(txt);
	}
	
	public void SetPb1(int prog)
	{
		if(menuTv2 == null)
			pb1 = prog;
		else
			menuPb1.setProgress(prog);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{	//When the fragment is created instantiate the list on the UI
		View rootView = inflater.inflate(R.layout.fragment_user_main, container, false);
		menuTv2 = (TextView) rootView.findViewById(R.id.menuTv2);
		menuPb1 = (ProgressBar) rootView.findViewById(R.id.menuPb1);
		menuTv2.setText(tv2Text);
		menuPb1.setProgress(pb1);
		//Populate the list view items for the UI
		listElems = new ArrayList<String>();
		String[] values = new String[] { "\tAccount Balance", "\tAccount Statistics", "\tUpdate Account" };
		for(int i = 0; i < values.length; ++i)
			listElems.add(values[i]);
		lv = (ListView) rootView.findViewById(R.id.um_lv);
		arrAdpt = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item, R.id.label, listElems);
		lv.setAdapter(arrAdpt);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) 
			{
				switch(position)
				{
					case 0:
					// Initialize the intent to view user account balance activity.
					Intent userViewAccountBalanceIntent = new Intent(UserMainActivity.this, ViewAccountBalanceActivity.class);
					// Build the intent with the user id.
					// Start the view-account-balance activity
					startActivity(userViewAccountBalanceIntent);
					break;
				case 1:
					// Initialize the intent to view user account balance activity.
					Intent userViewAccountStatisticsIntent = new Intent(UserMainActivity.this, ViewAccountStatisticsActivity.class);
					// Build the intent with the user id.
					// Start the view-account-statistics activity.
					startActivity(userViewAccountStatisticsIntent);
					break;
				case 2:
					// Initialize the intent to view user account balance activity.
					Intent userUpdateAccountStatisticsIntent = new Intent(UserMainActivity.this, UpdateAccountSettingActivity.class);
					// Build the intent with the user id.
					// Start the update-account-settings activity.
					startActivity(userUpdateAccountStatisticsIntent);
					break;
				default:
						
					break;		
				}
			}
		});
		return rootView;
	}
  }

}
