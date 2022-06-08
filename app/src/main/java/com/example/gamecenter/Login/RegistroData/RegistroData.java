package com.example.gamecenter.Login.RegistroData;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamecenter.Login.DataBase.DBHelper;
import com.example.gamecenter.MainActivity;
import com.example.gamecenter.R;

public class RegistroData extends AppCompatActivity {


    //Titulo de la actividad
    String Title = "Registro de Usuarios";
    //EditText para el ingreso de datos del usuario
    EditText Etusurname, EtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_data);
        //Pinta el titulo
        this.setTitle(Title);
        //Instanci las variables
        Etusurname = (EditText) findViewById(R.id.editTextTextUserName);
        EtPass = (EditText) findViewById(R.id.editTextTextPassword);
    }

    //Registra los datos del usuario
    public void RegistrarDataUser(View v) {
        //Crea un objeto de la clase DBHelper
        DBHelper admin = new DBHelper(this);
        //Abre la base de datos
        SQLiteDatabase db = admin.getWritableDatabase();
        //Crea dos variables string y las inicializa
        String UserName = Etusurname.getText().toString();
        String PassUser = EtPass.getText().toString();
        //Crea un objeto contentvalues y la instancia
        ContentValues values = new ContentValues();
        //Captura los valores
        values.put("username", UserName);
        values.put("clave_user", PassUser);
        //Llama al insert con el nombre de la base de datos y los valores
        db.insert("userstable", null, values);
        //Cierra la base de datos
        db.close();
        //Lanza un toast
        Toast ToastMens = Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT);
        //Muestra el toast
        ToastMens.show();
        //Lanza la actividad
        Intent intent = new Intent(this, MainActivity.class);
        //Inicia la actividad
        startActivity(intent);
    }
}
