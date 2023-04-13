package ru.mirea.laricheva.mireaproject;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mirea.laricheva.mireaproject.databinding.FragmentServiceBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceFragment extends Fragment {

    private int PermissionCode = 200;
    private FragmentServiceBinding binding;


    private long startTime;
    private boolean isServiceWork;
    private static final int REQUEST_CODE_PERMISSION =200;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ServiceFragment() {

    }
    public static ServiceFragment newInstance(String param1, String param2) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentServiceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        if (ContextCompat.checkSelfPermission(getContext(), POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            Log.d(MainActivity.class.getSimpleName().toString(), "Разрешения получены"); }
        else {
            Log.d(MainActivity.class.getSimpleName().toString(), "Нет разрешений!");
            ActivityCompat.requestPermissions(getActivity(), new String[]{POST_NOTIFICATIONS, FOREGROUND_SERVICE}, PermissionCode);
        }
        int permission = ContextCompat.checkSelfPermission(getContext(), POST_NOTIFICATIONS);
        if (permission == PackageManager.PERMISSION_GRANTED){
            isServiceWork = false;
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{POST_NOTIFICATIONS}, REQUEST_CODE_PERMISSION);
        }

        binding.buttonPlay.setText("press");
        binding.textViewResult.setText("");

        binding.buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceWork)
                {
                    isServiceWork = !isServiceWork;
                    getActivity().stopService(new Intent(getActivity(), TimerService.class));
                    Log.d("Start Time", Long.toString(startTime));
                    long diff = System.currentTimeMillis() - startTime;
                    float res = Float.valueOf(diff) / 1000;
                    binding.textViewResult.setText("Вы ощущаете "+ res + " секунд как 1 секунду");
                    binding.buttonPlay.setText("try again");

                }
                else
                {
                    isServiceWork = !isServiceWork;
                    Intent serviceIntent = new Intent(getActivity(), TimerService.class);
                    getActivity().startForegroundService(serviceIntent);
                    startTime = System.currentTimeMillis();
                    binding.buttonPlay.setText("press after 1 second");
                }
            }
        });

        return view;
    }

}