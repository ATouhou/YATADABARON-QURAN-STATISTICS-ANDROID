package abdlrhmanshehata.yatadabaron.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Sura implements Serializable{
    public int SuraID;
    public String SuraNameArabic;
    public String SuraNameEnglish;
    public int Location;
    public int SajdaLocation;
    public int AyatCount;

    public String GetSuraLocation(){
        String location = (this.Location==1)?"Makki":"Madani";
        return location;
    }
    public  String GetAyaCountAsString(){
        String ayaCount = String.valueOf(this.AyatCount) + " verses";
        return ayaCount;
    }
    public String GetSuraInfo(){
        String suraInfo = GetSuraLocation() + " - " +GetAyaCountAsString();
        if(SuraID == 0){
            suraInfo = GetAyaCountAsString();
        }
        return  suraInfo;
    }
}
