package ca.stevenlyall.comppass;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by stevenlyall on 16-02-14.
 */
public class GameLocation {

	private int locNum;

	private LatLng topLeft, topRight, bottomLeft, bottomRight;

	private double maxLat, maxLong, minLat, minLong;

	public GameLocation(int locNum) {
		this.locNum = locNum;
	}

	public GameLocation(int locNum, double maxLat, double maxLong, double minLat, double minLong) {
		this.locNum = locNum;
		this.maxLat = maxLat;
		this.maxLong = maxLong;
		this.minLat = minLat;
		this.minLong = minLong;

		topLeft = new LatLng(maxLat, minLong);
		bottomLeft = new LatLng(minLat, minLong);
		topRight = new LatLng(maxLat, maxLong);
		bottomRight = new LatLng(minLat, maxLong);

	}

	public LatLng getBottomLeft() {
		return bottomLeft;
	}

	public LatLng getBottomRight() {
		return bottomRight;
	}

	public int getLocNum() {
		return locNum;
	}

	public LatLng getTopLeft() {
		return topLeft;
	}

	public LatLng getTopRight() {
		return topRight;
	}

	public double getMaxLat() {
		return maxLat;
	}

	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	public double getMaxLong() {
		return maxLong;
	}

	public void setMaxLong(double maxLong) {
		this.maxLong = maxLong;
	}

	public double getMinLat() {
		return minLat;
	}

	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}

	public double getMinLong() {
		return minLong;
	}

	public void setMinLong(double minLong) {
		this.minLong = minLong;
	}

	public ArrayList<LatLng> getPoints() {
		ArrayList<LatLng> points = new ArrayList<>();
		points.add(topLeft);
		points.add(bottomLeft);
		points.add(bottomRight);
		points.add(topRight);
		return points;
	}

	@Override
	public String toString() {
		return "GameLocation{" +
				"locNum=" + locNum +
				",topLeft=" + topLeft +
				", topRight=" + topRight +
				", bottomLeft=" + bottomLeft +
				", bottomRight=" + bottomRight +
				'}';
	}
}
