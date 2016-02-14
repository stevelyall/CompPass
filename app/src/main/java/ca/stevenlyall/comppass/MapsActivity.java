package ca.stevenlyall.comppass;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	private final String TAG = "MapsActivity";
	LocationManager locationManager;
	LocationListener locationListener;
	String bestProvider;
	double lattitude;
	double longitude;
	private final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		// 1. choose the best location provider
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(false);
		bestProvider = locationManager.getBestProvider(criteria, true);

		Log.d("Location", "1- Recommended Location provider is " + bestProvider);

		// 4. getLastKnownLocation
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
				Log.d("Location", "2- A new location is found by the location provider ");
				UpdateLocation(location, "Location Changed");
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
			lattitude = location.getLatitude();
			longitude = location.getLongitude();
			Log.d("Location", "** " + state + " ** - Lattitue = " + lattitude + ", and Longitude = " + longitude);
		}
	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		setMapSettings();

		LatLng startLocation = new LatLng(lattitude, longitude);
		mMap.addMarker(new MarkerOptions().position(startLocation).title("Start Location"));
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 13), 2000, null);
	}

	private void setMapSettings() {
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mMap.setBuildingsEnabled(true);
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Log.d(TAG, "Location permissions not granted");
		}
		mMap.setMyLocationEnabled(true);
		UiSettings settings = mMap.getUiSettings();
		settings.setMapToolbarEnabled(false);
		settings.setMyLocationButtonEnabled(false);

	}
}
