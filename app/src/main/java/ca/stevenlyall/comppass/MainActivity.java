package ca.stevenlyall.comppass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
	private final String TAG = "MainActivity";
	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button startGameButton = (Button) findViewById(R.id.startGameBtn);
		startGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent startGame = new Intent(getApplicationContext(), MapsActivity.class);
				startActivity(startGame);
				finish();
			}
		});

		game = Game.getInstance();
		Log.d(TAG, "onCreate: locations loaded: " + game.getLocations().size());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
