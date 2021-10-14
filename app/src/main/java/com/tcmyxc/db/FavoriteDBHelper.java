package com.tcmyxc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tcmyxc.model.Album;
import com.tcmyxc.model.AlbumList;
import com.tcmyxc.util.LOG;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author : 徐文祥
 * @date : 2021/10/9 11:12
 * @description : Favorite数据库相关
 */
public class FavoriteDBHelper extends SQLiteOpenHelper {

    private static final String TAG = FavoriteDBHelper.class.getName();

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "favorite";
    private static final String KEY_ID = "id";
    private static final String KEY_ALBUM_ID = "album_id";
    private static final String KEY_ALBUM_JSON = "album_json";
    private static final String KEY_CREATE_TIME = "create_time";

    public FavoriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITE_TABLE = "create table " + TABLE_NAME + "("
                + KEY_ID + " integer primary key,"
                + KEY_ALBUM_ID + " integer,"
                + KEY_ALBUM_JSON + " text,"
                + KEY_CREATE_TIME + " text "
                + ")";
        // LOG.i(TAG + " : 建表语句是：" + CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    private String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    // 增
    public void add(Album album) {
        Album oldAlbum = getAlbumById(album.getAlbumId());// 看看原来是否就有
        // 没有则添加到数据库，有的话啥也不干
        if (oldAlbum == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ALBUM_ID, album.getAlbumId());
            values.put(KEY_ALBUM_JSON, album.toJson());
            values.put(KEY_CREATE_TIME, getCurrentTime());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }
    }

    // 删
    public void del(String albumId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ALBUM_ID + " =? ", new String[]{albumId});
        db.close();
    }

    // 查询所有
    public AlbumList getAllData() {
        AlbumList albumList = new AlbumList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "select * from " + TABLE_NAME + " order by datetime(" + KEY_CREATE_TIME + ") desc";// 按时间倒序排列
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // LOG.d("cursor " + cursor.getString(2));
                    Album album = Album.fromJson(cursor.getString(2));
                    albumList.add(album);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return albumList;
    }

    // 查
    public Album getAlbumById(String albumId) {
        SQLiteDatabase db = getReadableDatabase();
        /*
         * query(String table, String[] columns, String selection, String[] selectionArgs,
         *       String groupBy, String having, String orderBy)
         */
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{KEY_ALBUM_ID, KEY_ALBUM_JSON, KEY_CREATE_TIME},
                KEY_ALBUM_ID + " = ? ",
                new String[]{albumId}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {// 有数据
                String json = cursor.getString(1);// 对应KEY_ALBUM_JSON
                LOG.i(TAG + " getAlbumById, json is " + json);
                Album album = Album.fromJson(json);
                cursor.close();
                db.close();
                return album;
            } else {
                cursor.close();
                db.close();
                return null;
            }
        } else {
            db.close();
        }
        return null;
    }
}
