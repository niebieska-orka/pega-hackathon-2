package com.example.helathhero.ui.profil;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.helathhero.R;
import com.example.helathhero.TaskActivity;

import java.util.List;

public class ProfilFragment extends Fragment {

    private ProfilViewModel profilViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profilViewModel =
                ViewModelProviders.of(this).get(ProfilViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profil, container, false);
        final TextView exp = root.findViewById(R.id.profil_exp);
        profilViewModel.getExp().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                exp.setText(s);
            }
        });
        final TextView lvl = root.findViewById(R.id.profil_lvl);
        profilViewModel.getLvl().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                lvl.setText(s);
            }
        });
        final TextView recent_arch_text = root.findViewById(R.id.profil_rec_arch_txt);
        profilViewModel.getRecTxt().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                recent_arch_text.setText(s);
            }
        });
        final TextView name = root.findViewById(R.id.profil_name);
        profilViewModel.getName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                name.setText(s);
            }
        });
        final ProgressBar progress = root.findViewById(R.id.profil_progress_bar);
        profilViewModel.getProgress().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer s) {
                progress.setProgress(s);
            }
        });
        final ListView rec = root.findViewById(R.id.profil_rec_arch);
        profilViewModel.getRec().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> list) {
                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, list);

                rec.setAdapter(arrayAdapter);
            }
        });
        return root;
    }
}
