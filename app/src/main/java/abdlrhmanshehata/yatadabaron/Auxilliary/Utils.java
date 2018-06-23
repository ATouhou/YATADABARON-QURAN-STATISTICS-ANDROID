package abdlrhmanshehata.yatadabaron.Auxilliary;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.Collator;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utils {
    public static InputStream ReadAssetAsStream(Context c, String assetName){
        InputStream inputStream = null;
        try {
            inputStream = c.getAssets().open(assetName);
        } catch (Exception e) {
            Log.e("Utils.java", "Error Reading File : "+e.getMessage());
        }
        return inputStream;
    }
    public static String ReadAsset(Context c, String assetName){
        InputStream inputStream = null;
        try {
            inputStream = c.getAssets().open(assetName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedStreamReader = new BufferedReader(inputStreamReader);

            String line = "";
            StringBuilder lines = new StringBuilder();
            while ((line=bufferedStreamReader.readLine())!=null) {
               lines.append(line);
            }
            inputStreamReader.close();
            bufferedStreamReader.close();
            inputStreamReader = null;
            bufferedStreamReader = null;
            return lines.toString();
        } catch (Exception e) {
            Log.e("Utils.java", "Error Reading File : "+e.getMessage());
            return null;
        }
    }
    public static String ReadFile(String fileName){
        InputStream inputStream = null;
        try {
            inputStream =  new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedStreamReader = new BufferedReader(inputStreamReader);

            String line = "";
            StringBuilder lines = new StringBuilder();
            while ((line=bufferedStreamReader.readLine())!=null) {
                lines.append(line);
            }
            inputStreamReader.close();
            bufferedStreamReader.close();
            inputStreamReader = null;
            bufferedStreamReader = null;
            return lines.toString();
        } catch (Exception e) {
            Log.e("Utils.java/ReadFile", "Error Reading File : "+e.getMessage());
            return null;
        }
    }
    public static String GenerateToken(){
        return String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
    }
    public static String[] ArabicLetters(){
        return new String[]{"ء","آ","ا","أ","إ","ب","ت","ة","ث","ج","ح","خ","د","ذ","ر","ز","س","ش","ص","ض","ط","ظ","ع","غ","ف","ق","ك","ل","م","ن","ه","و","ؤ","ي","ئ","ى"};
    }
    public static void WriteToFile(InputStream inputStream,String path){
        BufferedOutputStream bfOutputStream = null;
        try{
            bfOutputStream = new BufferedOutputStream(new FileOutputStream(path));
            byte[] buff = new byte [8 * 1024];
            int len;
            while ((len = inputStream.read(buff)) > 0) {
                bfOutputStream.write(buff,0,len);
            }
            inputStream.close();
            bfOutputStream.close();
        } catch (IOException ex) {
            Log.e("Utils.java/WriteToFile", "Failed to write to file : " + ex.toString());
        }
    }
    public static void WriteToFile(String data,String path) {
        try {
            OutputStream outputStream = new FileOutputStream(path);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Path from = Paths.get(data);
                Path to = Paths.get(path);
                Files.copy(from, to, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            }
        }
        catch (IOException ioe) {
            Log.e("Utils.java/WriteToFile", "Failed to write to file : " + ioe.toString());
        }
    }
    public static boolean TashkelWordMatches(String keyword,String tashkelKeyword,WordLocation wordLocation){
        String raw = "";
        for (int i = 0; i < tashkelKeyword.length(); i++) {
            String crnt_char = String.valueOf(tashkelKeyword.charAt(i));
            if (Arrays.asList(Utils.ArabicLetters()).contains(crnt_char)){
                raw += crnt_char;
            };
        }
        boolean isMatchingExactly =(Arrays.equals(raw.getBytes(),keyword.getBytes()));
        boolean isContaining = raw.contains(keyword);
        if (wordLocation == WordLocation.Exactly){
            return isMatchingExactly;
        }else {
            return isContaining;
        }
    }
    public static Map<String, Float> SortLetterFrequency(String[] letters, Float[] freq) {
        Map<String,Float> unsortMap = new HashMap<String,Float>();
        for (int i = 0; i < freq.length; i++) {
            String s = letters[i];
            Float f = freq[i];
            unsortMap.put(s, f);
        }
        List<Map.Entry<String, Float>> list = new LinkedList<Map.Entry<String, Float>>(unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            public int compare(Map.Entry<String, Float> o1,
                               Map.Entry<String, Float> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        Map<String, Float> sortedMap = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static Map<String,Float> BuildLetterFrequencyMap(String[] letters,Float[] freq){
        Map<String,Float> unsortMap = new HashMap<String,Float>();
        for (int i = 0; i < freq.length; i++) {
            String s = letters[i];
            Float f = freq[i];
            unsortMap.put(s, f);
        }
        return unsortMap;
    }
}
