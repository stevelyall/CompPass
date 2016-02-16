package ca.stevenlyall.comppass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Chronometer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	private final String TAG = "MapsActivity";
	LocationManager locationManager;
	LocationListener locationListener;
	String bestProvider;

	Game game;
	Polygon polygon;

	Chronometer chronometer;

	double latitude;
	double longitude;
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		game = Game.getInstance();

		// TODO test results activity
		allLocationsReached();

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// choose the best location provider
		findBestLocationProvider();
		Log.d("Location", "1- Recommended Location provider is " + bestProvider);

		// last known location
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Log.d(TAG, "Location permissions not granted");
		}
		Location LastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
		UpdateLocation(LastKnownLocation, "Last Known Location");

		// 2. Define a listener that responds to location updates
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				UpdateLocation(location, "Location Changed");

				if (game.isLocInsideTarget(latitude, longitude)) {
					onTargetLocationReached();
				}
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}
		};

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		chronometer = (Chronometer) mapFragment.getView().findViewById(R.id.chronometer);
		startTimer();
	}

	private void startTimer() {
		chronometer.setFormat("00:%s");
		chronometer.setBackgroundColor(Color.WHITE);
		Log.d(TAG, "startTimer: timer started at " + chronometer.getBase());
		chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				long time = SystemClock.elapsedRealtime() - chronometer.getBase();
				game.setTimeElapsed(time);
			}
		});
		chronometer.start();
	}
	private void findBestLocationProvider() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(false);
		bestProvider = locationManager.getBestProvider(criteria, true);
	}

	private void onTargetLocationReached() {
		Log.d("Location", "Target location reached");
		game.playLocationReachedSound(getBaseContext());
		GameLocation loc = game.nextLocation();
		if (loc == null && game.getLocations().size() == game.numLocationsReached()) {
			allLocationsReached();
			chronometer.stop();
		}
		ArrayList<LatLng> points = loc.getPoints();
		polygon.setPoints(points);
	}

	private void allLocationsReached() {
		Log.d(TAG, "allLocationsReached: " + game.numLocationsReached() + " locations reached out of " + game.getLocations().size());

		Intent resultsActivity = new Intent(this, ResultsActivity.class);
		startActivity(resultsActivity);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Log.d(TAG, "Location permissions not granted");
		}
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);


	}

	public void UpdateLocation(Location location, String state) {
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			Log.d("Location", "** " + state + " ** - Lattitue = " + latitude + ", and Longitude = " + longitude);
		}
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		setMapSettings();

		LatLng startLocation = new LatLng(latitude, longitude);
		map.addMarker(new MarkerOptions().position(startLocation).title("Start Location"));
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 13), 2000, null);

		GameLocation loc = game.getTargetLocation();
		PolygonOptions rect = new PolygonOptions().addAll(loc.getPoints());
		rect.strokeColor(Color.YELLOW);
		polygon = map.addPolygon(rect);
	}

	private void setMapSettings() {
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.setBuildingsEnabled(true);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Log.d(TAG, "Location permissions not granted");
		}
		map.setMyLocationEnabled(true);
		UiSettings settings = map.getUiSettings();
		settings.setMapToolbarEnabled(false);
		settings.setMyLocationButtonEnabled(false);

	}

}
