package abdlrhmanshehata.yatadabaron.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import abdlrhmanshehata.yatadabaron.Auxilliary.IStatusListener;
import abdlrhmanshehata.yatadabaron.Auxilliary.LetterFrequency;
import abdlrhmanshehata.yatadabaron.Auxilliary.SearchMode;
import abdlrhmanshehata.yatadabaron.DataAdapters.FragmentsAdapter;
import abdlrhmanshehata.yatadabaron.DatabaseHelpers.DatabaseHelper;
import abdlrhmanshehata.yatadabaron.Model.Aya;
import abdlrhmanshehata.yatadabaron.Model.Sura;
import abdlrhmanshehata.yatadabaron.R;

public class SecondaryActivity extends AppCompatActivity implements IStatusListener {
    public Sura SelectedSura;
    public Aya[] Ayat;
    public DatabaseHelper MyHelper;

    private TextView txt_suraName;
    private TextView txt_suraInfo;
    private Context myContext;
    private Toolbar toolbar_secondary;
    public Map<String,Float> LettersFrequencySortedBasmala;
    public Map<String,Float> LettersFrequencySortedNotBasmala;
    public Map<String,Float> LettersFrequencyNotSortedBasmala;
    public Map<String,Float> LettersFrequencyNotSortedNotBasmala;
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
            sura_id = SelectedSura.SuraID + ". ";
        }
        txt_suraName.setText(sura_id+ SelectedSura.SuraNameArabic);
        txt_suraInfo.setText(SelectedSura.GetSuraInfo(true));
        final ProgressDialog dialog = ProgressDialog.show(this, "Preparing", "Please wait...", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                MyHelper.CheckDatabase();
                int input_id = SelectedSura.SuraID;
                SearchMode input_mode = SearchMode.SURA;
                if(SelectedSura.SuraID==0){
                    input_id=0;
                    input_mode = SearchMode.QURAN;
                }else{
                    Ayat = MyHelper.GetAyat(SelectedSura.SuraID,false);
                }
                LettersFrequencyNotSortedBasmala =  MyHelper.GetLettersFrequency(input_id, input_mode,true,false);
                LettersFrequencySortedBasmala = LetterFrequency.Sort(LettersFrequencyNotSortedBasmala.keySet().toArray(new String[]{}),LettersFrequencyNotSortedBasmala.values().toArray(new Float[]{}));
                LettersFrequencyNotSortedNotBasmala =  MyHelper.GetLettersFrequency(input_id, input_mode,false,false);
                LettersFrequencySortedNotBasmala =  LetterFrequency.Sort(LettersFrequencyNotSortedNotBasmala.keySet().toArray(new String[]{}),LettersFrequencyNotSortedNotBasmala.values().toArray(new Float[]{}));;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewPager viewPager = (ViewPager) findViewById(R.id.mainViewPager);
                        boolean showReadFragment = SelectedSura.SuraID != 0;
                        FragmentsAdapter adapter = new FragmentsAdapter(myContext, getSupportFragmentManager(),showReadFragment);
                        // Set the adapter onto the view pager
                        viewPager.setAdapter(adapter);
                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                        tabLayout.setupWithViewPager(viewPager);
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
