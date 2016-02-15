package ca.stevenlyall.comppass;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Game {
	private static Game instance;
	private final String TAG = "GAME";
	private ArrayList<GameLocation> locations;
	private ArrayList<GameLocation> reached;
	private GameLocation currentTarget;

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
		this.reached = new ArrayList<>();
		this.locations = new ArrayList<>();
		for (int i =0; i<numLocs; i++) {
			locations.add(new GameLocation(i+1));
		}

		for (int i = 0; i < numLocs; i++) {
			try {

				JSONObject maxJSON = (JSONObject) locationsJSON.get(i+1);
				JSONObject minJSON = (JSONObject) locationsJSON.get(i+2);
				double maxLat = Double.parseDouble(maxJSON.getString("latitude"));
				double minLat = Double.parseDouble(minJSON.getString("latitude"));
				double maxLong = Double.parseDouble(maxJSON.getString("longitude"));
				double minLong = Double.parseDouble(minJSON.getString("longitude"));

				GameLocation loc = new GameLocation(i, maxLat, maxLong, minLat, minLong);
				Log.d(TAG, "setLocations: " + loc);
				locations.set(i, loc);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void playLocationReachedSound(Context ctx) {
		MediaPlayer mediaPlayer = MediaPlayer.create(ctx, R.raw.beep);
		mediaPlayer.start();
	}

	public PolygonOptions init() {
		currentTarget = locations.get(0);
		Log.d(TAG, "init: first location " + currentTarget);
		ArrayList<LatLng> points = getPointsForLocation(currentTarget);
		PolygonOptions rectOptions = new PolygonOptions().addAll(points);
		return rectOptions;
	}

	private ArrayList<LatLng> getPointsForLocation(GameLocation location) {
		ArrayList<LatLng> points = new ArrayList<>();
		points.add(location.getTopLeft());
		points.add(location.getBottomLeft());
		points.add(location.getBottomRight());
		points.add(location.getTopRight());
		return points;
	}

	//get next location and update polygon
	public ArrayList<LatLng> getNextLocation() {
		reached.add(currentTarget);
		currentTarget = locations.get(currentTarget.getLocNum() + 1);
		if (currentTarget == null) {
			allLocationsReached();
		}
		Log.d(TAG, "getNextLocation: " + currentTarget);
		return getPointsForLocation(currentTarget);
	}

	private void allLocationsReached() {
		// TODO finish, determine and send score
	}

	public boolean isLocInsideTarget(double latitude, double longitude) {
		if (currentTarget == null) {
			return false;
		}
		return (latitude < currentTarget.getMaxLat() && latitude > currentTarget.getMinLat()) && (longitude < currentTarget.getMaxLong() && longitude > currentTarget.getMinLong());
	}
}
