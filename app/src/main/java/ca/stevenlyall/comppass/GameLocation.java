package ca.stevenlyall.comppass;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by stevenlyall on 16-02-14.
 */
public class GameLocation {
	private int locNum;
	private LatLng topLeft;
	private LatLng topRight;
	private LatLng bottomLeft;
	private LatLng bottomRight;

	public GameLocation(int locNum) {
		this.locNum = locNum;
	}

	public GameLocation(int locNum, LatLng bottomLeft, LatLng bottomRight, LatLng topLeft, LatLng topRight) {
		this.bottomLeft = bottomLeft;
		this.bottomRight = bottomRight;
		this.locNum = locNum;
		this.topLeft = topLeft;
		this.topRight = topRight;
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

	public void setBottomLeft(LatLng bottomLeft) {
		this.bottomLeft = bottomLeft;
	}

	public void setBottomRight(LatLng bottomRight) {
		this.bottomRight = bottomRight;
	}

	public void setTopLeft(LatLng topLeft) {
		this.topLeft = topLeft;
	}

	public void setTopRight(LatLng topRight) {
		this.topRight = topRight;
	}
}
