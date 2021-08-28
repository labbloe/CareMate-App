package com.example.caremate.ui.checkin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CheckinViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CheckinViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Check-In fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}