package ca.stevenlyall.comppass;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
	private final String TAG = "MainActivity";
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editText = (EditText) findViewById(R.id.nameEditText);

		Button startGameButton = (Button) findViewById(R.id.startGameBtn);
		startGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editText.getText().length() == 0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
					builder.setTitle("No Player Name Set").setMessage("You must enter a player name");
					builder.create().show();
				} else {
					Intent startGame = new Intent(getApplicationContext(), MapsActivity.class);
					startActivity(startGame);
					finish();
				}
			}
		});

		Game game = Game.getInstance();
		game.setPlayerName(editText.getText().toString());

		Log.d(TAG, "onCreate: locations loaded: " + game.getLocations().size());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
