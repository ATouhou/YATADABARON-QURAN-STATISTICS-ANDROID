package abdlrhmanshehata.yatadabaron.Model;

public class Aya {
    public int SuraID;
    public int AyaID;
    public int AyaIndex;
    public String AyaText;
    public String AyaTextTashkel;
    public String SuraNameArabic;
    public String SuraNameEnglish;

    public Aya(int id,int index,int suraID,String text,String tashkel,String suraNameArabic,String suraNameEnglish){
        this.AyaID = id;
        this.SuraID=suraID;
        this.AyaIndex = index;
        this.AyaText = text;
        this.AyaTextTashkel = tashkel;
        this.SuraNameArabic = suraNameArabic;
        this.SuraNameEnglish = suraNameEnglish;
    }

    public String GetAyaInfo(){
        return  SuraNameArabic+" - "+AyaIndex;
    }
}
