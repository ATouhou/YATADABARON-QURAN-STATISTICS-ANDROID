package abdlrhmanshehata.yatadabaron.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import abdlrhmanshehata.yatadabaron.Activities.SecondaryActivity;
import abdlrhmanshehata.yatadabaron.DataAdapters.ReadAyaAdapter;
import abdlrhmanshehata.yatadabaron.Model.Aya;
import abdlrhmanshehata.yatadabaron.R;


public class ReadFragment extends Fragment {
    private ListView lstView_aya_read;
    private SecondaryActivity myParent;
    final Fragment me = this;
    public ReadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myParent = (SecondaryActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_read, container, false);
        lstView_aya_read = (ListView) view.findViewById(R.id.lstView_aya_read);
        //final ProgressDialog dialog = ProgressDialog.show(myParent, "Executing", "Please wait...", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                myParent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Aya[] subList =  myParent.MyHelper.GetAyat(myParent.SelectedSura.SuraID,false);;
                        if (subList!=null) {
                            final ReadAyaAdapter adapter = new ReadAyaAdapter(myParent, R.layout.inflater_aya_read, subList, true);
                            lstView_aya_read.setAdapter(adapter);
                        }
                        //dialog.dismiss();
                    }
                });
            }
        }).start();
        return view;
    }
}
