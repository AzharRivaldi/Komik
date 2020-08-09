package com.azhar.komik.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.azhar.komik.R;
import com.azhar.komik.adapter.AdapterChapter;
import com.azhar.komik.model.ModelChapter;
import com.azhar.komik.model.ModelKomik;
import com.azhar.komik.networking.ApiEndpoint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailGenreActivity extends AppCompatActivity implements AdapterChapter.onSelectData {

    Toolbar toolbar;
    TextView tvTitle, tvName, tvType, tvStatus, tvNameAuthor, tvTC, tvSynopsis;
    ImageView imgCover, imgPhoto;
    String Endpoint, Cover, Thumb, Title, Type, Status, NameAuthor, Synopsis;
    ModelKomik modelKomik;
    RecyclerView rvChapter;
    AdapterChapter adapterChapter;
    ProgressDialog progressDialog;
    List<ModelChapter> modelChapter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan detail");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modelKomik = (ModelKomik) getIntent().getSerializableExtra("detailGenre");
        if (modelKomik != null) {

            Endpoint = modelKomik.getEndpoint();
            Title = modelKomik.getTitle();
            Cover = modelKomik.getThumb();

            //set Id
            imgCover = findViewById(R.id.imgCover);
            imgPhoto = findViewById(R.id.imgPhoto);
            tvTitle = findViewById(R.id.tvTitle);
            tvName = findViewById(R.id.tvName);
            tvType = findViewById(R.id.tvType);
            tvStatus = findViewById(R.id.tvStatus);
            tvNameAuthor = findViewById(R.id.tvNameAuthor);
            tvTC = findViewById(R.id.tvTC);
            tvSynopsis = findViewById(R.id.tvSynopsis);

            tvTitle.setText(Title);
            tvName.setText(Title);
            tvTitle.setSelected(true);
            tvName.setSelected(true);
            tvTC.setVisibility(View.GONE);

            Glide.with(this)
                    .load(Cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCover);

            rvChapter = findViewById(R.id.rvChapter);
            rvChapter.setHasFixedSize(true);
            rvChapter.setLayoutManager(new LinearLayoutManager(this));

            getTotalChapter();
        }
    }

    private void getTotalChapter() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.DETAILMANGAURL)
                .addPathParameter("endpoint", Endpoint)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Type = response.getString("type");
                            Status = response.getString("status");
                            NameAuthor = response.getString("author");
                            Synopsis = response.getString("synopsis");
                            Thumb = response.getString("thumb");

                            //get Image
                            Glide.with(getApplicationContext())
                                    .load(Thumb)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(imgPhoto);

                            //set Text
                            tvType.setText("Type : " + Type);
                            tvStatus.setText("Status : " + Status);
                            tvNameAuthor.setText(NameAuthor);
                            tvSynopsis.setText(Synopsis);

                            JSONArray jsonArray = response.getJSONArray("chapter");
                            for (int x = 0; x < jsonArray.length(); x++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(x);
                                ModelChapter dataApi = new ModelChapter();

                                dataApi.setChapterTitle(jsonObject.getString("chapter_title"));
                                dataApi.setChapterEndpoint(jsonObject.getString("chapter_endpoint"));
                                modelChapter.add(dataApi);
                                showAllChapter();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailGenreActivity.this,
                                    "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailGenreActivity.this, "Tidak ada jaringan internet!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAllChapter() {
        adapterChapter = new AdapterChapter(DetailGenreActivity.this, modelChapter, this);
        rvChapter.setAdapter(adapterChapter);
    }

    @Override
    public void onSelected(ModelChapter modelChapter) {
        Intent intent = new Intent(DetailGenreActivity.this, ChapterActivity.class);
        intent.putExtra("detailChapter", modelChapter);
        startActivity(intent);
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        window.setAttributes(winParams);
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
