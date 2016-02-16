package ca.stevenlyall.comppass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ResultsActivity extends AppCompatActivity {

	private TextView numLocationsReachedTextView, totalTimeTextView, playerNameTextView;
	private Button sendButton;
	private Result result;
	private boolean sent = false;
	private String TAG = "ResultsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);


		numLocationsReachedTextView = (TextView) findViewById(R.id.numLocationsReachedTextView);
		totalTimeTextView = (TextView) findViewById(R.id.totalTimeTextView);
		playerNameTextView = (TextView) findViewById(R.id.playerNameTextView);

		Game game = Game.getInstance();
		result = game.getResult();
		numLocationsReachedTextView.setText(result.getNumberLocationsReached() + "/" + game.getLocations().size());

		long time = result.getTotalTime();

		String formatted = new SimpleDateFormat("mm:ss").format(new Date(time));
		totalTimeTextView.setText(formatted);

		playerNameTextView.setText(result.getPlayerName());

		sendButton = (Button) findViewById(R.id.button);
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!sent) {
					sendResults();
				}
			}
		});
	}

	private void sendResults() {
		String resultsJSON = result.toJSON();
		Log.d(TAG, "sendResults: result to send:" + resultsJSON);
		SendJSON sendJSON = new SendJSON();
		sendJSON.execute(resultsJSON);
		sent = true;
	}
}
