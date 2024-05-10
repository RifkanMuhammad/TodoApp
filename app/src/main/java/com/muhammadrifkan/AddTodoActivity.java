package com.muhammadrifkan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadrifkan.connector.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTodoActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView TvStatus;
    Button BtnProses;
    EditText TxID, TxNama, TxJudul, Txtanggal, TxStatus;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        dbHelper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxID = (EditText)findViewById(R.id.txID);
        TxNama = (EditText)findViewById(R.id.txJudul);
        TxJudul = (EditText)findViewById(R.id.txDetail);
        Txtanggal = (EditText)findViewById(R.id.txTanggal);
        TxStatus = (EditText)findViewById(R.id.txStatus);

        TvStatus = (TextView)findViewById(R.id.tVStatus);
        BtnProses = (Button)findViewById(R.id.btnProses);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        getData();

        Txtanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        BtnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesSelesai();
            }
        });

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);
    }

    private void prosesSelesai() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddTodoActivity.this);
        builder.setMessage("Taks telah selesai?");
        builder.setCancelable(true);
        builder.setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idtask = TxID.getText().toString().trim();
                String selesai = "Selesai";

                ContentValues values = new ContentValues();

                values.put(DBHelper.row_status, selesai);
                dbHelper.updateData(values, id);
                Toast.makeText(AddTodoActivity.this, "Taks telah berhasil diselesaikan!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                Txtanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData() {
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        Cursor cur = dbHelper.oneData(id);
        if(cur.moveToFirst()){
            String idtaks = cur.getString(cur.getColumnIndex(DBHelper.row_id));
            String judul = cur.getString(cur.getColumnIndex(DBHelper.row_judul));
            String detail = cur.getString(cur.getColumnIndex(DBHelper.row_detail));
            String Tanggal = cur.getString(cur.getColumnIndex(DBHelper.row_tanggal));
            String status = cur.getString(cur.getColumnIndex(DBHelper.row_status));

            TxID.setText(idtaks);
            TxNama.setText(judul);
            TxJudul.setText(detail);
            Txtanggal.setText(Tanggal);
            TxStatus.setText(status);

            if (TxID.equals("")){
                TvStatus.setVisibility(View.GONE);
                TxStatus.setVisibility(View.GONE);
                BtnProses.setVisibility(View.GONE);
            }else{
                TvStatus.setVisibility(View.VISIBLE);
                TxStatus.setVisibility(View.VISIBLE);
                BtnProses.setVisibility(View.VISIBLE);
            }

            if(status.equals("Proses")){
                BtnProses.setVisibility(View.VISIBLE);
            }else {
                BtnProses.setVisibility(View.GONE);
                TxNama.setEnabled(false);
                TxJudul.setEnabled(false);
                Txtanggal.setEnabled(false);
                TxStatus.setEnabled(false);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        String idtask = TxID.getText().toString().trim();
        String status = TxStatus.getText().toString().trim();

        MenuItem itemDelete = menu.findItem(R.id.action_delete);
        MenuItem itemClear = menu.findItem(R.id.action_clear);
        MenuItem itemSave = menu.findItem(R.id.action_save);

        if (idtask.equals("")){
            itemDelete.setVisible(false);
            itemClear.setVisible(true);
        }else {
            itemDelete.setVisible(true);
            itemClear.setVisible(false);
        }

        if(status.equals("Selesai")){
            itemSave.setVisible(false);
            itemDelete.setVisible(true);
            itemClear.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertAndUpdate();
        }
        switch (item.getItemId()){
            case R.id.action_clear:
                TxNama.setText("");
                TxJudul.setText("");
                Txtanggal.setText("");
        }
        switch (item.getItemId()){
            case R.id.action_delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddTodoActivity.this);
                builder.setMessage("Taks ini akan dihapus, yakin?");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteData(id);
                        Toast.makeText(AddTodoActivity.this, "Taks berhasil dihapus!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertAndUpdate(){
        String idtask = TxID.getText().toString().trim();
        String nama = TxNama.getText().toString().trim();
        String judul = TxJudul.getText().toString().trim();
        String tglKembali = Txtanggal.getText().toString().trim();
        String status = "Proses";

        ContentValues values = new ContentValues();

        values.put(DBHelper.row_judul, nama);
        values.put(DBHelper.row_detail, judul);
        values.put(DBHelper.row_tanggal, tglKembali);
        values.put(DBHelper.row_status, status);

        if (nama.equals("") || judul.equals("") || tglKembali.equals("")){
            Toast.makeText(AddTodoActivity.this, "Ooops, taks anda tidak valid!", Toast.LENGTH_SHORT).show();
        }else {
            if(idtask.equals("")){
                dbHelper.insertData(values);
            }else {
                dbHelper.updateData(values, id);
            }

            Toast.makeText(AddTodoActivity.this, "Taks baru berhasil disimpan!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
