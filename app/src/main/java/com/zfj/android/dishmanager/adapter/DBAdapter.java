package com.zfj.android.dishmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import com.zfj.android.dishmanager.R;

/**
 * Created by zfj_ on 2017/6/6.
 */

public class DBAdapter extends CursorTreeAdapter {
    /**
     * @param groupCursor  组的游标
     * @param context
     */
    public DBAdapter(Cursor groupCursor, Context context) {
        super(groupCursor, context);
    }

    /**
     * 获取子条目游标
     */
    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        return null;
    }

    /**
     * 创建组视图
     */
    @Override
    protected View newGroupView(Context context, Cursor groupCursor, boolean isExpanded, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.group_item, null);

        return v;
    }

    /**
     * 绑定组视图
     */
    @Override
    protected void bindGroupView(View groupView, Context context, Cursor groupCursor, boolean isExpanded) {
        TextView groupText = (TextView) groupView.findViewById(R.id.group_item_tv);
        String groupName = groupCursor.getString(groupCursor.getColumnIndex("dish_type"));
        groupText.setText(groupName);
    }

    /**
     * 创建子视图
     */
    @Override
    protected View newChildView(Context context, Cursor childCursor, boolean isLastChild, ViewGroup parent) {
        TextView childView = new TextView(context);
        return childView;
    }

    /**
     * 绑定子视图
     */
    @Override
    protected void bindChildView(View childView, Context context, Cursor childCursor, boolean isLastChild) {
        TextView childText = (TextView) childView;

    }
}
