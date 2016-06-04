package com.imzoee.caikid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imzoee.caikid.R;
import com.imzoee.caikid.activity.CommentActivity;
import com.imzoee.caikid.activity.OrderDetailActivity;
import com.imzoee.caikid.convention.ConstConv;
import com.imzoee.caikid.dao.Order;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoey on 2016/6/4.
 *
 * Adapter for the OrderDetailActivity's list.
 */
public class OrderItemAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private Order order = null;

    private List<OrderDetailActivity.OrderTerm> itemList = null;

    public OrderItemAdapter(Context context, Order order){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.itemList = new ArrayList<>();
        this.order = order;
    }

    public void setOrderList(List<OrderDetailActivity.OrderTerm> items){
        this.itemList = items;
    }

    public void setOrder(Order order){
        this.order = order;
    }

    @Override
    public int getCount() {
        return this.itemList.size();
    }

    @Override
    public OrderDetailActivity.OrderTerm getItem(int position){
        return this.itemList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        OrderDetailActivity.OrderTerm item = itemList.get(position);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_order_cart,null);

            holder = new ViewHolder();

            holder.tvItemNum = (TextView) convertView.findViewById(R.id.tv_item_number);
            holder.tvRecipeName = (TextView) convertView.findViewById(R.id.tv_recipe_name);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_recipe_price);
            holder.tvCopies = (TextView) convertView.findViewById(R.id.tv_recipe_count);
            holder.btComment = (Button) convertView.findViewById(R.id.bt_comment);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tvItemNum.setText(String.valueOf(position + 1));
        if(item.getRecipe() != null) {
            holder.tvRecipeName.setText(item.getRecipe().getName());
            java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
            holder.tvPrice.setText(df.format(item.getRecipe().getPrice()));
            holder.tvCopies.setText(String.valueOf(item.getAmount()));

            if(order.getOrderFlag().equals(ConstConv.ORDER_STATUS_COMPLETED) && !item.getIsComment()) {
                final int productId = item.getProductId();
                holder.btComment.setVisibility(View.VISIBLE);
                holder.btComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Order order = orderAdapter.getItem(position);
                        //String recipeJsonStr = JSON.toJSONString(order);
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra(CommentActivity.INTENT_KEY_ORDERID, order.getOrderId());
                        intent.putExtra(CommentActivity.INTENT_KEY_PRODUCTID, productId);
                        OrderDetailActivity.getInstance().startActivity(intent);
                    }
                });
            } else {
                holder.btComment.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvItemNum;
        TextView tvRecipeName;
        TextView tvPrice;
        TextView tvCopies;
        Button btComment;
    }
}