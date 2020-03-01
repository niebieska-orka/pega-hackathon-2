package com.example.helathhero.ui.profil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.helathhero.ChildSession;
import com.example.helathhero.model.Child;

import java.util.ArrayList;
import java.util.List;

public class ProfilViewModel extends ViewModel {

    private MutableLiveData<String> exp;
    private MutableLiveData<String> lvl;
    private MutableLiveData<String> recent_arch_text;
    private MutableLiveData<String> name;
    private MutableLiveData<List<String>> recent_arch;
    private MutableLiveData<List<Byte>> avatar;
    private MutableLiveData<Integer> progress;


    public ProfilViewModel() {
        ChildSession session = ChildSession.getInstance(null);
        Child child = session.getChild();
        exp = new MutableLiveData<>();
        exp.setValue(String.valueOf(child.getXP()) + " XP");
        lvl = new MutableLiveData<>();
        lvl.setValue(String.valueOf(child.getLevel()));
        recent_arch_text = new MutableLiveData<>();
        recent_arch_text.setValue("Recent achievements:");
        name = new MutableLiveData<>();
        name.setValue(child.getCharacterName());
        recent_arch = new MutableLiveData<>();
        List<String> l = new ArrayList<>();
        l.add("arch1");
        l.add("arch2");
        l.add("arch3");
        recent_arch.setValue(l);
        progress = new MutableLiveData<>();
        progress.setValue(child.getXP());
    }

    public LiveData<String> getExp() {
        return exp;
    }

    public LiveData<String> getLvl() {
        return lvl;
    }

    public LiveData<String> getRecTxt() {
        return recent_arch_text;
    }

    public LiveData<List<String>> getRec() {
        return recent_arch;
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<Integer> getProgress() {
        return progress;
    }
}