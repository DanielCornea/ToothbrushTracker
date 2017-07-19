package com.example.android.toothbrushtracker;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by danok on 7/17/2017.
 */

public class BrushProvider extends ContentProvider {
    public static final String LOG_TAG = BrushProvider.class.getSimpleName();

    // URI match codes
    private static final int BRUSHES = 100;
    private static final int BRUSHES_ID = 101;

    // no matcher
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer
    static {
        sUriMatcher.addURI(BrushContract.CONTENT_AUTHORITY, BrushContract.PATH_BRUSHES, BRUSHES);

        sUriMatcher.addURI(BrushContract.CONTENT_AUTHORITY, BrushContract.PATH_BRUSHES + "/#", BRUSHES_ID);
    }

    //    the helper object
    private dbHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new dbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        //geting the db
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Log.v("DATABASE", database.toString());
        //return value
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BRUSHES:

                cursor = database.query(BrushContract.BrushEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BRUSHES_ID:

                selection = BrushContract.BrushEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};


                cursor = database.query(BrushContract.BrushEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BRUSHES:
                return BrushContract.BrushEntry.CONTENT_LIST_TYPE;
            case BRUSHES_ID:
                return BrushContract.BrushEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BRUSHES:
                return insertBrush(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertBrush(Uri uri, ContentValues values) {

        String name = values.getAsString(BrushContract.BrushEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Brush requires a name");
        }

        String quantity = values.getAsString(BrushContract.BrushEntry.COLUMN_QUANTITY);
        if (quantity == null || Integer.parseInt(quantity) <= 0) {
            throw new IllegalArgumentException("Brush quantity cannot be negative");
        }

        String brand = values.getAsString(BrushContract.BrushEntry.COLUMN_BRAND);
        if (brand == null) {
            throw new IllegalArgumentException("There must be a brand");
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();


        long id = database.insert(BrushContract.BrushEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify  listeners
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();

       int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BRUSHES:

                rowsDeleted = database.delete(BrushContract.BrushEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BRUSHES_ID:

                selection = BrushContract.BrushEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(BrushContract.BrushEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }


        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BRUSHES:
                return updateBrushes(uri, contentValues, selection, selectionArgs);
            case BRUSHES_ID:
                selection = BrushContract.BrushEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateBrushes(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateBrushes(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(BrushContract.BrushEntry.COLUMN_NAME)) {
            String name = values.getAsString(BrushContract.BrushEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Brush requires a name");
            }
        }

        if (values.containsKey(BrushContract.BrushEntry.COLUMN_QUANTITY)) {

            Integer quantity = values.getAsInteger(BrushContract.BrushEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity <= 0) {
                throw new IllegalArgumentException("Brush requires a name");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

       SQLiteDatabase database = dbHelper.getWritableDatabase();

         int rowsUpdated = database.update(BrushContract.BrushEntry.TABLE_NAME, values, selection, selectionArgs);
        Log.v("RowsUpdated: 000  ", String.valueOf(rowsUpdated));
        if (rowsUpdated != 0) {
            Log.v("URI ==", uri.toString());
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
