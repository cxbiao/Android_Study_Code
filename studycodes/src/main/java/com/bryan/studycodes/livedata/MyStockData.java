package com.bryan.studycodes.livedata;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

public class MyStockData extends MutableLiveData<String> {

    private static final String TAG = "MyStockData";

    private MyStockData(){

    }

   static class MyStockDataHolder {
        private static MyStockData mInstance=new MyStockData();

   }

   public static MyStockData getInstance(){
        return MyStockDataHolder.mInstance;
   }

    @Override
    protected void onActive() {
        Log.i(TAG, "onActive");
    }

    @Override
    protected void onInactive() {
        Log.i(TAG, "onInactive");
    }
}
