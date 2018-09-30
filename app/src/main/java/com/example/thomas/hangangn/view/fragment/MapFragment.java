package com.example.thomas.hangangn.view.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.thomas.hangangn.R;
import com.example.thomas.hangangn.domain.ApiDomain;
import com.example.thomas.hangangn.domain.Place;
import com.example.thomas.hangangn.model.HangangService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    @BindView(R.id.fragment_map_map_view)
    MapView mMapView;
    Unbinder unbinder;

    GoogleMap mMap;
    @BindView(R.id.fragment_map_my_location) TextView myLocation;
    @BindView(R.id.fragment_map_arrive_location_auto_tv) AutoCompleteTextView arriveLocationTv;

    private RxLocation rxLocation;
    private LocationRequest locationRequest;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private String serverKey = "AIzaSyAbEoltkio5_B-aCB0FBHaLWi7KxvfMkbo";
    private LatLng origin = new LatLng(37.7849569, -122.4068855);
    private LatLng destination = new LatLng(37.7814432, -122.4460177);

    String[] placeName = {"강서", "광나루", "난지", "뚝섬", "망원", "반포", "양화", "여의도", "이촌", "잠원", "잠실"};
    String[] placeUniqueName = {"GIGU012", "GIGU002", "GIGU010", "GIGU003", "GIGU011", "GIGU005", "GIGU009", "GIGU007", "GIGU006", "GIGU004", "GIGU001"};

    private List<LatLng> placeLocation= new ArrayList<>();

    List<String> gigu = new ArrayList<>();

    List<Place> places= new ArrayList<>();

    LatLng myLatLng;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //액티비티 처음실행시 실행
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);

        for(int i=0;i<placeName.length;i++){
            Place place = new Place();
            place.setName(placeName[i]);
            place.setPlaceName(placeUniqueName[i]);
            places.add(place);
        }

        initPermission();
        checkPlayServicesAvailable();

        placeList();

        return view;
    }

    private void placeList() {
        List<String> place = Arrays.asList(
                "배드민턴장", "씨름장", "론볼링장", "인라인스케이트장", "족구장",
                "트랙구장", "우드볼장", "X-GAME장", "국궁장", "파크골프장", "테니스장",
                "게이트볼장", "농구장", "배구장", "축구장", "자연학습장", "간이어린이야구장", "어린이놀이터",
                "식수대", "매점", "주차장", "자전거대여소", "자전거보관소", "공원안내소",
                "수상레저", "수상훈련장", "오리배선착장", "수상관광콜택시", "수영장",
                "보행자도로", "인라인도로");

        List<String> placeUrl = Arrays.asList(
                "GeoInfoBadmintonWGS", "GeoInfoWrestlingWGS", "GeoInfoLawnBowlingWGS", "GeoInfoInlineSkateWGS", "GeoInfoJokguWGS",
                "GeoInfoTrackWGS", "GeoInfoWoodballWGS", "GeoInfoXgameWGS", "GeoInfoArcheryWGS", "GeoInfoParkGolfWGS", "GeoInfoTennisWGS",
                "GeoInfoGateballWGS", "GeoInfoBasketballWGS", "GeoInfoVolleyballWGS", "GeoInfoSoccerWGS",
                "GeoInfoNatureStudyWGS", "GeoInfoBaseballWGS", "GeoInfoPlaygroundWGS",
                "GeoInfoDrinkWaterWGS", "GeoInfoStoreWGS", "GeoInfoParkParkingWGS", "GeoInfoBicycleLendWGS", "GeoInfoBicycleStorageWGS", "GeoInfoParkOfficeWGS",
                "GeoInfoWaterLeisureWGS", "GeoInfoWaterTrainingWGS", "GeoInfoDuckBoatWGS", "GeoInfoWaterTaxiWGS", "GeoInfoPoolWGS",
                "GeoInfoWorkRoadWGS", "GeoInfoInlineRoadWGS");


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, place);

        arriveLocationTv.setAdapter(stringArrayAdapter);
        arriveLocationTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                int pos = place.indexOf(parent.getItemAtPosition(position));
                initRetrofit(placeUrl.get(pos));

                //System.out.println("position = "+parent.getItemAtPosition(position)+" po2 = "+place.indexOf(parent.getItemAtPosition(position)));
//                System.out.println(place.get(pos)+"를 누름 "+placeUrl.get(pos));
            }
        });
    }

    private void initDirectionMap(LatLng myLoca,LatLng arriveLoca) {
        GoogleDirection.withServerKey(serverKey)
                .from(arriveLoca)
                .to(myLoca)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        Snackbar.make(arriveLocationTv, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
                        if (direction.isOK()) {
                            // Do something
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);

                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();

                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.RED);
                            mMap.addPolyline(polylineOptions);

                            LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
                            LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
                            LatLngBounds bounds = new LatLngBounds(southwest, northeast);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

                            Toast.makeText(getActivity(), "성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // Do something
                            System.out.println("GoogleDirection Error = " + direction.getStatus());
                            Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        System.out.println("onDirectionFailure = " + t.getMessage());
                    }
                });
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
                   // RxEventBus.getInstance().sendEvent(response.body().getGeoInfoWGS().getRow());
                    gigu.clear();
                    placeLocation.clear();
                    for(int i=0;i<response.body().getGeoInfoWGS().getRow().size();i++){
                        Double lat = Double.valueOf(response.body().getGeoInfoWGS().getRow().get(i).getLAT());
                        Double lng = Double.valueOf(response.body().getGeoInfoWGS().getRow().get(i).getLNG());
                        LatLng arrive = new LatLng(lat,lng);
                        placeLocation.add(arrive);
                        //gigu.add(response.body().getGeoInfoWGS().getRow().get(i).getGIGU());
                        for(int j=0;j<places.size();j++){
                            if(response.body().getGeoInfoWGS().getRow().get(i).getGIGU().equals(places.get(j).getPlaceName())){
                                gigu.add(places.get(j).getName());
                            }
                        }

                    }
//                    for(int i=0;i<gigu.size();i++){
//                        gigu.get(i).equals(placeName);
//                    }
                    String[] arr =gigu.toArray(new String[gigu.size()]);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(arr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMap.clear();
                            System.out.println("beforeDirection = "+myLatLng+" : " + placeLocation.get(which));


                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(placeLocation.get(which));
                            mMap.addMarker(markerOptions);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(placeLocation.get(which)));
                            //initDirectionMap(myLatLng,placeLocation.get(which));
                            arriveLocationTv.setText("");
                            arriveLocationTv.clearFocus();
                            View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }

                        }
                    });
                    builder.create().show();
                }
            }

            @Override
            public void onFailure(Call<ApiDomain> call, Throwable t) {

            }
        });
    }

    private void initLocation() {
        rxLocation = new RxLocation(getActivity());
        rxLocation.setDefaultTimeout(15, TimeUnit.SECONDS);

        this.locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1);

        disposable.add(//gps 켜기 and 갖고오기
                rxLocation.settings().checkAndHandleResolution(locationRequest)
                        .flatMapObservable(this::getAddressObservable)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::onAddressUpdate, throwable -> Log.e("MainPresenter", "Error fetching location/address updates", throwable))
        );
    }


    private void initMap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);//내위치보기
                mMap.getUiSettings().setZoomControlsEnabled(true);//확대축소 버튼
                mMap.getUiSettings().setCompassEnabled(true);//보통안뜨다 지도회전시 나침판뜸
                mMap.getUiSettings().setRotateGesturesEnabled(false);//손가락 2개로 지도 회전 불가하게
                mMap.setMinZoomPreference(14.5f);
            }
        });
    }


    @SuppressLint("MissingPermission")
    private Observable<Address> getAddressObservable(boolean success) {
        if (success) {
            return rxLocation.location().updates(locationRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(this::onLocationUpdate)
                    .flatMap(this::getAddressFromLocation);

        } else {
            // this.onLocationSettingsUnsuccessful();

            return rxLocation.location().lastLocation()
                    .doOnSuccess(this::onLocationUpdate)
                    .flatMapObservable(this::getAddressFromLocation);
        }
    }

    public void onAddressUpdate(Address address) {
        if (address != null) {
            myLocation.setText(getAddressText(address));
        }
    }

    private String getAddressText(Address address) {
        String addressText = "";
        final int maxAddressLineIndex = address.getMaxAddressLineIndex();

        for (int i = 0; i <= maxAddressLineIndex; i++) {
            addressText += address.getAddressLine(i);
            if (i != maxAddressLineIndex) {
                addressText += "\n";
            }
        }

        return addressText;
    }
    public void onLocationUpdate(Location location) {
        myLatLng= new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
    }

    private Observable<Address> getAddressFromLocation(Location location) {
        return rxLocation
                .geocoding()
                .fromLocation(location)
                .toObservable()
                .subscribeOn(Schedulers.io());
    }

    @SuppressLint("CheckResult")
    private void initPermission() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions
                .requestEachCombined(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .subscribe(permission -> {
                    if (permission.granted) {
                        //모든권한 허락함
                        //Log.e("granted","granted");
                        initMap();
                        initLocation();
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        //권한을 거부함
                        Toast.makeText(getActivity(), "권한을 수락해야만 사용할수 있어요ㅠ", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    } else {
                        //권한 다시보지 않기 체크 시
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        //Log.e("granted","else");
                    }
                });
    }

    private void checkPlayServicesAvailable() {
        final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int status = apiAvailability.isGooglePlayServicesAvailable(getActivity());

        if (status != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(status)) {
                apiAvailability.getErrorDialog(getActivity(), status, 1).show();
            } else {
                // Snackbar.make(lastUpdate, "Google Play Services unavailable. This app will not work", Snackbar.LENGTH_INDEFINITE).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            mMapView.onSaveInstanceState(outState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
