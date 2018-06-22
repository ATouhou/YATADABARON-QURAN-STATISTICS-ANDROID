package abdlrhmanshehata.yatadabaron.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import abdlrhmanshehata.yatadabaron.Auxilliary.Localization;

public class Sura implements Serializable{
    public int SuraID;
    public String SuraNameArabic;
    public String SuraNameEnglish;
    public int Location;
    public int SajdaLocation;
    public int AyatCount;

    public String GetSuraLocation(boolean arabic){
        String location = (this.Location==1)?"Makki":"Madani";
        if(arabic){
            location = (this.Location==1)?"مكية":"مدنية";
        }
        return location;
    }
    public  String GetAyaCountAsString(boolean arabic){
        String ayaCount = String.valueOf(this.AyatCount) + " verses";
        if(arabic){
            ayaCount = Localization.getArabicNumber(this.AyatCount) + " آية";
        }
        return ayaCount;
    }
    public String GetSuraInfo(boolean arabic){
        String suraInfo = GetSuraLocation(arabic) + " - " +GetAyaCountAsString(arabic);
        if(SuraID == 0){
            suraInfo = GetAyaCountAsString(arabic);
        }
        return  suraInfo;
    }
}
