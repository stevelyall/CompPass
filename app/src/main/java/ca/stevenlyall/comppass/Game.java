package ca.stevenlyall.comppass;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Game {
	private static Game instance;
	private final String TAG = "GAME";
	private ArrayList<GameLocation> locations;

	private Game() {
	}

	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}

	public ArrayList<GameLocation> getLocations() {
		return locations;
	}

	public void setLocations(JSONArray locationsJSON) throws Exception {
		Log.d(TAG, "setLocations JSON Object:");
		Log.d(TAG, locationsJSON.toString());

		int numLocs = locationsJSON.length()/3; // 3 data points for each location
		this.locations = new ArrayList<>();
		for (int i =0; i<numLocs; i++) {
			locations.add(new GameLocation(i+1));
		}

		for (int i = 0; i<locations.size(); i++) {
			try {
				GameLocation loc = locations.get(0);

				JSONObject maxJSON = (JSONObject) locationsJSON.get(i+1);
				JSONObject minJSON = (JSONObject) locationsJSON.get(i+2);
				double maxLat = Double.parseDouble(maxJSON.getString("latitude"));
				double minLat = Double.parseDouble(minJSON.getString("latitude"));
				double maxLong = Double.parseDouble(maxJSON.getString("longitude"));
				double minLong = Double.parseDouble(minJSON.getString("longitude"));

				loc.setTopLeft(new LatLng(maxLat, minLong));
				loc.setBottomLeft(new LatLng(minLat, minLong));
				loc.setTopRight(new LatLng(maxLat, maxLong));
				loc.setBottomRight(new LatLng(minLat, maxLong));

				locations.set(i, loc);
				Log.d(TAG, "setLocations: " + loc.getTopLeft() + " " + loc.getTopRight());
				Log.d(TAG, "setLocations: " + loc.getBottomLeft() + " " + loc.getBottomRight());

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


}
