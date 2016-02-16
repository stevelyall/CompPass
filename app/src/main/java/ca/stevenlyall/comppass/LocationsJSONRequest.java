package ca.stevenlyall.comppass;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationsJSONRequest extends AsyncTask<String, String, String> {

	private final String url = "http://stevenlyall.ca/comppass/get_locations.php";
		private final String TAG = "LocationsJSONRequest";

	@Override
	protected String doInBackground(String... params) {
		final OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url(url)
					.build();

		Response response = null;
		try {
			response = client.newCall(request).execute();
			String respStr = response.body().string();
			Log.d(TAG, respStr);
			Game game = Game.getInstance();
			game.setUpLocations(new JSONArray(respStr));
			return respStr;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

