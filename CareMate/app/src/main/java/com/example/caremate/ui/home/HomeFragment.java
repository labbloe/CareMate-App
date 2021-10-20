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

        //Dispense schedule and check in question list arrays
        ArrayList<String> dispenseArray = new ArrayList<>();
        ArrayList<String> questionArray = new ArrayList<>();

        dispenseArray.add("Compartment 1: 9:30 AM");
        dispenseArray.add("Compartment 2: 7:30 PM");
        dispenseArray.add("Compartment 3: 10:00 AM");
        dispenseArray.add("Compartment 4: 8:30 PM");

        questionArray.add("Note 1: 'Do you need me to contact you this morning?'");
        questionArray.add("Note 2: 'Rate your back pain between 1 and 10 this morning");
        questionArray.add("Note 3: 'Did you take your medication this morning?'");

        //Dispense schedule list
        ArrayAdapter dispenseList = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, dispenseArray);
        ListView dispenseListView = (ListView) getView().findViewById(R.id.scheduleList);
        dispenseListView.setAdapter(dispenseList);

        //Check-in Question list
        ArrayAdapter questionList = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, questionArray);
        ListView questionListView = (ListView) getView().findViewById(R.id.questionList);
        questionListView.setAdapter(questionList);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}