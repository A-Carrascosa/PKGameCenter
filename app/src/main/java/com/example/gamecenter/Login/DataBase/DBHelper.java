package com.example.gamecenter.Login.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gamecenter.DataListActivityScore;
import com.example.gamecenter.DataListActivityUsers;
import com.example.gamecenter.R;
import com.example.gamecenter.Score;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase dbWritable;
    private final static String DB_NAME = "instituto";
    private final static int DB_VERSION = 1;




    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  userstable(id_user integer  PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "username text NOT NULL,clave_user text NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS score (score INTEGER, game TEXT," +
                " user_name TEXT, _id INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY (user_name) REFERENCES " +
                "userstable (username));");

        db.execSQL("insert into userstable(username,clave_user) values('admin','admin')");
    }


    /**
     * Inserta los usuarios dentro de la base de datos.
     *
     * @param db Base de datos.
     * @param i  ID clave_user.
     * @param i1 ID username.
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("create table userstable(id_user integer  PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "username text NOT NULL,clave_user text NOT NULL)");

        //Valor predeterminado
        db.execSQL("insert into userstable(username,clave_user) values('admin','admin')");
    }

    public List<DataListActivityUsers> showUsers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userstable", null);
        List<DataListActivityUsers> usersList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                usersList.add(new DataListActivityUsers(cursor.getString(1)));
            }while (cursor.moveToNext());
        }
        return usersList;
    }

    public List<DataListActivityScore> showScore() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM score", null);
        List<DataListActivityScore> scoreList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                scoreList.add(new DataListActivityScore(cursor.getString(1),cursor.getString(3),cursor.getString(0)));
            }while (cursor.moveToNext());
        }
        return scoreList;
    }

    /**
     * Inserta los scores dentro de la base de datos.
     *
     * @param score Objeto donde se guardan los valores score.
     * @return Booleano para verificar si el score se ha introducido.
     */
    public boolean addScore(Score score) {

        boolean inserted = false;
        if (this.dbWritable == null) {
            this.dbWritable = this.getWritableDatabase();
        }

        try {
            ContentValues c = new ContentValues();
            c.put("score", score.getScore());
            c.put("game", score.getGame());
            c.put("user_name", score.getUser_name());
            dbWritable.insertOrThrow("score", null, c);
            inserted = true;
            dbWritable.close();
        } catch (Exception ex) {
            Log.d("ERROR", ex.getMessage());
            inserted = false;
        }

        return inserted;
    }

}
