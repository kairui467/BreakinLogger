package com.example.BreakinLogger.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.BreakinLogger.R;
import com.example.BreakinLogger.service.NameService;

/**
 * @Created by kerray on 14-3-19.
 * @方法名:com.example.BreakinLogger.activity
 * @功能:
 * @参数:
 * @创建人:kerray
 * @创建时间:14-3-19
 */
public class SetNameActivity extends Activity
{
    //*************************声明类*********************************//
    private NameService mNameService;                   // 动作名表的增删改查类

    //*************************声明控件*********************************//
    private Button mButton;                             // 添加和重命名 Button
    private EditText addEditText;                       // 编辑动作名的 Edittext
    private ListView mListView;                         // 显示动作名的 ListView
    private TextView mTextId;                           // id，为不显示
    private TextView mTextName;

    //*************************声明变量*********************************//
    private String result;                              // 输入的值保存为全局变量
    private boolean isSuccee;                           // 判断是否成功

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_spinner_list);

        mNameService = new NameService(SetNameActivity.this);

        addEditText = (EditText) findViewById(R.id.spinner_edittext);
        mListView = (ListView) findViewById(R.id.spinner_listView);
        mButton = (Button) findViewById(R.id.spinner_addbutton);

        upDataList(); // 更新 ListView

        mListView.setOnItemClickListener(new ListItemListener());
    }

    /**
     * 添加按钮点击事件
     * @param v
     */
    public void spOnClick(View v)
    {
        switch (v.getId())
        {
        case R.id.spinner_addbutton:
            result = addEditText.getText().toString();
            if (result == null || result.equals("") || result.length() == 0)
            {
                Toast.makeText(SetNameActivity.this, "内容不能为空！请重新输入！", 0).show();
            } else if (mButton.getText().equals("添加"))
            {
                isSuccee = mNameService.saveName(result);
                addEditText.setText("");
                Toast.makeText(SetNameActivity.this, "添加" + isSucceed(isSuccee), 0).show();
            } else if (mButton.getText().equals("重命名"))
            {
                isSuccee = mNameService.updateName(Integer.parseInt(mTextId.getText().toString()), result);
                addEditText.setText("");
                mButton.setText("添加");
                Toast.makeText(SetNameActivity.this, "重命名" + isSucceed(isSuccee), 0).show();
            }
            upDataList();
            break;
        case R.id.spinnrt_backbutton: // 返回按钮
            this.finish();
            break;
        }

    }

    /**
     * 更新 ListView
     */
    public void upDataList()
    {
        Cursor mCursor = mNameService.getCursorData();
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(SetNameActivity.this, R.layout.layout_spinner_item, mCursor, new String[] { "_id", "name" }, new int[] { R.id.spinner_item_text_id, R.id.Spinner_item_text_name }, 0);
        mListView.setAdapter(simpleCursorAdapter);
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

    public class ListItemListener implements AdapterView.OnItemClickListener
    {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            mTextId = (TextView) view.findViewById(R.id.spinner_item_text_id);
            mTextName = (TextView) view.findViewById(R.id.Spinner_item_text_name);

            new AlertDialog.Builder(SetNameActivity.this)
              .setItems(new String[] { "删除", "重命名" }, new DlgListListener())
              .show();
        }
    }

    /**
     * Dialog 中 item 的点击事件
     */
    public class DlgListListener implements DialogInterface.OnClickListener
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
            case 0:// 删除
                isSuccee = mNameService.deleteName(Integer.parseInt(mTextId.getText().toString()));
                Toast.makeText(SetNameActivity.this, "删除" + isSucceed(isSuccee), 0).show();
                upDataList();
                break;
            case 1:// 重命名
                addEditText.setText(mTextName.getText());
                mButton.setText("重命名");
                break;
            }
        }
    }


}
