package com.zfj.android.dishmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.zfj.android.dishmanager.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zfj_ on 2017/6/7.
 */

/**
 * 基本的Adapter 定义了两个抽象方法myGroupView，myChildView
 * 子类需要实现
 */
public abstract class DishAdapter extends BaseExpandableListAdapter {
    Context mContext;
    LayoutInflater mInflater;
    private List<String> groups;
    private List<List<String>> childs;

    /**
     * 返回childs链表
     *
     * @return
     */
    public List<List<String>> getChilds() {
        return childs;
    }

    public DishAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void addNewData(List<String> groups, List<List<String>> childs) {
        this.groups = groups;
        this.childs = childs;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childs.get(groupPosition).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return childs.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View groupView, ViewGroup parent) {
        return myGroupView(groupPosition, groupView);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View childView, ViewGroup parent) {
        return myChildView(groupPosition, childPosition, childView);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 抽象方法getGroupView，getChildView中会调用
     */
    public abstract View myGroupView(int groupPosition, View groupView);

    public abstract View myChildView(int groupPosition, int childPosition, View childView);
}
