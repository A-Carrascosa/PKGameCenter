package fp.karina.pkgamecenter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ViewHolder> {

    private ArrayList<Score> scoreListDefault;
    private ArrayList<Score> scoreList;
    private LayoutInflater mInflater;
    private Context context;
    private DatabaseHelper db;

    public enum Filter{normal,desc,asc};
    private Filter nickOrder;
    private Filter modeOrder;
    private Filter scoreOrder;

    public ScoreboardAdapter(ArrayList<Score> scoreList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.db = new DatabaseHelper(context);
        this.scoreListDefault = scoreList;
        this.scoreList = (ArrayList<Score>) scoreListDefault.clone();
        this.nickOrder = Filter.normal;
        this.modeOrder = Filter.normal;
        this.scoreOrder = Filter.normal;
        this.context = context;
    }

    public void setScoreList(ArrayList<Score> scoreList) {
        this.scoreListDefault = scoreList;
        this.scoreList = (ArrayList<Score>) scoreListDefault.clone();
        notifyDataSetChanged();
    }

    public void sortNick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            switch (nickOrder) {
                case normal:
                    scoreList.sort(Comparator.comparing(s -> s.user));
                    nickOrder = Filter.desc;
                    break;
                case desc:
                    Collections.reverse(scoreList);
                    nickOrder = Filter.asc;
                    break;
                case asc:
                    scoreList = (ArrayList<Score>) scoreListDefault.clone();
                    nickOrder = Filter.normal;
                    break;
            }
        }
        notifyDataSetChanged();
    }

    public void sortMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            switch (modeOrder) {
                case normal:
                    scoreList.sort(Comparator.comparing(s -> s.mode));
                    Collections.reverse(scoreList);
                    modeOrder = Filter.asc;
                    break;
                case desc:
                    scoreList = (ArrayList<Score>) scoreListDefault.clone();
                    modeOrder = Filter.normal;
                    break;
                case asc:
                    Collections.reverse(scoreList);
                    modeOrder = Filter.desc;
                    break;
            }
        }
        notifyDataSetChanged();
    }

    public void sortScore() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            switch (scoreOrder) {
                case normal:
                    scoreList.sort(Comparator.comparing(s -> s.score));
                    Collections.reverse(scoreList);
                    scoreOrder = Filter.asc;
                    break;
                case desc:
                    scoreList = (ArrayList<Score>) scoreListDefault.clone();
                    scoreOrder = Filter.normal;
                    break;
                case asc:
                    Collections.reverse(scoreList);
                    scoreOrder = Filter.desc;
                    break;
            }
        }
        notifyDataSetChanged();
    }

    public Filter getNickOrder() {
        return nickOrder;
    }

    public Filter getModeOrder() {
        return modeOrder;
    }

    public Filter getScoreOrder() {
        return scoreOrder;
    }

    @NonNull
    @Override
    public ScoreboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_score, parent,false);
        return new ScoreboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreboardAdapter.ViewHolder holder, int position) {
        holder.bindData(scoreList.get(position));
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nickname, mode, score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname = itemView.findViewById(R.id.nickname);
            mode = itemView.findViewById(R.id.mode);
            score = itemView.findViewById(R.id.score);
        }

        public void bindData(final Score score) {
            this.nickname.setText(score.user);
            this.mode.setText(score.mode);
            this.score.setText(score.score.toString());
        }
    }

}
