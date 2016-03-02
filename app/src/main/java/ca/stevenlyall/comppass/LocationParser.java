package ca.stevenlyall.comppass;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by stevenlyall on 16-02-15.
 */
public class LocationParser {

	private final String TAG = "LocationParser";
	private JSONArray locationsJSON;

	public LocationParser(JSONArray locationsJSON) {
		this.locationsJSON = locationsJSON;
	}

	public ArrayList<GameLocation> setLocations() throws Exception {
		ArrayList<GameLocation> locations = new ArrayList<>();
		Log.d(TAG, "setLocations JSON Object:");
		Log.d(TAG, locationsJSON.toString());

		ArrayList<ArrayList<JSONObject>> list = new ArrayList<>();
		String title, lastTitle = "";

		int j = 0;
		for (int i = 0; i < locationsJSON.length(); i++) {
			JSONObject obj = locationsJSON.getJSONObject(i);
			title = obj.getString("title");

			if (lastTitle.equals("")) {
				list.add(new ArrayList<JSONObject>());
			} else if (!title.equals(lastTitle)) {
				j++;
				list.add(new ArrayList<JSONObject>());

			}
			lastTitle = title;

			ArrayList<JSONObject> locList = list.get(j);
			locList.add(obj);
		}

		int locNum = 0;
		for (ArrayList<JSONObject> l : list) {
			double maxLat = 0, maxLong = 0, minLat = 0, minLong = 0;
			for (JSONObject o : l) {
				// get max
				if (o.getString("description").equals("Max")) {
					maxLat = Double.parseDouble(o.getString("latitude"));
					maxLong = Double.parseDouble(o.getString("longitude"));
				}
				if (o.getString("description").equals("Min")) {
					minLat = Double.parseDouble(o.getString("latitude"));
					minLong = Double.parseDouble(o.getString("longitude"));
				}
			}
			GameLocation location = new GameLocation(locNum, maxLat, maxLong, minLat, minLong);
			locations.add(location);
			locNum++;
		}
		Log.d(TAG, "setLocations: " + locations);
		return locations;
	}

}
