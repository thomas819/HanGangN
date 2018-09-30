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
import com.example.thomas.hangangn.util.RxEventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    @BindView(R.id.fragment_home_botttom_sport_Rv) RecyclerView sportRv;
    @BindView(R.id.fragment_home_botttom_child_Rv) RecyclerView childRv;
    @BindView(R.id.fragment_home_botttom_facilities_Rv) RecyclerView facilitiesRv;
    @BindView(R.id.fragment_home_botttom_water_Rv) RecyclerView waterRv;
    @BindView(R.id.fragment_home_botttom_way_Rv) RecyclerView wayRv;
    Unbinder unbinder;


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
        initChild();
        initFacilities();
        initWater();
        initWay();
        return view;
    }

    private void initSports() {
        List<String> place = Arrays.asList(
                "배드민턴장", "씨름장", "론볼링장", "인라인스케이트장", "족구장",
                "트랙구장", "우드볼장", "X-GAME장", "국궁장", "파크골프장", "테니스장",
                "게이트볼장", "농구장", "배구장", "축구장");

        List<String> placeUrl = Arrays.asList(
                "GeoInfoBadmintonWGS", "GeoInfoWrestlingWGS", "GeoInfoLawnBowlingWGS", "GeoInfoInlineSkateWGS", "GeoInfoJokguWGS",
                "GeoInfoTrackWGS", "GeoInfoWoodballWGS", "GeoInfoXgameWGS", "GeoInfoArcheryWGS", "GeoInfoParkGolfWGS", "GeoInfoTennisWGS",
                "GeoInfoGateballWGS", "GeoInfoBasketballWGS", "GeoInfoVolleyballWGS", "GeoInfoSoccerWGS");

        initRv(sportRv,place,placeUrl);
    }

    private void initChild() {
        List<String> place = Arrays.asList("자연학습장", "간이어린이야구장", "어린이놀이터");
        List<String> placeUrl = Arrays.asList("GeoInfoNatureStudyWGS", "GeoInfoBaseballWGS", "GeoInfoPlaygroundWGS");
        initRv(childRv,place,placeUrl);
    }

    private void initFacilities(){
        List<String> place = Arrays.asList("식수대", "매점", "주차장","자전거대여소","자전거보관소","공원안내소");
        List<String> placeUrl = Arrays.asList("GeoInfoDrinkWaterWGS", "GeoInfoStoreWGS", "GeoInfoParkParkingWGS","GeoInfoBicycleLendWGS","GeoInfoBicycleStorageWGS","GeoInfoParkOfficeWGS");
        initRv(facilitiesRv,place,placeUrl);
    }
    private void initWater(){
        List<String> place = Arrays.asList("수상레저", "수상훈련장", "오리배선착장","수상관광콜택시","수영장");
        List<String> placeUrl = Arrays.asList("GeoInfoWaterLeisureWGS", "GeoInfoWaterTrainingWGS", "GeoInfoDuckBoatWGS","GeoInfoWaterTaxiWGS","GeoInfoPoolWGS");
        initRv(waterRv,place,placeUrl);
    }
    private void initWay(){
        List<String> place = Arrays.asList("보행자도로", "인라인도로");
        List<String> placeUrl = Arrays.asList("GeoInfoWorkRoadWGS", "GeoInfoInlineRoadWGS");
        initRv(wayRv,place,placeUrl);
    }

    private void initRv(RecyclerView rv,List<String> placeName, List<String> placeUrl) {

        List<Filters> list = new ArrayList<>();
        for (int i = 0; i < placeName.size(); i++) {
            Filters filters = new Filters();
            filters.setPlaceName(placeName.get(i));
            list.add(filters);
        }

        rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        HomeBottomAdapter homeBottomAdapter = new HomeBottomAdapter(R.layout.layout_home_bottom_item, list);

        homeBottomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                initRetrofit(placeUrl.get(position));
            }
        });

        rv.setAdapter(homeBottomAdapter);
    }

    private void initRetrofit(String choice) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://openapi.seoul.go.kr:8088")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        HangangService hangangService = retrofit.create(HangangService.class);

        Call<ApiDomain> call = hangangService.test(choice);
        call.enqueue(new Callback<ApiDomain>() {
            @Override
            public void onResponse(Call<ApiDomain> call, Response<ApiDomain> response) {
                if (response.isSuccessful()) {
                    //System.out.println(response.toString());
                    //System.out.println(response.body().getGeoInfoWGS().getRow().toString());
                    RxEventBus.getInstance().sendEvent(response.body().getGeoInfoWGS().getRow());
                    dismiss();
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
