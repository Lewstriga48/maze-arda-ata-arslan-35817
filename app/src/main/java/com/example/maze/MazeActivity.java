package com.example.maze;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MazeActivity extends AppCompatActivity {

    // Current room and combination
    private int currentRoom = 0;
    private String currentCombination = "";
    private String[] roomCombinations = new String[5]; // 5-room maze

    // UI elements
    private Button btnUp, btnDown, btnLeft, btnRight;
    private TextView tvRoom, tvHint;

    private boolean keyObtained = false; // Key obtained

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        // Define UI elements
        btnUp = findViewById(R.id.btnUp);
        btnDown = findViewById(R.id.btnDown);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        tvRoom = findViewById(R.id.tvRoom);
        tvHint = findViewById(R.id.tvHint);

        // Generate combinations
        generateRoomCombinations();

        // Show initial hint
        updateHint();

        // Listen to button clicks
        btnUp.setOnClickListener(v -> handleMove("U"));
        btnDown.setOnClickListener(v -> handleMove("D"));
        btnLeft.setOnClickListener(v -> handleMove("L"));
        btnRight.setOnClickListener(v -> handleMove("R"));

        currentCombination = "";
    }

    private void generateRoomCombinations() {
        Random random = new Random();
        String directions = "UDLR";
        for (int i = 0; i < roomCombinations.length - 1; i++) {
            int combinationLength = random.nextInt(4) + 1; // Length between 1 and 4
            StringBuilder combination = new StringBuilder();
            for (int j = 0; j < combinationLength; j++) {
                combination.append(directions.charAt(random.nextInt(directions.length())));
            }
            roomCombinations[i] = combination.toString();
        }
        roomCombinations[roomCombinations.length - 1] = ""; // No combination for the last room
    }

    private void handleMove(String direction) {
        if (keyObtained) {
            moveToNextRoom();
            return;
        }

        if (roomCombinations[currentRoom].contains(direction)) {
            currentCombination += direction;
            if (currentCombination.equals(roomCombinations[currentRoom])) {
                keyObtained = true;
                showToast("Congratulations! You obtained the key and unlocked the door.");
                currentCombination = "";
                tvHint.setText("Key obtained! Press any button to move to the next room.");
            }
        } else {
            showToast("Wrong direction! Try again.");
            currentCombination = ""; // Reset combination on wrong direction
        }
    }

    private void moveToNextRoom() {
        keyObtained = false;
        currentRoom++;
        if (currentRoom == roomCombinations.length - 1) {
            showToast("Congratulations! You won the game.");
            // End the game and go to ResultActivity
            endGame();
        } else {
            updateHint();
        }
    }

    private void updateHint() {
        tvRoom.setText("Room: " + (currentRoom + 1));
        if (!keyObtained) {
            tvHint.setText("Combination: " + roomCombinations[currentRoom]);
        }
        updateButtonStates();
    }

    private void updateButtonStates() {
        String nextCombination = roomCombinations[currentRoom];
        btnUp.setEnabled(nextCombination.contains("U") || keyObtained);
        btnUp.setBackgroundColor(nextCombination.contains("U") || keyObtained ? Color.GREEN : Color.RED);

        btnDown.setEnabled(nextCombination.contains("D") || keyObtained);
        btnDown.setBackgroundColor(nextCombination.contains("D") || keyObtained ? Color.GREEN : Color.RED);

        btnLeft.setEnabled(nextCombination.contains("L") || keyObtained);
        btnLeft.setBackgroundColor(nextCombination.contains("L") || keyObtained ? Color.GREEN : Color.RED);

        btnRight.setEnabled(nextCombination.contains("R") || keyObtained);
        btnRight.setBackgroundColor(nextCombination.contains("R") || keyObtained ? Color.GREEN : Color.RED);
    }

    private void endGame() {
        Intent intent = new Intent(MazeActivity.this, ResultActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
