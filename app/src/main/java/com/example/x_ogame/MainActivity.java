package com.example.x_ogame;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
        import android.graphics.Color;
        import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;


import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private static final Random RANDOM = new Random();
    int randomRowValue;
    int randomColValue;


    int firstPlayerWinningNumbers ,secondPlayerWinningNumbers ,drawNumbers;
    boolean isGameFinished ;
    String typeOfPlaying;
    Players[][] matrix = new Players[3][3];
    Players player , startPlayer ;
    Button buttonClicked ;
    String buttonClickedId ;
    short rowNumber ;
    short colNumber ;
    short numOfPlaying ;
    Players winningPlayer ;
    Button restartAndDraw;
    TextView firstWinnerTextView , secondWinnerTextView ,drawTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        restartAndDraw = findViewById(R.id.restart_and_draw);
        firstWinnerTextView = findViewById(R.id.first_winner_text_view);
        secondWinnerTextView = findViewById(R.id.second_winner_text_view);
        drawTextView = findViewById(R.id.draw_text_view);

        Bundle bundle = getIntent().getExtras();
        typeOfPlaying = bundle.getString("Type");


        firstPlayerWinningNumbers =0 ;
        secondPlayerWinningNumbers = 0 ;
        drawNumbers=0;
        startPlayer = Players.secondPlayer;
        restartNewGame();

    }

    private void restartNewGame() {
        isGameFinished = false;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                matrix[row][col]= null;
                // String id ="@+id/Bu"+row+"x"+col ;
            }
        }
        resetButton((Button)findViewById(R.id.bu0x0));
        resetButton((Button)findViewById(R.id.bu0x1));
        resetButton((Button)findViewById(R.id.bu0x2));
        resetButton((Button)findViewById(R.id.bu1x0));
        resetButton((Button)findViewById(R.id.bu1x1));
        resetButton((Button)findViewById(R.id.bu1x2));
        resetButton((Button)findViewById(R.id.bu2x0));
        resetButton((Button)findViewById(R.id.bu2x1));
        resetButton((Button)findViewById(R.id.bu2x2));
        numOfPlaying  = 0 ;
        player = (startPlayer == Players.secondPlayer ) ?
                    Players.firstPlayer :
                   Players.secondPlayer;
//
//        if(!typeOfPlaying.contains("Single") ){
//            player = (startPlayer == Players.secondPlayer ) ?
//                    Players.firstPlayer :
//                    Players.secondPlayer;
//            startPlayer = (startPlayer == Players.secondPlayer ) ?
//                    Players.firstPlayer :
//                    Players.secondPlayer;
//
//            AlertDialog.Builder AlertWhoToStart = new AlertDialog.Builder(this);
//            AlertWhoToStart.setTitle("Player !"+ player.to );
//            AlertWhoToStart.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//
//            AlertDialog alert = AlertWhoToStart.create();
//            alert.show();
//
//        }
    }

    private void resetButton(Button button) {
        button.setEnabled(true);
        button.setBackgroundColor(Color.GRAY);
        button.setText(" ");
    }

    public void buttonClicked(View view) {
        buttonClicked = (Button) view;
        buttonClickedId = view.getResources().getResourceEntryName(view.getId());
        rowNumber =Short.parseShort(String.valueOf(buttonClickedId.charAt(2)));
        colNumber =Short.parseShort(String.valueOf(buttonClickedId.charAt(4)));
        numOfPlaying++;
        buttonClicked.setEnabled(false);
      //  Log.i("cell" , " " +matrix[rowNumber][colNumber] + rowNumber +colNumber);

        switch (player){
            case firstPlayer:
                player = Players.secondPlayer;
                buttonClicked.setBackgroundColor(Color.BLUE);
                buttonClicked.setText("X");
                matrix[rowNumber][colNumber] = Players.firstPlayer;
                break;
            case secondPlayer:
                player = Players.firstPlayer;
                buttonClicked.setBackgroundColor(Color.GREEN);
                buttonClicked.setText("O");
                matrix[rowNumber][colNumber] = Players.secondPlayer;
                break;
        }


        if(numOfPlaying<3);
        else if (numOfPlaying <= 9){
            winningPlayer = evaluate();
            if(winningPlayer != null){
                switch (winningPlayer){
                    case firstPlayer:
                        gameFinished("Player 1 \"X\" is The Winner!", Players.firstPlayer);
                        break;
                    case secondPlayer:
                        gameFinished("Player 2 \"O\" is The Winner!", Players.secondPlayer);
                        break;
                }
            }

            if(numOfPlaying==9)
                gameFinished("No Winner!" ,null);
        }
        if(typeOfPlaying.contains("Single")  && player == Players.secondPlayer && numOfPlaying<9 && !isGameFinished){
            computer();
        }
    }


    private void gameFinished(String message, Players player) {
        if(isGameFinished)return;
        Toast.makeText(this , message, Toast.LENGTH_LONG).show();
        if(player == null )drawNumbers++;
        else if(player==Players.firstPlayer )firstPlayerWinningNumbers++;
        else if(player==Players.secondPlayer )secondPlayerWinningNumbers++;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over!");
        builder.setMessage(message +"\n\n"+"Start New Game");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                restartNewGame();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        firstWinnerTextView.setText("Player 1 \"X\" Wins: "+ firstPlayerWinningNumbers);
        secondWinnerTextView.setText("Player 2 \"O\" Wins: "+ secondPlayerWinningNumbers);
        drawTextView.setText("Draws: " + drawNumbers);
        isGameFinished = true;

        findViewById(R.id.bu0x0).setEnabled(false);
        findViewById(R.id.bu0x1).setEnabled(false);
        findViewById(R.id.bu0x2).setEnabled(false);
        findViewById(R.id.bu1x0).setEnabled(false);
        findViewById(R.id.bu1x1).setEnabled(false);
        findViewById(R.id.bu1x2).setEnabled(false);
        findViewById(R.id.bu2x0).setEnabled(false);
        findViewById(R.id.bu2x1).setEnabled(false);
        findViewById(R.id.bu2x2).setEnabled(false);
    }


    // This is the evaluation function
    Players evaluate()
    {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++) {
            if (matrix[row][0] == matrix[row][1] &&
                    matrix[row][1] == matrix[row][2])
            {
                if (matrix[row][0] == Players.firstPlayer) return Players.firstPlayer;
                else if (matrix[row][0] == Players.secondPlayer) return Players.secondPlayer;
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++)
        {
            if (matrix[0][col] == matrix[1][col] &&
                    matrix[1][col] == matrix[2][col])
            {
                if (matrix[0][col] == Players.firstPlayer) return Players.firstPlayer;
                else if (matrix[0][col] == Players.secondPlayer) return Players.secondPlayer;
            }
        }

        // Checking for Diagonals for X or O victory.
        if (matrix[0][0] == matrix[1][1] && matrix[1][1] == matrix[2][2]) {
            if (matrix[0][0] == Players.firstPlayer) return Players.firstPlayer;
            else if (matrix[0][0] == Players.secondPlayer) return Players.secondPlayer;
        }

        if (matrix[0][2] == matrix[1][1] && matrix[1][1] == matrix[2][0]) {
            if (matrix[0][2] == Players.firstPlayer) return Players.firstPlayer;
            else if (matrix[0][2] == Players.secondPlayer) return Players.secondPlayer;
        }
        // Else if none of them have won then return null
        return null;
    }

    public void restartAndDrawClicked(View view) {
        gameFinished("No Winner!" , null);
        restartNewGame();
    }

    public void restartOnlyClicked(View view) {
        restartNewGame();
    }


    public void computer() {
            do {
                Log.i("cell" , " ");
                randomRowValue = RANDOM.nextInt(3);
                randomColValue = RANDOM.nextInt(3);
                //Log.i("cell" , " " +matrix[randomRowValue][randomRowValue] +randomRowValue + randomColValue );

            } while (matrix[randomRowValue][randomColValue]  != null);


        if( randomRowValue == 0 && randomColValue == 0) buttonClicked(findViewById(R.id.bu0x0));
        else if(randomRowValue == 0 && randomColValue == 1) buttonClicked(findViewById(R.id.bu0x1));
        else if(randomRowValue == 0 && randomColValue == 2) buttonClicked(findViewById(R.id.bu0x2));
        else if(randomRowValue == 1 && randomColValue == 0) buttonClicked(findViewById(R.id.bu1x0));
        else if(randomRowValue == 1 && randomColValue == 1) buttonClicked(findViewById(R.id.bu1x1));
        else if(randomRowValue == 1 && randomColValue == 2) buttonClicked(findViewById(R.id.bu1x2));
        else if(randomRowValue == 2 && randomColValue == 0) buttonClicked(findViewById(R.id.bu2x0));
        else if(randomRowValue == 2 && randomColValue == 1) buttonClicked(findViewById(R.id.bu2x1));
        else if(randomRowValue == 2 && randomColValue == 2) buttonClicked(findViewById(R.id.bu2x2));

    }
}
