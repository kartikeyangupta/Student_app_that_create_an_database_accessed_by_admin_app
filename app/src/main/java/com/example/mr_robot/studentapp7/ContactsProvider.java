package com.example.mr_robot.studentapp7;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ContactsProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.mr_robot.studentapp7.ContactsProvider";
    static final String URL = "content://"+ PROVIDER_NAME + "/students";
    static final Uri Content_URI = Uri.parse(URL);
    static final int uriCode =1;
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"students",uriCode);
    }
    private SQLiteDatabase sqlDB;
    @Override
    public boolean onCreate() {
        DBOpenHelper dbHelper = new DBOpenHelper(getContext());
        sqlDB = dbHelper.getWritableDatabase();
        if(sqlDB!=null)
            return true;
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBOpenHelper.TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case uriCode :
                queryBuilder.setProjectionMap(DBOpenHelper.values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI "+ uri);
                }
        Cursor cursor = queryBuilder.query(sqlDB,strings,s,strings1,null ,null,s1);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode :
                return "vnd.android.cursor.dir/students";
            default:
                throw new IllegalArgumentException("Unsupported URI "+ uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowID = sqlDB.insert(DBOpenHelper.TABLE_NAME,null,contentValues);
        if(rowID>0) {
            Uri _uri = ContentUris.withAppendedId(Content_URI,rowID);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }else {
            Toast.makeText(getContext(),"Row Inserted Failed",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int rowDeleted = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                rowDeleted = sqlDB.delete(DBOpenHelper.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int rowUpdated = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode:
                rowUpdated = sqlDB.update(DBOpenHelper.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowUpdated;
    }
}
