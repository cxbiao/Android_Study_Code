package com.bryan.studycodes.livedata;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

public class MyViewModel extends AndroidViewModel {

    private static final String TAG = "MyViewModel";
    private MutableLiveData<String> mData=new MutableLiveData<>();

    public MyViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getLiveData(){
        return mData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        Log.e(TAG,"onCleared");
    }
}
