package fp.karina.pkgamecenter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Scoreboard extends AppCompatActivity {

    private DatabaseHelper db;
    private ScoreboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        db = new DatabaseHelper(this);

        adapter = new ScoreboardAdapter(db.getScoreList(), this);

        RecyclerView scoreList = (RecyclerView) findViewById(R.id.scorelist);
        scoreList.setLayoutManager(new LinearLayoutManager(this));
        scoreList.setAdapter(adapter);

        FloatingActionButton cSearch = findViewById(R.id.clear_search);
        cSearch.animate().setDuration(0).scaleY(0f).scaleX(0f).alpha(0f).start();
        Button bSearch = findViewById(R.id.search_score);
        Button bNick = findViewById(R.id.bnick);
        Button bMode = findViewById(R.id.bmode);
        Button bScore = findViewById(R.id.bscore);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bNick.setCompoundDrawableTintList(getColorStateList(R.color.white));
            bMode.setCompoundDrawableTintList(getColorStateList(R.color.white));
            bScore.setCompoundDrawableTintList(getColorStateList(R.color.white));
        }

        cSearch.setOnClickListener(view -> {
            adapter.setScoreList(db.getScoreList());
            cSearch.animate().setDuration(500).scaleY(0f).scaleX(0f).alpha(0f).start();
            bNick.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            bMode.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            bScore.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        });

        bSearch.setOnClickListener(view -> {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_search);
            dialog.show();

            TextInputLayout sEdit = dialog.findViewById(R.id.sedit);
            Button sButton = dialog.findViewById(R.id.sbutton);
            sButton.setOnClickListener(view1 -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    List<Score> filtered = db.getScoreList().stream().filter(score -> score.user.toLowerCase(Locale.ROOT).contains(sEdit.getEditText().getText().toString().toLowerCase(Locale.ROOT))).collect(Collectors.toList());
                    adapter.setScoreList(new ArrayList<Score>(filtered));
                }
                cSearch.animate().setDuration(500).scaleY(1f).scaleX(1f).alpha(1f).start();
                bNick.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                bMode.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                bScore.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                dialog.dismiss();
            });

        });

        bNick.setOnClickListener(view -> {
            adapter.sortNick();
            switch (adapter.getNickOrder()) {
                case normal:
                    bNick.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    break;
                case asc:
                    bNick.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_arrow_up),null);
                    break;
                case desc:
                    bNick.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_arrow_down),null);
                    break;
            }
            bMode.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            bScore.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        });

        bMode.setOnClickListener(view -> {
            adapter.sortMode();
            switch (adapter.getModeOrder()) {
                case normal:
                    bMode.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    break;
                case asc:
                    bMode.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_arrow_down),null);
                    break;
                case desc:
                    bMode.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_arrow_up),null);
                    break;
            }
            bNick.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            bScore.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        });

        bScore.setOnClickListener(view -> {
            adapter.sortScore();
            switch (adapter.getScoreOrder()) {
                case normal:
                    bScore.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    break;
                case desc:
                    bScore.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_arrow_up),null);
                    break;
                case asc:
                    bScore.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_arrow_down),null);
                    break;
            }
            bMode.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            bNick.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        });

    }
}