package com.example.BreakinLogger.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;//版本
    private static final String DB_NAME = "PM_DataBase.db";//数据库名
    public static final String PM_TABLE = "pm_table";//表名
    public static final String PM_ID = "pm_id";//ID
    public static final String PM_TIME = "pm_time";//时间
    public static final String PM_NAME = "pm_name";//动作名
    public static final String PM_NUMBER = "pm_number";//练习次数

    //创建数据库语句
    private static final String CREATE_TABLE = "create table " + PM_TABLE + " ( " + PM_ID + " integer primary key autoincrement,"
      + PM_TIME + "  datetime not null," + PM_NAME + " varchar(20) not null," + PM_NUMBER + " integer not null)";

    public DBOpenHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //是在数据库每一次被创建的时候调用的
        db.execSQL(CREATE_TABLE);
        db.execSQL("create table nameArry (nameId  integer primary key autoincrement,name varchar(30) not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //db.execSQL("ALTER TABLE person ADD data integer");
    }

}
