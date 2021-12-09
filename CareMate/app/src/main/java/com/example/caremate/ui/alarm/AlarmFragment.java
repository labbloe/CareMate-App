package com.example.caremate.ui.alarm;

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
import android.widget.TextView;

import com.example.caremate.MainActivity;
import com.example.caremate.R;
import com.example.caremate.databinding.FragmentAlarmBinding;

import org.w3c.dom.Text;

import java.lang.reflect.Array;

public class AlarmFragment extends Fragment {

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmBinding binding;
    private MainActivity main;
    public MainActivity.ConnectThread conn;
    private Button saveButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alarmViewModel =
                new ViewModelProvider(this).get(AlarmViewModel.class);

        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        main = (MainActivity) getActivity();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences((MainActivity) getContext());

        Spinner alarmSelector_spinner = (Spinner) getView().findViewById(R.id.alarm_selector);
        ArrayAdapter<CharSequence> alarmSelectorAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pill_compartments, android.R.layout.simple_spinner_item);
        alarmSelectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alarmSelector_spinner.setAdapter(alarmSelectorAdapter);

        Spinner daySelector_spinner = (Spinner) getView().findViewById(R.id.weekday_selector);
        ArrayAdapter<CharSequence> daySelectorAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.day_selection, android.R.layout.simple_spinner_item);
        daySelectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySelector_spinner.setAdapter(daySelectorAdapter);

        TextView timeText = (TextView) getView().findViewById(R.id.alarmTime);

        conn = MainActivity.conn;

        alarmSelector_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String alarmInfo = preferences.getString("alarm" + alarmSelector_spinner.getSelectedItem().toString(),"");
                if(alarmInfo.contains(":")) {
                    String[] data = {"", ""};
                    data = alarmInfo.split(":");
                    for (int i = 0; i < 7; i++) {
                        if (daySelector_spinner.getItemAtPosition(i).toString().contains(data[0]))
                            daySelector_spinner.setSelection(i);
                    }
                    if (data[1] != null)
                        timeText.setText(data[1]);
                }
                else{
                    timeText.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveButton = (Button) getView().findViewById(R.id.alarmSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String alarmNum = alarmSelector_spinner.getSelectedItem().toString();
                String day = daySelector_spinner.getSelectedItem().toString();
                String time = timeText.getText().toString();

                if(time.contains(":"))
                    time.replace(":","");

                //Store data internally using Shared Preferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("alarm" + alarmNum, day + ":" + time);
                editor.apply();
                editor.commit();

                String msg = "{\"type\":\"alarm\",\"list\":[";
                //String msg = "{\"type\":\"alarm\",\"day\":\"" + day + "\",\"time\":" + time + "}";
                String[] data = {"",""};
                for(int i=1; i<8; i++){
                    data = preferences.getString("alarm" + i,"").toString().split(":");
                    if(preferences.getString("alarm" + i,"").toString().contains(":"))
                        msg += "{\"day\":\"" + data[0] + "\",\"time\":" + data[1] + "}";
                    else //Time sent is 2500 which is out of range and is ignored by CareMate
                        msg += "{\"day\":\" \",\"time\": 2500}";
                    if(i != 7)
                        msg += ",";
                }
                msg += "]}";

                conn.sendData(msg);
                Log.w("click",msg);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}