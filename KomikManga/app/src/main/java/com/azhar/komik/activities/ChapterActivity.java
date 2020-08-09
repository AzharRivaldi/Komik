package com.azhar.komik.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.azhar.komik.R;
import com.azhar.komik.adapter.AdapterImageChapter;
import com.azhar.komik.model.ModelChapter;
import com.azhar.komik.networking.ApiEndpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTitle, tvSubTitle;
    ImageView imgChapter;
    String ChapterEndpoint, Title, Subtitle;
    ModelChapter modelChapter;
    ViewPager viewPager;
    Button btnNext, btnPrev;
    ProgressDialog progressDialog;
    AdapterImageChapter adapter;
    List<ModelChapter> modelChapters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan gambar");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modelChapter = (ModelChapter) getIntent().getSerializableExtra("detailChapter");
        if (modelChapter != null) {

            ChapterEndpoint = modelChapter.getChapterEndpoint();
            Title = modelChapter.getChapterTitle();

            tvTitle = findViewById(R.id.tvTitle);
            tvTitle.setText(Title);
            tvTitle.setSelected(true);

            tvSubTitle = findViewById(R.id.tvSubTitle);
            tvSubTitle.setText(Title);

            viewPager = findViewById(R.id.viewPager);
            btnNext = findViewById(R.id.btnNext);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentItem = viewPager.getCurrentItem();
                    viewPager.setCurrentItem(currentItem + 1);
                }
            });
            btnPrev = findViewById(R.id.btnPrev);
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentItem = viewPager.getCurrentItem();
                    viewPager.setCurrentItem(currentItem - 1);
                }
            });
            
            getChapterImage();
        }
    }

    private void getChapterImage() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.CHAPTERURL)
                .addPathParameter("chapter_endpoint", ChapterEndpoint)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray playerArray = response.getJSONArray("chapter_image");
                            for (int i = 0; i < playerArray.length(); i++) {
                                JSONObject temp = playerArray.getJSONObject(i);
                                ModelChapter dataApi = new ModelChapter();
                                dataApi.setChapterImage(temp.getString("chapter_image_link"));
                                dataApi.setImageNumber(temp.getString("image_number"));

                                Subtitle = response.getString("title");
                                tvSubTitle.setText(Subtitle);
                                modelChapters.add(dataApi);
                                setImage();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChapterActivity.this, "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(ChapterActivity.this, "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setImage() {
        adapter = new AdapterImageChapter(modelChapters, this);
        viewPager.setAdapter(adapter);
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
