package com.example.caremate.ui.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.widget.TextView;

import com.example.caremate.R;
import com.example.caremate.databinding.FragmentAlarmBinding;

public class AlarmFragment extends Fragment {

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alarmViewModel =
                new ViewModelProvider(this).get(AlarmViewModel.class);

        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Spinner daySelector_spinner = (Spinner) getView().findViewById(R.id.weekday_selector);
        ArrayAdapter<CharSequence> daySelectorAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.day_selection, android.R.layout.simple_spinner_item);
        daySelectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySelector_spinner.setAdapter(daySelectorAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}