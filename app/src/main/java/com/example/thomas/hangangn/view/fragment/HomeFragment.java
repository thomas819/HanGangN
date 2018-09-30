package com.example.thomas.hangangn.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.thomas.hangangn.R;
import com.example.thomas.hangangn.adapter.HomeAdapter;
import com.example.thomas.hangangn.domain.Place;
import com.example.thomas.hangangn.model.Address;
import com.example.thomas.hangangn.view.activity.DetailActivity;
import com.example.thomas.hangangn.view.fragment.bottomsheet.HomeBottomFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    @BindView(R.id.fragment_home_rv)
    RecyclerView mRv;
    Unbinder unbinder;
    int[] imgs = {R.drawable.gangseo, R.drawable.gwangnaru, R.drawable.nanji, R.drawable.ttukseom, R.drawable.mangwon, R.drawable.banpo, R.drawable.yanghwa, R.drawable.yeouido, R.drawable.leechon, R.drawable.jamwon, R.drawable.jamsil};
    String[] placeName = {"강서", "광나루", "난지", "뚝섬", "망원", "반포", "양화", "여의도", "이촌", "잠원", "잠실"};
    String[] placeUniqueName = {"GIGU012", "GIGU002", "GIGU010", "GIGU003", "GIGU011", "GIGU005", "GIGU009", "GIGU007", "GIGU006", "GIGU004", "GIGU001"};

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


        list.clear();
        for (int i = 0; i < placeName.length; i++) {
            Place place = new Place();
            place.setImg(imgs[i]);
            place.setName(placeName[i]);
            place.setPlaceName(placeUniqueName[i]);
            list.add(place);
        }


        HomeAdapter homeAdapter = new HomeAdapter(R.layout.layout_home_item, list);
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("placeName", placeName[position]);
                intent.putExtra("placeImg", imgs[position]);
                intent.putExtra("placeUrl", Address.get().get(position));
                startActivity(intent);
            }
        });
        mRv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRv.setAdapter(homeAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fragment_home_filterBtn)
    public void onViewClicked() {
        HomeBottomFragment homeBottomFragment = new HomeBottomFragment();
        homeBottomFragment.show(getActivity().getSupportFragmentManager(), "bottom sheet");
    }
}
