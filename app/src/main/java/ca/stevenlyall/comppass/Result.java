package ca.stevenlyall.comppass;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by stevenlyall on 16-02-15.
 */
public class Result {

	private int numReached = 0;
	//private GameLocation[] reached;
	//private double[] timesToLocations;
	private String playerName;
	private long totalTime;
	private String dateCompleted;

	public Result(int numLocations) {
		//	reached = new GameLocation[numLocations];
		//	timesToLocations = new double[numLocations];
	}

	public void setDateCompleted(Date date) {
		this.dateCompleted = new SimpleDateFormat("MMM d, yyyy HH:mm z").format(date);
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public int getNumberLocationsReached() {
		return numReached;
	}

	public void locationReached(GameLocation location, long timeReached) {
//		reached[numReached] = location;
//		timesToLocations[numReached] = timeReached;
		numReached++;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String toJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("playerName", playerName);
			String time = new SimpleDateFormat("mm:ss").format(new Date(totalTime));
			obj.put("totalTime", time);
			obj.put("date", dateCompleted);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
}
