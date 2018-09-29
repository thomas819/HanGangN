package com.example.thomas.hangangn.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thomas.hangangn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {


    @BindView(R.id.fragment_favorites_tv)
    TextView favoritesTv;
    Unbinder unbinder;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        unbinder = ButterKnife.bind(this, view);

//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//
//        Retrofit retrofit = new Retrofit
//                .Builder()
//                .baseUrl("http://openapi.seoul.go.kr:8088")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//
//        HangangService hangangService = retrofit.create(HangangService.class);
//
//        Call<ApiDomain> call = hangangService.test("GeoInfoDrinkWaterWGS");
//    call.enqueue(new Callback<ApiDomain>() {
//        @Override
//        public void onResponse(Call<ApiDomain> call, Response<ApiDomain> response) {
//            if(response.isSuccessful()){
//                printLn(response.toString());
//                printLn(response.body().getGeoInfoDrinkWaterWGS().getRow().get(0).getGIGU());
//            }
//        }
//
//        @Override
//        public void onFailure(Call<ApiDomain> call, Throwable t) {
//
//        }
//    });
        return view;
    }


    private void printLn(String a){
        System.out.println(a);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
