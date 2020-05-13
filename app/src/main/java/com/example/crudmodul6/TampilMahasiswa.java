package com.example.crudmodul6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TampilMahasiswa extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextNIM;
    private EditText editTextNama;
    private EditText editTextJurusan;
    private EditText editTextFakultas;
    private EditText editTextEmail;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String nim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_mahasiswa);

        Intent intent = getIntent();

        nim = intent.getStringExtra(konfigurasi.MHS_NIM);

        editTextNIM = (EditText) findViewById(R.id.editTextNIM);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextJurusan = (EditText) findViewById(R.id.editTextJurusan);
        editTextFakultas = (EditText) findViewById(R.id.editTextFakultas);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        editTextNIM.setText(nim);

        getMahasiswa();
    }

    private void getMahasiswa() {
        class GetMahasiswa extends  AsyncTask<Void, Void, String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilMahasiswa.this, "Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showMahasiswa(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_MHS,nim);
                return s;
            }
        }
        GetMahasiswa gm = new GetMahasiswa();
        gm.execute();
    }

    private void showMahasiswa(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nim = c.getString(konfigurasi.TAG_NIM);
            String nama = c.getString(konfigurasi.TAG_NAMA);
            String jurusan = c.getString(konfigurasi.TAG_JURUSAN);
            String fakultas = c.getString(konfigurasi.TAG_FAKULTAS);
            String email = c.getString(konfigurasi.TAG_EMAIL);

            editTextNIM.setText(nim);
            editTextNama.setText(nama);
            editTextJurusan.setText(jurusan);
            editTextFakultas.setText(fakultas);
            editTextEmail.setText(email);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateMahasiswa() {
        final String nim = editTextNIM.getText().toString().trim();
        final String nama = editTextNama.getText().toString().trim();
        final String jurusan = editTextJurusan.getText().toString().trim();
        final String fakultas = editTextFakultas.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        class UpdateMahasiswa extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilMahasiswa.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilMahasiswa.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_MHS_NIM, nim);
                hashMap.put(konfigurasi.KEY_MHS_NAMA, nama);
                hashMap.put(konfigurasi.KEY_MHS_JURUSAN, jurusan);
                hashMap.put(konfigurasi.KEY_MHS_FAKULTAS, fakultas);
                hashMap.put(konfigurasi.KEY_MHS_EMAIL, email);

                RequestHandler rh = new RequestHandler();
                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE_MHS, hashMap);
                return s;
            }
        }
        UpdateMahasiswa um = new UpdateMahasiswa();
        um.execute();
    }

    private void deleteMahasiswa() {
        class DeleteMahasiswa extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilMahasiswa.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(TampilMahasiswa.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE_MHS, nim);
                return s;
            }
        }

        DeleteMahasiswa dm = new DeleteMahasiswa();
        dm.execute();
    }

    private void confirmDeleteMahasiswa(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hapus Data Mahasiswa?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteMahasiswa();
                        startActivity(new Intent(TampilMahasiswa.this,TampilSemuaMhs.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateMahasiswa();
        }

        if(v == buttonDelete){
            confirmDeleteMahasiswa();
        }
    }
}
