package abdlrhmanshehata.yatadabaron.DataAdapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import abdlrhmanshehata.yatadabaron.Auxilliary.LetterFrequencyModel;
import abdlrhmanshehata.yatadabaron.Auxilliary.Localization;
import abdlrhmanshehata.yatadabaron.R;


public class LetterAdapter extends ArrayAdapter<LetterFrequencyModel> {
    private LetterFrequencyModel[] myData;
    final Context myContext;
    final int layoutResourceId;

    public LetterAdapter(@NonNull Context context, int resource, @NonNull LetterFrequencyModel[] data) {
        super(context, resource, data);
        myData = data;
        myContext = context;
        layoutResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }
        LetterFrequencyModel current = myData[position];
        TextView letterTextView = (TextView) row.findViewById(R.id.txtView_letter);
        TextView frequencyTextView = (TextView) row.findViewById(R.id.txtView_frequency);

        letterTextView.setText(current.Letter);
        int frequency = Math.round(current.Frequency);
        frequencyTextView.setText(Localization.getArabicNumber(frequency));
        return row;
    }
}
