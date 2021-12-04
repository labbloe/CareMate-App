package com.example.caremate.ui.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import java.util.Calendar;
import java.util.Date;

import com.example.caremate.MainActivity;
import com.example.caremate.R;
import com.example.caremate.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private MainActivity main;
    public MainActivity.ConnectThread conn;
    private Button saveButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        main = (MainActivity) getActivity();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView ssid = (TextView) getView().findViewById(R.id.wifi_ssid);
        TextView pass = (TextView) getView().findViewById(R.id.wifi_password);

        conn = MainActivity.conn;
        saveButton = (Button) getView().findViewById(R.id.settingsSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get Current Time
                Date currentTime = Calendar.getInstance().getTime();

                String wifiSSID = ssid.getText().toString();
                wifiSSID.replaceAll("\\s+","*");
                String wifiPASS = pass.getText().toString();
                String msg = "{\"type\":\"wifi\",\"SSID\":\"" + wifiSSID + "\",\"pass\":\"" + wifiPASS + "\",\"time\"";
                msg += Integer.toString(currentTime.getHours()) + Integer.toString(currentTime.getMinutes()) + "}";
                conn.sendData(msg);
                Log.w("click", msg);
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