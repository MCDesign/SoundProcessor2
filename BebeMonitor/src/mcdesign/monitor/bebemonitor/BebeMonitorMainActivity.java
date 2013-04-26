package mcdesign.monitor.bebemonitor;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BebeMonitorMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bebe_monitor_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_bebe_monitor_main, menu);
		return true;
	}

}
