package pl.edu.uwr.pum.air_app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.paris.Paris;

import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PlaceDetailAdapter extends RecyclerView.Adapter<PlaceDetailAdapter.ViewHolder> {
    private Context mContext;
    //private int position;
    private Fragment mFragment;
    private String[] PM10;
    private  String[] PM25;
    private  String[] PM1;
    private  String[] names;
    private static final String[] advices= Utility.getAdvices();
    private static final String[] places= {"ul. Żytnia", "ul. Świerkowa", "ul. Okrzei", "ul. 1 Maja","SP6", "SP7", "Ratusz"};
    private static final String[] backgrounds= {"zytnia2", "swierkowa2", "okrzei2", "maja2","sp62", "sp72", "ratusz2"};
    private String[] backgrounds1= {"zytnia1", "swierkowa1", "okrzei2", "maja1","sp61", "sp71", "ratusz1"};

    private View currentView;

    public PlaceDetailAdapter(Context context, Fragment fragment, ArrayList<String> names, ArrayList<String> PM10, ArrayList<String> PM25, ArrayList<String> PM1)
    {
        this.mContext= context;
        this.mFragment= fragment;
        this.names= names.get(0).split("\\s+");
        this.PM10= PM10.get(0).split("\\s+");
        this.PM25 =PM25.get(0).split("\\s+");
        this.PM1= PM1.get(0).split("\\s+");


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mPM10;
        private TextView mPM25;
        private TextView mPM1;
        private TextView mPM10percentage;
        private TextView mPM25percentage;
        private TextView mCAQI;
        private TextView mName;
        private TextView mMessage;
        private LinearLayout mPM10layout;
        private LinearLayout mPM25layout;
        private LinearLayout mPM1layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPM10= itemView.findViewById(R.id.PM10_indicator);
            mPM25= itemView.findViewById(R.id.PM25_indicator);
            mPM1= itemView.findViewById(R.id.PM1_indicator);
            mPM10percentage = itemView.findViewById(R.id.PM10_percentage);
            mPM25percentage= itemView.findViewById(R.id.PM25_percentage);
            mCAQI= itemView.findViewById(R.id.detail_CAQI);
            mName =itemView.findViewById(R.id.place_name);
            mMessage= itemView.findViewById(R.id.message_main);
            currentView= itemView.findViewById(R.id.info);
            mPM10layout= itemView.findViewById(R.id.PM10_layout);
            mPM25layout= itemView.findViewById(R.id.PM25_layout);
            mPM1layout= itemView.findViewById(R.id.PM1_layout);
        }

        public void bind(int position)
        {

            mPM10.setText(PM10[position]+" µg/m³");
            mPM25.setText(PM25[position]+" µg/m³");
            mPM1.setText(PM1[position]+" µg/m³");
            mPM25percentage.setText((Utility.percentageNormPM25(Double.parseDouble(PM25[position])))+"%");
            mPM10percentage.setText((Utility.percentageNormPM10(Double.parseDouble(PM10[position])))+"%");
            int thisCAQI= (int) Utility.getCAQI(Double.parseDouble(PM10[position]),Double.parseDouble(PM25[position]));
            mCAQI.setText(String.valueOf(thisCAQI));
            mName.setText(places[position]);
            setTextAndDecoration(thisCAQI);
            //System.out.println(backgrounds[position]);

            int PM25CAQI= Utility.convertPM25ToCAQI(Double.parseDouble(PM25[position]));
            int PM10CAQI= Utility.convertPM10ToCAQI(Double.parseDouble(PM10[position]));
            setLook(mPM10layout, PM10CAQI);
            setLook(mPM25layout, PM25CAQI);
            /*
            try { //tutaj zamieniam element z tablicy stringow na drawable
                Class res = R.drawable.class;
                Field field = res.getField(backgrounds[getLayoutPosition()]);
                final int drawableId = field.getInt(null);
                Paris.styleBuilder(currentView).background(mContext.getResources().getDrawable(drawableId)).apply();
            }
            catch (Exception e) {
                Log.e("MyTag", "Failure to get drawable id.", e);
            }

             */


        }

        private void setLook(View mLayout, int CAQI) {
            if (CAQI <= 25) {
                //Paris.styleBuilder(mLayout).background(mContext.getResources().getDrawable(R.drawable.viewpager_greenbutton)).apply();
                Paris.styleBuilder(currentView).background(mContext.getResources().getDrawable(R.drawable.ratusz_blue)).apply();
            } else if (CAQI > 25 && CAQI <= 50) {
                //Paris.styleBuilder(mLayout).background(mContext.getResources().getDrawable(R.drawable.viewpager_yellowbutton)).apply();
                Paris.styleBuilder(currentView).background(mContext.getResources().getDrawable(R.drawable.ratusz_yellow)).apply();
            } else if (CAQI > 50 && CAQI <= 75) {
                //Paris.styleBuilder(mLayout).background(mContext.getResources().getDrawable(R.drawable.viewpager_orangebutton)).apply();
                Paris.styleBuilder(currentView).background(mContext.getResources().getDrawable(R.drawable.ratusz_orange)).apply();
            } else if (CAQI > 75 && CAQI <= 100) {
               // Paris.styleBuilder(mLayout).background(mContext.getResources().getDrawable(R.drawable.viewpager_redbutton)).apply();
                Paris.styleBuilder(currentView).background(mContext.getResources().getDrawable(R.drawable.ratusz_red)).apply();

            } else {
                //Paris.styleBuilder(mLayout).background(mContext.getResources().getDrawable(R.drawable.viewpager_purplebutton)).apply();
                Paris.styleBuilder(currentView).background(mContext.getResources().getDrawable(R.drawable.ratusz_purple)).apply();
            }

        }

        private void setTextAndDecoration(int CAQI) {
            if (CAQI <= 25)
            {
                mMessage.setText(advices[0]);
            }
            else if (CAQI > 25 && CAQI <=50)
            {
                mMessage.setText(advices[1]);
            }
            else if (CAQI > 50 && CAQI <=75)
            {
                mMessage.setText(advices[2]);
            }
            else if (CAQI > 75 && CAQI <=100)
            {
                mMessage.setText(advices[3]);

            }
            else {
                mMessage.setText(advices[4]);
            }

        }

    }
    @NonNull
    @Override
    public PlaceDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.air_detail_layout_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceDetailAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
