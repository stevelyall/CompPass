package ca.stevenlyall.comppass;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class SplashScreenActivity extends Activity {

	private static final int SPLASH_TIMEOUT = 1800;
	private final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getLocationsFromServer();
	}



	private void getLocationsFromServer() {
		ConnectionChecker checker = new ConnectionChecker(SplashScreenActivity.this);
		if (checker.isNetworkConnected()) {
			LocationsJSONRequest request = new LocationsJSONRequest();
			request.execute();

			// check for permissions
			checkForLocationPermissions();
		} else {
			checker.showNoConnectionMessage();
		}
	}



	private void checkForLocationPermissions() {
		// check for permissions
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// not granted
			// should explanation be shown?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
				//explain why permission is needed and request
				explainAndAskForPermissions();
			} else {
				//request
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
			}
			return;
		}
		// granted
		splashScreenOnTimer();
	}

	private void explainAndAskForPermissions() {
		AlertDialog info = new AlertDialog.Builder(SplashScreenActivity.this).create();
		info.setTitle("Important");
		info.setMessage("CompPass requires access to your device's location in order to play the game.");
		info.setButton(AlertDialog.BUTTON_POSITIVE, "Continue", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
			}
		});
		info.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case PERMISSIONS_REQUEST_FINE_LOCATION: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// granted
					Log.d("CompPass start", "Permissions granted by user");
					splashScreenOnTimer();
				} else {
					// was denied
					Log.d("CompPass start", "Permissions were denied by user");
					AlertDialog info = new AlertDialog.Builder(SplashScreenActivity.this).create();
					info.setTitle("Important");
					info.setMessage("CompPass requires access to your device's location in order to play the game.");
					info.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
					info.show();
				}
				return;
			}
		}
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
