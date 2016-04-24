package com.imzoee.caikid.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.imzoee.caikid.R;

/**
 * Created by zoey on 2016/4/5.
 */
public class RecipeAdapter extends BaseAdapter{
    private LayoutInflater inflater;

    public RecipeAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_recipe,null);
        }
        return convertView;
    }
}
