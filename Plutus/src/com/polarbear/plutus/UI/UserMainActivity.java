package com.polarbear.plutus.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.Stack;
import com.example.plutus.R;
import com.polarbear.plutus.domain.Transaction;
import com.polarbear.plutus.domain.User;
import com.polarbear.plutus.technical.AlertSystem;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class UserMainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener
{
  //Text to speech object for computer speech
  private TextToSpeech tts;
  //Prefix strings the compute can say
  private String[] prefixes = {"Okay, let's view ", "Great, let's view ", "Here is ", "Alright, let me bring up ", "Alright, here is ", "Okay, here is "};
  //Random object for selecting random prefixes
  private Random ran = new Random();
  //An array list of what the computer has said
  private ArrayList<String> compSpkAl = null;
  //Array adapter to display the sentances the compute has spoken
  private ArrayAdapter<String> compSpkAa = null;
  //Speech recognizer for voice commands
  private SpeechRecognizer sr = null;
  //A lookup table for the layouts (these match up with drawerTitles)
  private ImageView micBtn = null;
  private int[] layouts = {R.layout.activity_user_main, R.layout.transaction_act, R.layout.transaction_act, R.layout.activity_view_account_statistics, R.layout.activity_update_account_setting, R.layout.speech_main, R.layout.help};
  //Boolean used to differentiate between loading savings transactions and checking
  private boolean isSavings = false;
  //Drawer stuff
  private String[] drawerTitles = {" Home", " Savings", " Checking", " Charts", " Update Info", " Convo", " Help"};
  private DrawerLayout mDrawerLayout = null;
  private ListView mDrawerList;
  private FrameLayout contentFrame = null;
  //Intent from login activity
  private Intent umaIntent = null;
  //Relative layouts for the main menu (need to make clickable)
  private RelativeLayout savSumRl = null;
  private RelativeLayout chkSumRl = null;
  private RelativeLayout savPbRl = null;
  private RelativeLayout chkPbRl = null;
  private RelativeLayout alrtRl = null;
  //For transactions
  private ArrayList<Transaction> transListElem = null;
  //List view used for both the transaction window and the conversation window
  private ListView itemLv = null;
  private TransItemAdapter lstAdpt = null;
  //For Account Statistics
  private WebView grphWv = null;
  private boolean oldBuild = true;
  //Make first chart the table chart
  private int grphType = 5;
  //The user object
  private User curUser = null;
  //Maintain a stack of all the visited windows to handle the back button
  //The top of stack is the current layout
  private Stack<Integer> layoutStack = null;
  //The command promp edit text
  private EditText cmndEt = null;
  //keep track of if speech recognition is enabled
  private boolean spchEnabled = true;
  private AlertSystem as = null;
  private boolean emailAlertOn = false;
  private int ttsReqCod = 1234;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.drawer_layout);
    //Force the system to setup the TTS engine
    Intent checkIntent = new Intent();
    checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
    startActivityForResult(checkIntent, ttsReqCod);
    umaIntent = getIntent();
    //Retreive the user object for the database
    curUser = User.GetUser(umaIntent.getStringExtra("un"), umaIntent.getStringExtra("pwd"), getApplicationContext());
    //Find the command edit text
    cmndEt = (EditText) findViewById(R.id.main_cmd_et);
    //Create the side panel
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);
    // Set the adapter for the list view
    mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.drwr_tv, drawerTitles)
    {
    	@Override  
    	public View getView(int position, View view, ViewGroup viewGroup)
    	{
    		View v = super.getView(position, view, viewGroup);
    		int icoId = -1;
    		switch(position)
    		{ //Set the appropriate icon based on the position of the drawer icon
    		case 0:
    			icoId = R.drawable.house;
    			break;
    		case 1:
    			icoId = R.drawable.saving;
    			break;
    		case 2:
    			icoId = R.drawable.check;
    			break;
    		case 3:
    			icoId = R.drawable.chart;
    			break;
    		case 4:
    			icoId = R.drawable.edit;
    			break;
    		case 5:
    			icoId = R.drawable.convo;
    			break;
    		case 6:
    			icoId = R.drawable.help;
    			break;
    		}
    		//Get the TextView for the drawer item and modifiy it's DrawableLeft to add the icon
    		((TextView) v.findViewById(R.id.drwr_tv)).setCompoundDrawablesWithIntrinsicBounds(icoId, 0, 0, 0);
    		return v;
    	}
    });
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
    		//Set the appropriate layout from user's selection
    		SetLayout(layouts[position]);
    		//Close the drawer 
    		mDrawerLayout.closeDrawers();
    	}
	});

    //Make the button for speech recognition work
    micBtn = (ImageView) findViewById(R.id.main_spk_btn);
    micBtn.setOnClickListener(new View.OnClickListener() 
    {
		@Override
		public void onClick(View v) 
		{
		  //Check if the user has speech enabled
		  if(spchEnabled)
		  {	//Speech is enabled, listen for user's speech
			  //Performing listening
			  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			  intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"voice.recognition.test");
			  intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5); 
			  sr.startListening(intent);
		  }
		  else
		  {	//Get the command from command prompt
			  String resStr = cmndEt.getText().toString();
			  int oc = GetUserIntent(resStr);
			  CompSpeak(OcToText(oc));
			  HandleOC(oc);
		  }
		  
		}
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
    		//concatenate all the different results together
    		for(int i = 0; i < data.size(); ++i)
    			resStr += data.get(i);
    		//Parse the speech data
			oc = GetUserIntent(resStr);
			resStr = OcToText(oc);
			CompSpeak(resStr);
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
    layoutStack = new Stack<Integer>();
    SetLayout(R.layout.activity_user_main);
  }

  

  /**
   * Called when the TTS is initialized
   * @param i
   */
  public void onInit(int i)
  {	//Welcome the user
	  CompSpeak("Welcome " + curUser.GetUsername() + "! You have " + curUser.GetNumAlerts() + " " + (curUser.GetNumAlerts() == 1 ? "alert." : "alerts."));
  }

  
//This will be called once the intent to initialize the TTS was successful
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
	  //Check if the code is for TTS
      if(requestCode == ttsReqCod)
      {
          if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
          {
              // success, create the TTS instance
              tts = new TextToSpeech(this, this);
              tts.setLanguage(Locale.US);
          }
          else
          {
              // missing data, install it
              Intent installIntent = new Intent();
              installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
              startActivity(installIntent);
          }
      }
  }

  
  @Override
  public void onBackPressed() 
  {	//If the stack will be empty return to login screen
	  if(layoutStack.size() <= 1)
		  super.onBackPressed();
	  else
	  {	  //Pop the TOS and the one previous (it will be added again in SetLayout)
		  if(layoutStack.peek() == R.layout.activity_update_account_setting)
			  curUser.UpdateAccountInfo(GetUserInfoFromUI());
		  if(curUser.AccountInfoChanged())
			  ShowDialog(); //Let dialog handle the rest
		  else
		  {	//Don't need to show dialog window for this layout
			  layoutStack.pop();
			  SetLayout(layoutStack.pop());
		  }
	  }
  }
  
  @Override
  public void onDestroy() 
  { // Perform necessary cleanup for the text to speech
    if(tts != null)
    	tts.shutdown();
    //Perform cleanup for the speech recognizer
    if(sr != null)
    	sr.destroy();
    super.onDestroy();
  }
  
  @Override
  public void onSaveInstanceState(Bundle outState)
  {
	  super.onSaveInstanceState(outState);
	  if(layoutStack.size() > 0)
		  outState.putInt("curLayout", layoutStack.peek());
	  else
		  outState.putInt("curLayout", R.layout.activity_user_main);
	  return;
  }
  
  @Override
  public void onRestoreInstanceState(Bundle saveState)
  {
	  super.onSaveInstanceState(saveState);
	  SetLayout(saveState.getInt("curLayout"));
  }

  // When resume/running/visible to the user, open the database for the read
  // and write.
  @Override
  protected void onResume() 
  {
	curUser.Resume();
    super.onResume();
  }

  // When pause, close any open database.
  @Override
  protected void onPause() 
  {
    curUser.Pause();
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
    case R.id.action_spch:
    	//Change from enabled/disabled to disabled/enabled 
    	spchEnabled = !spchEnabled;
    	//Change the text on the menu item
        if(spchEnabled)
        	item.setTitle("Disable Speech");
        else
        	item.setTitle("Enable Speech");
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
	  //Text views for the main menu
	  TextView savSumTv = (TextView) findViewById(R.id.menu_sav_sum_amnt_tv);
	  TextView savSpndTv = (TextView) findViewById(R.id.menu_sav_sum_expns_tv);
	  TextView savThrshTv = (TextView) findViewById(R.id.menu_sav_thresh_amnt_tv);
	  TextView chkSumTv = (TextView) findViewById(R.id.menu_chk_sum_amnt_tv);
	  TextView chkSpndTv = (TextView) findViewById(R.id.menu_chk_sum_expns_tv);
	  TextView chkThrshTv = (TextView) findViewById(R.id.menu_chk_thresh_amnt_tv);
	  //Get the user data from the class and set the fields appropriately
	  savSumTv.setText(String.format("$%-9.2f", curUser.GetSavingBal()));
	  savSpndTv.setText(String.format("$%-9.2f", curUser.GetSavingSpend()));
	  savThrshTv.setText(String.format("$%-9.2f", curUser.GetSavingThresh()));
	  chkSumTv.setText(String.format("$%-9.2f", curUser.GetCheckBal()));
	  chkSpndTv.setText(String.format("$%-9.2f", curUser.GetCheckSpend()));
	  chkThrshTv.setText(String.format("$%-9.2f", curUser.GetCheckThresh()));
	  //Check if the user is below threshold
	  TextView alertTv = (TextView) findViewById(R.id.menu_alert_tv);
	  int numAlerts = curUser.GetNumAlerts();
	  alertTv.setText(numAlerts + " alerts");
	  if(numAlerts == 0) //User has no alerts, make the text green (red is default from xml)
		  alertTv.setTextColor(getResources().getColor(R.color.grn_txt));
	  if(!emailAlertOn)
	  	  alertTv.setTextColor(getResources().getColor(R.color.fade_col));
	  //Progress bar for user
	  ProgressBar savPb = (ProgressBar) findViewById(R.id.menu_sav_prog_pb);
	  ProgressBar chkPb = (ProgressBar) findViewById(R.id.menu_chk_prog_pb);
	  //Set 100% as 4 * threshold and mark the current balance accordingly
	  savPb.setProgress((int) Math.min(100.0, curUser.GetSavingBal() * 100 / (4 * curUser.GetSavingThresh())));
	  chkPb.setProgress((int) Math.min(100.0, curUser.GetCheckBal() * 100 / (4 * curUser.GetCheckThresh())));
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
	  //Make the progress bar buttons clickable
	  savPbRl = (RelativeLayout) findViewById(R.id.menu_sav_prog_rl);
	  savPbRl.setOnClickListener(new View.OnClickListener() 
	  {
		@Override
		public void onClick(View v) 
		{	//Load up savings transactions
			grphType = 3;
			SetLayout(R.layout.activity_view_account_statistics);
		}
	  });
	  chkPbRl = (RelativeLayout) findViewById(R.id.menu_chk_prog_rl);
	  chkPbRl.setOnClickListener(new View.OnClickListener() 
	  {
		@Override
		public void onClick(View v) 
		{	//Load up checking transactions
			grphType = 4;
			SetLayout(R.layout.activity_view_account_statistics);
		}
	  });
	  alrtRl = (RelativeLayout) findViewById(R.id.menu_alrt_rl);
	  alrtRl.setOnClickListener(new View.OnClickListener()
	  {
	  	@Override
		public void onClick(View v) 
		{	//Enable/disable email alerts
	  		CompSpeak(OcToText(11));
			HandleOC(11);
		}
	  });
	  setTitle("Home");

  }
  
  private void UpdateAccountHandler() 
  {
	  String[] userInf = new String[5];
	  userInf[0] = curUser.GetEmail();
	  userInf[1] = curUser.GetPhone();
	  userInf[2] = curUser.GetPassword();
	  userInf[3] = String.format("$%.2f", curUser.GetSavingThresh());
	  userInf[4] = String.format("$%.2f", curUser.GetCheckThresh());
	  SetUserInfoOnUI(userInf);
	  setTitle("Update Account");
  }
  
  private void SpeechMainHandler() 
  {
	  itemLv = (ListView) findViewById(R.id.spch_conv_lv);
	  compSpkAa = new ArrayAdapter<String>(getApplicationContext(), R.layout.speech_li, R.id.speech_li_tv, compSpkAl);
	  itemLv.setAdapter(compSpkAa);
	  itemLv.setSelection(compSpkAa.getCount() - 1);
	  setTitle("Conversation");
  }

  private void ViewTransHandler() 
  {
	//A text view used for the title of a layout
	TextView titleTv = null;
	titleTv = (TextView) findViewById(R.id.trns_title_tv);
	// Set the title to savings or checking
	if(isSavings)
	{
	  titleTv.setText("Savings Transactions:");
	  transListElem = curUser.GetSavingTrans();
	}
	else
	{
	  titleTv.setText("Checking Transactions:");
	  transListElem = curUser.GetCheckTrans();
	}
	// Populate transaction list
	itemLv = (ListView) findViewById(R.id.trns_lv);
	lstAdpt = new TransItemAdapter(getApplicationContext(), transListElem);
	itemLv.setAdapter(lstAdpt);
	setTitle("Account Detail");
  }

@SuppressLint("SetJavaScriptEnabled")
private void ViewStatsHandler() 
  {
	  //Find the web view for displaying the graphs
	  grphWv = (WebView) findViewById(R.id.webView1);
	  //Need to check if the phone supports SVG in its browser
	  if(android.os.Build.VERSION.RELEASE.startsWith("1.") || android.os.Build.VERSION.RELEASE.startsWith("2."))
	  { //Use old google image chart API for android version 1.0 and 2.0
		  oldBuild = true;
		  grphWv.loadUrl(GenerateChartUrl());
	  }
	  else
	  {
		  oldBuild = false;
		  grphWv.getSettings().setJavaScriptEnabled(true);
		  grphWv.loadDataWithBaseURL(null, GenerateChartUrl(), "text/html", "UTF-8", null);
	  }
	  //Need to set transparency AFTER loading web page (if you set before it doesn't work)
	  grphWv.setBackgroundColor(0x00000000);
	  setTitle("Account Charts");
  }
	
  //Generates a chart URL for google chart API based on the curUser and system variables
  private String GenerateChartUrl()
	{
		String url = "";
		String cat = "Food|Gas|Bills|Drugs";
		HashMap<String, Double> catAmnts = new HashMap<String, Double>();
		ArrayList<Transaction> trnList = null;
		if(isSavings)
			trnList = curUser.GetSavingTrans();
		else
			trnList = curUser.GetCheckTrans();
		for(int i = 0; i < trnList.size(); ++i)
		{
			if(!catAmnts.containsValue(trnList.get(i).trnsType))
				catAmnts.put(trnList.get(i).trnsType, trnList.get(i).trnsTotal);
			else
			{
				double temp = catAmnts.get(trnList.get(i).trnsType);
				catAmnts.remove(trnList.get(i).trnsType);
				catAmnts.put(trnList.get(i).trnsType, temp + trnList.get(i).trnsTotal);
			}
		}
		if(oldBuild)
		{ //Use old google image chart API for android version 1.0 and 2.0
			//Charts for the savings account
			url = "https://chart.googleapis.com/chart?chco=0000FF&cht=";
			if(grphType == 0)
				url += "p3&";
			else if(grphType == 1)
				url += "bhs&";
			else 
				url += "lc&";
			url += "chd=s:Uf9a&chs=300x150&chl=" + cat + "&chf=bg,s,65432100";
		}
		else
		{	//Use latest google chart api
			String[] catTitles = catAmnts.keySet().toArray(new String[catAmnts.size()]);
			String title = "";
			if(isSavings)
				title = "Savings Expenses";
			else
				title = "Checking Expenses";
			String catStr = "[['Category', 'Amount']";
			for(int i = 0; i < catAmnts.size(); ++i)
				catStr += ",['" + catTitles[i] + "', " + Double.toString(catAmnts.get(catTitles[i])) + "]";
			catStr += "]";
			int height = 475;
			int width = 360;
			if(grphType == 0)	//Pie chart
				url = "<html><head><script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script><script type=\"text/javascript\">google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});google.setOnLoadCallback(drawChart);function drawChart() {var data = google.visualization.arrayToDataTable(" + catStr + ");var options = {'title':'" + title + "', 'width':" + width + ", 'height':" + height + ", 'backgroundColor': 'transparent', is3D: true};var chart = new google.visualization.PieChart(document.getElementById('piechart'));chart.draw(data, options);}</script></head><body><div id=\"piechart\"></div></body></html>";
			else if(grphType == 1)	//Bar chart
				url = "<html><head><script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script><script type=\"text/javascript\">google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});google.setOnLoadCallback(drawChart);function drawChart() {var data = google.visualization.arrayToDataTable(" + catStr + ");var options = {'title':'" + title + "', 'width':" + width + ", 'height':" + height + ", 'backgroundColor': 'transparent', legend: {position: 'none'}}; var chart = new google.visualization.BarChart(document.getElementById('chart_div'));chart.draw(data, options);}</script></head><body><div id=\"chart_div\"></div></body></html>";
			else if(grphType == 2)	//Table
				url = "<html><head><script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script><script type=\"text/javascript\">google.load(\"visualization\", \"1\", {packages:[\"table\"]});google.setOnLoadCallback(drawTable);function drawTable() {var data = google.visualization.arrayToDataTable(" + catStr + ");var options = {'width':" + (width - 25) + ", 'height':" + height + ", 'backgroundColor': 'transparent'};var table = new google.visualization.Table(document.getElementById('table_div'));table.draw(data, options);}</script></head><body><div id=\"table_div\"></div></body></html>";
			else if(grphType == 3) //Saving threshold summary
				url = "<html><head><script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script><script type=\"text/javascript\">google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});google.setOnLoadCallback(drawChart);function drawChart() {var data = google.visualization.arrayToDataTable([['Category', 'Amount', 'Threshold'],['Balance', " + String.format("%.2f", curUser.GetSavingBal()) + ", " + String.format("%.2f", curUser.GetSavingThresh()) + "], ['Spending', " + String.format("%.2f", curUser.GetSavingSpend()) + ", " + String.format("%.2f", curUser.GetSavingThresh()) +"]]);var options = {'title':'Saving Summary', 'width':" + width + ", 'height':" + height + ", 'backgroundColor': 'transparent', legend: {position: 'none'}}; var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));chart.draw(data, options);}</script></head><body><div id=\"chart_div\"></div></body></html>";
			else if(grphType == 4) //Checking threshold summary
				url = "<html><head><script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script><script type=\"text/javascript\">google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});google.setOnLoadCallback(drawChart);function drawChart() {var data = google.visualization.arrayToDataTable([['Category', 'Amount', 'Threshold'],['Balance', " + String.format("%.2f", curUser.GetCheckBal()) + ", " + String.format("%.2f", curUser.GetCheckThresh()) + "], ['Spending', " + String.format("%.2f", curUser.GetCheckSpend()) + ", " + String.format("%.2f", curUser.GetCheckThresh()) +"]]);var options = {'title':'Checking Summary', 'width':" + width + ", 'height':" + height + ", 'backgroundColor': 'transparent', legend: {position: 'none'}}; var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));chart.draw(data, options);}</script></head><body><div id=\"chart_div\"></div></body></html>";
			else if(grphType == 5)
				url = "<html><body><object height=\"400\" width=\"300\" classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\"codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,28,0\" id=\"widget_name\"><param name=\"movie\" value=\"http://vhss-d.oddcast.com/vhss_editors/voki_player.swf?doc=http://vhss-d.oddcast.com/php/vhss_editors/getvoki/chsm=d0d78a2e6c69694b04aae5ca9d516880%26sc=10712548\" /><param name=\"quality\" value=\"high\" /><param name=\"allowScriptAccess\" value=\"always\" /><param name=\"width\" value=\"300\" /><param name=\"height\" value=\"400\" /><param name=\"allowNetworking\" value=\"all\"/><param name=\"wmode\" value=\"transparent\" /><param name=\"allowFullScreen\" value=\"true\" /><embed height=\"400\" width=\"300\" src=\"http://vhss-d.oddcast.com/vhss_editors/voki_player.swf?doc=http%3A%2F%2Fvhss-d.oddcast.com%2Fphp%2Fvhss_editors%2Fgetvoki%2Fchsm=d0d78a2e6c69694b04aae5ca9d516880%26sc=10712548\" quality=\"high\" allowScriptAccess=\"always\" allowNetworking=\"all\" wmode=\"transparent\" allowFullScreen=\"true\" pluginspage=\"http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash\" type=\"application/x-shockwave-flash\" name=\"widget_name\"></object></body></html>";
		}
		return url;
	}  
  
  private void HelpHandler()
  {
	  /**
	  setTitle("Help");
	  VideoView vv = (VideoView) findViewById(R.id.help_vv);
	  vv.setVideoPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/Foo.mp4");
	  vv.start();
	  */
	  // Initialize the intent to user's main activity
	  Intent helpIntent = new Intent(this, HelpActivity.class);
	  startActivity(helpIntent);

  }
  
  //Handles changing the main content frame, pass it one of the layouts in the layout array
  private void SetLayout(int layout)
  {
	if(layout != R.layout.help)
	{
		if(layoutStack.size() == 0 || layout != layoutStack.peek())
			layoutStack.push(layout);
		contentFrame = (FrameLayout) findViewById(R.id.content_frame);
		View child = getLayoutInflater().inflate(layout, null);
		//Remove the old view and set a new one
		contentFrame.removeAllViews();
		contentFrame.addView(child);
	}
	if(layout == R.layout.activity_user_main)
		MainMenuHandler();
	else if(layout == R.layout.activity_update_account_setting)
		UpdateAccountHandler();
	else if(layout == R.layout.transaction_act)
		ViewTransHandler();
	else if(layout == R.layout.activity_view_account_statistics)
		ViewStatsHandler();
	else if(layout == R.layout.speech_main)
		SpeechMainHandler();
	else if(layout == R.layout.help)
		HelpHandler();
  }
  
  //Handles the TTS, just pass a string and the phone will speak
  private void CompSpeak(String speech)
  {
	  //Append the string to the list of spoken sentances
	  compSpkAl.add(speech);
	  Toast.makeText(getApplicationContext(), speech, Toast.LENGTH_SHORT).show();
	  //Perform the tts
	  tts.speak(speech, TextToSpeech.QUEUE_ADD, null);
	  return;
  }
  
  //Gets a string the computer should speak from an operation code
  private String OcToText(int opCode)
  {
	  String txt = prefixes[ran.nextInt(prefixes.length)];
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
		  txt += "a table for your savings account.";
		  break;
	  case 4:
		  txt += "a pie chart for your checking account.";
		  break;
	  case 5:
		  txt += "a bar chart for your checking account.";
		  break;
	  case 6:
		  txt += "a table for your checking account.";
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
	  case 10:
		  txt = "That would without a doubt be Nicholas Smith. Seriously, he is pretty cool.";
		  break;
	  case 11:
		  //The boolean will change in a call after this point (so needs to be reversed)
		  if(!emailAlertOn)
			  txt = "Email alerts now active.";
		  else
			  txt = "Email alerts have been deactivated";
		  break;
	  default:
		  txt = "Sorry I didn't get that.";
		  break;
			  
	  }
	  return txt;
  }
  
  //Handle a system operation code
  private void HandleOC(int opCode)
  {
	  int newLayoutId = -1;
	  switch(opCode)
	  {//Any logic that needs to be accomplished before loading the layout goes here
	  case 0:	//User wants to view savings account balance
		  newLayoutId = R.layout.activity_user_main;
		  break;
	  case 1:	//User wants to view pie chart for savings
		  grphType = 0;
		  isSavings = true;
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 2:	//User wants to view a bar chart for savings
		  grphType = 1;
		  isSavings = true;
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 3:	//User wants to view a table for savings
		  grphType = 2;
		  isSavings = true;
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 4:	//User wants to view pie chart for checking
		  grphType = 0;
		  isSavings = false;
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 5:	//User wants to view bar chart for checking
		  grphType = 1;
		  isSavings = false;
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 6:	//User wants to view table for checking
		  grphType = 2;
		  isSavings = false;
		  newLayoutId = R.layout.activity_view_account_statistics;
		  break;
	  case 7:	//User wants to view transactions for savings
		  isSavings = true;
		  newLayoutId = R.layout.transaction_act;
		  break;	
	  case 8:	//User wants to view transactions for checking
		  isSavings = false;
		  newLayoutId = R.layout.transaction_act;
		  break;
	  case 9:	//User wants to update account info
		  newLayoutId = R.layout.activity_update_account_setting;
		  break;
	  //Case 10 can go to default ;-)
	  case 11:
		  newLayoutId = R.layout.speech_main;
		  emailAlertOn = !emailAlertOn;
		  if(emailAlertOn)
			  StartAlertSystem();
		  break;
	  default:	//User wants to view convo window
		  newLayoutId = R.layout.speech_main;
		  break;
	  }
	  //Set the layout
	  SetLayout(newLayoutId);
	  
  }
  
  //Turn on email alerts, check the accounts if they are underthreshold and send an email if they are
  private void StartAlertSystem()
  {
	  as = new AlertSystem();
	  if(curUser.SavingAlert())
	  {
		try 
		{
			as.SendEmailAlert(curUser.GetUsername(), curUser.GetEmail(), "savings");
		} catch (Exception e1) 
		{
			e1.printStackTrace();
		}
		CompSpeak(curUser.GetUsername() + ", an email alert has been sent to your account.");
	  }
	  if(curUser.CheckingAlert())
	  {
		try 
		{
			as.SendEmailAlert(curUser.GetUsername(), curUser.GetEmail(), "checking");
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		CompSpeak(curUser.GetUsername() + ", an email alert has been sent to your account.");
	  }
  }
  
  //Get an operation code from the user's speech that indicates which action the system should take
  private int GetUserIntent(String speech)
  {	//parse the user's speech
	  int opCode = -1;
	  if(speech.contains("account") && speech.contains("balance"))
		  opCode = 0; //User probably wants to view his/her balance
	  else if(speech.contains("pie") && speech.contains("saving"))
		  opCode = 1; //User probably wants to view a pie chart for his/her savings account
	  else if(speech.contains("bar") && speech.contains("saving"))
		  opCode = 2; //User probably wants to view a bar chart for his/her savings account
	  else if(speech.contains("table") && speech.contains("saving"))
		  opCode = 3; //User probably wants to view a bar chart for his/her savings account
	  else if(speech.contains("pie") && speech.contains("checking"))
		  opCode = 4; //User probably wants to view a pie chart for his/her savings account
	  else if(speech.contains("bar") && speech.contains("checking"))
		  opCode = 5; //User probably wants to view a bar chart for his/her savings account
	  else if(speech.contains("table") && speech.contains("checking"))
		  opCode = 6; //User probably wants to view a bar chart for his/her savings account
	  else if(speech.contains("saving") && speech.contains("transaction"))
		  opCode = 7; //User probably wants to view a bar chart for his/her savings transactions
	  else if(speech.contains("checking") && speech.contains("transaction"))
		  opCode = 8; //User probably wants to view a bar chart for his/her checking transactions
	  else if(speech.contains("update"))
		  opCode = 9; //User probably wants to update his/her account
	  else if(speech.contains("cool") && speech.contains("person"))
		  opCode = 10; //User is probably wondering who is the coolest person in the world
	  else if(speech.contains("email") && speech.contains("alert"))
		  opCode = 11; //User wants to turn on/off email alerts
	  return opCode;
  }
  
  //Use this function to get the user's info from the Update Account Info Screen
  private String[] GetUserInfoFromUI()
  {
	  String[] userInf = new String[5];
	  userInf[0] = ((EditText) findViewById(R.id.ua_email_et)).getText().toString();
	  userInf[1] = ((EditText) findViewById(R.id.ua_phone_et)).getText().toString();
	  userInf[2] = ((EditText) findViewById(R.id.ua_pwd_et)).getText().toString();
	  userInf[3] = ((EditText) findViewById(R.id.ua_savthresh_et)).getText().toString();
	  userInf[4] = ((EditText) findViewById(R.id.ua_chkthresh_et)).getText().toString();
	  return userInf;
  }
  
  //User this function to set the user's info on the Update Account Info screen
  private void SetUserInfoOnUI(String[] userInf)
  {
	  ((EditText) findViewById(R.id.ua_email_et)).setText(userInf[0]);
	  ((EditText) findViewById(R.id.ua_phone_et)).setText(userInf[1]);
	  ((EditText) findViewById(R.id.ua_pwd_et)).setText(userInf[2]);
	  ((EditText) findViewById(R.id.ua_savthresh_et)).setText(userInf[3]);
	  ((EditText) findViewById(R.id.ua_chkthresh_et)).setText(userInf[4]);
  }
  
  //Prompt the user to write changes or not
  private void ShowDialog()
  {
	  new AlertDialog.Builder(this)
	    .setTitle("Write Changes?")
	    .setMessage("Are you sure you want to save changes?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) 
	        { 
	        	curUser.WriteChanges();
	        	layoutStack.pop();
	        	SetLayout(layoutStack.pop());
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) 
	        {
	        	layoutStack.pop();
	        	SetLayout(layoutStack.pop());
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
  }
}
  
