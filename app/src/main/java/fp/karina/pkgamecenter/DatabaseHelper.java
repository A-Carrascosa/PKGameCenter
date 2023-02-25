package fp.karina.pkgamecenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private final SQLiteDatabase db = this.getWritableDatabase();

    private static final String DATABASE_NAME = "PK_Center.db";
    private static final int DATABASE_VERSION = 5;

    public static final String TABLE_USERS = "USERS";

    public static final String USER = "USER";
    public static final String PASSWORD = "PASSWORD";

    public static final String TABLE_2048 = "GAME_2048";

    public static final String SCORE = "SCORE";
    public static final String MODE = "MODE";

    public static final String TABLE_ACTIVE_USER = "ACTIVE_USER";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query;
        query = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                USER + " TEXT PRIMARY KEY," +
                PASSWORD + " TEXT)";

        sqLiteDatabase.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + TABLE_2048 + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER + " TEXT," +
                MODE + " TEXT," +
                SCORE + " INTEGER)";

        sqLiteDatabase.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVE_USER + " (" +
                USER + " TEXT PRIMARY KEY)";

        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_2048);
        onCreate(sqLiteDatabase);

    }

    synchronized public boolean registerUser(String user, String password) {

        ContentValues cv = new ContentValues();
        cv.put(USER, user);
        cv.put(PASSWORD, password);

        long rs = db.insert(TABLE_USERS, null, cv);

        if (rs == -1) {
            Log.e(TAG, "Failed to register: " + user);
            return false;
        } else {
            Log.i(TAG, "User registered: " + user);
            return true;
        }
    }

    synchronized public boolean editUser(String oldUser, String newUser) {

        ContentValues cv = new ContentValues();
        cv.put(USER, newUser);

        try {
            long rs = db.update(TABLE_USERS, cv,USER + " LIKE \"" + oldUser + "\"", null);

            if (rs == -1) {
                Log.e(TAG, "Failed to edit user: " + oldUser);
                return false;
            } else {
                Log.i(TAG, "User edited: " + oldUser + " -> " + newUser);
                rs = db.update(TABLE_ACTIVE_USER, cv,USER + " LIKE \"" + oldUser + "\"", null);
                rs = db.update(TABLE_2048, cv,USER + " LIKE \"" + oldUser + "\"", null);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to edit user: " + oldUser + " -> " + newUser);
            return false;
        }
    }

    synchronized public Cursor readAllData(String table) {

        Cursor cursor = null;
        String query = "SELECT * FROM " + table;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    synchronized public ArrayList<Score> getScoreList() {
        Cursor c = readAllData(TABLE_2048);

        ArrayList<Score> list = new ArrayList<>();

        while (c.moveToNext()) {
            list.add(new Score(
                c.getString(c.getColumnIndexOrThrow(USER)),
                c.getString(c.getColumnIndexOrThrow(MODE)),
                c.getInt(c.getColumnIndexOrThrow(SCORE))
            ));
        }

        c.close();
        return list;
    }

    synchronized public boolean addScore(String game, String user, String mode, Integer score) {

        ContentValues cv = new ContentValues();
        cv.put(USER, user);
        cv.put(MODE, mode);
        cv.put(SCORE, score);

        long rs = db.insert(game, null, cv);

        if (rs == -1) {
            Log.e(TAG, "Failed to register score: " + user + ", " + score);
            return false;
        } else {
            Log.i(TAG, "Score registered: " + user + ", " + score);
            return true;
        }
    }

    synchronized public Integer getBestScore(String user) {
        String query = "SELECT * FROM " + TABLE_2048 + " WHERE " + USER + " LIKE '" + user + "' ORDER BY " + SCORE + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int score = cursor.getInt(cursor.getColumnIndexOrThrow(SCORE));
            return score;
        }
        cursor.close();
        return 0;
    }

    synchronized boolean login(String user, String password) {

        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + USER + " LIKE '" + user + "' AND " + PASSWORD + " LIKE '" + password + "'";
        Cursor cursor = db.rawQuery(query, null);

        ContentValues cv = new ContentValues();
        cv.put(USER, user);

        if (cursor.getCount() > 0) {

            long rs = db.insert(TABLE_ACTIVE_USER, null, cv);
            cursor.close();

            if (rs == -1) {
                Log.e(TAG, "Failed to set user: " + user);
            } else {
                Log.i(TAG, "Active user setted: " + user);
                return true;
            }
        }

        Log.i(TAG, "User not logged: " + user);
        cursor.close();
        return false;
    }

    synchronized void logout() {

        long rs = db.delete(TABLE_ACTIVE_USER, null, null);

        if (rs == -1) {
            Log.e(TAG, "Failed to logout");
        } else {
            Log.i(TAG, "Logout");
        }
    }

    synchronized public String getActiveUser() {
        String query = "SELECT * FROM " + TABLE_ACTIVE_USER;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(USER));
        }
        cursor.close();
        return null;
    }

}
