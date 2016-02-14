package ca.stevenlyall.comppass;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;


public class SplashScreenActivity extends Activity {

	private static final int SPLASH_TIMEOUT = 1800;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		splashScreenOnTimer();
	}

	/**
	 * Runs a timer to show splash image
	 */
	private void splashScreenOnTimer() {
		// timer
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				proceedToMainActivity();
			}
		}, SPLASH_TIMEOUT);

	}

	private void proceedToMainActivity() {
		Intent startMain = new Intent(getBaseContext(), MainActivity.class);
		startActivity(startMain);
		finish();
	}

}
