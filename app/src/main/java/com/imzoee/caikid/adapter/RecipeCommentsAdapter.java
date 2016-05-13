package com.imzoee.caikid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.imzoee.caikid.R;
import com.imzoee.caikid.dao.Comment;

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

    public RecipeCommentsAdapter(Context context, List<Comment> commentsList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.commentList = commentsList;
    }

    public void setCommentList(List<Comment> commentList){
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return this.commentList.size();
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
        final Comment comment = commentList.get(position);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_recipe_comment,null);

            holder = new ViewHolder();

            holder.tvFloor = (TextView) convertView.findViewById(R.id.tv_floor_number);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_comment_date);
            holder.rbarScore = (RatingBar) convertView.findViewById(R.id.rbar_recipe_rate);
            holder.tvScore = (TextView) convertView.findViewById(R.id.tv_recipe_rate_numbers);
            holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment_content);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvFloor.setText(String.valueOf(position + 1));
        holder.tvUserName.setText(comment.getUserName());
        //(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(comment.getDate());
        holder.tvDate.setText(java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT).format(comment.getDate()));
        holder.rbarScore.setRating( comment.getScore().floatValue() );
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");
        holder.tvScore.setText(df.format(comment.getScore()));
        holder.tvComment.setText(comment.getContent());

        return convertView;
    }

    private class ViewHolder {
        public TextView tvFloor;
        public TextView tvUserName;
        public TextView tvDate;
        public RatingBar rbarScore;
        public TextView tvScore;
        public TextView tvComment;
    }
}