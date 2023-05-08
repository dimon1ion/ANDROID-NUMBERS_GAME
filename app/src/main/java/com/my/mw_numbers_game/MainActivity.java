package com.my.mw_numbers_game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;


public class MainActivity extends AppCompatActivity {

    SortedSet<Integer> numbers;
    Handler timer = new Handler();
    ProgressBar progressBar;
    boolean timerStarted = false;
    boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numbers = new TreeSet<>();
        Random random = new Random();
        int max = 100;
        progressBar = findViewById(R.id.progress);

        GridLayout gridLayout = findViewById(R.id.grid_main);
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++) {
                Button button = new Button(this);
                int number = random.nextInt(max);
                while(!numbers.add(number)){
                    if (number >= max){
                        number = 0;
                    } else {
                        number++;
                    }
                }
                button.setText(Integer.toString(number));
                button.setBackgroundColor(Color.parseColor("#00BCD4"));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                params.height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics());
                params.columnSpec = GridLayout.spec(i, 1, 1);
                params.rowSpec = GridLayout.spec(j, 1, 1);
                params.bottomMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                params.rightMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                params.topMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                params.leftMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                button.setLayoutParams(params);
                button.setOnClickListener(view -> {
                    if (gameOver || numbers.isEmpty()){
                        return;
                    }
                    if (!timerStarted){
                        timer.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (gameOver){
                                    return;
                                }
                                timerStarted = true;
                                progressBar.incrementProgressBy(1);

                                if (progressBar.getProgress() == progressBar.getMax()){
                                    Objects.requireNonNull(getSupportActionBar())
                                            .setTitle("You lose");
                                    gameOver = true;
                                    return;
                                }
                                increment();
                                timer.postDelayed(this, 1000);
                            }
                        }, 1000);
                    }
                    if (Integer.parseInt(button.getText().toString()) != numbers.first()){
                        progressBar.incrementProgressBy(1);
                        increment();
                        return;
                    }
                    numbers.remove(numbers.first());
                    button.setBackgroundColor(Color.parseColor("#4CAF50"));
                    button.setEnabled(false);
                    if (numbers.isEmpty()){
                        gameOver = true;
                        Objects.requireNonNull(getSupportActionBar())
                                .setTitle("You won!");
                    }
                });
                gridLayout.addView(button);
            }
        }


    }
    public void increment(){
        Objects.requireNonNull(getSupportActionBar())
                .setTitle("Timer: 00:" + (progressBar.getProgress() < 10 ? "0" + progressBar.getProgress() : progressBar.getProgress()));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("game", "h");
        progressBar.setProgress(progressBar.getMin());

    }
}