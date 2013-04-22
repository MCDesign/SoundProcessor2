package mindcastdesign.media.soundtrap;




import java.net.URI;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	SoundMeter a = new SoundMeter();
	private static final int POLL_INTERVAL = 500;
	private int mTickCount = 0;
	private int mdi = 0;
	private Handler mHandler = new Handler();
	Boolean isStarted = false;
	Runnable mPollTask;
	//private SoundLevelView mDisplay;
	private int overThr = 0;
	private int poll_interval = POLL_INTERVAL;
	private int isAlarm = 0;
	private EditText phone_no ;
	public static DevicePolicyManager mDPM;
	ComponentName devAdminReceiver; 
	ImageView img_led_on;
	ImageView img_led_alarm;
	ImageView img_tune;
	private int app_state = 0;
	private int count_state = 0;
	/* app_state 
	 * 0 - init
	 * 1 - running looking for noise
	 * 
	 * */
	private int mdi_custom = 300;
	private int mdi_calculat = -1;
	private int delay_to_alarm = 10;
	
    void UpdateMe()
    {
    	TextView txt = (TextView)findViewById(R.id.textView1);
    	
    	int xx = (int)(a.getAmplitude()*100);
    	mdi = (mdi + xx)/2;
    	String s = "X="+mdi;
    	Log.d("SoundLevel",s);
    	txt.setText(s);
    	poll_interval = POLL_INTERVAL;
    	//sk.setProgress(xx);
    	//mDisplay.setLevel(xx/2);
    	//mDisplay.invalidate();
    	if (count_state >0)
    	{
    		count_state--;
    		if (app_state == 0)
    		{
    			if (mdi_calculat == -1)
    				mdi_calculat = mdi;
    			mdi_calculat = (mdi + mdi_calculat)/2;
    			mdi_custom = (int) (mdi_calculat * 1.5);
    		}
    	}
    	else
    	{
    		if (app_state == 0)
    		{
    			img_tune.setImageResource(R.drawable.led_green);
    			app_state = 1;
    		}
    	}
    	if (isAlarm > 0)
    	{
    		isAlarm--;
    		return;
    	}
    	if (app_state > 0)
    	if (mdi > mdi_custom)
    	{
    		overThr++;
    		String txtOvr = "("+(delay_to_alarm-overThr)+")";
    		TextView txt2 = (TextView)findViewById(R.id.textView2);
    		txt2.setText("OK<"+mdi_custom+txtOvr);
    		
    	}
    	else
    	{
    		overThr = 0;
    		String txtOvr = "("+(delay_to_alarm-overThr)+")";
    		TextView txt2 = (TextView)findViewById(R.id.textView2);
    		txt2.setText("OK<"+mdi_custom+txtOvr);
    	}
    	
    	if (overThr > delay_to_alarm)
    	{
    		TextView txt2 = (TextView)findViewById(R.id.textView2);
    		txt2.setText("ALARM");
    		isAlarm = 100;
    		SmsManager sms = SmsManager.getDefault();
    		//Toast.makeText(getApplicationContext(),bd_sTelefon, Toast.LENGTH_LONG).show();
    		PendingIntent pi_sent = PendingIntent.getActivity(getApplicationContext(), 0,
    	            new Intent(getApplicationContext(), MainActivity.class), 0);  	
    		PendingIntent pi_delivery = PendingIntent.getActivity(getApplicationContext(), 0,
    	            new Intent(getApplicationContext(), MainActivity.class), 0);  
    		Toast.makeText(getApplicationContext(), phone_no.getText().toString(), Toast.LENGTH_LONG).show();
    
    		if (phone_no.getText().toString().length()>0)
    		sms.sendTextMessage(phone_no.getText().toString(), 
    				null, 
    				"ALARM", 
    				null, 
    				null);
    		

    		boolean admin = mDPM.isAdminActive(devAdminReceiver);
    		if (admin)
    		    mDPM.lockNow();
    		else Log.i("TAG","Not an admin");
    		
    		img_led_alarm.setImageResource(R.drawable.red_led);
    		if (phone_no.getText().toString().length()>0)
    			call();
    				
    				
    	}
    	else
    	{
    		img_led_alarm.setImageResource(R.drawable.led_green);
    	}
    	//Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    	
    }
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDPM = (DevicePolicyManager)getSystemService(getApplicationContext().DEVICE_POLICY_SERVICE);
        devAdminReceiver = new ComponentName(getApplicationContext(), MainActivity.class);
        img_led_on = (ImageView)findViewById(R.id.imageView1);
        img_led_alarm = (ImageView)findViewById(R.id.ImageView2); 
        img_tune = (ImageView)findViewById(R.id.ImageView01);
        phone_no = (EditText) findViewById(R.id.editText1);
//        mDisplay = (SoundLevelView) findViewById(R.id.volume);
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            
            	if (isStarted)
            	{
            		isStarted = false;
                	a.stop();
                	phone_no.setEnabled(true);
                	phone_no.setClickable(true);
                	img_led_on.setImageResource(R.drawable.led_gray);
                	
            	}else
            	{
            		a.start();
            		isStarted = true;
            		Log.d("SoundCapture start","SoundCaputere");
            		phone_no.setEnabled(false);
            		phone_no.setClickable(false);
            		img_led_on.setImageResource(R.drawable.led_green);
            		app_state = 0;
            		count_state = 50;
            		img_tune.setImageResource(R.drawable.red_led);
            	}
            	//a.stop();
            	
            	
            } 	
        });
        
        final Button button_addr_book = (Button)findViewById(R.id.btb_addr_book);
        button_addr_book.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), ContactStore.class);
				
		        //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        //getApplicationContext().startActivity(i);  
		        startActivityForResult(i, 1234);
		        
		        return ;
			}
		});
        
        
  
        
   
       mPollTask = new Runnable() {
    		public void run() {
    			
   
    			mTickCount++;
    			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    			if (isStarted)
    				UpdateMe();
    			//	mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    			
    			
    		}
    	};
        
    	mPollTask.run();
    }

    @Override 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {     
       
    	super.onActivityResult(requestCode, resultCode, data); 
    	
      switch(requestCode) { 
        case (1234) : { 
          if (resultCode == Activity.RESULT_OK) { 
          String newText = data.getStringExtra("Telefon");
          // TODO Update your TextView.
          //Toast.makeText(getApplicationContext(), "am primit mesajul:"+newText, Toast.LENGTH_LONG).show();
          phone_no.setText(newText);
          } 
          break; 
        } 
      } 
    }
    
    public void onDraw(Canvas canvas) {
    	Log.e("MyAppTag: ", "HttpException: ");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void call() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phone_no.getText().toString()));
            startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
             Log.e("helloandroid dialing example", "Call failed", activityException);
        }
        
    }
    
}
