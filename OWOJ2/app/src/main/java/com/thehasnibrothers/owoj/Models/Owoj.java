package com.thehasnibrothers.owoj.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 22/4/2015.
 */
public class Owoj {
    int id;
    Date startDate;
    Date endDate;
    int khatamNo;
    String[] juz;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public Owoj(){}

    public Owoj(String dateFormat){
        format = new SimpleDateFormat(dateFormat);
    }

    public void setId(int value)
    {
        id = value;
    }

    public int getId()
    {
        return id;
    }

    public void setStartDate(String value)
    {
        try {
            startDate = format.parse(value);
        }catch (Exception e)
        {}
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setEndDate(String value)
    {
        try {
            endDate = format.parse(value);
        }catch (Exception e)
        {}
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setKhatamNo(int value)
    {
        khatamNo = value;
    }

    public int getKhatamNo()
    {
        return khatamNo;
    }

    public void setJuz(String[] values)
    {
        juz = values;
    }

    public String[] getJuz()
    {
        return juz;
    }

    public String getJuz(int index)
    {
        return juz[index];
    }

    public String getJuz(int index, String formatDate)
    {
        try
        {
            DateFormat df = new SimpleDateFormat(formatDate);
            Date date = format.parse(juz[index]);
            return df.format(date);
        }catch (Exception e)
        {return "";}
    }
}
