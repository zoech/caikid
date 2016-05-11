package com.imzoee.caikid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.model.CaikidCart;
import com.imzoee.caikid.utils.misc.ObservablesFactory;
import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;

import java.util.List;

/**
 * Created by zoey on 2016/5/9.
 *
 * Cart activity's listView adapter.
 */
public class CartItemAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private CaikidCart cart = null;

    private List<CaikidCart.CartItem> itemList = null;

    public CartItemAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.itemList = BaseApp.getCart().getItemList();
        this.cart = BaseApp.getCart();
    }

    public void setRecipesList(List<CaikidCart.CartItem> items){
        this.itemList = items;
    }

    @Override
    public int getCount() {
        return this.itemList.size();
    }

    @Override
    public CaikidCart.CartItem getItem(int position){
        return this.itemList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        CaikidCart.CartItem item = itemList.get(position);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_cart,null);

            holder = new ViewHolder();

            holder.tvRecipeName = (TextView) convertView.findViewById(R.id.tv_recipe_name);
            holder.tvRecipePrice = (TextView) convertView.findViewById(R.id.tv_recipe_price);
            holder.tvItemCount = (TextView) convertView.findViewById(R.id.tv_item_count);
            holder.btDecrease = (Button) convertView.findViewById(R.id.bt_decrease);
            holder.btIncrease = (Button) convertView.findViewById(R.id.bt_increase);
            holder.llRemove = (LinearLayout) convertView.findViewById(R.id.ll_item_remove);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvRecipeName.setText(item.getRecipe().getName());

        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        holder.tvRecipePrice.setText(df.format(item.getRecipe().getPrice()));

        holder.tvItemCount.setText(String.valueOf(item.getCount()));
        holder.btDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"decrease item in pos:" + position, Toast.LENGTH_LONG).show();
                cart.decreaseItem(position);
                ObservablesFactory.cartActionObservable(cart);
            }
        });
        holder.btIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.increaseItem(position);
                ObservablesFactory.cartActionObservable(cart);
            }
        });
        holder.llRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.removeItem(position);
                ObservablesFactory.cartActionObservable(cart);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tvRecipeName;
        TextView tvRecipePrice;
        TextView tvItemCount;
        Button btDecrease;
        Button btIncrease;
        LinearLayout llRemove;
    }
}