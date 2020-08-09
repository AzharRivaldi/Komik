package com.azhar.komik.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.azhar.komik.R;
import com.azhar.komik.activities.DetailPopulerActivity;
import com.azhar.komik.adapter.AdapterKomik;
import com.azhar.komik.adapter.AdapterSlider;
import com.azhar.komik.model.ModelKomik;
import com.azhar.komik.model.ModelSlider;
import com.azhar.komik.networking.ApiEndpoint;
import com.github.islamkhsh.CardSliderViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements AdapterKomik.onSelectData {

    private RecyclerView rvTerbaru;
    private AdapterSlider adapterSlider;
    private AdapterKomik adapterKomik;
    private ProgressDialog progressDialog;
    private CardSliderViewPager cardSliderViewPager;
    private List<ModelKomik> modelKomik = new ArrayList<>();
    private List<ModelSlider> modelSlider = new ArrayList<>();
    private TextView greetText;
    private Spinner spPage;
    private String [] numberPage = {"1", "2", "3", "4", "5"
            , "6", "7", "8", "9", "10"};
    private String Page;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        greetText = rootView.findViewById(R.id.tvGreeting);
        cardSliderViewPager = rootView.findViewById(R.id.viewPager);
        spPage = rootView.findViewById(R.id.spPage);

        ArrayAdapter<String> adpWisata = new ArrayAdapter<>((getActivity()), android.R.layout.simple_spinner_item, numberPage);
        adpWisata.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPage.setAdapter(adpWisata);

        spPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object itemDB = parent.getItemAtPosition(pos);
                Page = itemDB.toString();
                getKomikTerbaru();
            }

            public void onNothingSelected(AdapterView<?> parent) { }
        });

        rvTerbaru = rootView.findViewById(R.id.rvTerbaru);
        rvTerbaru.setHasFixedSize(true);
        rvTerbaru.setLayoutManager(new LinearLayoutManager(getActivity()));

        getGreeting();
        getImageSlider();
        //getKomikTerbaru();

    }

    @SuppressLint("SetTextI18n")
    private void getGreeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12){
            greetText.setText("Selamat Pagi Azhar");
        } else if (timeOfDay >= 12 && timeOfDay < 15) {
            greetText.setText("Selamat Siang Azhar");
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            greetText.setText("Selamat Sore Azhar");
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            greetText.setText("Selamat Malam Azhar");
        }
    }

    private void getImageSlider() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.ALSOURL)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray playerArray = response.getJSONArray("manga_list");
                            for (int i = 0; i < 8; i++) {
                                JSONObject temp = playerArray.getJSONObject(i);
                                ModelSlider dataApi = new ModelSlider();
                                dataApi.setThumb(temp.getString("thumb"));
                                modelSlider.add(dataApi);
                                adapterSlider = new AdapterSlider(modelSlider);
                            }
                            cardSliderViewPager.setAdapter(new AdapterSlider(modelSlider));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getKomikTerbaru() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.BASEURL + Page)
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
                                dataApi.setThumb(temp.getString("thumb"));
                                dataApi.setType(temp.getString("type"));
                                dataApi.setUpdated(temp.getString("updated_on"));
                                dataApi.setEndpoint(temp.getString("endpoint"));
                                dataApi.setChapter(temp.getString("chapter"));
                                modelKomik.add(dataApi);
                                showKomikTerbaru();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Gagal menampilkan data!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showKomikTerbaru() {
        adapterKomik = new AdapterKomik(getActivity(), modelKomik, this);
        rvTerbaru.setAdapter(adapterKomik);
        adapterKomik.notifyDataSetChanged();
    }

    @Override
    public void onSelected(ModelKomik modelKomik) {
        Intent intent = new Intent(getActivity(), DetailPopulerActivity.class);
        intent.putExtra("detailKomik", modelKomik);
        startActivity(intent);
    }
}
