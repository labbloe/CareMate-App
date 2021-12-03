package com.example.caremate.ui.alarm;

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
import android.widget.TextView;

import com.example.caremate.MainActivity;
import com.example.caremate.R;
import com.example.caremate.databinding.FragmentAlarmBinding;

import org.w3c.dom.Text;

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

        Spinner daySelector_spinner = (Spinner) getView().findViewById(R.id.weekday_selector);
        ArrayAdapter<CharSequence> daySelectorAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.day_selection, android.R.layout.simple_spinner_item);
        daySelectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySelector_spinner.setAdapter(daySelectorAdapter);

        TextView timeText = (TextView) getView().findViewById(R.id.alarmTime);

        conn = MainActivity.conn;

        saveButton = (Button) getView().findViewById(R.id.alarmSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String day = daySelector_spinner.getSelectedItem().toString();
                String time = timeText.getText().toString();
                String msg = "{\"type\":\"alarm\",\"day\":\"" + day + "\",\"time\":" + time + "}";
                conn.sendData(msg);
                Log.w("click",msg);
                //conn.sendData("{bin1,monday-1159,bin2,tuesday-0800,wednesday-0830}");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}