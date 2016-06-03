package com.imzoee.caikid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imzoee.caikid.R;
import com.imzoee.caikid.dao.Order;

import java.util.List;

/**
 * Created by zoey on 2016/6/3.
 *
 */
public class OrderAdapter  extends BaseAdapter {

/*    private Context context;
    private CaikidCart cart = null;*/

    private List<Order> itemList = null;
    private LayoutInflater inflater;

    public OrderAdapter(Context context, List<Order> items){
        this.inflater = LayoutInflater.from(context);
        this.itemList = items;
    }

    public void setOrderList(List<Order> items){
        this.itemList = items;
    }

    @Override
    public int getCount() {
        return this.itemList.size();
    }

    @Override
    public Order getItem(int position){
        return this.itemList.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Order item = itemList.get(position);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_order,null);

            holder = new ViewHolder();
            holder.tvFloor = (TextView) convertView.findViewById(R.id.tv_floor_number);
            holder.tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_total_price);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_order_time);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_order_status);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvFloor.setText(String.valueOf(position + 1));
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        holder.tvTotalPrice.setText(df.format(item.getPrice()));
        holder.tvDate.setText(item.getOrderTime());
        holder.tvStatus.setText(item.getOrderFlag());

        return convertView;
    }

    private class ViewHolder {
        TextView tvFloor;
        TextView tvTotalPrice;
        TextView tvDate;
        TextView tvStatus;
    }
}