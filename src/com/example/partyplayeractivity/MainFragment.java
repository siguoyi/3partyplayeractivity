package com.example.partyplayeractivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.WiFi.WiFiFactory;
import com.example.WiFi.WiFiFactory.WiFiType;
import com.example.WiFi.WiFiPulic;
import com.example.WiFi.WiFiBroad.WiFiBroad;
import com.example.dashproxyserver.DashProxyServer;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MainFragment extends Fragment {
	private static final String TAG = MainFragment.class.getSimpleName();

	private Button btStart;
	private Button btStop;
//	private EditText etUrl;
//	private Button btConfirm;
	private Button btCaptain;
	private Button btPlayer;
	//private Button btLow;
	//private Button btMid;
    //private Button btHigh;
	
	private DashProxyServer server;
	public static ConfigureData configureData;
	private static final boolean SEVER_START_TAG = true;
	private static final boolean SEVER_STOP_TAG = false;
	private static final String SETTING_DIALOG_TAG = "setting";
	public static String rateTag = "";
	
	private Handler myHandler;
	private boolean adhocSelect = false;
	
	private List<String> list = new ArrayList<String>();
	private ArrayAdapter<String> adapter;  
	private Spinner mySpinner;  
	private WiFiPulic wifiInstance;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		configureData = new ConfigureData(null);
		myHandler = new Handler();
		
		list.add("NONE");
		list.add("ADHOC MODE");
		list.add("BT MODE");
		list.add("NCP2 MODE");
		list.add("TCP MODE");

	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, parent, false);

		btStart = (Button) v.findViewById(R.id.btStart);
		btStop = (Button) v.findViewById(R.id.btStop);
//		btConfirm = (Button) v.findViewById(R.id.btConfirm);
		btPlayer = (Button) v.findViewById(R.id.btChoose);
		btCaptain = (Button) v.findViewById(R.id.btCaptain);
//		btCaptain.setClickable(false);
//		etUrl = (EditText) v.findViewById(R.id.url_edit_text);
		
		mySpinner = (Spinner) v.findViewById(R.id.Spinner_wifi_);
		adapter = new ArrayAdapter<String>
			(getActivity(),android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mySpinner.setAdapter(adapter);
		
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				switch (arg2){
				case 0: //none
					adhocSelect = false;
					break;
				case 1: //adhoc
					adhocSelect = true;
					wifiInstance = WiFiFactory.getInstance(getActivity(), WiFiType.BROAD);
					
					break;
				case 2: //bt
					adhocSelect = false;
					break;
				case 3: //ncp2
					adhocSelect = false;
				    break;
				case 4: //tcp
					adhocSelect = false;
					break;
					
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				// arg0.setSelection(0);
			}
		});
		
		btCaptain.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(adhocSelect){
					
					try {
						WiFiBroad.send("I am Captain!");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					myHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Toast.makeText(getActivity(), "I am captain!", Toast.LENGTH_SHORT)
							.show();
						}
					});
				}
				
				
			}
		});

		
//		btLow = (Button) v.findViewById(R.id.rate_low);
//		btMid = (Button) v.findViewById(R.id.rate_mid);
//		btHigh = (Button) v.findViewById(R.id.rate_high);
//		
//		btLow.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				rateTag = "100";
//			}
//		});
//		
//		btMid.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				rateTag = "300";
//			}
//		});
//		
//		btHigh.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				rateTag = "500";
//			}
//		});

		btStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				server = new DashProxyServer();

				try {
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}

				sendNotification(SEVER_START_TAG);
				configureData.setServiceAlive(true);
				getActivity().getActionBar().setSubtitle("Service is running");

			}
		});

		btStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (server != null) {
					server.stop();
				}

				sendNotification(SEVER_STOP_TAG);
				getActivity().getActionBar().setSubtitle(
						"Service is not running");
				configureData.setServiceAlive(false);
			}
		});

		configureData.setUrl("http://127.0.0.1:9999/4/s-1.mp4");

//  		etUrl.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// TODO Auto-generated method stub
//				configureData.setUrl(s.toString());
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//
//			}
//		});

//		btConfirm.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				if (configureData.getUrl() != null
//						&& isLegel(configureData.getUrl())) {
//					Toast.makeText(getActivity(),
//							"URL is\n" + configureData.getUrl(),
//							Toast.LENGTH_SHORT).show();
//				} else {
//					Toast.makeText(getActivity(), "URL illegal",
//							Toast.LENGTH_SHORT).show();
//				}
//
//			}
//		});

		btPlayer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null == configureData.getUrl()) {
					Toast.makeText(getActivity(), "Please set URL!",
							Toast.LENGTH_SHORT).show();
					// }else if(!isNetworkAvailable()){
					// Toast.makeText(getActivity(),
					// "Please connect to the network!",
					// Toast.LENGTH_SHORT).show();
				} else if (!configureData.isServiceAlive()) {
					Toast.makeText(getActivity(), "Please start Service!",
							Toast.LENGTH_SHORT).show();
				} else if (!isLegel(configureData.getUrl())) {
					Toast.makeText(getActivity(), "Please give correct URL!",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent j = new Intent();
					j.setDataAndType(Uri.parse(configureData.getUrl()),
							"video/*");
					// j = Intent.createChooser(j,
					// "Please select media player");
					startActivity(j);
				}

			}
		});

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// no inspection SimplifiableIfStatement
		if (id == R.id.actionsettings) {
			android.app.FragmentManager fm = getActivity().getFragmentManager();
			SettingDialog stDialog = new SettingDialog();
			stDialog.show(fm, SETTING_DIALOG_TAG);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void sendNotification(boolean status) {

		Notification notification;

		if (status) {
			notification = new NotificationCompat.Builder(getActivity())
					.setTicker("InterComponent Service Started!")
					.setSmallIcon(android.R.drawable.ic_menu_report_image)
					.setContentTitle("InterComponent Status")
					.setContentText(
							"InterComponent Service has Started, and is caputuring NC packets")
					.setAutoCancel(true).build();
		} else {
			notification = new NotificationCompat.Builder(getActivity())
					.setTicker("InterComponent Service Stopped!")
					.setSmallIcon(android.R.drawable.ic_menu_report_image)
					.setContentTitle("InterComponent Status")
					.setContentText(
							"InterComponent Service has Stopped, and is not caputuring NC packets")
					.setAutoCancel(true).build();
		}
		NotificationManager notificationManager = (NotificationManager) getActivity()
				.getSystemService(Activity.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);

	}

	public boolean isNetworkAvailable() {
		boolean available = false;
		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		WifiManager wm = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		available = cm.getBackgroundDataSetting()
				&& cm.getActiveNetworkInfo() != null;
		available = available && wm.isWifiEnabled();

		return available;
	}

	public boolean isLegel(String url) {
		boolean legel = false;

		String[] s = url.split("://");

		Log.v(TAG, "s0 " + s[0]);

		if (s[0].toLowerCase().equals("http")) {
			legel = true;

		}

		return legel;
	}

}
