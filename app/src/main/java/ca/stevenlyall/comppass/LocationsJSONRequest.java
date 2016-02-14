package ca.stevenlyall.comppass;

import android.os.AsyncTask;
import android.util.Log;


import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationsJSONRequest extends AsyncTask<String, String, String>{

		private final String url = "http://stevenlyall.ca/3160project/get_locations.php";


	@Override
	protected String doInBackground(String... params) {
		final OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url(url)
					.build();

		Response response = null;
		try {
			response = client.newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
		Log.d("Request", s);
	}
}

