package pl.edu.uwr.pum.air_app;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.paris.Paris;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    private Activity mActivity;
    private Elements PM10;
    private Elements PM25;
    private Elements PM1;
    private Elements names;
    private Fragment mFragment;

    public PlaceAdapter(Activity context, Fragment fragment, Elements names, Elements PM10, Elements PM25, Elements PM1) {
        this.mActivity = context;
        this.mFragment= fragment;
        this.names = names;
        this.PM10 = PM10;
        this.PM25 = PM25;
        this.PM1=PM1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mPlaceName;
        private TextView mPlaceCAQI;
        private Button mButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlaceName = itemView.findViewById(R.id.placeName);
            mPlaceCAQI = itemView.findViewById(R.id.placeCAQI);
            mButton = itemView.findViewById(R.id.placeCircle);
        }

        public void bind(String name, String CAQI) {
            String[] splitname = name.split("[(]"); //usuwam niepotrzebne informacje sciagniete przy pobraniu miejsc
            mPlaceName.setText(splitname[0]);
            CAQI= CAQI.split("[.]")[0]; // usuwam czesc po przecinku
            mPlaceCAQI.setText(CAQI);
            View mLayout = itemView.findViewById(R.id.place_layout);
            setLook(mLayout, Integer.parseInt(CAQI));

            mLayout.setOnClickListener((view -> {
                int placeIndex= getAdapterPosition();
                Intent intent= new Intent(mActivity, PlaceDetailActivity.class);
                ArrayList<String> PM10x = new ArrayList<>(Arrays.asList(PM10.text())); //te dane przydadza sie do przekazania do viewpagera
                ArrayList<String>PM25x = new ArrayList<>(Arrays.asList(PM25.text()));
                ArrayList<String> PM1x = new ArrayList<>(Arrays.asList(PM1.text()));
                ArrayList<String> namesx = new ArrayList<>(Arrays.asList(names.text()));

                intent.putExtra("Position", placeIndex);
                intent.putExtra("PM10", PM10x);
                intent.putExtra("PM25", PM25x);
                intent.putExtra("names", namesx);
                intent.putExtra("PM1", PM1x);
                mFragment.startActivity(intent);
            }));
        }


        private void setLook(View mLayout, int CAQI) {
            if (CAQI <= 25) {
                Paris.styleBuilder(mLayout).background(mActivity.getResources().getDrawable(R.drawable.buttonstylegreen)).apply();
                Paris.styleBuilder(mButton).background(mActivity.getResources().getDrawable(R.drawable.circle_green)).apply();
            } else if (CAQI > 25 && CAQI <= 50) {
                Paris.styleBuilder(mLayout).background(mActivity.getResources().getDrawable(R.drawable.buttonstyleyellow)).apply();
                Paris.styleBuilder(mButton).background(mActivity.getResources().getDrawable(R.drawable.circle_yellow)).apply();
            } else if (CAQI > 50 && CAQI <= 75) {
                Paris.styleBuilder(mLayout).background(mActivity.getResources().getDrawable(R.drawable.buttonstyleorange)).apply();
                Paris.styleBuilder(mButton).background(mActivity.getResources().getDrawable(R.drawable.circle_orange)).apply();
            } else if (CAQI > 75 && CAQI <= 100) {
                Paris.styleBuilder(mLayout).background(mActivity.getResources().getDrawable(R.drawable.buttonstylered)).apply();
                Paris.styleBuilder(mButton).background(mActivity.getResources().getDrawable(R.drawable.circle_red)).apply();
            } else {
                Paris.styleBuilder(mLayout).background(mActivity.getResources().getDrawable(R.drawable.buttonstylepurple)).apply();
                Paris.styleBuilder(mButton).background(mActivity.getResources().getDrawable(R.drawable.circle_purple)).apply();
            }

        }

        @Override
        public void onClick(View v) {
           // System.out.println("CLicked");
        }
    }


    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.place_item,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {

        String name = names.get(position).text();
        double pm10 = Double.parseDouble(PM10.get(position).text());
        double pm25 = Double.parseDouble(PM25.get(position).text());
        double CAQI = Utility.getCAQI(pm10, pm25);
        holder.bind(name, String.valueOf(CAQI));
    }

    @Override
    public int getItemCount() {
        return 7;
    }


}