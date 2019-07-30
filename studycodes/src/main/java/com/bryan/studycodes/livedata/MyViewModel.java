package com.bryan.studycodes.livedata;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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
