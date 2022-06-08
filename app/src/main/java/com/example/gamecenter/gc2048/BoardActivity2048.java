package com.example.gamecenter.gc2048;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.Login.DataBase.DBHelper;
import com.example.gamecenter.R;
import com.example.gamecenter.DataListActivityScore;
import com.example.gamecenter.Score;


public class BoardActivity2048 extends AppCompatActivity implements GameStateChangedListener {

    protected TextView scoreTextView;
    protected TextView highScoreTextView;
    private GameBoard2048 board;
    private Game2048 game;
    private int highScore;
    private int score;
    private boolean wonInformed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_2048);

        this.board = (GameBoard2048) this.findViewById(R.id.gameBoard);
        this.scoreTextView = (TextView) this.findViewById(R.id.currentScore);
        this.highScoreTextView = (TextView) this.findViewById(R.id.highScore);
        this.wonInformed = false;
        this.restart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.confirmLeaveDialog();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * Devuelve la high score actual de las que están guardadas.
     * @return High score.
     */
    protected int getHighScore() {
        SharedPreferences preferences = this.getSharedPreferences(this.getString(R.string.preference_key), Context.MODE_PRIVATE);
        return preferences.getInt(this.getString(R.string.high_score_key), 0);
    }

    /**
     * Guarda la high score.
     * @param score High score para guardar.
     */
    protected void saveHighScore(int score) {
        SharedPreferences preferences = this.getSharedPreferences(this.getString(R.string.preference_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String userName = preferences.getString("userName", "Wrong.User.Name");
        int puntuation = Integer.parseInt(scoreTextView.getText().toString());
        DBHelper db = new DBHelper(this);
        db.addScore(new Score(puntuation, "2048",userName));
        editor.putInt(this.getString(R.string.high_score_key), score);
        editor.apply();
    }

    /**
     * Reinicia el juego.
     */
    protected void restart() {
        this.game = new Game2048(this.board.getGestureDetector(), this.board, this);
        this.game.start();
        this.highScore = this.getHighScore();
        this.highScoreTextView.setText(this.highScore + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_board_2048, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_restart:
                this.confirmRestartDialog();
                break;
            default:
                this.confirmLeaveDialog();
                break;
        }

        return true;
    }

    /**
     * Muestra un diálogo de confirmación al querer salir del juego.
     */
    protected void confirmLeaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Leave the game");
        builder.setMessage("Do you really want to leave this game?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                checkSaveHighScore();
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Muestra un diálogo de confirmación al querer reiniciar el juego.
     */
    protected void confirmRestartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Restart the game");
        builder.setMessage("Do you really want to restart this game?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                checkSaveHighScore();
                restart();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Muestra un diálogo confirmando que has ganado y pregunta si quieres volver a jugar.
     */
    private void confirmWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("You won!");
        builder.setMessage("Congratulations, you won! Do you want to continue?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                checkSaveHighScore();
                finish();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Muestra un diálogo confirmando que has perdido y pregunta si quieres intentarlo de nuevo.
     */
    protected void confirmLostDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Looser!");
        builder.setMessage("You lost. :( Do you want to try again?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                checkSaveHighScore();
                restart();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkSaveHighScore();
                finish();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Comprueba el score y si es más alta que el high score actual se cambia por la nueva score conseguida.
     */
    protected void checkSaveHighScore() {
        if (this.score > this.highScore) {
            this.highScore = this.score;
            this.highScoreTextView.setText(this.highScore + "");
            this.saveHighScore(this.score);
        }
    }
    /**
     * Starts the RecordActivity.
     */
    public void openRecords(){
        Intent intent=new Intent(this, DataListActivityScore.class);
        startActivity(intent);
    }
    @Override
    public void gameStateChanged() {
        if (this.scoreTextView != null && this.highScoreTextView != null) {
            this.score = this.game.getScore();
            this.scoreTextView.setText(this.score + "");
            this.checkSaveHighScore();

            if(this.game.isWin() && !this.wonInformed) {
                this.confirmWinDialog();
            }

            if(this.game.isLoose()) {
                this.confirmLostDialog();
            }
        }
    }
}
