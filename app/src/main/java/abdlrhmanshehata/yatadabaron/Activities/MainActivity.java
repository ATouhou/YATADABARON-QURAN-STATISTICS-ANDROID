package abdlrhmanshehata.yatadabaron.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.Serializable;
import java.util.List;

import abdlrhmanshehata.yatadabaron.Auxilliary.IStatusListener;
import abdlrhmanshehata.yatadabaron.DataAdapters.SuraAdapter;
import abdlrhmanshehata.yatadabaron.DatabaseHelpers.DatabaseHelper;
import abdlrhmanshehata.yatadabaron.Model.Sura;
import abdlrhmanshehata.yatadabaron.R;

public class MainActivity extends AppCompatActivity implements IStatusListener {
    private DatabaseHelper myHelper;
    private ListView sura_lst_view;
    private Toolbar toolbar_main;
    private Button btn_wholeQuran;
    private Button btn_singleChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sura_lst_view = (ListView) findViewById(R.id.main_lstView);
        btn_wholeQuran = (Button) findViewById(R.id.btn_wholeQuran);
        btn_singleChapter = (Button) findViewById(R.id.btn_singleChapter);

        myHelper = new DatabaseHelper(this);
        final Context myContext = this;
        final ProgressDialog dialog = ProgressDialog.show(this, "Copying Files", "Please wait...", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                myHelper.CheckDatabase();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Sura> data = myHelper.GetSuras();
                        final SuraAdapter adapter = new SuraAdapter(myContext,R.layout.inflater_chapters,data.toArray(new Sura[]{}));
                        sura_lst_view.setAdapter(adapter);
                        sura_lst_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Sura selectedSura = adapter.getItem(position);
                                Intent intent = new Intent();
                                intent.setClass(myContext,SecondaryActivity.class);
                                intent.putExtra("SELECTED_SURA",(Serializable) selectedSura);
                                startActivity(intent);
                                sura_lst_view.setVisibility(View.GONE);
                            }
                        });
                        btn_wholeQuran.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(myContext,SecondaryActivity.class);
                                Parcelable _null = null;
                                intent.putExtra("SELECTED_SURA",_null);
                                startActivity(intent);
                                sura_lst_view.setVisibility(View.GONE);
                            }
                        });
                        btn_singleChapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sura_lst_view.setVisibility(View.VISIBLE);
                            }
                        });
                        dialog.dismiss();

                    }
                });
            }
        }).start();
    }

    @Override
    public void QueryStatusChanged(){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void ConnectionStatusChanged() {
        runOnUiThread(new Runnable(){
            @Override
            public void run() {

            }
        });
    }
}
