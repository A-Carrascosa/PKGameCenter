package com.example.gamecenter.PegSolitaire;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.gamecenter.R;


public class GameActivityPegSolitaire extends Activity {

    private Drawable defaultSquare;

    private TableLayout gameTableLayout;
    private PegLayout[][] squares = new PegLayout[7][7];
    private TableRow[] row  = new TableRow[7];
    private PegView[][] pieces = new PegView[7][7];
    private TextView textviewScore;

    private int score = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_peg_solitaire);

        defaultSquare = getResources().getDrawable(R.drawable.square);

        gameTableLayout = (TableLayout) findViewById(R.id.game_table_layout);

        int height = dpToPixels(45);
        int width = dpToPixels(45);

        for (int r = 0; r < 7; r++) {
            row[r] = new TableRow(this);
            for (int c = 0; c < 7; c++) {
                if (!((r == 0 || r == 1 || r == 5 || r == 6) &&
                        (c == 0 || c == 1 || c == 5 || c == 6))) {
                    squares[r][c] = new PegLayout(this, r, c);
                    squares[r][c].setBackgroundDrawable(defaultSquare);
                    squares[r][c].setOnDragListener(new SquareDragListener());
                    if (!((r == 3) && (c == 3))) {
                        pieces[r][c] = new PegView(this, r, c);
                        pieces[r][c].setImageResource(R.drawable.ball);
                        pieces[r][c].setLayoutParams(new LayoutParams(height,width));
                        pieces[r][c].setOnTouchListener(new PegTouchListener());
                        squares[r][c].addView(pieces[r][c]);
                    }
                    row[r].addView(squares[r][c]);
                    TableRow.LayoutParams params = (TableRow.LayoutParams)squares[r][c].getLayoutParams();
                    params.column = c;
                    params.height = height;
                    params.width = width;
                    squares[r][c].setLayoutParams(params);
                }
            }
            gameTableLayout.addView(row[r], new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }

        textviewScore = (TextView) findViewById(R.id.textview_score);
        textviewScore.setText(Integer.toString(getScore()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_peg_solitaire, menu);
        return true;
    }

    public int dpToPixels(int dps) {
        float scale = getBaseContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    public PegLayout[][] getSquares() {
        return squares;

    }

    public void setScore(int s) {
        score = s;
    }

    public int getScore() {
        return score;
    }

    public void updateTextViewScore() {

        int score = getScore();
        if (score == 1) {
            openDialog();
        }

        textviewScore.setText(Integer.toString(getScore()));

    }

    public void finishGame(View view) {
        openDialog();

    }

    public void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("You had " + getScore() + " pegs left!");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                GameActivityPegSolitaire.this.finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private final class PegTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == event.ACTION_DOWN) {
                //ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
                v.startDrag(null, shadowBuilder, v, 0);
                v.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class SquareDragListener implements OnDragListener {

        Drawable defaultSquare = getResources().getDrawable(R.drawable.square);
        Drawable hoverSquare = getResources().getDrawable(R.drawable.hover);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            PegView view;
            PegLayout oldSquare;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(hoverSquare);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(defaultSquare);
                    break;
                case DragEvent.ACTION_DROP:
                    view = (PegView) event.getLocalState();
                    PegLayout newSquare = (PegLayout) v;
                    oldSquare = (PegLayout) view.getParent();
                    if (view.move(oldSquare, newSquare, getSquares())) {
                        int score = getScore();
                        score--;
                        setScore(score);
                        updateTextViewScore();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    view = (PegView) event.getLocalState();
                    view.setVisibility(View.VISIBLE);
                    v.setBackgroundDrawable(defaultSquare);
                default:
                    break;
            }
            return true;
        }

    }
}
