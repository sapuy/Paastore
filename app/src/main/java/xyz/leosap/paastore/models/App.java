package xyz.leosap.paastore.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import xyz.leosap.paastore.common.Constants;

/**
 * Created by LeoSap on 7/01/2017.
 */

public class App {

    private static final String tabla = "app";

    //properties
    private String id;
    private String name;
    private String title;
    private String image;
    private String summary;
    private String price;
    private String priceCurrency;
    private String rights;
    private String link;
    private String artist;
    private String category;
    private String releaseDate;


    public static ArrayList<App> getAll(SQLiteDatabase db) {

        ArrayList<App> list = new ArrayList<>();
        String query = "select * from " + tabla;

        if (Constants.debug) Log.d("LS query", query);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(loadFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return list;
    }

    public static App find(SQLiteDatabase db, String id) {


        String query = "select * from " + tabla + " where id='" + id + "'";

        if (Constants.debug) Log.d("LS query", query);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return loadFromCursor(cursor);
        } else return null;


    }

    public static void truncate(SQLiteDatabase db) {
        String query = "delete from " + tabla;
        if (Constants.debug) Log.d("LS query", query);
        db.execSQL(query);


    }

    private static App loadFromCursor(Cursor cursor) {
        App app = new App();
        app.setId(cursor.getString(0));
        app.setName(cursor.getString(1));
        app.setTitle(cursor.getString(2));
        app.setImage(cursor.getString(3));
        app.setSummary(cursor.getString(4));
        app.setPrice(cursor.getString(5));
        app.setPriceCurrency(cursor.getString(6));
        app.setRights(cursor.getString(7));
        app.setLink(cursor.getString(8));
        app.setArtist(cursor.getString(9));
        app.setCategory(cursor.getString(10));
        app.setReleaseDate(cursor.getString(11));

        return app;
    }


    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public void update(SQLiteDatabase db) {
        String where = "id=?";
        String[] whereArgs = new String[]{getId()};

        db.update(tabla, getContentValues(), where, whereArgs);

    }

    public void save(SQLiteDatabase db) {
        Long result = db.insert(tabla, null, getContentValues());
        if (Constants.debug) Log.d("LS insert", String.valueOf(result));


    }

    private ContentValues getContentValues() {
        ContentValues content = new ContentValues();
        content.put("id", getId());
        content.put("name", getName());
        content.put("title", getTitle());
        content.put("image", getImage());
        content.put("summary", getSummary());
        content.put("price", getPrice());
        content.put("price_currency", getPriceCurrency());
        content.put("rights", getRights());
        content.put("link", getLink());
        content.put("artist", getArtist());
        content.put("category", getCategory());
        content.put("release_date", getReleaseDate());

        return content;
    }
}
