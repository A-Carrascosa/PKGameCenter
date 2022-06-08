package com.example.gamecenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterScore extends RecyclerView.Adapter<RecyclerViewAdapterScore.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {


        private TextView userText;
        private TextView scoreText;
        private TextView gameText;

        public ViewHolder(View itemView) {
            super(itemView);
            userText = (TextView) itemView.findViewById(R.id.textViewUser);
            scoreText = (TextView) itemView.findViewById(R.id.textViewScore);
            gameText = (TextView) itemView.findViewById(R.id.textViewGame);
        }
    }

    public List<DataListActivityScore> dataList;

    public RecyclerViewAdapterScore(List<DataListActivityScore> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userText.setText(dataList.get(position).getUserText());
        holder.scoreText.setText(dataList.get(position).getScoreText());
        holder.gameText.setText(dataList.get(position).getGameText());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
