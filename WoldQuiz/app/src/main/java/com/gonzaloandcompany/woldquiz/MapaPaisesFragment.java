package com.gonzaloandcompany.woldquiz;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gonzaloandcompany.woldquiz.models.ItemsMap;
import com.gonzaloandcompany.woldquiz.models.MarkerClusterRenderer;
import com.gonzaloandcompany.woldquiz.models.Pais;
import com.gonzaloandcompany.woldquiz.service.PaisService;
import com.gonzaloandcompany.woldquiz.service.ServiceGeneratorPais;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapaPaisesFragment extends Fragment implements OnMapReadyCallback {

    PaisService paisService;
    List<Pais> listaPaises;

    public MapaPaisesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }


    public void onMapReady(GoogleMap googleMap) {
        setUpClusterManager(googleMap);
    }

    private void setUpClusterManager(GoogleMap googleMap) {
        ClusterManager<ItemsMap> clusterManager = new ClusterManager(getContext(), googleMap);
        googleMap.setOnCameraIdleListener(clusterManager);

        clusterManager.setRenderer(new MarkerClusterRenderer(getContext(), googleMap, clusterManager));

        paisService = ServiceGeneratorPais.createService(PaisService.class);
        Call<List<Pais>> allCountries = paisService.listPaises();
        allCountries.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                if (response.isSuccessful()) {
                    listaPaises = response.body();
                    for (Pais p : listaPaises) {
                        if (!p.getLatlng().isEmpty()) {
                            Glide.with(getContext())
                                    .asBitmap()
                                    .load("https://www.countryflags.io/" + p.getAlpha2Code() + "/flat/64.png")
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            ItemsMap a = new ItemsMap(p.getLatlng().get(0), p.getLatlng().get(1), p.getName(), p.getCapital(), resource, p);
                                            clusterManager.addItem(a);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });

                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {
                Log.e("Network Failure", t.getMessage());

            }
        });
        clusterManager.cluster();


    }

}