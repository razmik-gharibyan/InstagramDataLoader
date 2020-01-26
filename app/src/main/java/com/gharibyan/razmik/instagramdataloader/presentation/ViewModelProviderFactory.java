package com.gharibyan.razmik.instagramdataloader.presentation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel();
        }

        throw new IllegalArgumentException("Not part of viewmodel class");
    }
}
