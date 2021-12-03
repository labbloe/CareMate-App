package com.example.caremate.ui.notification;

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

import com.example.caremate.MainActivity;
import com.example.caremate.R;
import com.example.caremate.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment {

    private NotificationViewModel notificationViewModel;
    private FragmentNotificationBinding binding;
    private MainActivity main;
    public MainActivity.ConnectThread conn;
    private Button saveButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationViewModel =
                new ViewModelProvider(this).get(NotificationViewModel.class);

        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        main = (MainActivity) getActivity();

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView email = (TextView) getView().findViewById(R.id.profileEmail);
        TextView phone = (TextView) getView().findViewById(R.id.profileNumber);

        conn = MainActivity.conn;
        saveButton = (Button) getView().findViewById(R.id.notificationSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String emailSend = email.getText().toString();
                String phoneSend = phone.getText().toString();
                String msg = "{\"type\":\"notification\",\"email\":\"" + emailSend + "\",\"phone\":\"" + phoneSend + "\"}";
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