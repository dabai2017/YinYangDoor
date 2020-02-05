package com.dabai.TaiChi.utils;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dabai.TaiChi.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AppsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public AppsAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, @Nullable String s) {
        baseViewHolder.setText(R.id.fruit_name,"1");
    }
}