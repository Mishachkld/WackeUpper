package com.example.pillsmessaging.ProjectStructure;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pillsmessaging.DataBasePills.ItemPill;

import java.util.List;

public class PillsViewModel extends AndroidViewModel {
    private LiveData<List<ItemPill>> allPills;
    private PillsRepository repository;



    public PillsViewModel(@NonNull Application application) {
        super(application);
        repository = new PillsRepository(application);
        allPills = repository.getAllPills();
    }


    public void insert(ItemPill itemPills){
        repository.insert(itemPills);
    }
    public void addItem(ItemPill... itemPills){
        repository.insertAll(itemPills);
    }
    public void deleteItem(ItemPill itemPills){
        repository.deleteItem(itemPills);
    }

    public void updateOneItem(ItemPill itemPill){
        repository.updateItem(itemPill);
    }

    public LiveData<List<ItemPill>> getAllPills(){
        return allPills;
    }

    public  LiveData<List<ItemPill>> getAvailableItems(){
        return repository.getAvailablePills();
    }

}
