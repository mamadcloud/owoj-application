package com.thehasnibrothers.owoj.Services;

import com.thehasnibrothers.owoj.Models.Owoj;

import org.json.JSONObject;

/**
 * Created by user on 22/4/2015.
 */
public class OwojService {
    public Owoj parseJson(JSONObject jsonObject)
    {
        Owoj owoj = new Owoj("yyyy-MM-dd HH:mm:ss");
        try{
            owoj.setId(jsonObject.getInt("id"));
            owoj.setStartDate(jsonObject.getString("startDate"));
            owoj.setEndDate(jsonObject.getString("endDate"));
            owoj.setKhatamNo(jsonObject.getInt("khatamNo"));
            String[] juz = new String[30];
            for(int i=1; i<=30; i++)
            {
                juz[i-1] = jsonObject.getString("juz"+String.valueOf(i));
            }
            owoj.setJuz(juz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return owoj;
    }
}
