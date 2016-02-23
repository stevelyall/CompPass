package ca.stevenlyall.comppass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by stevenlyall on 16-02-22.
 */
public class ConnectionChecker {

	Context context;

	public ConnectionChecker(Context context) {
		this.context = context;
	}

	public boolean isNetworkConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	public void showNoConnectionMessage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			}
		});
		builder.setTitle("No Network Connection").setMessage("Please connect your device to a WiFi or cellular network and try again");

		builder.create().show();
	}
}
