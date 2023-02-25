package fp.karina.pkgamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button b2048;
    Button bLightsOut;
    Button bSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        b2048 = findViewById(R.id.b2048);
        bLightsOut = findViewById(R.id.bLightsOut);
        bSettings = findViewById(R.id.bSettings);

        b2048.setOnClickListener(view -> {
            Intent intentSecondary = new Intent(MainActivity.this, GC2048.class);
            MainActivity.this.startActivity(intentSecondary);
        });

        bLightsOut.setOnClickListener(view -> {
            Intent intentSecondary = new Intent(MainActivity.this, GCLightsOut.class);
            MainActivity.this.startActivity(intentSecondary);
        });

        bSettings.setOnClickListener(view -> {
            Intent intentSecondary = new Intent(MainActivity.this, Settings.class);
            MainActivity.this.startActivity(intentSecondary);
        });
    }
}