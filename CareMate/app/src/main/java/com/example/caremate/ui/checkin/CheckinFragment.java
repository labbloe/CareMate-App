package com.example.caremate.ui.checkin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.widget.TextView;

import com.example.caremate.R;
import com.example.caremate.databinding.FragmentCheckinBinding;

public class CheckinFragment extends Fragment {

    private CheckinViewModel checkinViewModel;
    private FragmentCheckinBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        checkinViewModel =
                new ViewModelProvider(this).get(CheckinViewModel.class);

        binding = FragmentCheckinBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}