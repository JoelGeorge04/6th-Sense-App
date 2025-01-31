package com.joelgeo.okok;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {

    private TextView teamScoreTextView;
    private EditText scoreInputEditText;
    private AppCompatButton addScoreButton, resetScoreButton;
    private LinearLayout scoreHistoryLayout;
    private int totalScore = 0;
    private ArrayList<Integer> scoreHistory = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ScorePrefs";
    private static final String SCORE_KEY = "TotalScore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Initialize views
        teamScoreTextView = findViewById(R.id.teamScore);
        scoreInputEditText = findViewById(R.id.scoreInput);
        addScoreButton = findViewById(R.id.addScoreButton);
        resetScoreButton = findViewById(R.id.resetScoreButton);
        scoreHistoryLayout = findViewById(R.id.scoreHistoryLayout);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        totalScore = sharedPreferences.getInt(SCORE_KEY, 0); // Load saved score
        teamScoreTextView.setText("Team Score: " + totalScore); // Display saved score

        // Add score logic
        addScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scoreInput = scoreInputEditText.getText().toString();
                if (!TextUtils.isEmpty(scoreInput)) {
                    int score = Integer.parseInt(scoreInput);
                    totalScore += score;
                    scoreHistory.add(score); // Add score to history
                    saveTotalScore(); // Save the updated total score

                    // Update total score display
                    teamScoreTextView.setText("Team Score: " + totalScore);
                    displayScoreInHistory(score);

                    scoreInputEditText.setText(""); // Clear input field
                } else {
                    Toast.makeText(MainActivity3.this, "Please enter a score", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Reset score logic
        resetScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalScore = 0;
                scoreHistory.clear(); // Clear history
                saveTotalScore(); // Save reset score
                teamScoreTextView.setText("Team Score: 0");
                scoreHistoryLayout.removeAllViews(); // Clear history from UI
                Toast.makeText(MainActivity3.this, "Score reset", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayScoreInHistory(int score) {
        LinearLayout scoreItemLayout = new LinearLayout(this);
        scoreItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        scoreItemLayout.setPadding(10, 10, 10, 10);

        TextView scoreTextView = new TextView(this);
        scoreTextView.setText("Score : " + score);
        scoreTextView.setTextSize(18);
        scoreTextView.setPadding(10, 10, 10, 10);

        AppCompatButton deleteButton = new AppCompatButton(this);
        deleteButton.setText("delete");
        deleteButton.setPadding(20, 10, 20, 10);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreHistory.remove(Integer.valueOf(score));
                totalScore -= score;
                saveTotalScore(); // Save the updated total score
                teamScoreTextView.setText("Team Score: " + totalScore);
                scoreHistoryLayout.removeView(scoreItemLayout);
            }
        });

        scoreItemLayout.addView(scoreTextView);
        scoreItemLayout.addView(deleteButton);
        scoreHistoryLayout.addView(scoreItemLayout);
    }

    private void saveTotalScore() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SCORE_KEY, totalScore);
        editor.apply();
    }
}
