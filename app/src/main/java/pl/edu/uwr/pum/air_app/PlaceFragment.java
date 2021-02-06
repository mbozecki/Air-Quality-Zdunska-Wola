package pl.edu.uwr.pum.air_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.UUID;

public class PlaceFragment extends Fragment {
    private ViewPager2 viewPager2;
    private PlaceDetailAdapter mAdapter;
    private Elements PM10;
    private Elements PM1;
    private Elements PM25;
    private Elements names;
    private ArrayList<String> PM10x;
    private ArrayList<String> PM25x;
    private ArrayList<String> PM1x;
    private ArrayList<String> namesx;
    public PlaceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =
                inflater.inflate(R.layout.fragment_viewpager2, container, false);

        viewPager2 = v.findViewById(R.id.detail_view_pager);
        PM10x = getArguments().getStringArrayList("PM10");
        PM25x = getArguments().getStringArrayList("PM25");
        namesx= getArguments().getStringArrayList("names");
        PM1x= getArguments().getStringArrayList("PM1");
        int position = getArguments().getInt("Position");
        mAdapter = new PlaceDetailAdapter(getContext(), this, namesx,PM10x, PM25x, PM1x);
        viewPager2.setAdapter(mAdapter);
        viewPager2.setCurrentItem(position, false);
        return v;
    }
}
