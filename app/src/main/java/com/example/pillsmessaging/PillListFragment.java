package com.example.pillsmessaging;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pillsmessaging.CustomListAdapter.PillAdapter;
import com.example.pillsmessaging.CustomListAdapter.RecyclerViewAction;
import com.example.pillsmessaging.DataBasePills.ItemPill;
import com.example.pillsmessaging.DialogFragments.ChangeItemDialogFragment;
import com.example.pillsmessaging.ProjectStructure.PillsViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class PillListFragment extends Fragment implements RecyclerViewAction {

    private RecyclerView recyclerView;
    private PillsViewModel viewModel;
    private List<ItemPill> data;
    private PillAdapter adapter;

    private boolean isShowAvailable = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pill_list, container, false);
        if (rootView != null) {
            setHasOptionsMenu(true);
            recyclerView = rootView.findViewById(R.id.recycler_view_list_items);
            recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
            viewModel = new ViewModelProvider(requireActivity()).get(PillsViewModel.class);
            viewModel.getAllPills().getValue();
            viewModel.getIsNeedOnlyAvailable().postValue(false);
            adapter = new PillAdapter(data, this);
            recyclerView.setAdapter(adapter);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            isShowAvailable = !isShowAvailable;
            viewModel.getIsNeedOnlyAvailable().postValue(isShowAvailable);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.getIsNeedOnlyAvailable().observe(getViewLifecycleOwner(), showAvailable -> {
            if (isShowAvailable)
                viewModel.getAvailableItems().observe(getViewLifecycleOwner(), itemPills -> adapter.updateData(data = itemPills));
            else
                viewModel.getAllPills().observe(getViewLifecycleOwner(), itemPills -> adapter.updateData(data = itemPills));
        });
        //viewModel.getAvailableItems().observe(getViewLifecycleOwner(), itemPills -> adapter.updateData(data = itemPills));




    }


    private void deleteItemOnClick(ItemPill itemPill) {
        Snackbar.make(recyclerView, "Удалить элемент? ", Snackbar.LENGTH_LONG).setAction("Удалить", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.deleteItem(itemPill);
            }
        }).show();


    }


    @Override
    public void itemClickListener(int position) {
        deleteItemOnClick(data.get(position));
    }

    @Override
    public void checkBoxClickListener(int position) {
        data.get(position).setAvailable(!data.get(position).isAvailable());
        viewModel.updateOneItem(data.get(position));
        String text;
        if (data.get(position).isAvailable())
            text = "Notification On";
        else
            text = "Notification Off";
        Snackbar.make(recyclerView, text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void descriptionClickListener(int position, String description) {
        new ChangeItemDialogFragment(data.get(position)).show(getChildFragmentManager(), "UPDATE_ITEM");
        // new AddItemDialogFragment(getContext()).show(getChildFragmentManager(), AddItemDialogFragment.TAG);
    }
}