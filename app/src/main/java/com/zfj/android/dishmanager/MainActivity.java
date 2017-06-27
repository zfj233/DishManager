package com.zfj.android.dishmanager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.zfj.android.dishmanager.Listener.LongClickChildListener;
import com.zfj.android.dishmanager.adapter.DBAdapter;
import com.zfj.android.dishmanager.adapter.LocalDishAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    public static final String DISH_NAME = "dish_name";
    public static final String DISH_TYPE = "dish_type";
    public static final String DISH_ID = "dish_id";
    public static final String CONTENT_COM_IMOOC_MENUPROVIDER = "content://com.imooc.menuprovider";


    ContentResolver resolver;
    private ExpandableListView dishList;
    private List<String> groups;
    private List<List<String>> childs;
    private TextView addDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadDishData();
        bindEvents();
    }

        /**
         * 添加事件
         */

    private void bindEvents() {
        LocalDishAdapter localDishAdapter = new LocalDishAdapter(this);
        //添加数据
        localDishAdapter.addNewData(groups, childs);
        dishList.setAdapter(localDishAdapter);
        //LocalDishAdapter也实现了OnItemLongClickListener接口
        dishList.setOnItemLongClickListener(localDishAdapter);

        addDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddDishUI.class);
                intent.putStringArrayListExtra("groupList", (ArrayList<String>) groups);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        dishList = (ExpandableListView) findViewById(R.id.elv_dish);
        addDish = (TextView) findViewById(R.id.add_new_dish);
    }

    /**
     * 从ContentProvider中获取表数据，
     * 建立链表对应为Group和Child的List
     */
    private void loadDishData() {
        resolver = getContentResolver();
        Cursor c = resolver.query(Uri.parse(CONTENT_COM_IMOOC_MENUPROVIDER), null, null, null, null);
        String temp = "";
        groups = new ArrayList<>();
        childs = new ArrayList<>();
        List<String> item = new ArrayList<>();
        while (c.moveToNext()) {
            if (temp.equals("")) {
                //第一行运行的操作
                temp = c.getString(c.getColumnIndex(DISH_TYPE));
                groups.add(c.getString(c.getColumnIndex(DISH_TYPE)));
                item.add(c.getString(c.getColumnIndex(DISH_NAME)));

            } else if (!c.getString(c.getColumnIndex(DISH_TYPE)).equals(temp)) {
                //如果下一行类型不同，则childs将item添加进来
                childs.add(item);
                item = new ArrayList<>();
                item.add(c.getString(c.getColumnIndex(DISH_NAME)));
                temp = c.getString(c.getColumnIndex(DISH_TYPE));
                groups.add(c.getString(c.getColumnIndex(DISH_TYPE)));
            } else if (c.getString(c.getColumnIndex(DISH_TYPE)).equals(temp)) {
                //类型和上一行相同，item直接添加
                item.add(c.getString(c.getColumnIndex(DISH_NAME)));
            }

        }
        //将最后一组类型添加到childs中
        childs.add(item);
        c.close();
    }
}
