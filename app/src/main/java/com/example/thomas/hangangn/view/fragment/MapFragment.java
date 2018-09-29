package com.example.thomas.hangangn.view.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.thomas.hangangn.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    @BindView(R.id.fragment_map_map_view)
    MapView mMapView;
    Unbinder unbinder;

    GoogleMap mMap;
    @BindView(R.id.fragment_map_my_location) TextView myLocation;
    @BindView(R.id.fragment_map_arrive_location) TextView arriveLocation;
    @BindView(R.id.fragment_map_arrive_location_tv) TextView test;
    private RxLocation rxLocation;
    private LocationRequest locationRequest;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private String serverKey = "AIzaSyAbEoltkio5_B-aCB0FBHaLWi7KxvfMkbo";
    private LatLng origin = new LatLng(37.7849569, -122.4068855);
    private LatLng destination = new LatLng(37.7814432, -122.4460177);
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

        initPermission();
        checkPlayServicesAvailable();

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(test, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
                initDirectionMap();
            }
        });
        return view;
    }

    private void initDirectionMap(){
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .transportMode(TransportMode.WALKING)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        Snackbar.make(test, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
                        if(direction.isOK()) {
                            // Do something
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);

                            ArrayList<LatLng> directionPositionList =leg.getDirectionPoint();

                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(getActivity(),directionPositionList,5, Color.RED);
                            mMap.addPolyline(polylineOptions);

                            LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
                            LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
                            LatLngBounds bounds = new LatLngBounds(southwest, northeast);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

                            Toast.makeText(getActivity(), "성공", Toast.LENGTH_SHORT).show();
                        } else {
                            // Do something
                            System.out.println("GoogleDirection Error = "+direction.getErrorMessage());
                            Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        System.out.println("onDirectionFailure = "+t.getMessage());
                    }
                });
    }

    private void initLocation() {
        rxLocation = new RxLocation(getActivity());
        rxLocation.setDefaultTimeout(15, TimeUnit.SECONDS);

        this.locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);

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
                mMap.setMinZoomPreference(15.0f);
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
        if(address !=null){
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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

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
