package abdlrhmanshehata.yatadabaron.DataAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import abdlrhmanshehata.yatadabaron.Auxilliary.Localization;
import abdlrhmanshehata.yatadabaron.Auxilliary.SearchMode;
import abdlrhmanshehata.yatadabaron.Model.Aya;
import abdlrhmanshehata.yatadabaron.Model.Sura;
import abdlrhmanshehata.yatadabaron.R;

public class SearchAyaAdapter extends ArrayAdapter<Aya> {
    Context context;
    int layoutResourceId;
    Aya[] data = new Aya[]{};
    String SearchWord = "";
    boolean Tashkel = false;

    public SearchAyaAdapter(@NonNull Context context, int resource, Aya[] data, String SearchWord, boolean tashkel) {
        super(context, resource,data);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = data;
        this.SearchWord = SearchWord;
        this.Tashkel = tashkel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView txt_text =(TextView)row.findViewById(R.id.txt_ayaText);
        TextView txt_id =(TextView)row.findViewById(R.id.txt_ayaID);

        String suraNameArabic =data[position].SuraNameArabic;
        String ayaIndex = Localization.getArabicNumber(data[position].AyaIndex);
        String header = String.format("%s : {%s}",suraNameArabic,ayaIndex);
        String ayaText = data[position].AyaText;
        String ayaTextTashkel = data[position].AyaTextTashkel;


        String textToBeViewed = "";
        int start = 0;
        int end = 0;
        if (Tashkel){
            textToBeViewed=(ayaTextTashkel);
//            start = ayaText.indexOf(SearchWord);
//            end = start + SearchWord.length();
        }else{
            textToBeViewed  = ayaText;
            if(textToBeViewed.contains(SearchWord)) {
                start = textToBeViewed.indexOf(SearchWord);
                end = start + SearchWord.length();
            }
        }
        Spannable ayaTextSpannable = new SpannableString(textToBeViewed);
        ayaTextSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FF8821")),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_text.setText(ayaTextSpannable,TextView.BufferType.SPANNABLE);
        txt_id.setText(header);
        return row;
    }
}
