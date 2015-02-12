package com.example.BreakinLogger.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.example.BreakinLogger.R;
import com.example.BreakinLogger.service.DBOpenHelper;
import com.example.BreakinLogger.service.PersonService;

public class MainActivity extends Activity
{
    //*************************声明类*********************************//
    private PersonService mPersonService;                   // 练习记录表的增删改查类

    //*************************声明控件*********************************//
    private ListView mListView1;                            // 显示每个动作练习次数的 ListView
    private ListView mListView2;                            // 显示该动作所有数据的 ListView
    private TextView pm_id;                                 // id，不显示
    private TextView pm_time;
    private TextView pm_name;
    private TextView pm_number;
    private Button readButon;                               // 返回 ListView1 的按钮，默认不显示

    //*************************声明变量*********************************//
    private boolean isSuccee;                   // 判断是否成功

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        init();// 初始化

        // 迷你广告调用方式
        // ad();
    }

    /**
     * Activity 重启时调用
     */
    @Override
    protected void onResume()
    {
        showData();
        showList1();
        super.onResume();
    }

    /**
     * 设置菜单项
     */
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 1, R.string.about);
        menu.add(0, 2, 2, R.string.update);
        menu.add(0, 3, 3, R.string.exit);
        return true;
    }

    /**
     * 判断选择的菜单功能
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getTitle().equals("关于"))
            menuDlg(R.layout.layout_about, R.string.about_developer);

        else if (item.getTitle().equals("更新"))
            menuDlg(R.layout.layout_updata, R.string.update);

        else if (item.getTitle().equals("退出"))
        {
            this.finish();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 如果退出当前 Activity 则结束线程
     */
    protected void onDestroy()
    {
        this.finish();
        System.exit(0);
        super.onDestroy();
    }

    /*********************************************************************************************/
    /*****************************************方法区**********************************************/
    /** **************************************************************************************** */

    private void init()
    {
        // 初始化 PersonService() 类
        mPersonService = new PersonService(this);
        mListView1 = (ListView) findViewById(R.id.listViewOne);
        mListView2 = (ListView) findViewById(R.id.listViewTwo);
        readButon = (Button) findViewById(R.id.backButton);

        mListView1.setOnItemClickListener(new list1ItemClick());
        mListView2.setOnItemClickListener(new list2ItemClick());

    }

    /**
     * 万普迷你广告方法
     */
    private void ad()
    {
        /*AppConnect.getInstance(this).setAdBackColor(Color.argb(50, 120, 240, 120));//设置迷你广告背景颜色
        AppConnect.getInstance(this).setAdForeColor(Color.YELLOW);//设置迷你广告文字颜色
        LinearLayout miniLayout = (LinearLayout) findViewById(R.id.miniAdLinearLayout);
        AppConnect.getInstance(this).showMiniAd(this, miniLayout, 600);// 刷新间隔（秒）*/
    }

    /**
     * Button 点击处理方法
     * @param v 视图对象
     */
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.button_skip_data:
            Intent _intent = new Intent(MainActivity.this, AddDataActivity.class);
            _intent.putExtra("type", "save");
            startActivity(_intent);
            break;
        case R.id.backButton:
            showData();
            showList1();
            break;
        }
    }

    /**
     * 判断是否成功
     * @param bool
     * @return
     */
    public String isSucceed(boolean bool)
    {
        return (bool = true) ? "成功" : "失败";
    }


    /**
     * 显示数据到 ListView
     */
    private void showData()
    {
        if (mPersonService.getCount() == 0)
        {
            Toast.makeText(MainActivity.this, R.string.NoData, 0).show();
        }
        Cursor cursor = mPersonService.getPMnum();
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.layout_item_one, cursor,
          new String[] { "_id", DBOpenHelper.PM_NAME, DBOpenHelper.PM_NUMBER },
          new int[] { R.id.text_one_personid, R.id.text_one_PMName, R.id.text_one_number }, 0);
        mListView1.setAdapter(simpleCursorAdapter);
    }

    /**
     * 显示 ListView1，隐藏 ListView2
     */
    public void showList1()
    {
        mListView2.setVisibility(View.GONE);
        mListView1.setVisibility(View.VISIBLE);
        readButon.setVisibility(View.GONE);
    }

    /**
     * 显示 ListView2，隐藏 ListView1
     */
    public void showList2()
    {
        mListView2.setVisibility(View.VISIBLE);
        mListView1.setVisibility(View.GONE);
        readButon.setVisibility(View.VISIBLE);
    }

    /**
     * 自定义的 Dialog
     * @param playout xml 布局文件引用路径
     * @param title
     * @param title   Dialog 标题
     */
    private void menuDlg(int playout, int title)
    {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(playout, (ViewGroup) findViewById(R.id.dialog));
        new AlertDialog.Builder(this)
          .setTitle(title)
          .setView(layout)
          .setPositiveButton(R.string.Yes, null)
          .setNegativeButton(R.string.Cancel, null).show();
    }

    /**
     * 选择删除还是修改记录的 Dialog
     */
    public void selectDlg()
    {
        AlertDialog.Builder _dialog = new AlertDialog.Builder(MainActivity.this);
        _dialog.setItems(new String[] { "删除该条记录", "修改该条记录" }, new dlgOnClickListener());
        _dialog.show();
    }

    /**
     * 删除记录的 Dialog
     */
    public void deleteDlg()
    {
        AlertDialog.Builder _dialog = new AlertDialog.Builder(MainActivity.this);
        _dialog.setMessage(R.string.ConDelThisData);
        _dialog.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                isSuccee = mPersonService.delete(Integer.parseInt(pm_id.getText().toString()));
                Toast.makeText(MainActivity.this, "删除" + isSucceed(isSuccee), 0).show();
                // 更新记录
                showData();
                showList1();
                dialog.dismiss();
            }
        });
        _dialog.setNegativeButton(R.string.Cancel, null);
        _dialog.show();
    }

    /*********************************************************************************************/
    /*****************************************接口继承区******************************************/
    /*********************************************************************************************/


    /**
     * ListView1 单击处理事件
     */
    class list1ItemClick implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Cursor cursor = mPersonService.getThisPMnum(((TextView) view.findViewById(R.id.text_one_PMName)).getText().toString());
            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(MainActivity.this, R.layout.layout_item_two, cursor,
              new String[] { "_id", DBOpenHelper.PM_TIME, DBOpenHelper.PM_NAME, DBOpenHelper.PM_NUMBER },
              new int[] { R.id.text_two_person_id, R.id.text_two_time, R.id.text_two_PMName, R.id.text_two_number }, 0);

            mListView2.setAdapter(simpleCursorAdapter);
            showList2();
        }
    }


    /**
     * ListView2 单击处理事件
     */
    class list2ItemClick implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            pm_id = (TextView) view.findViewById(R.id.text_two_person_id);
            pm_time = (TextView) view.findViewById(R.id.text_two_time);
            pm_name = (TextView) view.findViewById(R.id.text_two_PMName);
            pm_number = (TextView) view.findViewById(R.id.text_two_number);

            selectDlg();
        }
    }

    /**
     * Dialog 响应事件
     */
    class dlgOnClickListener implements DialogInterface.OnClickListener
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
            case 0:
                deleteDlg();
                break;
            case 1:// 发送数据并跳转到 AddDataActivity
                Intent _intent = new Intent(MainActivity.this, AddDataActivity.class);
                _intent.putExtra("type", "update");
                _intent.putExtra("pm_id", Integer.parseInt(pm_id.getText().toString()));
                _intent.putExtra("pm_time", pm_time.getText());
                _intent.putExtra("pm_name", pm_name.getText());
                _intent.putExtra("pm_number", pm_number.getText());
                startActivity(_intent);
                break;
            }
        }
    }
}
