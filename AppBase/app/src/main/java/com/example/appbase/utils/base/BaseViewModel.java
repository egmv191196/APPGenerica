package com.example.appbase.utils.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BaseViewModel extends ViewModel {
    protected CompositeDisposable compositeDisposable;
    protected MutableLiveData<String> alert;
    protected MutableLiveData<Boolean> loader;

    public BaseViewModel() {
        super();
        loader = new MutableLiveData<>();
        alert = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    public LiveData<Boolean> getLoader() {
        return loader;
    }

    public LiveData<String> getAlert() {
        return alert;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

    public void clearViewModel() {
        onCleared();
    }

}
