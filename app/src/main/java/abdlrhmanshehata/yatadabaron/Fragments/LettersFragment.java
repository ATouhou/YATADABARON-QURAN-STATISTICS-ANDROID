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
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abdlrhmanshehata.yatadabaron.Activities.SecondaryActivity;
import abdlrhmanshehata.yatadabaron.Auxilliary.SearchMode;
import abdlrhmanshehata.yatadabaron.Auxilliary.Utils;
import abdlrhmanshehata.yatadabaron.R;


public class LettersFragment extends Fragment{
    private TextView txt_suraName;
    private BarChart myChart;
    private SecondaryActivity myParent;
    private CheckBox chkbx_basmala_letters;
    private CheckBox chkbx_sort_letters;

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

        DrawLettersFrequencyBySura(myParent.SelectedSura.SuraID,true,false);
        chkbx_basmala_letters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DrawLettersFrequencyBySura(myParent.SelectedSura.SuraID,isChecked,chkbx_sort_letters.isChecked());
            }
        });
        chkbx_sort_letters.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DrawLettersFrequencyBySura(myParent.SelectedSura.SuraID,chkbx_basmala_letters.isChecked(),isChecked);
            }
        });

        return view;
    }

    private Map<String,Float> GetProperLetterFrequency(boolean sort,boolean basmala){
        Map<String,Float> result = null;
        if(basmala && sort){
            result = myParent.LettersFrequencySortedBasmala;
        }
        if (!basmala && !sort){
            result = myParent.LettersFrequencyNotSortedNotBasmala;
        }
        if (basmala && !sort){
            result = myParent.LettersFrequencyNotSortedBasmala;
        }
        if (!basmala && sort){
            result = myParent.LettersFrequencySortedNotBasmala;
        }
        return result;
    }
    private void DrawLettersFrequencyBySura(final int suraID,final boolean basmala,final boolean sort){
        final ProgressDialog dialog = ProgressDialog.show(myParent, "Executing", "Please wait...", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchMode mode = SearchMode.SURA;
                if(suraID == 0){
                    mode = SearchMode.QURAN;
                }
                final Map<String,Float> result = GetProperLetterFrequency(sort,basmala);
                if (result != null) {
                    myParent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<BarEntry> entries = new ArrayList<BarEntry>();
                            int i = 0;
                            for (Float frequency : result.values()) {
                                entries.add(new BarEntry(i, frequency));
                                i++;
                            }
                            BarDataSet barData = new BarDataSet(entries, "");
                            myChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(result.keySet()));
                            if(basmala) {
                                barData.setColors(Color.parseColor("#FFA800"));
                            }
                            myChart.setData(new BarData(barData));
                            myChart.invalidate();
                            dialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }
}
