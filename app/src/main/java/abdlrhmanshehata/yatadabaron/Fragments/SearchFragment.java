package abdlrhmanshehata.yatadabaron.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import abdlrhmanshehata.yatadabaron.Activities.SecondaryActivity;
import abdlrhmanshehata.yatadabaron.Auxilliary.Localization;
import abdlrhmanshehata.yatadabaron.Auxilliary.SearchMode;
import abdlrhmanshehata.yatadabaron.Auxilliary.WordLocation;
import abdlrhmanshehata.yatadabaron.DataAdapters.SearchAyaAdapter;
import abdlrhmanshehata.yatadabaron.Model.Aya;
import abdlrhmanshehata.yatadabaron.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private SecondaryActivity myParent;
    private TextView txt_word;
    private ListView aya_listView;
    private SearchAyaAdapter ayaAdapter;
    private TextView txt_count;
    private Spinner spinner_wordLocation;
    private List<Aya> Results;
    private int CurrentPageIndex;
    private int ResultsPerPage;
    private int PagesCount;
    private TextView txt_pagesCount;
    private TextView txt_currentPageIndex;
    private Button btn_navigateNext;
    private Button btn_navigatePrevious;
    private String SearchWord;
    private CheckBox chkbx_tashkel;
    private CheckBox chkbx_basmala;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myParent = (SecondaryActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        txt_word = (TextView) view.findViewById(R.id.txt_word);
        aya_listView = (ListView) view.findViewById(R.id.lstView_aya);
        txt_count = (TextView) view.findViewById(R.id.txt_count);
        spinner_wordLocation = (Spinner) view.findViewById(R.id.spinner_wordLocation);
        txt_currentPageIndex = (TextView) view.findViewById(R.id.txt_currentPageIndex);
        txt_pagesCount = (TextView) view.findViewById(R.id.txt_pagesCount);
        btn_navigateNext = (Button) view.findViewById(R.id.btn_navigateNext);
        btn_navigatePrevious = (Button) view.findViewById(R.id.btn_navigatePrevious);
        chkbx_tashkel = (CheckBox) view.findViewById(R.id.chkbx_tashkel);
        chkbx_basmala = (CheckBox) view.findViewById(R.id.chkbx_basmala);


        String[] wordLocations = new String[]{"مطابقة", "تحتوي على"};
        spinner_wordLocation.setAdapter(new ArrayAdapter<String>(myParent, R.layout.support_simple_spinner_dropdown_item, wordLocations));
        CurrentPageIndex = -1;
        PagesCount = 0;
        ResultsPerPage = 100;
        SearchWord = "";
        Results = null;

        btn_navigateNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Results != null && CurrentPageIndex <= PagesCount && CurrentPageIndex >= 1) {
                    CurrentPageIndex++;
                    Navigate();
                }
            }
        });

        btn_navigatePrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Results != null && CurrentPageIndex <= PagesCount && CurrentPageIndex > 1) {
                    CurrentPageIndex--;
                    Navigate();
                }
            }
        });

        txt_word.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Search();
                return false;
            }
        });

        chkbx_basmala.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (txt_word.getText()!=null && txt_word.getText().length()>0) {
                    Search();
                }
            }
        });

        chkbx_tashkel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (txt_word.getText()!=null && txt_word.getText().length()>0) {
                    Search();
                }
            }
        });



        return view;
    }
    public void Search(){
        final ProgressDialog dialog = ProgressDialog.show(myParent, "Executing", "Please wait...", true);
        Results = new ArrayList<Aya>();
        ayaAdapter = new SearchAyaAdapter(myParent, R.layout.inflater_aya, new Aya[]{}, SearchWord, chkbx_tashkel.isChecked());
        aya_listView.setAdapter(ayaAdapter);
        PagesCount = 0;
        CurrentPageIndex = -1;
        txt_pagesCount.setText(Localization.getArabicNumber(0));
        txt_currentPageIndex.setText(Localization.getArabicNumber(0));
        txt_count.setText("غ/م");
        //Start New Search
        new Thread(new Runnable() {
            @Override
            public void run() {
                String word = txt_word.getText().toString();
                int suraID = myParent.SelectedSura.SuraID;
                SearchMode mode = SearchMode.SURA;
                if (suraID == 0){
                    mode = SearchMode.QURAN;
                }
                WordLocation location =  spinner_wordLocation.getSelectedItem() == "تحتوي على" ? WordLocation.Contains:WordLocation.Exactly;
                Results = myParent.MyHelper.SearchForWord(word, location,suraID,mode,chkbx_basmala.isChecked());
                SearchWord = word;
                myParent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(Results!=null) {
                            int quotient = (Results.size() / ResultsPerPage);
                            int reminder = (Results.size() % ResultsPerPage);
                            PagesCount = quotient + ((reminder > 0) ? 1 : 0) * 1;
                            CurrentPageIndex = 1;
                            Navigate();
                            String found_msg = "لا يوجد نتائج!";
                            if(Results.size()>0){
                                found_msg = String.format("تم العثور علي %s آية", Localization.getArabicNumber(Results.size()));
                            }
                            txt_count.setText(found_msg);
                            dialog.dismiss();
                        }
                    }
                });
            }
        }).start();
    }
    public void Navigate(){
        if (Results!=null && CurrentPageIndex<=PagesCount && CurrentPageIndex >= 1) {

            txt_pagesCount.setText(Localization.getArabicNumber(PagesCount));
            int start = (CurrentPageIndex - 1) * ResultsPerPage;
            int end = start + ResultsPerPage;
            if (CurrentPageIndex == PagesCount) {
                end = Results.size() - 1;
            }
            Aya[] ayaSubList = Results.subList(start, end + 1).toArray(new Aya[]{});
            ayaAdapter = new SearchAyaAdapter(myParent, R.layout.inflater_aya, ayaSubList, SearchWord, chkbx_tashkel.isChecked());
            aya_listView.setAdapter(ayaAdapter);
            txt_currentPageIndex.setText(Localization.getArabicNumber(CurrentPageIndex));
        }
    }
}