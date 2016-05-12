package com.imzoee.caikid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.imzoee.caikid.R;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Comment;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zoey on 2016/5/12.
 *
 * Adapter used in recipe detail activity.
 */
public class RecipeCommentsAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    private List<Comment> commentList = null;
    private int headerCount = 0;
    private int footerCount = 0;

    public RecipeCommentsAdapter(Context context, List<Comment> commentsList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.commentList = commentsList;
    }

    public void setRecipesList(List<Comment> commentList){
        this.commentList = commentList;
    }

    public void setHeaderCount(int count){
        headerCount = count;
    }

    public void setFooterCount(int count){
        footerCount = count;
    }

    @Override
    public int getCount() {
        return 5;//this.commentList.size();
    }

    @Override
    public Comment getItem(int position){
        return this.commentList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        //final Comment comment = commentList.get(position);

        /* test */
        String[] lll = {"haha", "shit", "eee", "u", "what?"};
        /* test */

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_recipe_comment,null);

            holder = new ViewHolder();

            holder.tv = (TextView) convertView.findViewById(R.id.tv_test);
            /*
            holder.ivImg = (ImageView) convertView.findViewById(R.id.iv_recipe_shortcut);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_recipe_name);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_recipe_desc);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_recipe_price);
            holder.tvSales = (TextView) convertView.findViewById(R.id.tv_recipe_sales);
            holder.tvCommentsNum = (TextView) convertView.findViewById(R.id.tv_comment_number);
            holder.btCart = (Button) convertView.findViewById(R.id.bt_add_to_cart);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rbar_recipe_rate);
            holder.tvRating = (TextView) convertView.findViewById(R.id.tv_recipe_rate_numbers);
            */
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(lll[position]);
/*
        Picasso.with(context)
                .load(ConstConv.IMGPATH_URLPREFIX + comment.getImg_path())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_default_img_place_holder)
                .error(R.drawable.ic_default_img_place_holder)
                .into(holder.ivImg);

        holder.tvName.setText(recipe.getName());
        holder.tvDesc.setText(recipe.getInfo());
        holder.tvPrice.setText(String.valueOf(recipe.getPrice()));
        holder.tvSales.setText(String.valueOf(recipe.getSales()));
        holder.tvCommentsNum.setText(String.valueOf(recipe.getNumber_comment()));

        holder.btCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApp.getCart().addItem(recipe);
                ObservablesFactory.cartActionObservable(BaseApp.getCart());
            }
        });

        holder.ratingBar.setRating( recipe.getScore().floatValue()  );

        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");
        holder.tvRating.setText(df.format(recipe.getScore()));
*/

        return convertView;
    }

    private class ViewHolder {
        /*
        public ImageView ivImg;
        public TextView tvName;
        public TextView tvDesc;
        public TextView tvSales;
        public TextView tvPrice;
        public TextView tvCommentsNum;
        public Button btCart;
        public RatingBar ratingBar;
        public TextView tvRating;
        */

        /* test */
        public TextView tv;
    }
}