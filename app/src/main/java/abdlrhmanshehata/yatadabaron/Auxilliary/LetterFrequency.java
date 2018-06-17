package abdlrhmanshehata.yatadabaron.Auxilliary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import abdlrhmanshehata.yatadabaron.Auxilliary.Utils;

public class LetterFrequency {
    public static Map<String, Float> Sort(String[] letters,Float[] freq) {
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