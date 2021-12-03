package com.example.caremate.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

        //Get shared preferences to gather data
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());


        //Dispense schedule and check in question list arrays
        ArrayList<String> dispenseArray = new ArrayList<>();
        ArrayList<String> questionArray = new ArrayList<>();

        dispenseArray.add(sharedPreferences.getString("medication1",""));
        dispenseArray.add(sharedPreferences.getString("medication2",""));
        dispenseArray.add(sharedPreferences.getString("medication3",""));
        dispenseArray.add(sharedPreferences.getString("medication4",""));
        dispenseArray.add(sharedPreferences.getString("medication5",""));
        dispenseArray.add(sharedPreferences.getString("medication6",""));
        dispenseArray.add(sharedPreferences.getString("medication7",""));

        questionArray.add(sharedPreferences.getString("question1",""));
        questionArray.add(sharedPreferences.getString("question2",""));
        questionArray.add(sharedPreferences.getString("question3",""));
        questionArray.add(sharedPreferences.getString("question4",""));
        questionArray.add(sharedPreferences.getString("question5",""));
        questionArray.add(sharedPreferences.getString("question6",""));
        questionArray.add(sharedPreferences.getString("question7",""));

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