package ca.stevenlyall.comppass;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

public class Game {
	private static Game instance;
	private final String TAG = "GAME";
	private int currentTarget;
	private ArrayList<GameLocation> locations;
	private String playerName;
	private Result result;
	private long timeElapsed;

	private Game() {
	}

	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}

	public long getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(long timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	public Result getResult() {
		return result;
	}

	public int numLocationsReached() {
		return result.getNumberLocationsReached();
	}

	public void setUpLocations(final JSONArray locationsJSON) throws Exception {
		LocationParser parser = new LocationParser(locationsJSON);
		locations = parser.setLocations();
		result = new Result(locations.size());

		currentTarget = 0;
	}

	public ArrayList<GameLocation> getLocations() {
		return locations;
	}

	public void playLocationReachedSound(Context ctx) {
		MediaPlayer mediaPlayer = MediaPlayer.create(ctx, R.raw.beep);
		mediaPlayer.start();
	}

	public void playTickSound(Context ctx) {
		MediaPlayer mediaPlayer = MediaPlayer.create(ctx, R.raw.tick);
		mediaPlayer.start();
	}

	public GameLocation getTargetLocation() {
		return locations.get(currentTarget);
	}

	//get next location and update polygon
	public GameLocation nextLocation() {
		result.locationReached(locations.get(currentTarget), timeElapsed);

		// check for end of list
		if (currentTarget + 1 == locations.size()) {
			return null;
		}

		currentTarget++;
		Log.d(TAG, "nextLocation: " + locations.get(currentTarget));
		return getTargetLocation();

	}

	public boolean isLocInsideTarget(double latitude, double longitude) {
		if (locations.get(currentTarget) == null) {
			return false;
		}
		return (latitude < locations.get(currentTarget).getMaxLat() && latitude > locations.get(currentTarget).getMinLat()) && (longitude < locations.get(currentTarget).getMaxLong() && longitude > locations.get(currentTarget).getMinLong());
	}

	public void finish() {
		result.setTotalTime(timeElapsed);
		result.setPlayerName(playerName);
		result.setDateCompleted(new Date());
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
