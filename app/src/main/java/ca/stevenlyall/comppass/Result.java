package ca.stevenlyall.comppass;

/**
 * Created by stevenlyall on 16-02-15.
 */
public class Result {

	private int numReached = 0;
	private GameLocation[] reached;
	private double[] timesToLocations;
	private double totalTime;

	public Result(int numLocations) {
		reached = new GameLocation[numLocations];
		timesToLocations = new double[numLocations];
	}

	public int getNumberLocationsReached() {
		return numReached;
	}

	public void locationReached(GameLocation location, double timeReached) {
		reached[numReached] = location;
		timesToLocations[numReached] = timeReached;
		numReached++;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}


}
