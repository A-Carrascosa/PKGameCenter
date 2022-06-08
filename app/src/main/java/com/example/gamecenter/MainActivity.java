package com.example.gamecenter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamecenter.Login.DataBase.DBHelper;
import com.example.gamecenter.Login.Principal.PrincipalMenu;
import com.example.gamecenter.Login.RegistroData.RegistroData;

public class MainActivity extends AppCompatActivity {

    //Variables

    EditText et1, et2;

    private Cursor fila;

    String Title = "Inicio de Sesión";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Pinta el titulo de la actividad
        this.setTitle(Title);
        //Habilita para que se pueda visualizar el action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Empereja las variable con el xml editText usuario y password
        et1 = (EditText) findViewById(R.id.etusuario);
        et2 = (EditText) findViewById(R.id.edtclave);
    }

    public void InicioSesion(View v) {
        //Crea un objeto de la clase DBHelper
        DBHelper admin = new DBHelper(this);
        //Abre la base de datos
        SQLiteDatabase db = admin.getWritableDatabase();
        //Crea dos variables para el usuario y contraseña
        String usuario = et1.getText().toString();
        String contrasena = et2.getText().toString();
        //Inicializa el cursor y llama al objeto de la DataBase para realizar una query
        fila = db.rawQuery("select username,clave_user from userstable where username='" +
                usuario + "' and clave_user='" + contrasena + "'", null);
        //Try catch para la captura de errores
        try {

            if (fila.moveToFirst()) {
            //Captura los valores del cursor y los guarda
                String usua = fila.getString(0);
                String pass = fila.getString(1);
                //Pregunta si los datos ingresados son iguales
                if (usuario.equals(usua) && contrasena.equals(pass)) {
                    //Si son iguales entonces vamos a otra ventana
                    Intent ven = new Intent(this, PrincipalMenu.class);
                    startActivity(ven);
                    //Limpia las cajas de texto
                    et1.setText("");
                    et2.setText("");
                }
            }//Si la primera condicion no se cumple entonces envia un toast
            else {
                Toast toast = Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_LONG);
                toast.show();
            }

        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void RegistroData(View v) {
        Intent rdata = new Intent(this, RegistroData.class);
        startActivity(rdata);
    }
}