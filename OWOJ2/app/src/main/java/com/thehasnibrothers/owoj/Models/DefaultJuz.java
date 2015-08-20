package com.thehasnibrothers.owoj.Models;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by user on 18/4/2015.
 */
public class DefaultJuz {
    private Map<String, Integer> defaultJuz;

    public DefaultJuz(){
        defaultJuz = new LinkedHashMap<String, Integer>();
        defaultJuz.put("Umi",2);
        defaultJuz.put("A. Mia", 3);
        defaultJuz.put("A. Ema", 4);
        defaultJuz.put("A. Erna", 5);
        defaultJuz.put("A. Tikah", 6);
        defaultJuz.put("C. Dolah", 7);
        defaultJuz.put("A. Jenab", 8);
        defaultJuz.put("A. Suraya", 9);
        defaultJuz.put("A. Maya", 10);
        defaultJuz.put("C. Jafar", 11);
        defaultJuz.put("Nadia", 12);
        defaultJuz.put("Yasmin Hasni", 13);
        defaultJuz.put("Moh Hasni", 14);
        defaultJuz.put("Dinar", 15);
        defaultJuz.put("Kamal", 16);
        defaultJuz.put("Hasyim", 17);
        defaultJuz.put("Nca", 18);
        defaultJuz.put("Huda", 19);
        defaultJuz.put("Wafa", 20);
        defaultJuz.put("Hasan H.", 21);
        defaultJuz.put("Chanan", 22);
        defaultJuz.put("Nahl", 23);
        defaultJuz.put("Alwi", 24);
        defaultJuz.put("Nahyda", 25);
        defaultJuz.put("Ali", 26);
        defaultJuz.put("Bagir", 27);
        defaultJuz.put("Husain", 28);
        defaultJuz.put("Hasan", 29);
        defaultJuz.put("Muna", 30);
        defaultJuz.put("Ali Assery", 1);
    }

    public Map<String, Integer> getDefaultJuzs()
    {
        return defaultJuz;
    }

    public Map.Entry<String, Integer> getDefaultJuz(int value)
    {
        Map.Entry<String, Integer> map = null;
        for (Map.Entry<String, Integer> entry : defaultJuz.entrySet())
        {
            if(map.getValue() == value) {
                map = entry;
                break;
            }
        }
        return map;
    }

    public Map.Entry<String, Integer> getDefaultJuz(String value)
    {
        Map.Entry<String, Integer> map = null;
        for (Map.Entry<String, Integer> entry : defaultJuz.entrySet())
        {
            if(entry.getKey().equals(value)) {
                map = entry;
                break;
            }
        }
        return map;
    }
}
