package com.example.pillsmessaging.ProjectStructure;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;

import com.example.pillsmessaging.DataBasePills.ItemPill;
import com.example.pillsmessaging.DataBasePills.PillDao;
import com.example.pillsmessaging.DataBasePills.PillRoomDatabase;

import java.util.List;

public class PillsRepository {
    private PillDao pillDao;
    private LiveData<List<ItemPill>> allPills;


    public PillsRepository(Application application){
        PillRoomDatabase database = PillRoomDatabase.getDatabase(application);
        pillDao = database.pillDao();
        allPills = pillDao.getAll();
    }


    public LiveData<List<ItemPill>> getAllPills() {
        return allPills;
    }

    public void insert(ItemPill itemPills){
        PillRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                pillDao.insert(itemPills);
            }
        });


    }
    public void insertAll(ItemPill... itemPills){
        PillRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                pillDao.insertAll(itemPills);
            }
        });
    }

    public void deleteItem(ItemPill itemPill){
        PillRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                pillDao.deleteItem(itemPill);
            }
        });

    }
}
