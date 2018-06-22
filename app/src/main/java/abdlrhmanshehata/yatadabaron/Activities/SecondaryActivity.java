package abdlrhmanshehata.yatadabaron.Activities;

import android.app.Fragment;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import abdlrhmanshehata.yatadabaron.Auxilliary.IStatusListener;
import abdlrhmanshehata.yatadabaron.Auxilliary.Localization;
import abdlrhmanshehata.yatadabaron.DataAdapters.FragmentsAdapter;
import abdlrhmanshehata.yatadabaron.DatabaseHelpers.DatabaseHelper;
import abdlrhmanshehata.yatadabaron.Fragments.ReadFragment;
import abdlrhmanshehata.yatadabaron.Model.Sura;
import abdlrhmanshehata.yatadabaron.R;

public class SecondaryActivity extends AppCompatActivity implements IStatusListener {
    public Sura SelectedSura;
    public DatabaseHelper MyHelper;

    private TextView txt_suraName;
    private TextView txt_suraInfo;
    private Context myContext;
    private Toolbar toolbar_secondary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        SelectedSura = (Sura) getIntent().getSerializableExtra("SELECTED_SURA");
        setTitle(SelectedSura.SuraNameEnglish);
        toolbar_secondary = (Toolbar) findViewById(R.id.toolbar_secondary);
        setSupportActionBar(toolbar_secondary);
        MyHelper = new DatabaseHelper(this);
        txt_suraName = (TextView) findViewById(R.id.txt_suraName);
        txt_suraInfo = (TextView) findViewById(R.id.txt_suraInfo);
        String sura_id = "";
        if(SelectedSura.SuraID!=0) {
            sura_id = Localization.getArabicNumber(SelectedSura.SuraID) + ". ";
        }
        txt_suraName.setText(sura_id+ SelectedSura.SuraNameArabic);
        txt_suraInfo.setText(SelectedSura.GetSuraInfo(true));
        final ProgressDialog dialog = ProgressDialog.show(this, "جاري التحميل", "برجاء الانتظار ...", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHelper.CheckDatabase();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final TabLayout myTab = (TabLayout) findViewById(R.id.main_tabLayout);
                        final ViewPager myViewPager = (ViewPager) findViewById(R.id.mainViewPager);
                        boolean showRead = SelectedSura.SuraID!=0;
                        FragmentsAdapter adapter= new FragmentsAdapter(myContext,getSupportFragmentManager(),showRead);
                        myViewPager.setAdapter(adapter);
                        myTab.setupWithViewPager(myViewPager);
                        myViewPager.setCurrentItem(adapter.getCount());
                        myTab.setSelectedTabIndicatorHeight(10);
                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }

    @Override
    public void ConnectionStatusChanged() {

    }

    @Override
    public void QueryStatusChanged() {

    }
}
