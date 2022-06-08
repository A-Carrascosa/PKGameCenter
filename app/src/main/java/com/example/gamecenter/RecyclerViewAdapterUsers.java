package com.example.gamecenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterUsers extends RecyclerView.Adapter<RecyclerViewAdapterUsers.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout linearLayout;
        private TextView userText;

        public ViewHolder(View itemView) {
            super(itemView);
            this.linearLayout=itemView.findViewById(R.id.linearLayoutUsers);
            this.userText = (TextView) itemView.findViewById(R.id.textUsers);
        }
    }

    public List<DataListActivityUsers> dataList;

    public RecyclerViewAdapterUsers(List<DataListActivityUsers> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userText.setText(dataList.get(position).getUserText());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
