package abdlrhmanshehata.yatadabaron.DataAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Locale;

import abdlrhmanshehata.yatadabaron.Auxilliary.Localization;
import abdlrhmanshehata.yatadabaron.Model.Sura;
import abdlrhmanshehata.yatadabaron.R;

public class SuraAdapter extends ArrayAdapter<Sura> {
    int layoutResourceId;
    public SuraAdapter(@NonNull Context context, int resource,Sura[] data) {
        super(context, resource,data);
        this.layoutResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        Sura currentSura = super.getItem(position);
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)super.getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        TextView txt_suraName =(TextView)row.findViewById(R.id.txt_suraName);
        TextView txt_suraInfo =(TextView)row.findViewById(R.id.txt_suraInfo);
        TextView txt_suraID = (TextView)row.findViewById(R.id.txt_suraID);


        txt_suraName.setText(currentSura.SuraNameArabic);
        txt_suraID.setText(Localization.getArabicNumber(currentSura.SuraID));
        txt_suraInfo.setText(currentSura.GetSuraInfo(true));
        if (currentSura.SuraID==0) {
            txt_suraID.setVisibility(View.GONE);
            txt_suraInfo.setVisibility(View.GONE);
            txt_suraName.setGravity(Gravity.CENTER);
            //txt_suraName.setTextColor(Color.parseColor("#360000"));
            txt_suraName.setPadding(0,45,0,45);
            txt_suraName.setPaintFlags(txt_suraName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        }else{
            txt_suraID.setVisibility(View.VISIBLE);
            txt_suraInfo.setVisibility(View.VISIBLE);
            txt_suraName.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
            //txt_suraName.setTextColor(Color.parseColor("#000000"));
            txt_suraName.setPadding(0,0,0,0);
            txt_suraName.setPaintFlags(txt_suraName.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        }
        return row;
    }
}
