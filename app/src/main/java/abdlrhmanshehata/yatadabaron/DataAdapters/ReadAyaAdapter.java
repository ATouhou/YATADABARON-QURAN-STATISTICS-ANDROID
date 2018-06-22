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

public class ReadAyaAdapter extends ArrayAdapter<Aya> {
    Context context;
    int layoutResourceId;
    Aya[] data = new Aya[]{};
    boolean Tashkel = false;

    public ReadAyaAdapter(@NonNull Context context, int resource, Aya[] data, boolean tashkel) {
        super(context, resource,data);
        this.layoutResourceId = resource;
        this.context = context;
        this.data = data;
        this.Tashkel = tashkel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        Aya currentAya = data[position];
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView txt_text =(TextView)row.findViewById(R.id.txt_ayaText);
        TextView txt_id =(TextView)row.findViewById(R.id.txt_ayaID);

        String suraNameArabic =currentAya.SuraNameArabic;
        String ayaIndex = String.valueOf(currentAya.AyaIndex);
        String header = currentAya.GetAyaInfo();
        String ayaText = currentAya.AyaText;
        String ayaTextTashkel = currentAya.AyaTextTashkel;

        String textToBeViewed = "";
        if (Tashkel){
            textToBeViewed=(ayaTextTashkel);
        }else{
            textToBeViewed  = ayaText;
        }
        txt_text.setText(textToBeViewed + " {"+ Localization.getArabicNumber(currentAya.AyaIndex)+"}");
        return row;
    }
}
