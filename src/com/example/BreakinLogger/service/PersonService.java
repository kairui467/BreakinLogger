package com.example.BreakinLogger.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.BreakinLogger.domain.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonService
{
    private DBOpenHelper dbHelp;

    public PersonService(Context context)
    {
        this.dbHelp = new DBOpenHelper(context);
    }


    /**
     * 添加记录
     * @param person
     */
    public boolean save(Person person)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db != null)
        {
            db.execSQL("insert into " + DBOpenHelper.PM_TABLE + "(" + DBOpenHelper.PM_TIME + "," + DBOpenHelper.PM_NAME + "," + DBOpenHelper.PM_NUMBER + ") values(?,?,?)",
              new Object[] { person.getTime(), person.getPMName(), person.getNumber() });
            return true;
        }
        return false;
    }


    /**
     * 删除记录
     * @param id 记录ID
     */
    public boolean delete(Integer id)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db != null)
        {
            db.execSQL("delete from " + DBOpenHelper.PM_TABLE + " where " + DBOpenHelper.PM_ID + "=?", new Object[] { id });
            return true;
        }
        return false;
    }


    /**
     * 更新记录
     * @param person
     */
    public boolean update(Person person)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db != null)
        {
            db.execSQL("update " + DBOpenHelper.PM_TABLE + " set " + DBOpenHelper.PM_TIME + "=?," + DBOpenHelper.PM_NAME + "=?,"
                + DBOpenHelper.PM_NUMBER + "=? where " + DBOpenHelper.PM_ID + "=?",
              new Object[] { person.getTime(), person.getPMName(), person.getNumber(), person.getId() }
            );
            return true;
        }
        return false;
    }


    /**
     * 查询记录
     * @param id 记录ID
     * @return
     */
    public Person find(Integer id)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        Cursor cursor = null;
        if (db != null)
        {
            cursor = db.rawQuery("select * from " + DBOpenHelper.PM_TABLE + " where " + DBOpenHelper.PM_ID + "=?", new String[] { id.toString() });
        }
        if (cursor != null)
        {
            if (cursor.moveToNext())
            {
                int personid = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.PM_ID));
                String time = cursor.getString(cursor.getColumnIndex(DBOpenHelper.PM_TIME));
                String PMName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.PM_NAME));
                int number = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.PM_NUMBER));
                return new Person(personid, time, PMName, number);
            }
        }
        cursor.close();
        return null;
    }

    /**
     * 获取每个动作练习的总数
     * @return
     */
    public Cursor getPMnum()
    {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        if (db != null)
        {
            return db.rawQuery("select " + DBOpenHelper.PM_ID + " as _id," + DBOpenHelper.PM_NAME + ",sum(" + DBOpenHelper.PM_NUMBER + ") as " + DBOpenHelper.PM_NUMBER + " from " + dbHelp.PM_TABLE + " " +
              "group by " + DBOpenHelper.PM_NAME + " order by " + DBOpenHelper.PM_NAME + " DESC", null);
        }
        return null;
    }

    /**
     * 获得该动作的练习次数
     * @param PMName 动作名
     * @return
     */
    public Cursor getThisPMnum(String PMName)
    {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        if (db != null)
        {
            return db.rawQuery("select " + dbHelp.PM_ID + " as _id," + dbHelp.PM_TIME + "," + dbHelp.PM_NAME + "," + dbHelp.PM_NUMBER + " from " + dbHelp.PM_TABLE +
              " " + " where " + dbHelp.PM_NAME + "=? order by " + DBOpenHelper.PM_TIME + " DESC", new String[] { PMName });
        }
        return null;
    }


    /**
     * 分页获取记录
     * @param offset    跳过前面多少条记录
     * @param maxResult 每页获取多少条记录
     * @return
     */
    public List<Person> getScrollData(int offset, int maxResult)
    {
        List<Person> persons = new ArrayList<Person>();
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery("select * from " + DBOpenHelper.PM_TABLE + " order by " + DBOpenHelper.PM_ID + " asc limit ?,?", new String[] { String.valueOf(offset), String.valueOf(maxResult) });
        else
            return null;

        while (cursor.moveToNext())
        {
            int personid = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.PM_ID));
            String time = cursor.getString(cursor.getColumnIndex(DBOpenHelper.PM_TIME));
            String PMName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.PM_NAME));
            int number = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.PM_NUMBER));
            persons.add(new Person(personid, time, PMName, number));
        }
        cursor.close();
        return persons;
    }

    /**
     * 分页获取记录
     * @param offset    跳过前面多少条记录
     * @param maxResult 每页获取多少条记录
     * @return
     */
    public Cursor getCursorScrollData(int offset, int maxResult)
    {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        if (db != null)
        {
            return db.rawQuery("select " + DBOpenHelper.PM_ID + " as _id," + DBOpenHelper.PM_TIME + "," + DBOpenHelper.PM_NAME + ","
                + DBOpenHelper.PM_NUMBER + " from " + DBOpenHelper.PM_TABLE + " order by " + DBOpenHelper.PM_ID + " DESC limit ?,?",
              new String[] { String.valueOf(offset), String.valueOf(maxResult) }
            );
        }
        return null;
    }

    /**
     * 获取记录总数
     * @return
     */
    public long getCount()
    {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery("select count(*) from " + DBOpenHelper.PM_TABLE + "", null);
        else
            return 0;

        cursor.moveToFirst();
        long result = cursor.getLong(0);
        cursor.close();
        return result;
    }

    /**
     * 获取该动作的练习次数
     * @param pm_name 动作名
     * @return
     */
    public long getNameCount(String pm_name)
    {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery("select count(*) from " + DBOpenHelper.PM_TABLE + " where " + DBOpenHelper.PM_NAME + "=?", new String[] { pm_name });
        else
            return 0;

        cursor.moveToFirst();
        long result = cursor.getLong(0);
        cursor.close();
        return result;
    }
}
