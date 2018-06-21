package abdlrhmanshehata.yatadabaron.Auxilliary;

import android.content.Context;
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
    public String ToArabic(String x){
        String y =  x;
        y = y.replace("0","");
        y = y.replace("1","");
        y = y.replace("2","");
        y = y.replace("3","");
        y = y.replace("4","");
        y = y.replace("5","");
        y = y.replace("6","");
        y = y.replace("7","");
        y = y.replace("8","");
        y = y.replace("9","");
        return y;
    }
}
