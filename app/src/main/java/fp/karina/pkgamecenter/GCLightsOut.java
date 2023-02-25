package fp.karina.pkgamecenter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class GCLightsOut extends AppCompatActivity {

    final int maxInitialTouches = 12;
    final int minInitialTouches = 8;
    final int maxTouchments = 45;
    final int fadeDuration = 250;
    Drawable light_on;
    Drawable light_off;
    Drawable light_solution;
    TextView[][] grid;
    boolean[][] gridValue;
    ArrayList<int[]> solutionGrid;
    boolean freezeGame;
    int touchments;
    int GRID_RANGE;
    private Button restart;
    private View viewSolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gclights_out);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setupGame();

        restart = findViewById(R.id.restart_LO);
        restart.setOnClickListener(view -> {
            setupGame();
            setupGrid();
            viewSolution.setEnabled(true);
        });

        viewSolution = findViewById(R.id.view_solution);
        viewSolution.setOnClickListener(view -> {
            viewSolution();
        });

        if (savedInstanceState != null) {
            this.gridValue = (boolean[][]) savedInstanceState.getSerializable("GRID_KEY");
            this.solutionGrid = (ArrayList<int[]>) savedInstanceState.getSerializable("INITIALGRID_KEY");
            this.touchments = savedInstanceState.getInt("TOUCHMENTS_KEY");
            this.freezeGame = savedInstanceState.getBoolean("FREEZE_KEY");

            if (solutionGrid.size() < 1) viewSolution.setEnabled(false);

            if (freezeGame) viewSolution();

            for (int x = 0; x < GRID_RANGE; x++) {
                for (int y = 0; y < GRID_RANGE; y++) {
                    resetLight(x, y);
                }
            }
        } else {
            setupGrid();
        }

    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void setupGame() {
        GridLayout gridLayout = findViewById(R.id.grid_LO);
        GRID_RANGE = gridLayout.getColumnCount();
        this.grid = new TextView[GRID_RANGE][GRID_RANGE];
        this.gridValue = new boolean[GRID_RANGE][GRID_RANGE];
        this.solutionGrid = new ArrayList<>();

        this.freezeGame = false;
        this.touchments = 0;

        TextView touchmentsText = findViewById(R.id.touchments);
        touchmentsText.setText("0");
        TextView maxTouchmentsText = findViewById(R.id.restant_touchments);
        maxTouchmentsText.setText(Integer.toString(maxTouchments));

        this.light_on = getDrawable(R.drawable.light_on);
        this.light_off = getDrawable(R.drawable.light_off);
        this.light_solution = getDrawable(R.drawable.light_solution);

        int index = 0;
        for (int i = 0; i < GRID_RANGE; i++) {

            for (int j = 0; j < GRID_RANGE; j++) {

                this.grid[i][j] = (TextView) gridLayout.getChildAt(index);

                this.grid[i][j].animate()
                        .setDuration(fadeDuration)
                        .alpha(0f)
                        .scaleX(0f)
                        .scaleY(0f)
                        .start();

                this.grid[i][j].setOnClickListener(v -> {

                    if (freezeGame) return;

                    int vId = v.getId();
                    for (int x = 0; x < GRID_RANGE; x++) {

                        for (int y = 0; y < GRID_RANGE; y++) {

                            if (grid[x][y].getId() == vId) {

                                int[] coord = new int[]{x, y};

                                changeState(x, y);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    Object[] a = this.solutionGrid.stream().filter(ints -> Arrays.equals(ints, coord)).toArray();
                                    if (a.length > 0) {
                                        solutionGrid.removeIf(ints -> Arrays.equals(ints, coord));
                                    } else {
                                        this.solutionGrid.add(0, coord);
                                    }
                                }
                                touchments += 1;
                                touchmentsText.setText(Integer.toString(touchments));
                                maxTouchmentsText.setText(Integer.toString(maxTouchments - touchments));

                                break;
                            }

                        }

                    }

                    checkWin();

                });

                index += 1;
            }
        }

    }

    private void setupGrid() {

        int touches = (int) Math.floor(Math.random() * (maxInitialTouches - minInitialTouches)) + minInitialTouches;

        boolean[][] listed = new boolean[GRID_RANGE][GRID_RANGE];

        for (int i = touches; i > 0; i--) {
            int x = (int) Math.floor(Math.random() * GRID_RANGE);
            int y = (int) Math.floor(Math.random() * GRID_RANGE);


            while (listed[x][y]) {
                x = (int) Math.floor(Math.random() * GRID_RANGE);
                y = (int) Math.floor(Math.random() * GRID_RANGE);
            }
            listed[x][y] = true;

            this.solutionGrid.add(0, new int[]{x, y});

            changeState(x, y);
        }

    }

    private void changeState(int x, int y) {

        for (int i = x - 1; i <= x + 1; i++) {
            if (i >= 0 && i < GRID_RANGE) {
                changeLight(i, y);
            }
        }

        for (int i = y - 1; i <= y + 1; i += 2) {
            if (i >= 0 && i < GRID_RANGE) {
                changeLight(x, i);
            }
        }

    }

    private void changeLight(int x, int y) {

        gridValue[x][y] = !gridValue[x][y];
        grid[x][y].animate()
                .setDuration(fadeDuration)
                .alpha(gridValue[x][y] ? 1f : 0f)
                .scaleX(gridValue[x][y] ? 1f : 0f)
                .scaleY(gridValue[x][y] ? 1f : 0f)
                .start();

    }

    private void resetLight(int x, int y) {

        grid[x][y].animate()
                .setDuration(fadeDuration)
                .alpha(gridValue[x][y] ? 1f : 0f)
                .scaleX(gridValue[x][y] ? 1f : 0f)
                .scaleY(gridValue[x][y] ? 1f : 0f)
                .start();

    }

    private void viewSolution() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!freezeGame) {
                freezeGame = true;
                restart.setEnabled(false);

                for (int i = 0; i < solutionGrid.size(); i++) {
                    int[] coord = solutionGrid.get(i);
                    //int finalI = i+1;
                    grid[coord[0]][coord[1]].animate()
                            .setDuration(fadeDuration / 2 + fadeDuration / 4)
                            .alpha(0f)
                            .scaleX(0f)
                            .scaleY(0f)
                            .withEndAction(() -> {

                                grid[coord[0]][coord[1]].setBackground(light_solution);
                                //grid[coord[0]][coord[1]].setText(Integer.toString(finalI));

                                grid[coord[0]][coord[1]].animate()
                                        .setDuration(fadeDuration / 2 + fadeDuration / 4)
                                        .alpha(1f)
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .start();

                            })
                            .start();
                }
            } else {
                freezeGame = false;
                restart.setEnabled(true);

                for (int i = 0; i < solutionGrid.size(); i++) {
                    int[] coord = solutionGrid.get(i);

                    grid[coord[0]][coord[1]].animate()
                            .setDuration(fadeDuration / 2 + fadeDuration / 4)
                            .alpha(0f)
                            .scaleX(0f)
                            .scaleY(0f)
                            .withEndAction(() -> {

                                grid[coord[0]][coord[1]].setBackground(light_on);
                                //grid[coord[0]][coord[1]].setText("");

                                grid[coord[0]][coord[1]].animate()
                                        .setDuration(fadeDuration / 2 + fadeDuration / 4)
                                        .alpha(gridValue[coord[0]][coord[1]] ? 1f : 0f)
                                        .scaleX(gridValue[coord[0]][coord[1]] ? 1f : 0f)
                                        .scaleY(gridValue[coord[0]][coord[1]] ? 1f : 0f)
                                        .start();

                            })
                            .start();
                    
                }
            }
        }
    }

    private void checkWin() {
        boolean win = true;
        for (int x = 0; x < GRID_RANGE; x++) {
            for (int y = 0; y < GRID_RANGE; y++) {
                if (gridValue[x][y]) {
                    win = false;
                    break;
                }
            }
        }
        if (win) {
            Toast.makeText(this, getString(R.string.game_won), Toast.LENGTH_SHORT).show();
            freezeGame = true;
            viewSolution.setEnabled(false);
        } else if (maxTouchments - touchments <= 0) {
            Toast.makeText(this, getString(R.string.game_lose), Toast.LENGTH_SHORT).show();
            freezeGame = true;
        }

    }

    /**
     * Saves the desired info, stats and states
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable("GRID_KEY", gridValue);
        savedInstanceState.putSerializable("INITIALGRID_KEY", solutionGrid);
        savedInstanceState.putInt("TOUCHMENTS_KEY", touchments);
        savedInstanceState.putBoolean("FREEZE_KEY", freezeGame);

    }
}