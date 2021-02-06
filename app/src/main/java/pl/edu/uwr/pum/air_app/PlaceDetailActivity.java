package pl.edu.uwr.pum.air_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class PlaceDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment2);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment =
                fragmentManager.findFragmentById(R.id.fragment_container2);

        if(fragment == null) {
            fragment = createFragment(); // TU WYWOLUJE SIE TA METODA ABSTARKCYJNA
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container2, fragment)
                    .commit();
        }
    }
    protected Fragment createFragment() {
        PlaceFragment placeFragment = new PlaceFragment();

        Intent intent=new Intent();
        Bundle bundle= new Bundle();
        bundle.putInt("Position", getIntent().getIntExtra("Position",0));
        bundle.putStringArrayList("PM10", getIntent().getStringArrayListExtra("PM10"));
        bundle.putStringArrayList("PM25", getIntent().getStringArrayListExtra("PM25"));
        bundle.putStringArrayList("PM1", getIntent().getStringArrayListExtra("PM1"));
        bundle.putStringArrayList("names", getIntent().getStringArrayListExtra("names"));

        placeFragment.setArguments(bundle);
        setResult(1, intent);
        return placeFragment;
    }
}
