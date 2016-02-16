package ca.stevenlyall.comppass;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by stevenlyall on 16-01-13.
 */
public class SendJSON extends AsyncTask<String, String, String> {

	private final String TAG = "SendJSON";
	private final String URL = "http://stevenlyall.ca/comppass/post_results.php";

	String strFrileContents = null;
	Context ctx;

	@Override
	protected String doInBackground(String... params) {
		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;

		try {
			URL url = new URL(URL);

			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(1000);
			conn.setConnectTimeout(1000);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setFixedLengthStreamingMode(params[0].getBytes().length);

			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

			conn.connect();

			os = new BufferedOutputStream(conn.getOutputStream());
			os.write(params[0].getBytes());

			os.flush();

			is = conn.getInputStream();

			BufferedInputStream in = new BufferedInputStream(is);

			byte[] contents = new byte[1024];

			int bytesRead = 0;

			while ((bytesRead = in.read(contents)) != -1) {
				strFrileContents = new String(contents, 0, bytesRead);
			}
			Log.d(TAG, "Input Stream = " + strFrileContents);


		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// clean up
			try {
				if (os != null && is != null) {
					os.close();
					is.close();
				}

				if (is == null) {
					Log.d(TAG, "is null pointer");
				}

				if (os == null) {
					Log.d(TAG, "os null pointer");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			conn.disconnect();
			if (strFrileContents != null) {
				//publishProgress("Data sent to server");
				Log.d(TAG, "Data sent to server");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//publishProgress("Could not connect to server");
			Log.d(TAG, "Could not connect to server");
		}

		return "";
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		String msg = values[0];
	}
}