package com.imzoee.caikid.model;

/**
 * Created by zoey on 2016/6/4.
 *
 * Used as the detail items for the Order.
 */
public class OrderItem {
    private int productId;
    private int amount;
    private boolean isComment;

    public int getProductId(){
        return productId;
    }

    public void setProductId(int id){
        this.productId = id;
    }

    public int getAmount(){
        return this.amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public boolean getIsComment(){
        return this.isComment;
    }

    public void setIsComment(boolean isComment){
        this.isComment = isComment;
    }
}
