package com.example.android.toothbrushtracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by danok on 7/18/2017.
 */

public class BrushCursorAdapter extends CursorAdapter {

    public BrushCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.list_name);
        TextView brandTextView = (TextView) view.findViewById(R.id.list_brand);
        TextView quantityTextView = (TextView) view.findViewById(R.id.list_quantity);

        int nameColumnIndex = cursor.getColumnIndex(BrushContract.BrushEntry.COLUMN_NAME);
        int brandColumnIndex = cursor.getColumnIndex(BrushContract.BrushEntry.COLUMN_BRAND);
        int quantityColumnIndex = cursor.getColumnIndex(BrushContract.BrushEntry.COLUMN_QUANTITY);

        String brushName = cursor.getString(nameColumnIndex);
        String brushBrand = cursor.getString(brandColumnIndex);
        String brushQuantity = cursor.getString(quantityColumnIndex);


        nameTextView.setText(brushName);
        brandTextView.setText(brushBrand);
        quantityTextView.setText(brushQuantity);
    }
}
