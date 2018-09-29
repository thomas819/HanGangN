package com.example.thomas.hangangn.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.thomas.hangangn.R;
import com.example.thomas.hangangn.domain.Place;

import java.util.List;

public class HomeAdapter extends BaseQuickAdapter<Place, BaseViewHolder> {

    public HomeAdapter(int layoutResId, @Nullable List<Place> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Place item) {

        helper.setText(R.id.home_item_tv, item.getName());
        Glide.with(mContext).load(item.getImg()).apply(new RequestOptions().centerCrop().autoClone()).into((ImageView) helper.getView(R.id.home_item_img));

        helper.setVisible(R.id.home_item_color,true);
    }
}
