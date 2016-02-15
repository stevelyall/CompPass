package ca.stevenlyall.comppass;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;

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

	public void setUpLocations(final JSONArray locationsJSON) throws Exception {
		reached = new ArrayList<GameLocation>();

		LocationParser parser = new LocationParser(locationsJSON);
		locations = parser.setLocations();

	}

	public ArrayList<GameLocation> getLocations() {
		return locations;
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
		// check for end of list
		if (currentTarget.getLocNum() + 1 >= locations.size() - 1) {
			allLocationsReached();
		}
		currentTarget = locations.get(currentTarget.getLocNum() + 1);

		Log.d(TAG, "getNextLocation: " + currentTarget);
		return getPointsForLocation(currentTarget);
	}

	private void allLocationsReached() {
		Log.d(TAG, "allLocationsReached: " + reached.size() + " locations visited");
		// TODO play sound, new activity
		// TODO finish, determine and send score
	}

	public boolean isLocInsideTarget(double latitude, double longitude) {
		if (currentTarget == null) {
			return false;
		}
		return (latitude < currentTarget.getMaxLat() && latitude > currentTarget.getMinLat()) && (longitude < currentTarget.getMaxLong() && longitude > currentTarget.getMinLong());
	}
}
