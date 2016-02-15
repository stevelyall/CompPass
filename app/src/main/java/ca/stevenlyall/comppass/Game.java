package ca.stevenlyall.comppass;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Game {
	private static Game instance;
	private final String TAG = "GAME";
	private ArrayList<GameLocation> locations;
	private GameLocation currentTarget;

	private Polygon currentTargetRectangle;

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

		for (int i = 0; i < numLocs; i++) {
			try {

				JSONObject maxJSON = (JSONObject) locationsJSON.get(i+1);
				JSONObject minJSON = (JSONObject) locationsJSON.get(i+2);
				double maxLat = Double.parseDouble(maxJSON.getString("latitude"));
				double minLat = Double.parseDouble(minJSON.getString("latitude"));
				double maxLong = Double.parseDouble(maxJSON.getString("longitude"));
				double minLong = Double.parseDouble(minJSON.getString("longitude"));

				GameLocation loc = new GameLocation(i, maxLat, maxLong, minLat, minLong);

				locations.set(i, loc);
				Log.d(TAG, "setLocations: " + loc.getTopLeft() + " " + loc.getTopRight());
				Log.d(TAG, "setLocations: " + loc.getBottomLeft() + " " + loc.getBottomRight());

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public Polygon init(GoogleMap map) {
		currentTarget = locations.get(0);
		PolygonOptions rectOptions = new PolygonOptions().add(currentTarget.getTopLeft()).add(currentTarget.getBottomLeft()).add(currentTarget.getBottomRight()).add(currentTarget.getTopRight()).strokeColor(Color.YELLOW);
		currentTargetRectangle = map.addPolygon(rectOptions);
		return currentTargetRectangle;
	}

	//get next location and update polygon
	public void getNextLocation() {
		currentTarget = locations.get(currentTarget.getLocNum() + 1);
		ArrayList<LatLng> points = new ArrayList<>();
		points.add(currentTarget.getTopLeft());
		points.add(currentTarget.getBottomLeft());
		points.add(currentTarget.getBottomRight());
		points.add(currentTarget.getTopRight());
		currentTargetRectangle.setPoints(points);
	}

	// TODO call when location in polygon
	public GameLocation getCurrentTarget() {
		return currentTarget;
	}

	public boolean isLocInsideTarget(double lattitude, double longitude) {
		return (lattitude < currentTarget.getMaxLat() && lattitude > currentTarget.getMinLat()) && (longitude < currentTarget.getMaxLong() && longitude > currentTarget.getMinLong());
	}
}
