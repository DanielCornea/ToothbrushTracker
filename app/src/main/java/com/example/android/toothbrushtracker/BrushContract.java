package com.example.android.toothbrushtracker;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by danok on 7/17/2017.
 */

public class BrushContract {

    //    this one must not be instantiated
    private BrushContract() {
    }

    //    defining content authority
    public static final String CONTENT_AUTHORITY = "com.example.android.toothbrushtracker";

    //    creating URIs base
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BRUSHES = "toothbrushtracker";

    public static final class BrushEntry implements BaseColumns {

        //        URI to acces the data
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BRUSHES);
        //        MIME for a list of brushes
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRUSHES;
        //        MIME for single brush
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BRUSHES;

//        name for the brushes table
        public final static String TABLE_NAME = "brushes";

//        here comes the columns of the table

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NAME = "name";

        public final static String COLUMN_BRAND = "brand";

        public final static String COLUMN_QUANTITY = "quantity";

    }
}
