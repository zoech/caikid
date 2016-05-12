package com.imzoee.caikid.model;

/**
 * Created by zoey on 2016/5/12.
 *
 * The model of Shop address. Used only in recipe fragment.
 */
public class ShopAddr {
    private int addrId;
    private String addrName;
    private boolean addrFlag;

    public int getAddrId(){
        return addrId;
    }

    public void setAddrId(int id){
        addrId = id;
    }

    public String getAddrName(){
        return addrName;
    }

    public void setAddrName(String name){
        addrName = name;
    }

    public boolean getAddrFlag(){
        return addrFlag;
    }

    public void setAddrFlag(boolean flag){
        addrFlag = flag;
    }
}
