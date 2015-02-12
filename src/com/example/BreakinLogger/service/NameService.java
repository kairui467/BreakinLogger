package com.example.BreakinLogger.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by kerray on 14-3-20.
 * @方法名:com.example.BreakinLogger.service
 * @功能:
 * @参数:
 * @创建人:kerray
 * @创建时间:14-3-20
 */
public class NameService
{
    private DBOpenHelper dbHelp;

    public NameService(Context context)
    {
        this.dbHelp = new DBOpenHelper(context);
        initAdd();
    }

    private void initAdd()
    {
        // 数据为空时添加默认数据
        if (getNameCount() == 0)
        {
            String[] data = { "Flare（托马斯）", "Windmill(风车）", "HALO(刷头风车） ", "BABY MILLS", "Airflare(大回环)", "ELBOW FLARE(肘托马)" };
            for (String str : data)
                saveName(str);
        }
    }

    /**
     * 保存
     * @param name
     * @return
     */
    public boolean saveName(String name)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db != null)
        {
            db.execSQL("insert into nameArry(name) values(?)", new String[] { name });
            return true;
        }
        return false;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean deleteName(int id)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db != null)
        {
            db.execSQL("delete from nameArry where nameId =?", new Object[] { id });
            return true;
        }
        return false;
    }

    /**
     * 更新
     * @param id
     * @param name 需更新的变量
     * @return
     */
    public boolean updateName(int id, String name)
    {
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        if (db != null)
        {
            db.execSQL("update nameArry set name=? where nameId=?", new Object[] { name, id });
            return true;
        }
        return false;
    }

    /**
     * 获取全部数据
     * @return
     */
    public Cursor getCursorData()
    {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        if (db != null)
        {
            return db.rawQuery("select nameId as _id,name from nameArry", null);
        }
        return null;
    }

    /**
     * 获取 List<> 数组的所有动作名
     * @return
     */
    public List<String> getListData()
    {
        List<String> list = new ArrayList<String>();
        Cursor cursor = getCursorData();
        while (cursor.moveToNext())
        {
            list.add(cursor.getString(cursor.getColumnIndex("name")));
        }
        cursor.close();
        return list;
    }

    /**
     * 获取总数
     * @return
     */
    public int getNameCount()
    {
        SQLiteDatabase db = dbHelp.getReadableDatabase();
        Cursor cursor = null;
        if (db != null)
            cursor = db.rawQuery("select count(*) from nameArry", null);
        else
            return 0;
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return result;
    }
}
