package com.example.crudmodul6;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextNIM;
    private EditText editTextNama;
    private EditText editTextJurusan;
    private EditText editTextFakultas;
    private EditText editTextEmail;

    private Button buttonAdd;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNIM = (EditText) findViewById(R.id.editTextNIM);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextJurusan = (EditText) findViewById(R.id.editTextJurusan);
        editTextFakultas = (EditText) findViewById(R.id.editTextFakultas);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);

        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);
    }

    private void addMahasiswa() {

        final String nim = editTextNIM.getText().toString().trim();
        final String nama = editTextNama.getText().toString().trim();
        final String jurusan = editTextJurusan.getText().toString().trim();
        final String fakultas = editTextFakultas.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        class AddMahasiswa extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_MHS_NIM, nim);
                params.put(konfigurasi.KEY_MHS_NAMA, nama);
                params.put(konfigurasi.KEY_MHS_JURUSAN, jurusan);
                params.put(konfigurasi.KEY_MHS_FAKULTAS, fakultas);
                params.put(konfigurasi.KEY_MHS_EMAIL, email);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD, params);
                return res;
            }
        }

        AddMahasiswa am = new AddMahasiswa();
        am.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAdd) {
            addMahasiswa();
        }

        if (v == buttonView) {
            startActivity(new Intent(this, TampilSemuaMhs.class));
        }
    }
}
