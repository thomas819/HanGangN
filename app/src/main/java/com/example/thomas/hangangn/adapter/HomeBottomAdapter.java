package com.example.thomas.hangangn.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.thomas.hangangn.R;
import com.example.thomas.hangangn.domain.Filters;

import java.util.List;

public class HomeBottomAdapter extends BaseQuickAdapter<Filters,BaseViewHolder>{
    public HomeBottomAdapter(int layoutResId, @Nullable List<Filters> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Filters item) {
        helper.setText(R.id.layout_home_item_tv,item.getPlaceName());
    }

}
