package com.example.gamecenter.Login.Principal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.R;
import com.example.gamecenter.Score;
import com.example.gamecenter.Users;
import com.example.gamecenter.gc2048.MainActivity2048;
import com.example.gamecenter.PegSolitaire.MainMenuActivityPegSolitaire;


public class PrincipalMenu extends AppCompatActivity implements View.OnClickListener{

    Button boton2048;
    Button botonPegSolitaire;
    Button scores;
    Button usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_menu);
        //Crea una variable string para el titulo
        String Title = "Welcome";
        //Pinta el titulo de la actividad
        this.setTitle(Title);

        boton2048 = (Button) findViewById(R.id.game2048);
        boton2048.setOnClickListener(this);

        botonPegSolitaire = (Button) findViewById(R.id.pegsolitaire);
        botonPegSolitaire.setOnClickListener(this);

        scores = (Button) findViewById(R.id.scores);
        scores.setOnClickListener(this);

        usuarios = (Button) findViewById(R.id.users);
        usuarios.setOnClickListener(this);

    }

    @Override
    public void onClick(@NonNull View view) {

        switch (view.getId()){
            case R.id.game2048:

                Intent intent = new Intent(this, MainActivity2048.class);
                startActivity(intent);

                break;

            case R.id.pegsolitaire:

                Intent intent2 = new Intent(this, MainMenuActivityPegSolitaire.class);
                startActivity(intent2);

                break;

            case R.id.scores:

                Intent intent3 = new Intent(this, Score.class);
                startActivity(intent3);

                break;
            case R.id.users:

                Intent intent4 = new Intent(this, Users.class);
                startActivity(intent4);

                break;
        }
    }
}
