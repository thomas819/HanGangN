package com.example.thomas.hangangn;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thomas.hangangn.adapter.HomeAdapter;
import com.example.thomas.hangangn.domain.Place;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {



    @BindView(R.id.fragment_home_rv) RecyclerView mRv;
    Unbinder unbinder;
    int[] imgs={R.drawable.gangseo,R.drawable.gwangnaru,R.drawable.nanji,R.drawable.ttukseom,R.drawable.mangwon,R.drawable.banpo,R.drawable.yanghwa,R.drawable.yeouido,R.drawable.leechon,R.drawable.jamwon,R.drawable.jamsil};
    String[] placeName={"강서","광나루","난지","뚝섬","망원","반포","양화","여의도","이촌","잠원","잠실"};
    List<Place> list = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        for(int i=0;i<placeName.length;i++){
            Place place = new Place();
            place.setImg(imgs[i]);
            place.setName(placeName[i]);
            list.add(place);
        }

        HomeAdapter homeAdapter = new HomeAdapter(R.layout.home_item,list);
        mRv.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        mRv.setAdapter(homeAdapter);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
