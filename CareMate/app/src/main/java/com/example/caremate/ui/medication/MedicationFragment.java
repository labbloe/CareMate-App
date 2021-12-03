package com.example.caremate.ui.medication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences((MainActivity) getContext());

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

        TextView dispenseTime = (TextView) getView().findViewById(R.id.dispenseTime);
        TextView dispenseNotes = (TextView) getView().findViewById(R.id.dispenseNotes);


        conn = MainActivity.conn;
        //dispenseTime.setText(preferences.getString("medication" + pillCompartment_spinner.getSelectedItem(), ""));
        //dispenseNotes.setText(preferences.getString("medication" + pillCompartment_spinner.getSelectedItem(), ""));
        pillCompartment_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String data = preferences.getString("medication" + pillCompartment_spinner.getSelectedItem().toString(), "");
                if(data.contains(":")) {
                    String[] dataTmp = data.split(":");
                    String[] dataTmp2 = dataTmp[1].split("-");
                    Log.w("DAY SELECTOR=============",dataTmp[0]);
                    for(int i=0; i<7; i++){
                        if(daySelector_spinner.getItemAtPosition(i).toString().contains(dataTmp[0]))
                            daySelector_spinner.setSelection(i);
                    }
                    if(dataTmp2[0] != null)
                        dispenseTime.setText(dataTmp2[0]);
                    if(dataTmp2[1] != null)
                        dispenseNotes.setText(dataTmp2[1]);
                }
                else{
                    dispenseTime.setText("");
                    dispenseNotes.setText("");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){};
        });

        saveButton = (Button) getView().findViewById(R.id.dispenseSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String compartment = pillCompartment_spinner.getSelectedItem().toString();
                String day = daySelector_spinner.getSelectedItem().toString();
                String time = dispenseTime.getText().toString();
                String notes = dispenseNotes.getText().toString();
                String msg = "{\"type\":\"medication\",\"bin\":" + compartment + ",\"day\":\"" + day + "\",\"time\":" + time + "}";
                //conn.sendData(msg);
                Log.w("click",msg);

                //Store data internally using Shared Preferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("medication" + compartment, day + ":" + time + "-" + notes);
                editor.apply();
                editor.commit();

                //Send all data
                String sendData = "{\"type\":\"medication\",\"list\":[";
                String[] dataTmp = {"",""};
                String[] dataTmp2 = {"", ""};
                for(int i=0; i<7; i++){
                    String position = Integer.toString(i+1);
                    String data = preferences.getString("medication" + position, "");
                    if(data.contains(":")) {
                        dataTmp = data.split(":");
                        dataTmp2 = dataTmp[1].split("-");
                    }
                    else{
                        dataTmp[0] = "";
                        dataTmp2[0] = "";
                    }
                    sendData += "{\"bin\":" + position + ",\"day\":\"" + dataTmp[0] + "\",\"time\":" + dataTmp2[0] + "}";
                    if(i != 6)
                        sendData += ",";
                }
                sendData += "]}";
                conn.sendData(sendData);
                Log.w("bluetooth",sendData);
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