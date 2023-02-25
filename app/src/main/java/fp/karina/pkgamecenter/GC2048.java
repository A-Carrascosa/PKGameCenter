package fp.karina.pkgamecenter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GestureDetectorCompat;

import java.util.Arrays;

public class GC2048 extends AppCompatActivity {

    private DatabaseHelper db;

    final int BLANK_VALUE = 0;
    int INITIAL_VALUE = 2;
    int score;
    int bestScore;
    int GRID_RANGE = 4;
    String MODE = "4x4";
    int[][] valueGrid;
    int[][] lastGrid;
    TextView[][] grid;
    TextView scoreView;
    TextView bestScoreView;
    Button undoButton;
    int gridDimension;
    final int animationDuration = 250;

    private GestureDetectorCompat mDetector;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gc2048);

        db = new DatabaseHelper(this);
        user = db.getActiveUser() != null ? db.getActiveUser() : "Guest";

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        setupGame();

        if (savedInstanceState != null) {

            score = savedInstanceState.getInt("SCORE_KEY", 0);
            bestScore = savedInstanceState.getInt("BESTSCORE_KEY", 0);
            GRID_RANGE = savedInstanceState.getInt("GRID_RANGE", 4);
            valueGrid = (int[][]) savedInstanceState.getSerializable("GRID_KEY");
            refreshGridLayout();

        } else {

            initialRandom();

        }

        this.undoButton = findViewById(R.id.undo);
        this.undoButton.setOnClickListener(view -> {

            if (lastGrid == null) {

                Toast.makeText(GC2048.this, "Undo failed", Toast.LENGTH_SHORT).show();

            } else {

                valueGrid = copyGrid(lastGrid);
                refreshGridLayout();
                checkUndo(valueGrid, lastGrid);

            }

        });

        Button restartButton = findViewById(R.id.restart);
        restartButton.setOnClickListener(view -> {

            db.addScore(DatabaseHelper.TABLE_2048, user, MODE, score);
            setupGame();
            initialRandom();
            checkUndo(valueGrid, lastGrid);

        });

        Button settingsButton = findViewById(R.id.settings2048);
        settingsButton.setOnClickListener(view -> {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_settings_2048);
            dialog.show();

            SwitchCompat sw = dialog.findViewById(R.id.cheats);
            if (INITIAL_VALUE == 512) sw.setChecked(true);
            sw.setOnClickListener(view1 -> {
                if (sw.isChecked())
                    INITIAL_VALUE = 512;
                else
                    INITIAL_VALUE = 2;
                dialog.dismiss();
            });

            RadioGroup rg = dialog.findViewById(R.id.maps);

            switch (GRID_RANGE) {
                case 3:
                    rg.check(R.id.map3);
                    break;
                case 4:
                    rg.check(R.id.map4);
                    break;
                case 5:
                    rg.check(R.id.map5);
                    break;
            }

            Button applySettings = dialog.findViewById(R.id.apply_settings_2048);
            applySettings.setOnClickListener(view1 -> {

                switch (rg.getCheckedRadioButtonId()) {
                    case R.id.map3:
                        GRID_RANGE = 3;
                        MODE = "3x3";
                        break;
                    case R.id.map4:
                        GRID_RANGE = 4;
                        MODE = "4x4";
                        break;
                    case R.id.map5:
                        GRID_RANGE = 5;
                        MODE = "5x5";
                        break;
                }

                setupGame();
                initialRandom();
                checkUndo(valueGrid, lastGrid);
                dialog.dismiss();

            });
        });

        Button scoreboardButton = findViewById(R.id.scoreboard);
        scoreboardButton.setOnClickListener(view -> {
            Intent intentSecondary = new Intent(GC2048.this, Scoreboard.class);
            GC2048.this.startActivity(intentSecondary);
        });

    }

    /**
     * Setups the variables required for the game
     */
    private void setupGame() {

        GridLayout gGridLayout = findViewById(R.id.grid_2048);
        GridLayout bGridLayout = findViewById(R.id.grid_2048_background);

        gGridLayout.removeAllViews();
        bGridLayout.removeAllViews();

        gGridLayout.setColumnCount(GRID_RANGE);
        gGridLayout.setRowCount(GRID_RANGE);
        bGridLayout.setColumnCount(GRID_RANGE);
        bGridLayout.setRowCount(GRID_RANGE);

        for (int i = 0; i < GRID_RANGE * GRID_RANGE; i++) {
            LayoutInflater.from(this).inflate(R.layout.game_cell, gGridLayout);
            LayoutInflater.from(this).inflate(R.layout.background_cell, bGridLayout);
        }

        this.grid = new TextView[GRID_RANGE][GRID_RANGE];
        this.valueGrid = new int[GRID_RANGE][GRID_RANGE];

        this.score = 4;
        this.scoreView = findViewById(R.id.actual_score);

        this.bestScore = user != null ? db.getBestScore(this.user) : 0;
        this.bestScoreView = findViewById(R.id.best_score);

        this.gridDimension = gGridLayout.getWidth();

        int index = 0;

        for (int i = 0; i < GRID_RANGE; i++) {

            for (int j = 0; j < GRID_RANGE; j++) {

                this.grid[i][j] = (TextView) gGridLayout.getChildAt(index);
                this.valueGrid[i][j] = BLANK_VALUE;
                index += 1;

            }

        }

    }

    /**
     * Generates the two initial random numbers
     */
    private void initialRandom() {

        int x = (int) Math.floor(Math.random() * GRID_RANGE);
        int y = (int) Math.floor(Math.random() * GRID_RANGE);
        int x2 = (int) Math.floor(Math.random() * GRID_RANGE);
        int y2 = (int) Math.floor(Math.random() * GRID_RANGE);

        while (x == x2 && y == y2) {

            x2 = (int) Math.floor(Math.random() * GRID_RANGE);
            y2 = (int) Math.floor(Math.random() * GRID_RANGE);

        }

        this.valueGrid[x][y] = INITIAL_VALUE;
        this.valueGrid[x2][y2] = INITIAL_VALUE;

        this.lastGrid = copyGrid(valueGrid);
        refreshGridLayout();

    }

    /**
     * Refresh the cells printed with the new values
     */
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void refreshGridLayout() {

        scoreView.setText(Integer.toString(score));
        bestScoreView.setText(Integer.toString(bestScore));

        for (int x = 0; x < GRID_RANGE; x++) {

            for (int y = 0; y < GRID_RANGE; y++) {

                if (valueGrid[x][y] > 0) {
                    this.grid[x][y].setText(Integer.toString(valueGrid[x][y]));
                    this.grid[x][y].setBackground(getDrawable(R.drawable.cell_container));
                }
                else {
                    this.grid[x][y].setText("");
                    this.grid[x][y].setBackground(null);
                }

            }

        }

    }

    /**
     * Saves the desired info, stats and states
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable("GRID_KEY", valueGrid);
        savedInstanceState.putInt("SCORE_KEY", score);
        savedInstanceState.putInt("BESTSCORE_KEY", bestScore);
        savedInstanceState.putInt("GRID_RANGE", GRID_RANGE);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    /**
     * Move cells to the right
     */
    private void moveRight() {

        int[][] tempGrid = copyGrid(valueGrid);
        boolean[][] mutableGrid = new boolean[GRID_RANGE][GRID_RANGE];

        for (int x = 0; x < GRID_RANGE; x++) {

            for (int y = GRID_RANGE-1; y >= 0; y--) {

                if (valueGrid[x][y] != 0) {

                    int[] newPosition = new int[]{x,y};

                    for (int y2 = y; y2 < GRID_RANGE-1; y2++) {

                        getNewPosition(x, y2, x, y2 + 1, newPosition, mutableGrid);

                    }

                    if (!Arrays.deepEquals(valueGrid, tempGrid)) animation(x, y, newPosition);

                }

            }

        }

    }

    /**
     * Move cells to the left
     */
    private void moveLeft() {

        int[][] tempGrid = copyGrid(valueGrid);
        boolean[][] mutableGrid = new boolean[GRID_RANGE][GRID_RANGE];

        for (int x = 0; x < GRID_RANGE; x++) {

            for (int y = 1; y < GRID_RANGE; y++) {

                if (valueGrid[x][y] != 0) {

                    int[] newPosition = new int[]{x,y};

                    for (int y2 = y; y2 > 0; y2--) {

                        getNewPosition(x, y2, x, y2 - 1, newPosition, mutableGrid);

                    }

                    if (!Arrays.deepEquals(valueGrid, tempGrid)) animation(x, y, newPosition);

                }

            }

        }

    }

    /**
     * Move cells to bottom
     */
    private void moveDown() {

        int[][] tempGrid = copyGrid(valueGrid);
        boolean[][] mutableGrid = new boolean[GRID_RANGE][GRID_RANGE];

        for (int y = 0; y < GRID_RANGE; y++) {

            for (int x = GRID_RANGE-1; x >= 0; x--) {

                if (valueGrid[x][y] != 0) {

                    int[] newPosition = new int[]{x,y};

                    for (int x2 = x; x2 < GRID_RANGE-1; x2++) {

                        getNewPosition(x2, y, x2 + 1, y, newPosition, mutableGrid);

                    }

                    if (!Arrays.deepEquals(valueGrid, tempGrid)) animation(x, y, newPosition);

                }

            }

        }

    }

    /**
     * Move cells to top
     */
    private void moveUp() {

        int[][] tempGrid = copyGrid(valueGrid);
        boolean[][] mutableGrid = new boolean[GRID_RANGE][GRID_RANGE];

        for (int y = 0; y < GRID_RANGE; y++) {

            for (int x = 1; x < GRID_RANGE; x++) {

                if (valueGrid[x][y] != 0) {

                    int[] newPosition = new int[]{x,y};

                    for (int x2 = x; x2 > 0; x2--) {

                        getNewPosition(x2, y, x2 - 1, y, newPosition, mutableGrid);

                    }

                    if (!Arrays.deepEquals(valueGrid, tempGrid)) animation(x, y, newPosition);

                }

            }

        }

    }

    /**
     * Get the new position of the current cell
     * @param x Current X
     * @param y Current Y
     * @param newX New X
     * @param newY new Y
     * @param newPosition Calculated position
     * @param mutableGrid Grid to see if can mutate again
     */
    private void getNewPosition(int x, int y, int newX, int newY, int[] newPosition, boolean[][] mutableGrid) {

        if (valueGrid[x][y] == valueGrid[newX][newY]) {

            if (!mutableGrid[newX][newY]) {
                newPosition[0] = newX;
                newPosition[1] = newY;
                mutableGrid[newX][newY] = true;
                valueGrid[newX][newY] += valueGrid[x][y];
                valueGrid[x][y] = 0;
            }

        } else if (valueGrid[newX][newY] == 0) {

            newPosition[0] = newX;
            newPosition[1] = newY;

            valueGrid[newX][newY] += valueGrid[x][y];
            valueGrid[x][y] = 0;

        } else {
            mutableGrid[newX][newY] = true;
        }

    }

    private void animation(int i, int j, int[] newPosition) {
        if (i != newPosition[0] || j != newPosition[1]) {

            float cellGridSpaceWidth = (float) (findViewById(R.id.grid_2048).getWidth() - 30 ) / GRID_RANGE;

            float spaceToMoveX = 0f;
            float spaceToMoveY = 0f;

            if (i != newPosition[0]) spaceToMoveY = (float) (newPosition[0] - i)*cellGridSpaceWidth;
            if (j != newPosition[1]) spaceToMoveX = (float) (newPosition[1] - j)*cellGridSpaceWidth;

            float finalSpaceToMoveX = spaceToMoveX;
            float finalSpaceToMoveY = spaceToMoveY;

            grid[i][j].animate()
                    .setDuration(animationDuration)
                    .translationXBy(finalSpaceToMoveX)
                    .translationYBy(finalSpaceToMoveY)
                    .withEndAction(() -> {
                        refreshGridLayout();
                        grid[i][j].animate().setDuration(0).translationXBy(-finalSpaceToMoveX).translationYBy(-finalSpaceToMoveY).start();
                    }
            ).start();

        }
    }

    /**
     * Checks if there is a win or if is it blocked, and updates the score
     */
    private void newRound() {

        this.score += INITIAL_VALUE;
        boolean win = checkWin();

        if (win) {

            db.addScore(DatabaseHelper.TABLE_2048, user, MODE, score);
            Toast.makeText(this, getString(R.string.game_won), Toast.LENGTH_SHORT).show();

        }

        if (!checkGridCompleted()) {

            int x = (int) Math.floor(Math.random() * GRID_RANGE);
            int y = (int) Math.floor(Math.random() * GRID_RANGE);

            while (valueGrid[x][y] != 0) {

                x = (int) Math.floor(Math.random() * GRID_RANGE);
                y = (int) Math.floor(Math.random() * GRID_RANGE);

            }

            this.valueGrid[x][y] = INITIAL_VALUE;
            int finalX = x;
            int finalY = y;
            grid[x][y].animate().setDuration(animationDuration).withEndAction(() -> {
                this.grid[finalX][finalY].setAlpha(0f);
                this.grid[finalX][finalY].setText(Integer.toString(INITIAL_VALUE));
                this.grid[finalX][finalY].setBackground(getDrawable(R.drawable.cell_container));
                grid[finalX][finalY].animate().setDuration(animationDuration/2).alpha(1f).start();
            }).start();
            checkUndo(valueGrid, lastGrid);


            if (!checkMovementsAvailable()) {
                db.addScore(DatabaseHelper.TABLE_2048, user, MODE, score);
                Toast.makeText(this, getString(R.string.game_lose), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean checkWin() {
        for (int x = 0; (x < GRID_RANGE); x++) {

            for (int y = 0; (y < GRID_RANGE); y++) {

                if (valueGrid[x][y] == 2048) return true;

            }

        }
        return false;
    }

    private boolean checkGridCompleted() {
        for (int x = 0; (x < GRID_RANGE); x++) {

            for (int y = 0; (y < GRID_RANGE); y++) {

                if (valueGrid[x][y] == 0) return false;

            }

        }
        return true;
    }

    private boolean checkMovementsAvailable() {

        for (int x = 0; x < GRID_RANGE; x++) {
            for (int y = 0; y < GRID_RANGE; y++) {

                if (x > 0) {
                    if (valueGrid[x][y] == valueGrid[x-1][y] || valueGrid[x][y] == 0 || valueGrid[x-1][y] == 0)
                        return true;
                }
                if (y > 0) {
                    if (valueGrid[x][y] == valueGrid[x][y-1] || valueGrid[x][y] == 0 || valueGrid[x][y-1] == 0)
                        return true;
                }
            }
        }

        return false;
    }


    /**
     * Copy a grid for save last movement or restore las movement
     *
     * @param grid Grid to copy
     * @return The copy of the grid passed
     */
    private int[][] copyGrid(int[][] grid) {

        try {
            int[][] copy = new int[grid.length][grid[0].length];

            for (int i = 0; i < grid.length; i++) {

                copy[i] = Arrays.copyOf(grid[i], grid[i].length);

            }

            return copy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Check if the grids are equal for enable the undo button
     */
    private void checkUndo(int[][] grid, int[][] grid2) {

        boolean equals = Arrays.deepEquals(grid, grid2);

        if (equals && undoButton.isEnabled()) {

            undoButton.setEnabled(false);

        } else if (!equals && !undoButton.isEnabled()) {

            undoButton.setEnabled(true);

        }

    }

    /**
     * GestureListener for detect movements
     */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gesto detectado";

        /*@Override
        public boolean onDown(MotionEvent event) {
            //Log.d(DEBUG_TAG, "onDown: " + event.toString());
            Log.d(DEBUG_TAG, "x: " + event.getX() + " y: " + event.getY());
            return true;
        }*/

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            //Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            Log.d(DEBUG_TAG, "x: " + event1.getX() + " y: " + event1.getY());
            Log.d(DEBUG_TAG, "x: " + event2.getX() + " y: " + event2.getY());

            float DifX = Math.abs(event1.getX() - event2.getX());
            float DifY = Math.abs(event1.getY() - event2.getY());
            Log.d(DEBUG_TAG, DifX + " " + DifY);
            final int MIN_DIST = 250;

            if (DifX > MIN_DIST && DifY > MIN_DIST || DifX < MIN_DIST && DifY < MIN_DIST) {
                if (DifX < MIN_DIST && DifY < MIN_DIST) {
                    Log.d(DEBUG_TAG, String.valueOf(R.string.too_short));
                    Toast.makeText(getApplicationContext(), R.string.too_short, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(DEBUG_TAG, getString(R.string.skipped_diagonal));
                    Toast.makeText(getApplicationContext(), R.string.skipped_diagonal, Toast.LENGTH_SHORT).show();
                }
            } else {

                int[][] tempGrid = copyGrid(lastGrid);
                lastGrid = copyGrid(valueGrid);

                if (DifX > MIN_DIST) {
                    if (event1.getX() < event2.getX()) {
                        Log.d(DEBUG_TAG, "DERECHA");
                        moveRight();
                    } else {
                        Log.d(DEBUG_TAG, "IZQUIERDA");
                        moveLeft();
                    }
                } else {
                    if (event1.getY() < event2.getY()) {
                        Log.d(DEBUG_TAG, "ABAJO");
                        moveDown();
                    } else {
                        Log.d(DEBUG_TAG, "ARRIBA");
                        moveUp();
                    }
                }

                if (Arrays.deepEquals(lastGrid, valueGrid)) {
                    lastGrid = copyGrid(tempGrid);
                } else {
                    newRound();
                }

            }
            return true;
        }
    }
}