package com.azhar.komik.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.azhar.komik.R;
import com.azhar.komik.adapter.AdapterListGenre;
import com.azhar.komik.model.ModelGenre;
import com.azhar.komik.model.ModelKomik;
import com.azhar.komik.networking.ApiEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListGenreActivity extends AppCompatActivity implements AdapterListGenre.onSelectData {

    RecyclerView rvListGenre;
    ProgressDialog progressDialog;
    AdapterListGenre adapterListGenre;
    TextView tvType;
    List<ModelKomik> modelKomik = new ArrayList<>();
    ModelGenre modelGenre;
    Toolbar toolbar;
    String Endpoint, Type;
    private Spinner spPage;
    private String [] numberPage = {"1", "2", "3", "4", "5"
            , "6", "7", "8", "9", "10"};
    private String Page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_genre);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modelGenre = (ModelGenre) getIntent().getSerializableExtra("listGenre");
        if (modelGenre != null) {

            Endpoint = modelGenre.getEndpoint();
            Type = modelGenre.getTitle();

            rvListGenre = findViewById(R.id.rvListGenre);
            rvListGenre.setHasFixedSize(true);
            rvListGenre.setLayoutManager(new LinearLayoutManager(this));

            tvType = findViewById(R.id.tvType);
            tvType.setText(Type);

            spPage = findViewById(R.id.spPage);

            ArrayAdapter<String> adpWisata = new ArrayAdapter<>((this), android.R.layout.simple_spinner_item, numberPage);
            adpWisata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPage.setAdapter(adpWisata);

            spPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Object itemDB = parent.getItemAtPosition(pos);
                    Page = itemDB.toString();
                    getGenreList();
                }

                public void onNothingSelected(AdapterView<?> parent) { }
            });

        }
    }

    private void getGenreList() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.GENREDETAIL + Page)
                .addPathParameter("endpoint", Endpoint)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            modelKomik = new ArrayList<>();
                            JSONArray playerArray = response.getJSONArray("manga_list");
                            for (int i = 0; i < playerArray.length(); i++) {
                                JSONObject temp = playerArray.getJSONObject(i);
                                ModelKomik dataApi = new ModelKomik();
                                dataApi.setTitle(temp.getString("title"));
                                dataApi.setType(temp.getString("type"));
                                dataApi.setThumb(temp.getString("thumb"));
                                dataApi.setEndpoint(temp.getString("endpoint"));
                                modelKomik.add(dataApi);
                                showGenre();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ListGenreActivity.this, "Gagal menampilkan data!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(ListGenreActivity.this, "Tidak ada jaringan internet!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showGenre() {
        adapterListGenre = new AdapterListGenre(ListGenreActivity.this, modelKomik, this);
        rvListGenre.setAdapter(adapterListGenre);
        adapterListGenre.notifyDataSetChanged();
    }

    @Override
    public void onSelected(ModelKomik modelKomik) {
        Intent intent = new Intent(ListGenreActivity.this, DetailGenreActivity.class);
        intent.putExtra("detailGenre", modelKomik);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
