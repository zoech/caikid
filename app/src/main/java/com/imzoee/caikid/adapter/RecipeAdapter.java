package com.imzoee.caikid.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.dao.Recipe;
import com.imzoee.caikid.utils.misc.ObservablesFactory;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zoey on 2016/4/5.
 *
 * Adapter for RecipeFragment's recipe list view.
 */
public class RecipeAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;

    private List<Recipe> recipesList = null;

    public RecipeAdapter(Context context, List<Recipe> recipesList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.recipesList = recipesList;
    }

    public void setRecipesList(List<Recipe> recipesList){
        this.recipesList = recipesList;
    }

    @Override
    public int getCount() {
        return this.recipesList.size();
    }

    @Override
    public Recipe getItem(int position){
        return this.recipesList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        final Recipe recipe = recipesList.get(position);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_recipe,null);

            holder = new ViewHolder();
            holder.ivImg = (ImageView) convertView.findViewById(R.id.iv_recipe_shortcut);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_recipe_name);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_recipe_desc);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_recipe_price);
            holder.tvSales = (TextView) convertView.findViewById(R.id.tv_recipe_sales);
            holder.tvCommentsNum = (TextView) convertView.findViewById(R.id.tv_comment_number);
            holder.btCart = (Button) convertView.findViewById(R.id.bt_add_to_cart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Picasso.with(context)
                .load(recipe.getImg_path())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_local_dining_black_48dp)
                .error(R.drawable.ic_info_black_48dp)
                .into(holder.ivImg);

        holder.tvName.setText(recipe.getName());
        holder.tvDesc.setText(recipe.getInfo());
        holder.tvPrice.setText(String.valueOf(recipe.getPrice()));
        holder.tvSales.setText(String.valueOf(recipe.getSales()));
        //holder.tvCommentsNum.setText();
        holder.btCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApp.getCart().addItem(recipe);
                ObservablesFactory.cartActionObservable(BaseApp.getCart());
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public ImageView ivImg;
        public TextView tvName;
        public TextView tvDesc;
        public TextView tvSales;
        public TextView tvPrice;
        public TextView tvCommentsNum;
        public Button btCart;
    }
}
