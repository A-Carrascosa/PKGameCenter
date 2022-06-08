package com.example.gamecenter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamecenter.Login.DataBase.DBHelper;

public class Users extends AppCompatActivity {

    private String username;

    private RecyclerView recyclerView;
    private RecyclerViewAdapterUsers recyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listrecord_users);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBHelper dbHelper = new DBHelper(getApplicationContext());

        recyclerViewAdapter = new RecyclerViewAdapterUsers(dbHelper.showUsers());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public Users() {
    }

    public Users(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
