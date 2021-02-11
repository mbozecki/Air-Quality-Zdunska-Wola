package pl.edu.uwr.pum.air_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.select.Elements;

public class PlaceListFragment extends Fragment {
    private RecyclerView placeRecyclerView;
    public PlaceAdapter mAdapter;
    private Elements PM10;
    private Elements PM25;
    private Elements PM1;
    private static Elements names;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity activity= (MainActivity) getActivity();
        PM10 = activity.getPM10();
        PM25 = activity.getPM25();
        names= activity.getNames();
        PM1= activity.getPM1();
        /*
        for (int i=0; i<7; i++)
        {
            Log.d("h", String.valueOf(PM10.get(i).text()));
        }

         */
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =
                inflater.inflate(R.layout.fragment_place_list, container, false);
        placeRecyclerView= v.findViewById(R.id.recycler_view);
        placeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(mAdapter == null) {
            mAdapter = new PlaceAdapter(getActivity(),this, names, PM10, PM25, PM1);
            placeRecyclerView.setAdapter(mAdapter);
        }
        return v;
    }
}
