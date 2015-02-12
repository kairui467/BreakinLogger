package com.example.BreakinLogger.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.BreakinLogger.R;
import com.example.BreakinLogger.domain.Person;
import com.example.BreakinLogger.service.NameService;
import com.example.BreakinLogger.service.PersonService;

public class AddDataActivity extends Activity
{
    //*************************声明类*********************************//
    private PersonService mPersonService;       // 练习记录表的增删改查类
    private NameService mNameService;           // 动作名表的增删改查类
    private Intent mIntent;
    private Person person;

    //*******************声明 SharedPreferences ************************//
    private SharedPreferences sp;               // 声明 SharedPreferences 偏好
    private SharedPreferences.Editor editor;    // 声明 SharedPreferences.Editor 偏好编辑

    //*************************声明控件*********************************//
    private DatePicker mDatePicker;             // 日期控件
    private TimePicker mTimePicker;             // 时间控件
    private Spinner mSpinner;                   // 下拉列表
    private EditText mEditText;                 // 记录练习次数的输入框
    private Button cutButton;                   // 减小按钮
    private Button setButton;                   // 设置递增大小的按钮
    private EditText setDlgEditText;            // Dialog 里面的 EditText

    //*************************声明变量*********************************//
    private String type = null;                 // 用来判断是更新还是添加
    private int pm_id;                          // 把 id 存储为全局变量
    private boolean isSuccee;                   // 判断是否成功
    private int setNumber;                      // 每次增加的个数，默认为5个

    //*************************定义常量*********************************//
    private final String UPDATE = "update";
    private final String SAVE = "save";


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_data);
    }


    /**
     * Activity 完毕后调用
     */
    protected void onResume()
    {
        init();
        super.onResume();
    }

    /**
     * 初始化
     */
    void init()
    {
        mPersonService = new PersonService(AddDataActivity.this);
        mNameService = new NameService(AddDataActivity.this);

        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        mSpinner = (Spinner) findViewById(R.id.spinner_type);
        mEditText = (EditText) findViewById(R.id.editText_number);
        cutButton = (Button) findViewById(R.id.button_cut_number);
        setButton = (Button) findViewById(R.id.button_setting_addnumber);

        // 初始化 SharedPreferences
        initSP();
        // 初始化控件设置
        initWidgetSet();

        // 接收 Intent 发送过来的值
        mIntent = getIntent();
        type = mIntent.getStringExtra("type");

        // 如果是更新数据则调用 addWidgetValue()
        if (type.equals(UPDATE))
            addWidgetValue();
    }

    /*********************************************************************************************/
    /*****************************************方法区**********************************************/
    /*********************************************************************************************/


    /**
     * 接收和添加 SharedPreferences 值
     */
    public void initSP()
    {
        // 获取 SharedPreferences 对象
        sp = AddDataActivity.this.getSharedPreferences("my_sp", MODE_PRIVATE);

        // 如果 SharedPreferences 没有存在数据则添加默认值 10 进去
        if (sp.getInt("number", 0) == 0)
            addNumberSp(10);
        else
            setNumber = sp.getInt("number", 0);
    }

    /**
     * 默认设置
     */
    public void initWidgetSet()
    {
        // 初始化 Spinner
        mSpinner.setAdapter(new ArrayAdapter<String>(AddDataActivity.this, android.R.layout.simple_spinner_dropdown_item, mNameService.getListData()));

        //设置为24小时制
        mTimePicker.setIs24HourView(true);
        //设置减小按钮为不可点击， 因为第一次数据为0
        cutButton.setEnabled(false);

        //隐藏右侧日历
        if (mDatePicker != null)
            ((ViewGroup) mDatePicker.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);

        // 设置递增大小按钮的监听事件
        setButton.setOnClickListener(new SetButtonListener());

        // 当 EditText 内容发生改变时调用
        mEditText.addTextChangedListener(new editTextChange());
        // 设置 EditText 为不可编辑
        mEditText.setEnabled(false);
    }

    /**
     * 把接收到的值填充到控件中
     */
    public void addWidgetValue()
    {
        pm_id = mIntent.getIntExtra("pm_id", 0);
        String pm_time = mIntent.getStringExtra("pm_time");
        String pm_name = mIntent.getStringExtra("pm_name");
        String pm_number = mIntent.getStringExtra("pm_number");

        // 截取日期设置到 mDatePicker
        mDatePicker.updateDate(Integer.parseInt(pm_time.substring(0, 4)), Integer.parseInt(pm_time.substring(5, 7)) - 1, Integer.parseInt(pm_time.substring(8, 10)));

        // 截取时间设置到 mTimePicker
        mTimePicker.setCurrentHour(Integer.parseInt(pm_time.substring(11, 13)));
        mTimePicker.setCurrentMinute(Integer.parseInt(pm_time.substring(14, 16)));

        // 遍历数组填充动作名到 mSpinner
        int sum = 0;
        for (String s : mNameService.getListData())
        {
            if (s.equals(pm_name))
            {
                pm_name = String.valueOf(sum);
                break;
            }
            sum++;
        }
        mSpinner.setSelection(Integer.parseInt(pm_name), true);

        // 填充练习次数到 mEditText
        mEditText.setText(pm_number);
    }

    /**
     * 在小于 10 的数字前面加 0
     * @param time
     * @return
     */
    public String isLessTen(int time)
    {
        return ((time < 10) ? 0 + String.valueOf(time) : time) + "";
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
     * 更改 number
     * @param number
     */
    void addNumberSp(int number)
    {
        editor = sp.edit();
        editor.putInt("number", number);
        editor.commit();
        // 重新赋值给 setNumber
        setNumber = sp.getInt("number", 0);
    }

    /**
     * 所有 Button 触发事件
     * @param v
     */
    public void buttonOnClick(View v)
    {
        int _getNumber = Integer.parseInt(mEditText.getText().toString());
        switch (v.getId())
        {
        case R.id.spinner_setButton:
            Intent intent = new Intent(AddDataActivity.this, SetNameActivity.class);
            startActivity(intent);
            break;
        case R.id.button_add_nubmer:
            mEditText.setText("" + (_getNumber + setNumber));
            break;
        case R.id.button_cut_number:
            mEditText.setText("" + (_getNumber - setNumber));
            break;
        case R.id.button_ok:// 保存 Button
            // 得到时间字符串
            String strDate = mDatePicker.getYear() + "-" + isLessTen(mDatePicker.getMonth() + 1) + "-" + isLessTen(mDatePicker.getDayOfMonth()) + " "
              + isLessTen(mTimePicker.getCurrentHour()) + ":" + isLessTen(mTimePicker.getCurrentMinute());

            // 判断是保存还是修改
            if (type.equals(SAVE))
            {
                // 添加该条数据到 Person
                person = new Person(strDate, mSpinner.getSelectedItem().toString(), _getNumber);
                // 保存 Person 数据到数据库
                isSuccee = mPersonService.save(person);
                Toast.makeText(AddDataActivity.this, "添加" + isSucceed(isSuccee), 0).show();

            } else if (type.equals(UPDATE))
            {
                // 添加该条数据到 Person
                person = new Person(strDate, mSpinner.getSelectedItem().toString(), _getNumber, pm_id);
                isSuccee = mPersonService.update(person);
                Toast.makeText(AddDataActivity.this, "更新" + isSucceed(isSuccee), 0).show();
            }
            closeActivity();
            break;
        case R.id.button_back: // 返回 Button
            closeActivity();
            break;
        }
    }

    /**
     * 返回主 Activity 方法
     */
    public void closeActivity()
    {
        AddDataActivity.this.finish();
    }

    /*********************************************************************************************/
    /*****************************************接口继承区******************************************/
    /*********************************************************************************************/


    /**
     * EditText 内容发生改变时
     */
    class editTextChange implements TextWatcher
    {
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            //如果次数会小于 0 则减小按钮不可点击
            if ((Integer.parseInt(s.toString()) - setNumber) < 0)
                cutButton.setEnabled(false);
            else
                cutButton.setEnabled(true);
        }

        public void afterTextChanged(Editable s)
        {
        }
    }

    /**
     * 弹出设置数目的 Dialog 监听事件
     */
    class SetButtonListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            // 把自定义视图添加到 Dialog 中
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.layout_set_dlg, (ViewGroup) findViewById(R.id.dialog));

            setDlgEditText = (EditText) layout.findViewById(R.id.set_dlg_editText);
            setDlgEditText.setText("" + setNumber);

            AlertDialog.Builder builder = new AlertDialog.Builder(AddDataActivity.this);
            builder.setTitle("设置");
            builder.setView(layout);
            builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    String result = setDlgEditText.getText().toString();
                    if (result == null || result.equals("") || result.length() == 0)
                        Toast.makeText(AddDataActivity.this, "输入错误！", 0).show();
                    else
                        addNumberSp(Integer.parseInt(setDlgEditText.getText().toString()));
                }
            });
            builder.setNegativeButton(R.string.Cancel, null);
            builder.show();
        }
    }
}