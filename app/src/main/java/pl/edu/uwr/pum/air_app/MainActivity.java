//Mikołaj Bożęcki 309830

package pl.edu.uwr.pum.air_app;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.airbnb.paris.Paris;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import pl.edu.uwr.pum.air_app.fragment.CenteredTextFragment;
import pl.edu.uwr.pum.air_app.menu.DrawerAdapter;
import pl.edu.uwr.pum.air_app.menu.DrawerItem;
import pl.edu.uwr.pum.air_app.menu.SimpleItem;


public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    String url = "https://www.airqlab.pl/polair_ZW.php";
    private String[] advices= Utility.getAdvices();
    private Elements names;
    private Elements temperatures;
    private static Elements PM10;
    private static Elements PM25;
    private static Elements PM1;
    private View currentView;
    private TextView mMainMessage;
    private TextView mMainCAQI;
    private TextView mDate;
    private boolean hasScraped=true;

    private static final int POS_START = 0;
    private static final int POS_NORM = 1;
    private static final int POS_SRC = 2;
    private static final int POS_CONTACT = 3;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fragment_new);
        currentView = this.findViewById(R.id.info);
        View layout= findViewById(R.id.main_layout);
        mMainCAQI= layout.findViewById(R.id.main_CAQI);
        mMainMessage= layout.findViewById(R.id.main_message);
        mDate= layout.findViewById(R.id.date);

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = myDateObj.format(myFormatObj);
        mDate.setText(formattedDate);
        new Scraper().execute(); //laduje dany ze strony, airqlab

        //------------------------------------------------------------------wysuwane menu od toolbara
        Toolbar toolbar = findViewById(R.id.toolbar);
        slidingRootNav = new SlidingRootNavBuilder(this)

                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_START),
                createItemFor(POS_NORM),
                createItemFor(POS_SRC),
                createItemFor(POS_CONTACT)), this);
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_START);
    }

    public void hasFailed()
    {
        mMainMessage.setText("Brak internetu...");
    }

    public void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment =
                fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void setTextAndDecoration(int CAQI) {
        mMainCAQI.setText(String.valueOf(CAQI));
            if (CAQI <= 25)
            {
                mMainMessage.setText(advices[0]);
               Paris.styleBuilder(currentView).background(getResources().getDrawable(R.drawable.gradient_button_blue)).apply();
            }
            else if (CAQI > 25 && CAQI <=50)
            {
               mMainMessage.setText(advices[1]);
               Paris.styleBuilder(currentView).background(getResources().getDrawable(R.drawable.gradient_button_yellow)).apply();
            }
            else if (CAQI > 50 && CAQI <=75)
            {
                mMainMessage.setText(advices[2]);
               Paris.styleBuilder(currentView).background(getResources().getDrawable(R.drawable.gradient_button_orange)).apply();
            }
            else if (CAQI > 75 && CAQI <=100)
            {
                mMainMessage.setText(advices[3]);
               Paris.styleBuilder(currentView).background(getResources().getDrawable(R.drawable.gradient_button_red)).apply();

            }
            else {
                mMainMessage.setText(advices[4]);
                Paris.styleBuilder(currentView).background(getResources().getDrawable(R.drawable.gradient_button_purple)).apply();
            }

    }
    protected Fragment createFragment() {
        return new PlaceListFragment();
    }


    private class Scraper extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document document = Jsoup.connect(url).data("query", "Java")
                        .userAgent("Mozilla")
                        .timeout(60000)
                        .cookie("x", "x")
                        .ignoreHttpErrors(true)
                        .parser(Parser.htmlParser().settings(new ParseSettings(true, true)))
                        .get();

                document.outputSettings().charset("UTF-8");

                //Parser parser = Parser.htmlParser();
                //parser.settings(new ParseSettings(true, true)); // tag, attribute preserve case
                //document = parser.parseInput(url, "");
                //Document document = Jsoup.parse(url, "", Parser.xmlParser());
                names= document.select("tr td:nth-child(1)");
                temperatures= document.select("tr td:nth-child(3)");
                PM10= document.select("tr td:nth-child(6)");
                PM25= document.select("tr td:nth-child(7)");
                PM1= document.select("tr td:nth-child(8)");
                //System.out.println(PM10.get(0));
            } catch (IOException e) {
                e.printStackTrace();

                hasScraped=false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (hasScraped)
            {
                initFragment();
                double averageCAQI= getAverageCAQI();
                setTextAndDecoration(Integer.valueOf((int) averageCAQI));
            }
            else {
                hasFailed();
            }

        }
    }

    public Elements getTemperatures()
    {
        return temperatures;
    }
    public Elements getPM10() {return PM10;}
    public Elements getPM25() {return PM25;}
    public Elements getPM1() {return PM1;}


    public Elements getNames() { return names; }

    private double getAverageCAQI()
    {
        double average=0;
        for (int i=0; i<6; i++)
        {
            double pm10= Double.parseDouble(PM10.get(i).text());
            double pm25= Double.parseDouble(PM10.get(i).text());
            double placeCAQI= Utility.getCAQI(pm10, pm25);
            average+=placeCAQI;
        }
        return average/7;
    }


    @Override
    public void onItemSelected(int position) {
        slidingRootNav.closeMenu();
        Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        showFragment(selectedScreen);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}

