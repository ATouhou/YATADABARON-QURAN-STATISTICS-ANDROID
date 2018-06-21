package abdlrhmanshehata.yatadabaron.DatabaseHelpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abdlrhmanshehata.yatadabaron.Auxilliary.ConnectionStatus;
import abdlrhmanshehata.yatadabaron.Auxilliary.IStatusListener;
import abdlrhmanshehata.yatadabaron.Auxilliary.QueryStatus;
import abdlrhmanshehata.yatadabaron.Auxilliary.SearchMode;
import abdlrhmanshehata.yatadabaron.Auxilliary.Utils;
import abdlrhmanshehata.yatadabaron.Auxilliary.WordLocation;
import abdlrhmanshehata.yatadabaron.Model.Aya;
import abdlrhmanshehata.yatadabaron.Auxilliary.LetterFrequency;

import abdlrhmanshehata.yatadabaron.Model.Sura;

public class DatabaseHelper{
    //Properties
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public ConnectionStatus MyConnectionStatus;
    public QueryStatus MyQueryStatus;
    private List<IStatusListener> StatusListeners = new ArrayList<IStatusListener>();



    //Global Variables
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private final String DB_NAME_INTERNAL_STORAGE = "quran.db";
    private final String DB_NAME_ASSET = "quran.db";
    private String PATH_DB_INTERNAL_STORAGE(){
        return myContext.getFilesDir().getAbsolutePath() + "/" + DB_NAME_INTERNAL_STORAGE;
    }


    //Event Handlers
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void RegisterNewListener(IStatusListener toAdd) {
        StatusListeners.add(toAdd);
    }
    private Context myContext;
    private void FireConnectionStatusEvent(ConnectionStatus cs) {
        MyConnectionStatus = cs;
        for (IStatusListener s : StatusListeners){
            s.ConnectionStatusChanged();
        }
    }
    private void FireQueryStatusEvent(QueryStatus qs) {
        MyQueryStatus = qs;
        for (IStatusListener s : StatusListeners){
            s.QueryStatusChanged();
        }
    }


    //Database Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void CheckDatabase(){
        boolean exists = new File(PATH_DB_INTERNAL_STORAGE()).exists();
        if (!exists) {
            FireConnectionStatusEvent(ConnectionStatus.CONNECTING);
            InputStream initialDatabaseAsStream = Utils.ReadAssetAsStream(myContext, DB_NAME_ASSET);
            Utils.WriteToFile(initialDatabaseAsStream, PATH_DB_INTERNAL_STORAGE());
            StandardizeDatabase();
            FireConnectionStatusEvent(ConnectionStatus.CONNECTED);
        }else{
            FireConnectionStatusEvent(ConnectionStatus.CONNECTED);
            FireQueryStatusEvent(QueryStatus.READY);
        }
    }
    private SQLiteDatabase GetDatabaseInstance(){
        SQLiteDatabase db = null;
        try{
            db = SQLiteDatabase.openDatabase(PATH_DB_INTERNAL_STORAGE(), null, SQLiteDatabase.OPEN_READWRITE);
        }catch(Exception ex){}
        return db;
    }
    private void StandardizeDatabase(){
        SQLiteDatabase db = GetDatabaseInstance();
        if (db==null){return;}
        FireQueryStatusEvent(QueryStatus.EXECUTING);
        try{
            db.execSQL("CREATE TABLE 'android_metadata' ('locale' TEXT DEFAULT 'en_US')");
        }catch (Exception ex){
            Log.e("DatabaseHelper","Error While Standardizing Database: "+ex.getMessage());
        }
        try{
            db.execSQL("INSERT INTO 'android_metadata' VALUES ('en_US')");
            Log.println(Log.ASSERT,"DatabaseHelper","StandardizeDatabase()/Row Inserted Successfully");
        }catch (Exception ex){
            Log.e("DatabaseHelper.java","StandardizeDatabase()/Error While Standardizing Database: "+ex.getMessage());
        }
        FireQueryStatusEvent(QueryStatus.READY);
        db.close();
    }



    /// Query Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public List<Aya> SearchForWord(String word, WordLocation location, int id, SearchMode mode,boolean basmala){
        if(MyQueryStatus != QueryStatus.READY || MyConnectionStatus != ConnectionStatus.CONNECTED){return null;}
        SQLiteDatabase db = GetDatabaseInstance();
        if(db == null){return null;}
        FireQueryStatusEvent(QueryStatus.EXECUTING);
        Cursor c = null;
        List<Aya> result = null;
        String keyword = word ;
        String from = "verses_content";
        if(!basmala){
            from = "verses_content_no_basmala";
        }
        try {
            switch (location){
                case Exactly:keyword="% "+keyword+" %";
                case StartsWith:keyword=keyword+"%";
                case EndsWith:keyword="%"+keyword;
                case Contains:keyword="%"+keyword+"%";
                default:keyword=keyword;
            }
            String condition = mode == SearchMode.AYA ? "and "+from+".docid="+id : ((mode == SearchMode.SURA)?"and "+from+".c0sura="+id:"");
            String sql = String.format("select %s.docid,%s.c0sura,%s.c1ayah, %s.c2text,arabic_text.text,chapters.arabic as sura_name_arabic,chapters.latin as sura_name_english from %s inner join arabic_text on arabic_text.sura = %s.c0sura and arabic_text.ayah = %s.c1ayah inner join chapters on chapters.c0sura = %s.c0sura where %s.c2text like '%s' %s",
                    from,from,from,from,from,from,from,from,from,keyword,condition);
            c = db.rawQuery(sql, null);
            result = new ArrayList<Aya>();
            while(c.moveToNext()){
                int ayaID = -1;
                int ayaIndex = -1;
                int suraID = -1;
                String ayaText = "";
                String ayaTextTashkel = "";
                String suraNameEnglish;
                String suraNameArabic;
                try{
                    ayaID = Integer.parseInt(c.getString(0));
                    suraID = Integer.parseInt(c.getString(1));
                    ayaIndex = Integer.parseInt(c.getString(2));
                    ayaText = c.getString(3);
                    ayaTextTashkel = c.getString(4);
                    suraNameArabic = c.getString(5);
                    suraNameEnglish = c.getString(6);
                    Aya aya = new Aya(ayaID,ayaIndex,suraID,ayaText,ayaTextTashkel,suraNameArabic,suraNameEnglish);
                    result.add(aya);
                }catch (NumberFormatException ex){
                    return  null;
                }
            }
        } catch (Exception ex) {
            Log.e("DatabaseHelper", "ExecuteReader()/" + ex.getMessage());
        }
        db.close();
        FireQueryStatusEvent(QueryStatus.READY);
        return result;
    }

    public  Map<String,Float> GetLettersFrequency(int id, SearchMode mode,boolean basmala,boolean sort){
        if(MyQueryStatus != QueryStatus.READY || MyConnectionStatus != ConnectionStatus.CONNECTED){return null;}
        SQLiteDatabase db = GetDatabaseInstance();
        String condition = mode == SearchMode.AYA ? "where docid="+id : ((mode == SearchMode.SURA)?"where c0sura="+id:"");
        if(db == null){return null;}
        FireQueryStatusEvent(QueryStatus.EXECUTING);
        Cursor c = null;
        List<Float> result = null;
        String from = "verses_content";
        if(!basmala){
            from = "verses_content_no_basmala";
        }
        try {
            String[] letters = Utils.ArabicLetters();
            String sql = "select sum(length(c2text)-length(replace(c2text,'%s',''))),sum(length(c2text)-length(replace(c2text,'%s',''))),sum(length(c2text)-length(replace(c2text,'%s',''))),sum(length(c2text)-length(replace(c2text,'%s',''))),sum(length(c2text)-length(replace(c2text,'%s',''))),sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))),sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))), sum(length(c2text)-length(replace(c2text,'%s',''))),sum(length(c2text)-length(replace(c2text,'%s',''))),sum(length(c2text)-length(replace(c2text,'%s',''))),sum(length(c2text)-length(replace(c2text,'%s',''))) from "+from+" "+condition;
            sql = String.format(sql,letters);
            c = db.rawQuery(sql, null);
            c.moveToFirst();
            result = new ArrayList<Float>();
            for (String colName:c.getColumnNames()){
                String value = c.getString(c.getColumnIndex(colName));
                Float value_as_float = Float.parseFloat(value);
                result.add(value_as_float);
            }
        } catch (Exception ex) {
            Log.e("DatabaseHelper", "ExecuteReader()/" + ex.getMessage());
        }
        db.close();
        FireQueryStatusEvent(QueryStatus.READY);
        Map<String,Float> results = LetterFrequency.BuildLetterFrequencyMap(Utils.ArabicLetters(),result.toArray(new Float[]{}));
        if (sort){
            results = LetterFrequency.Sort(Utils.ArabicLetters(),result.toArray(new Float[]{}));
        }
        return results;
    }

    public List<Sura> GetSuras(){
        if(MyQueryStatus != QueryStatus.READY || MyConnectionStatus != ConnectionStatus.CONNECTED){return null;}
        SQLiteDatabase db = GetDatabaseInstance();
        if(db == null){return null;}
        FireQueryStatusEvent(QueryStatus.EXECUTING);
        Cursor c = null;
        List<Sura> result = new ArrayList<>();
        try {
            String sql = "select * from chapters";
            c = db.rawQuery(sql, null);
            while(c.moveToNext()){
                Sura sura = new Sura();
                sura.SuraID = Integer.parseInt( c.getString(0) );
                sura.SuraNameArabic =c.getString(1);
                sura.SuraNameEnglish =c.getString(2);
                sura.Location = Integer.parseInt(c.getString(4));
                sura.SajdaLocation = Integer.parseInt(c.getString(5));
                sura.AyatCount = Integer.parseInt(c.getString(6));
                result.add(sura);
            }
            Sura wholeQuran = new Sura();
            wholeQuran.AyatCount = 6236;
            wholeQuran.SajdaLocation = 0;
            wholeQuran.SuraNameEnglish = "Holy Quran";
            wholeQuran.SuraNameArabic = "القرآن الكريم";
            wholeQuran.SuraID = 0;
            wholeQuran.Location = 0;
            result.add(0,wholeQuran);
        } catch (Exception ex) {
            Log.e("DatabaseHelper", "GetSuras()/" + ex.getMessage());
        }
        db.close();
        FireQueryStatusEvent(QueryStatus.READY);
        return result;
    }

    public Aya[] GetAyat(int suraID,boolean basmala){
        if(MyQueryStatus != QueryStatus.READY || MyConnectionStatus != ConnectionStatus.CONNECTED){return null;}
        SQLiteDatabase db = GetDatabaseInstance();
        if(db == null){return null;}
        FireQueryStatusEvent(QueryStatus.EXECUTING);
        Cursor c = null;
        List<Aya> result = new ArrayList<>();
        try {
            String table_normal = basmala?"verses_content":"verses_content_no_basmala";
            String table_tashkel = basmala?"arabic_text":"arabic_text_no_basmala";
            String condition = String.format("where %s.c0sura=%s and %s.sura=%s",
                    table_normal,
                    String.valueOf(suraID),
                    table_tashkel,
                    String.valueOf(suraID));
            if(suraID == 0){
                condition = "";
            }
            //"select verses_content.docid,verses_content.c0sura,verses_content.c1ayah,verses_content.c2text,arabic_text.text,chapters.arabic,chapters.latin from verses_content left join arabic_text on verses_content.c0sura = arabic_text.sura and verses_content.c1ayah = arabic_text.ayah left join chapters on chapters.c0sura = verses_content.c0sura ";
            String sql = String.format("select %s.docid,%s.c0sura,%s.c1ayah,%s.c2text,%s.text,chapters.arabic,chapters.latin from %s left join %s on %s.c0sura = %s.sura and %s.c1ayah = %s.ayah left join chapters on chapters.c0sura = %s.c0sura %s",
                    table_normal,table_normal,table_normal,table_normal,table_tashkel,table_normal,table_tashkel,table_normal,table_tashkel,table_normal,table_tashkel,table_normal,condition);
            c = db.rawQuery(sql, null);
            while(c.moveToNext()){
                int docid =  c.getInt(0);
                int c0sura = c.getInt(1);
                int c1ayah = c.getInt(2);
                String c2text = c.getString(3);
                String tashkelText = c.getString(4);
                String suraar = c.getString(5);
                String suraen = c.getString(6);
                Aya aya = new Aya(docid,c1ayah,c0sura,c2text,tashkelText,suraar,suraen);
                result.add(aya);
            }
        } catch (Exception ex) {
            Log.e("DatabaseHelper", "GetAyat()/" + ex.getMessage());
        }
        db.close();
        FireQueryStatusEvent(QueryStatus.READY);
        return result.toArray(new Aya[]{});
    }
    //Constructor
    public DatabaseHelper(Context c){
        myContext = c;
        IStatusListener asStatusListener =(IStatusListener)c;
        RegisterNewListener(asStatusListener);
        MyConnectionStatus=ConnectionStatus.NULL;
        MyQueryStatus=QueryStatus.NULL;
    }

}
