package abdlrhmanshehata.yatadabaron.Auxilliary;

import android.content.Context;
import android.util.Log;

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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        OutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(path);
            int byteRead;
            while ((byteRead = inputStream.read()) != -1) {
                outputStream.write(byteRead);
            }
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
        }
        catch (IOException ioe) {
            Log.e("Utils.java/WriteToFile", "Failed to write to file : " + ioe.toString());
        }
    }
}
