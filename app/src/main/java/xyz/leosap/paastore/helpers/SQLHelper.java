package xyz.leosap.paastore.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LeoSap on 7/01/2017.
 */

public class SQLHelper extends SQLiteOpenHelper {
    private static final String CREATE_TABLE_APP = "CREATE TABLE app (" +
            "id TEXT, " +
            "name TEXT," +
            "title TEXT," +
            "image TEXT, " +
            "summary TEXT, " +
            "price TEXT, " +
            "price_currency TEXT, " +
            "rights TEXT, " +
            "link TEXT, " +
            "artist TEXT, " +
            "category TEXT, " +
            "release_date TEXT " +
            ")";

    public SQLHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_APP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS tema");
        db.execSQL("DROP TABLE IF EXISTS app");
        db.execSQL(CREATE_TABLE_APP);

    }
}