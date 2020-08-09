package com.azhar.komik.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.azhar.komik.R;
import com.azhar.komik.activities.ListGenreActivity;
import com.azhar.komik.adapter.AdapterGenre;
import com.azhar.komik.model.ModelGenre;
import com.azhar.komik.networking.ApiEndpoint;
import com.azhar.komik.utils.LayoutMarginDecoration;
import com.azhar.komik.utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GenreFragment extends Fragment implements AdapterGenre.onSelectData {

    private RecyclerView rvGenre;
    private AdapterGenre adapterGenre;
    private ProgressDialog progressDialog;
    private LayoutMarginDecoration gridMargin;
    private List<ModelGenre> modelGenre = new ArrayList<>();

    public GenreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genre, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Mohon Tunggu");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sedang menampilkan data");

        rvGenre = rootView.findViewById(R.id.rvGenre);
        rvGenre.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),
                3, RecyclerView.VERTICAL, false);
        rvGenre.setLayoutManager(mLayoutManager);
        gridMargin = new LayoutMarginDecoration(3, Tools.dp2px(getActivity(), 12));
        rvGenre.addItemDecoration(gridMargin);
        getGenre();

    }

    private void getGenre() {
        progressDialog.show();
        AndroidNetworking.get(ApiEndpoint.GENREURL)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray playerArray = response.getJSONArray("list_genre");
                            for (int i = 0; i < playerArray.length(); i++) {
                                JSONObject temp = playerArray.getJSONObject(i);
                                ModelGenre dataApi = new ModelGenre();
                                dataApi.setTitle(temp.getString("title"));
                                dataApi.setEndpoint(temp.getString("endpoint"));
                                modelGenre.add(dataApi);
                                showGenre();
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

    private void showGenre() {
        adapterGenre = new AdapterGenre(getActivity(), modelGenre, this);
        rvGenre.setAdapter(adapterGenre);
    }

    @Override
    public void onSelected(ModelGenre modelGenre) {
        Intent intent = new Intent(getActivity(), ListGenreActivity.class);
        intent.putExtra("listGenre", modelGenre);
        startActivity(intent);
    }
}
