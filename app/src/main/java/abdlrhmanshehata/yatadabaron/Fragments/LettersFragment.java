package abdlrhmanshehata.yatadabaron.Fragments;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import abdlrhmanshehata.yatadabaron.Activities.SecondaryActivity;
import abdlrhmanshehata.yatadabaron.Auxilliary.LetterFrequencyModel;
import abdlrhmanshehata.yatadabaron.Auxilliary.SearchMode;
import abdlrhmanshehata.yatadabaron.Auxilliary.Utils;
import abdlrhmanshehata.yatadabaron.DataAdapters.LetterAdapter;
import abdlrhmanshehata.yatadabaron.Model.Sura;
import abdlrhmanshehata.yatadabaron.R;


public class LettersFragment extends Fragment{
    private TextView txt_suraName;
    private BarChart myChart;
    private SecondaryActivity myParent;
    private CheckBox chkbx_basmala_letters;
    private CheckBox chkbx_sort_letters;
    private CheckBox chkbx_chart_letters;
    private ListView lstView_lettersTable;

    private Map<String,Float> LettersFrequencySortedBasmala;
    private Map<String,Float> LettersFrequencySortedNotBasmala;
    private Map<String,Float> LettersFrequencyNotSortedBasmala;
    private Map<String,Float> LettersFrequencyNotSortedNotBasmala;
    private int SuraID;

    public LettersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_letters, container, false);
        myChart = (BarChart) view.findViewById(R.id.chart);
        myParent = (SecondaryActivity) this.getActivity();
        chkbx_basmala_letters = (CheckBox)  view.findViewById(R.id.chkbx_basmala_letters);
        chkbx_sort_letters = (CheckBox) view.findViewById(R.id.chkbx_sort_letters);
        chkbx_chart_letters = (CheckBox) view.findViewById(R.id.chkbx_chart_letters);
        lstView_lettersTable = (ListView) view.findViewById(R.id.lstView_lettersTable);

        SuraID = (!myParent.wholeQuranMode)? myParent.SelectedSura.SuraID:0;
        SearchMode input_mode = (!myParent.wholeQuranMode)? SearchMode.SURA:SearchMode.QURAN;
        LettersFrequencyNotSortedBasmala =  myParent.MyHelper.GetLettersFrequency(SuraID, input_mode,true,false);
        LettersFrequencySortedBasmala = Utils.SortLetterFrequency(LettersFrequencyNotSortedBasmala.keySet().toArray(new String[]{}), LettersFrequencyNotSortedBasmala.values().toArray(new Float[]{}));
        LettersFrequencyNotSortedNotBasmala =  myParent.MyHelper.GetLettersFrequency(SuraID, input_mode,false,false);
        LettersFrequencySortedNotBasmala =  Utils.SortLetterFrequency(LettersFrequencyNotSortedNotBasmala.keySet().toArray(new String[]{}),LettersFrequencyNotSortedNotBasmala.values().toArray(new Float[]{}));;

        DrawLettersFrequencyBySura(SuraID);
        chkbx_basmala_letters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewFrequency();
            }
        });
        chkbx_sort_letters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewFrequency();
            }
        });
        chkbx_chart_letters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ViewFrequency();
            }
        });
        return view;
    }


    private void ViewFrequency(){
        if (chkbx_chart_letters.isChecked()) {
            lstView_lettersTable.setVisibility(View.GONE);
            myChart.setVisibility(View.VISIBLE);
            DrawLettersFrequencyBySura(SuraID);
        }else{
            lstView_lettersTable.setVisibility(View.VISIBLE);
            myChart.setVisibility(View.GONE);
            FillLetterFrequencyTable();
        }
    }
    private void FillLetterFrequencyTable(){
        Map<String,Float> result = GetProperLetterFrequency();
        List<LetterFrequencyModel> letFreqMod = new ArrayList();
        for (String k:result.keySet()) {
            Float v =  result.get(k);
            letFreqMod.add(new LetterFrequencyModel(k,v));
        }
        LetterFrequencyModel[] arr = letFreqMod.toArray(new LetterFrequencyModel[]{});
        LetterAdapter adapter = new LetterAdapter(myParent,R.layout.inflater_letter,arr);
        lstView_lettersTable.setAdapter(adapter);
    }
    private Map<String,Float> GetProperLetterFrequency(){
        boolean basmala = chkbx_basmala_letters.isChecked();
        boolean sort = chkbx_sort_letters.isChecked();
        Map<String,Float> result = null;
        if(basmala && sort){
            result = LettersFrequencySortedBasmala;
        }
        if (!basmala && !sort){
            result = LettersFrequencyNotSortedNotBasmala;
        }
        if (basmala && !sort){
            result = LettersFrequencyNotSortedBasmala;
        }
        if (!basmala && sort){
            result = LettersFrequencySortedNotBasmala;
        }
        return result;
    }
    private void DrawLettersFrequencyBySura(final int suraID){
        final ProgressDialog dialog = ProgressDialog.show(myParent, "جاري حساب تكرارات الحروف", "برجاء الانتظار ...", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchMode mode = SearchMode.SURA;
                if(suraID == 0){
                    mode = SearchMode.QURAN;
                }
                final Map<String,Float> result = GetProperLetterFrequency();
                myParent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            List<BarEntry> entries = new ArrayList<BarEntry>();
                            int i = 0;
                            for (Float frequency : result.values()) {
                                entries.add(new BarEntry(i, frequency));
                                i++;
                            }
                            String suraName = (myParent.wholeQuranMode)?"القرآن الكريم كاملاً":myParent.SelectedSura.SuraNameArabic;
                            BarDataSet barData = new BarDataSet(entries, suraName);
                            myChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(result.keySet()));
                            if (chkbx_basmala_letters.isChecked()) {
                                barData.setColors(Color.parseColor("#FF5900"));
                            } else {
                                barData.setColors(Color.parseColor("#FFA800"));
                            }

                            myChart.getAxisLeft().setEnabled(false);
                            myChart.setData(new BarData(barData));
                            myChart.invalidate();
                        }
                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }
}
