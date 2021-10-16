package com.example.caremate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.caremate.R;
import com.example.caremate.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> dispenseArray = new ArrayList<>();
        dispenseArray.add("test");
        dispenseArray.add("test2");

        ArrayAdapter dispenseList = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, dispenseArray);
        ListView dispenseListView = (ListView) getView().findViewById(R.id.scheduleList);
        dispenseListView.setAdapter(dispenseList);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}