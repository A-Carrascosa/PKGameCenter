package fp.karina.pkgamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView background = findViewById(R.id.splashbackground);
        ImageView logo = findViewById(R.id.splashlogo);
        TextView title = findViewById(R.id.splashtext);

        background.animate().setDuration(0).translationY(-2000f).rotation(250f).alpha(0f).start();
        logo.animate().setDuration(0).translationY(2000f).alpha(0f).start();
        title.animate().setDuration(0).alpha(0f).start();

        background.animate().setDuration(950).translationY(0f).rotation(0f).alpha(1f).start();
        logo.animate().setDuration(750).translationY(-150f).alpha(1f).withEndAction(() ->
            logo.animate().setDuration(250).translationY(50).withEndAction(() ->
                logo.animate().setDuration(250).translationY(0).withEndAction(() ->
                    title.animate().setDuration(200).alpha(1f).start()
                ).start()
            ).start()
        ).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}