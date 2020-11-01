package com.example.hmo.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hmo.MainNavigator;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> fullName;

    public HomeViewModel() {
        fullName = new MutableLiveData<>();
        fullName.setValue(MainNavigator.userFirstName + " " + MainNavigator.userLastName);
    }

    public LiveData<String> getText() {
        return fullName;
    }
}