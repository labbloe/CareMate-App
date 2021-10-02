package com.example.caremate.ui.medication;

import android.widget.Spinner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.caremate.R;

public class MedicationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MedicationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Medication fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
}