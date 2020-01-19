package com.example.pt16;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlocListFragment extends Fragment {
    private ArrayList<Bloc> blocs;
    private int blocLayout;
    private BlocAdapter.TemperatureUnit temperatureUnit;

    public BlocListFragment() {
        // Required empty public constructor
    }

    static BlocListFragment newInstance(ArrayList<Bloc> blocs, int blocLayout, BlocAdapter.TemperatureUnit temperatureUnit) {
        BlocListFragment blocListFragment = new BlocListFragment();

        blocListFragment.blocs = blocs;
        blocListFragment.blocLayout = blocLayout;
        blocListFragment.temperatureUnit = temperatureUnit;

        return blocListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bloc_list, container, false);

        RecyclerView blocRecyclerView = view.findViewById(R.id.bloc_recycler_view);
        // La llista no canvia en runtime; mateixa mida.
        blocRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());


        RecyclerView.Adapter<BlocAdapter.BlocViewHolder> blocViewHolderAdapter =
                new BlocAdapter(this.blocs, this.blocLayout, this.temperatureUnit)
        ;

        blocRecyclerView.setLayoutManager(layoutManager);
        blocRecyclerView.setAdapter(blocViewHolderAdapter);

        return view;
    }
}
