package com.example.plutus;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserMainActivity extends ActionBarActivity {
  // Initialize the user id.
  private int userid = -1;
  // Initialize the bank database with the bank database manager.
  private BankDatabaseManager plutusDbManager = new BankDatabaseManager(this);
  //Text to speech object for computer speech
  private TextToSpeech tts;
  //Prefix strings the compute can say
  private String[] prefixes = {"Okay, let's view ", "Great, let's view ", "Here is "};
  //Random object for selecting random prefixes
  private Random ran = new Random();
  //An array list of what the computer has said
  private ArrayList<String> compSpkAl = null;
  //Array adapter to display the sentances the compute has spoken
  private ArrayAdapter<String> compSpkAa = null;
  //Speech recognizer for voice commands
  private SpeechRecognizer sr = null;
  //A lookup table for the layouts (these match up with drawerTitles)
  private ImageButton micBtn = null;
  private int[] layouts = {R.layout.activity_user_main, R.layout.transaction_act, R.layout.transaction_act, R.layout.activity_view_account_statistics, R.layout.activity_update_account_setting, R.layout.speech_main};
  //Boolean used to differentiate between loading savings transactions and checking
  private boolean isSavings = false;
  //Drawer stuff
  private String[] drawerTitles = {"Home", "Savings", "Checking", "Graphs", "Update Info", "Convo"};
  private DrawerLayout mDrawerLayout = null;
  private ListView mDrawerList;
  private FrameLayout contentFrame = null;
  //Intent from login activity
  private Intent umaIntent = null;
  private Bank bank = new Bank();
  private String txtToSpeak = "";
  //Relative layouts for the main menu (need to make clickable)
  private RelativeLayout savSumRl = null;
  private RelativeLayout chkSumRl = null;
  //Keeps track of the current layout
  private int curLayout = -1;
  //A text view used for the title of a layout
  private TextView titleTv = null;
  //For transactions
  private ArrayList<Transaction> transListElem = null;
  //List view used for both the transaction window and the conversation window
  private ListView itemLv = null;
  private TransItemAdapter lstAdpt = null;
  //For Account Statistics
  private WebView grphWv = null;
  private int curAcnt = 0;
  private int curWeek = 0;
  private int curGrph = 0;
  private boolean oldBuild = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.drawer_layout);
    umaIntent = getIntent();
<<<<<<< HEAD
    //Create the side panel
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);
    // Set the adapter for the list view
    mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.drwr_tv, drawerTitles));
    //Make the drawer clickable
    mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() 
    {
    	@Override
    	public void onItemClick(AdapterView<?> parent, final View view, int position, long id) 
    	{	//Load the appropriate layout on click
    		if(position == 1)
    			isSavings = true;
    		else
    			isSavings = false;
    		SetLayout(layouts[position]);
    		mDrawerLayout.closeDrawers();
    	}
	});
    //Make the button for speech recognition work
    micBtn = (ImageButton) findViewById(R.id.main_spk_btn);
    micBtn.setOnClickListener(new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
		  //Performing listening
		  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);        
		  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		  intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
		  intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5); 
		  sr.startListening(intent);
		}
=======
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
>>>>>>> origin/master
    });
    //Create the speech recognizer object
    sr = SpeechRecognizer.createSpeechRecognizer(this);
    sr.setRecognitionListener(new RecognitionListener()
    {
    	@Override
    	public void 
    	onResults(Bundle results)
    	{
    		ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    		int oc = -1;
    		String resStr = "";
    		for(int i = 0; i < data.size(); ++i)
    			resStr += data.get(i);
			oc = GetUserIntent(resStr);
			CompSpeak(OcToText(oc));
    		HandleOC(oc);
    	}
    	
    	@Override
    	public void onEndOfSpeech(){}
    
    	@Override
    	public void onBufferReceived(byte[] buf) {}

		@Override
		public void onBeginningOfSpeech() {}

		@Override
		public void onError(int arg0) {}

		@Override
		public void onEvent(int arg0, Bundle arg1) {}

		@Override
		public void onPartialResults(Bundle arg0) {}

		@Override
		public void onReadyForSpeech(Bundle arg0) {}

		@Override
		public void onRmsChanged(float arg0) {}
    });
    //Set up the array list to store the conversation items
    compSpkAl = new ArrayList<String>();
    SetLayout(R.layout.activity_user_main);
    //Only welcome the user the first time
    if(savedInstanceState == null)
    	CompSpeak("Welcome Nicholas! You have 0 alerts.");
    
  }

  @Override
  public void onDestroy() 
  { // Perform necessary cleanup for the text to speech
    tts.shutdown();
    //Perform cleanup for the speech recognizer
    sr.destroy();
    super.onDestroy();
  }

  // When resume/running/visible to the user, open the database for the read
  // and write.
  @Override
  protected void onResume() 
  {
    plutusDbManager.openReadMode();
    super.onResume();
  }

  // When pause, close any open database.
  @Override
  protected void onPause() 
  {
    plutusDbManager.close();
    super.onPause();
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
  
  private void MainMenuHandler()
  {
	  //TODO fill the blank text views with account info
	  //Make the account summary buttons clickable
	  savSumRl = (RelativeLayout) findViewById(R.id.menu_sav_sum_rl);
	  savSumRl.setOnClickListener(new View.OnClickListener() 
	  {
		@Override
		public void onClick(View v) 
		{	//Load up savings transactions
			isSavings = true;
			SetLayout(R.layout.transaction_act);
		}
	  });
	  chkSumRl = (RelativeLayout) findViewById(R.id.menu_chk_sum_rl);
	  chkSumRl.setOnClickListener(new View.OnClickListener() 
	  {
		@Override
		public void onClick(View v) 
		{	//Load up checking transactions
			isSavings = false;
			SetLayout(R.layout.transaction_act);
		}
	  });
  }
  
  private void UpdateAccountHandler() 
  {
	  //TODO get the text views
	  //TODO ask the user to confirm when exiting

  }
  
  private void SpeechMainHandler() 
  {
	  //TODO when should this be called? (when should the conversation window load)
	  itemLv = (ListView) findViewById(R.id.spch_conv_lv);
	  compSpkAa = new ArrayAdapter<String>(getApplicationContext(), R.layout.speech_li, R.id.speech_li_tv, compSpkAl);
	  itemLv.setAdapter(compSpkAa);
  }

  private void ViewTransHandler() 
  {
	titleTv = (TextView) findViewById(R.id.trns_title_tv);
	// Set the title to savings or checking
	if(isSavings)
	  titleTv.setText("Savings Transactions:");
	else
	  titleTv.setText("Checking Transactions:");
	// TODO get the transaction list for the user
	// Populate transaction list
	transListElem = bank.GetTransactions(userid);
	itemLv = (ListView) findViewById(R.id.trns_lv);
	lstAdpt = new TransItemAdapter(getApplicationContext(), transListElem);
	itemLv.setAdapter(lstAdpt);
	  
  }

  private void ViewStatsHandler() 
  {
	  //Find the web view for displaying the graphs
	  grphWv = (WebView) findViewById(R.id.webView1);
	  //Need to check if the phone supports SVG in its browser
	  if(android.os.Build.VERSION.RELEASE.startsWith("1.") || android.os.Build.VERSION.RELEASE.startsWith("2."))
	  { //Use old google image chart API for android version 1.0 and 2.0
		  oldBuild = true;
		  grphWv.loadUrl(GenerateChartUrl(curAcnt, curWeek, curGrph));
	  }
	  else
	  {
		  oldBuild = false;
		  grphWv.getSettings().setJavaScriptEnabled(true);
		  //TODO need to change to loadData
		  grphWv.loadUrl(GenerateChartUrl(curAcnt, curWeek, curGrph));
	  }
	  //Load the chart
	  grphWv.loadUrl(GenerateChartUrl(curAcnt, curWeek, curGrph));
	  //Need to set transparency AFTER loading web page (if you set before it doesn't work)
	  grphWv.setBackgroundColor(0x00000000);
  }
	
  private String GenerateChartUrl(int acntNum, int weekNum, int grphNum)
	{
		String url = "";
		String cat = "Food|Gas|Bills|Drugs";
		if(oldBuild)
		{ //Use old google image chart API for android version 1.0 and 2.0
			//Charts for the savings account
			url = "https://chart.googleapis.com/chart?chco=0000FF&cht=";
			if(grphNum == 0)
				url += "p3&";
			else if(grphNum == 1)
				url += "lc&";
			else 
				url += "bhs&";
			url += "chd=s:Uf9a&chs=300x150&chl=" + cat + "&chf=bg,s,65432100";
		}
		else
		{	//Use latest google chart api
			//TODO get the chart file and edit it dynamically
			if(grphNum == 0)
				url = "file:///android_asset/piechart.html";
			else if(grphNum == 1)
				url = "file:///android_asset/linechart.html";
			else 
				url = "file:///android_asset/barchart.html";
		}
		return url;
	}  
  
  private void SetLayout(int layout)
  {
	curLayout = layout;
	contentFrame = (FrameLayout) findViewById(R.id.content_frame);
	View child = getLayoutInflater().inflate(curLayout, null);
	//Remove the old view and set a new one
	contentFrame.removeAllViews();
	contentFrame.addView(child);
	if(curLayout == R.layout.activity_user_main)
		MainMenuHandler();
	else if(curLayout == R.layout.activity_update_account_setting)
		UpdateAccountHandler();
	else if(curLayout == R.layout.transaction_act)
		ViewTransHandler();
	else if(curLayout == R.layout.activity_view_account_statistics)
		ViewStatsHandler();
	else if(curLayout == R.layout.speech_main)
		SpeechMainHandler();
  }
  
  private void CompSpeak(String speech)
  {
	  //Append the string to the list of spoken sentances
	  compSpkAl.add(speech);
	  //Perform the tts
	  txtToSpeak = speech;
	  tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() 
	  {
	      @Override
	      public void onInit(int status) 
	      {
	        if (status == TextToSpeech.SUCCESS) 
	        {
	          Toast.makeText(getApplicationContext(), txtToSpeak, Toast.LENGTH_SHORT).show();
	      	  tts.setLanguage(Locale.US);
	      	  tts.speak(txtToSpeak, TextToSpeech.QUEUE_FLUSH, null);
		    }
	      }
	  });
	  return;
  }
  private String OcToText(int opCode)
  {
	  String txt = "";
	  txt += prefixes[ran.nextInt(prefixes.length)];
	  switch(opCode)
	  {
	  case 0:
		  txt += "your account balances.";
		  break;
	  case 1:
		  txt += "a pie chart for your savings account.";
		  break;
	  case 2:
		  txt += "a bar chart for your savings account.";
		  break;
	  case 3:
		  txt += "a line chart for your savings account.";
		  break;
	  case 4:
		  txt += "a pie chart for your checking account.";
		  break;
	  case 5:
		  txt += "a bar chart for your checking account.";
		  break;
	  case 6:
		  txt += "a line chart for your checking account.";
		  break;
	  case 7:
		  txt += "the transactions for your savings account.";
		  break;
	  case 8:
		  txt += "the transactions for your checking account.";
		  break;
	  case 9:
		  txt += "your account info.";
		  break;
	  default:
		  txt = "Sorry I didn't get that.";
		  break;
			  
	  }
	  return txt;
  }
  
  private void HandleOC(int opCode)
  {
	  int newLayoutId = -1;
	  switch(opCode)
	  {//Any logic that needs to be accomplished before loading the layout goes here
	  case 0:	//User wants to view savings account balance
		  newLayoutId = R.layout.activity_user_main;
		  break;
	  case 1:
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 2:
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 3:
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 4:
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 5:
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 6:
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 7:
		  isSavings = true;
		  newLayoutId = R.layout.transaction_act;
		  break;
	  case 8:
		  isSavings = false;
		  newLayoutId = R.layout.transaction_act;
		  break;
	  case 9:
		  newLayoutId = R.layout.activity_update_account_setting;
		  break;
	  default:
		  newLayoutId = R.layout.speech_main;
		  break;
	  }
	  //Set the layout
	  SetLayout(newLayoutId);
	  
  }
  
  private int GetUserIntent(String speech)
  {	//TODO parse the user's speech better and add more opcodes for different commands
	  int opCode = -1;
	  if(speech.contains("account") && speech.contains("balance"))
		  opCode = 0; //User probably wants to view his/her balance
	  else if(speech.contains("pie") && speech.contains("saving"))
		  opCode = 1; //User probably wants to view a pie chart for his/her savings account
	  else if(speech.contains("bar") && speech.contains("saving"))
		  opCode = 2; //User probably wants to view a bar chart for his/her savings account
	  else if(speech.contains("line") && speech.contains("saving"))
		  opCode = 3; //User probably wants to view a bar chart for his/her savings account
	  else if(speech.contains("pie") && speech.contains("checking"))
		  opCode = 4; //User probably wants to view a pie chart for his/her savings account
	  else if(speech.contains("bar") && speech.contains("checking"))
		  opCode = 5; //User probably wants to view a bar chart for his/her savings account
	  else if(speech.contains("line") && speech.contains("checking"))
		  opCode = 6; //User probably wants to view a bar chart for his/her savings account
	  else if(speech.contains("saving") && speech.contains("transaction"))
		  opCode = 7; //User probably wants to view a bar chart for his/her savings transactions
	  else if(speech.contains("checking") && speech.contains("transaction"))
		  opCode = 8; //User probably wants to view a bar chart for his/her checking transactions
	  else if(speech.contains("update"))
		  opCode = 9; //User probably wants to update his/her account
	  //TODO more speech parsing
	  
	  return opCode;
  }
}
