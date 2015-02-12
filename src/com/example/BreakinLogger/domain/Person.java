package com.example.BreakinLogger.domain;

public class Person
{
    private Integer id;
    private String time;
    private String PMName;
    private Integer number;

    public Person()
    {
    }

    public Person(String pTime, String pPM_name, Integer pNumber)
    {
        super();
        this.time = pTime;
        this.PMName = pPM_name;
        this.number = pNumber;
    }

    public Person(Integer _id, String pTime, String pPM_name, Integer pNumber)
    {
        super();
        this.id = _id;
        this.time = pTime;
        this.PMName = pPM_name;
        this.number = pNumber;
    }

    public Person(String pTime, String pPM_name, Integer pNumber, Integer _id)
    {
        super();
        this.id = _id;
        this.time = pTime;
        this.PMName = pPM_name;
        this.number = pNumber;
    }


    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }


    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getPMName()
    {
        return PMName;
    }

    public void setPMName(String PMName)
    {
        this.PMName = PMName;
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }
}
