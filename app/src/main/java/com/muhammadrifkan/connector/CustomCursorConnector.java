package com.muhammadrifkan.connector;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.muhammadrifkan.R;


public class CustomCursorConnector extends CursorAdapter {

    private LayoutInflater ly;
    private SparseBooleanArray mSelectedItems;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CustomCursorConnector(Context context, Cursor c, int flags) {
        super(context, c, flags);
        ly = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedItems = new SparseBooleanArray();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View v = ly.inflate(R.layout.list_todo, viewGroup, false);
        MyHolder holder = new MyHolder();

        holder.ListID = (TextView)v.findViewById(R.id.listID);
        holder.ListJudul = (TextView)v.findViewById(R.id.listJudul);
        holder.ListDetail = (TextView)v.findViewById(R.id.listDetail);
        holder.ListTanggal = (TextView)v.findViewById(R.id.listTanggal);
        holder.ListStatus = (TextView)v.findViewById(R.id.listStatus);

        v.setTag(holder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        MyHolder holder = (MyHolder)view.getTag();

        holder.ListID.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_id)));
        holder.ListJudul.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_detail)));
        holder.ListDetail.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_judul)));
        holder.ListTanggal.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_tanggal)));
        holder.ListStatus.setText(cursor.getString(cursor.getColumnIndex(DBHelper.row_status)));
    }

    class MyHolder{
        TextView ListID;
        TextView ListJudul;
        TextView ListDetail;
        TextView ListTanggal;
        TextView ListStatus;
    }
}
