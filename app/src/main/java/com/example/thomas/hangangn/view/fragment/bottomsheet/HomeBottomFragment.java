package com.example.thomas.hangangn.view.fragment.bottomsheet;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.hangangn.R;
import com.example.thomas.hangangn.adapter.HomeBottomAdapter;
import com.example.thomas.hangangn.domain.ApiDomain;
import com.example.thomas.hangangn.domain.Filters;
import com.example.thomas.hangangn.model.HangangService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeBottomFragment extends BottomSheetDialogFragment {

    @BindView(R.id.fragment_home_botttom_sport)
    RecyclerView sportRv;
    Unbinder unbinder;
    List<Filters> list = new ArrayList<>();

    public HomeBottomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_bottom, container, false);
        unbinder = ButterKnife.bind(this, view);
        initSports();
        return view;
    }

    private void initSports() {
        List<String> placeUrl = Arrays.asList(
                "GeoInfoBadmintonWGS",
                "GeoInfoWrestlingWGS",
                "GeoInfoLawnBowlingWGS",
                "GeoInfoInlineSkateWGS",
                "GeoInfoJokguWGS",
                "GeoInfoTrackWGS",
                "GeoInfoWoodballWGS",
                "GeoInfoXgameWGS",
                "GeoInfoArcheryWGS",
                "GeoInfoParkGolfWGS",
                "GeoInfoTennisWGS",
                "GeoInfoGateballWGS",
                "GeoInfoBasketballWGS",
                "GeoInfoVolleyballWGS",
                "GeoInfoSoccerWGS");


        List<String> place = Arrays.asList(
                "배드민턴장",
                "씨름장",
                "론볼링장",
                "인라인스케이트장",
                "족구장",
                "트랙구장",
                "우드볼장",
                "X-GAME장",
                "국궁장",
                "파크골프장",
                "테니스장",
                "게이트볼장",
                "농구장",
                "배구장",
                "축구장");

        for (int i = 0; i < place.size(); i++) {
            Filters filters = new Filters();
            filters.setPlaceName(place.get(i));
            list.add(filters);
        }

        sportRv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        HomeBottomAdapter homeBottomAdapter = new HomeBottomAdapter(R.layout.layout_home_bottom_item, list);

        homeBottomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                initData(placeUrl.get(position));
            }
        });

        sportRv.setAdapter(homeBottomAdapter);

    }

    private void initData(String choice) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        



        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://openapi.seoul.go.kr:8088")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        HangangService hangangService = retrofit.create(HangangService.class);

        Call<ApiDomain> call = hangangService.test(choice);
        call.enqueue(new Callback<ApiDomain>() {
            @Override
            public void onResponse(Call<ApiDomain> call, Response<ApiDomain> response) {
                if(response.isSuccessful()){
                    System.out.println(response.toString());
                    System.out.println(response.body().getGeoInfoJokguWGS().toString());
                    //RxEventBus.getInstance().sendEvent(response.body().getGeoInfoJokguWGS().getRow());

                }
            }

            @Override
            public void onFailure(Call<ApiDomain> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
