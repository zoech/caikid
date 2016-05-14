package com.imzoee.caikid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imzoee.caikid.BaseApp;
import com.imzoee.caikid.R;
import com.imzoee.caikid.model.CaikidCart;

import java.util.List;

/**
 * Created by zoey on 2016/5/9.
 *
 * Cart activity's listView adapter.
 */
public class OrderCartAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private CaikidCart cart = null;

    private List<CaikidCart.CartItem> itemList = null;

    public OrderCartAdapter(Context context){
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
            convertView = inflater.inflate(R.layout.item_order_cart,null);

            holder = new ViewHolder();

            holder.tvItemNum = (TextView) convertView.findViewById(R.id.tv_item_number);
            holder.tvRecipeName = (TextView) convertView.findViewById(R.id.tv_recipe_name);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_recipe_price);
            holder.tvCopies = (TextView) convertView.findViewById(R.id.tv_recipe_count);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvItemNum.setText(String.valueOf(position + 1));
        holder.tvRecipeName.setText(item.getRecipe().getName());
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        holder.tvPrice.setText(df.format(item.getRecipe().getPrice()));
        holder.tvCopies.setText(String.valueOf(item.getCount()));
        return convertView;
    }

    private class ViewHolder {
        TextView tvItemNum;
        TextView tvRecipeName;
        TextView tvPrice;
        TextView tvCopies;
    }
}
