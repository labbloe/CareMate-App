package com.example.caremate.ui.medication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.caremate.MainActivity;
import com.example.caremate.R;
import com.example.caremate.databinding.FragmentMedicationBinding;

public class MedicationFragment extends Fragment {

    private MedicationViewModel medicationViewModel;
    private FragmentMedicationBinding binding;
    private MainActivity main;
    public MainActivity.ConnectThread conn;
    private Button saveButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        medicationViewModel =
                new ViewModelProvider(this).get(MedicationViewModel.class);

        binding = FragmentMedicationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        main = (MainActivity) getActivity();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner pillCompartment_spinner = (Spinner) getView().findViewById(R.id.compartment_spinner);
        ArrayAdapter<CharSequence> compartmentAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pill_compartments, android.R.layout.simple_spinner_item);
        compartmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pillCompartment_spinner.setAdapter(compartmentAdapter);

        Spinner daySelector_spinner = (Spinner) getView().findViewById(R.id.day_selector);
        ArrayAdapter<CharSequence> daySelectorAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.day_selection, android.R.layout.simple_spinner_item);
        daySelectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySelector_spinner.setAdapter(daySelectorAdapter);

        conn = MainActivity.conn;

        saveButton = (Button) getView().findViewById(R.id.dispenseSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                conn.sendData("{bin1,monday-1159,bin2,tuesday-0800,wednesday-0830}");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void saveClick(View v){
        conn.sendData("Medication Save Button Pressed");
        Log.w("BUTTON", "Medication Save Button Pressed");
    }
}