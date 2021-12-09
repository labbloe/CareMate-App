package com.example.caremate.ui.checkin;

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
import com.example.caremate.databinding.FragmentCheckinBinding;

public class CheckinFragment extends Fragment {

    private CheckinViewModel checkinViewModel;
    private FragmentCheckinBinding binding;
    private MainActivity main;
    public MainActivity.ConnectThread conn;
    private Button saveButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        checkinViewModel =
                new ViewModelProvider(this).get(CheckinViewModel.class);

        binding = FragmentCheckinBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        main = (MainActivity) getActivity();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences((MainActivity) getContext());

        Spinner questionSelector_spinner = (Spinner) getView().findViewById(R.id.questionSelector);
        ArrayAdapter<CharSequence> questionSelectorAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pill_compartments, android.R.layout.simple_spinner_item);
        questionSelectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionSelector_spinner.setAdapter(questionSelectorAdapter);

        TextView displayTime = (TextView) getView().findViewById(R.id.displayTime);
        displayTime.setText(preferences.getString("questionTime",""));

        TextView question = (TextView) getView().findViewById(R.id.questionText);
        question.setText(preferences.getString("question" + questionSelector_spinner.getSelectedItem(), ""));
        questionSelector_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question.setText(preferences.getString("question" + questionSelector_spinner.getSelectedItem(), ""));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){};
        });


        conn = MainActivity.conn;

        saveButton = (Button) getView().findViewById(R.id.questionSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                String questionNumber = questionSelector_spinner.getSelectedItem().toString();
                String questionText = question.getText().toString();
                String time = displayTime.getText().toString();

                if(time.contains(":"))
                    time.replace(":","");

                questionText.replaceAll("\\s+","*");
                String msg = "\"question\":\"" + questionText + "\",\"number\":" + questionNumber;
                //conn.sendData(msg);
                Log.w("click",msg);


                //Store data internally using Shared Preferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("question" + questionNumber, questionText);
                editor.putString("questionTime",time);
                editor.apply();
                editor.commit();

                //Send all questions
                String sendMsg = "{\"type\":\"question\",\"time\":" + time + ",\"list\":[";
                for(int i=0; i<7; i++) {
                    String position = "question" + Integer.toString(i+1);
                    String text = preferences.getString(position,"");
                    if(text.isEmpty())
                        text = " ";
                    sendMsg += "\"" + text + "\"";
                    if(i != 6)
                        sendMsg +=",";
                }
                sendMsg += "]}";
                conn.sendData(sendMsg);
                Log.w("bluetooth",sendMsg);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}