package com.zfj.android.dishmanager.adapter;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zfj.android.dishmanager.R;

import java.util.List;

import static com.zfj.android.dishmanager.MainActivity.CONTENT_COM_IMOOC_MENUPROVIDER;
import static com.zfj.android.dishmanager.MainActivity.DISH_ID;
import static com.zfj.android.dishmanager.MainActivity.DISH_NAME;

/**
 * Created by zfj_ on 2017/6/8.
 */

/**
 * 本地数据适配器，继承了基本适配器，同时实现了OnItemLongClickListener接口
 */
public class LocalDishAdapter extends DishAdapter implements AdapterView.OnItemLongClickListener {


    public LocalDishAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 重写的抽象方法，加载Group视图
     *
     * @param groupPosition
     * @param groupView
     * @return
     */
    @Override
    public View myGroupView(int groupPosition, View groupView) {
        View v = mInflater.inflate(R.layout.group_item, null);
        TextView txtType = (TextView) v.findViewById(R.id.group_item_tv);
        txtType.setText(getGroup(groupPosition));
        txtType.setPadding(30, 0, 0, 0);
        //这里setTag是为了在OnItemLongClickListener的重写方法中接收这两个数据
        //用来反映长按父视图还是子视图，这里第一个参数只能用id资源。
        //child_item_tv,这个值返回-1，表示长按父视图
        v.setTag(R.id.group_item_tv, groupPosition);
        v.setTag(R.id.child_item_tv, -1);
        return v;
    }

    @Override
    public View myChildView(int groupPosition, int childPosition, View childView) {
        View v = mInflater.inflate(R.layout.child_item, null);
        TextView txtFood = (TextView) v.findViewById(R.id.child_item_tv);
        txtFood.setText(getChild(groupPosition, childPosition));
        //同样setTag
        v.setTag(R.id.group_item_tv, groupPosition);
        v.setTag(R.id.child_item_tv, childPosition);
        return v;
    }

    /**
     * 实现长按处理事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //获取到setTag中的值
        final int childPos = (int) view.getTag(R.id.child_item_tv);
        final int groupPos = (int) view.getTag(R.id.group_item_tv);
        if (childPos == -1) {
            //长按了父视图
            //如果不判断或是不setTag，长按父视图会导致程序崩溃
            return true;
        } else {
            //得到长按的子视图的TextView
            final TextView tv = (TextView) view.findViewById(R.id.child_item_tv);
            //建立一个对话框，这里将NegativeButton和PositiveButton分别设置为“确定”和“取消”
            //是因为一般确定按钮一般显示在左边，取消一般在右边
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setMessage(mContext.getString(R.string.ask_is_del1) + tv.getText().toString() + mContext.getString(R.string.ask_is_del2))
                    .setNegativeButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //单击确定按钮时，删除数据
                            deleteDish(tv, groupPos, childPos);
                        }
                    })
                    .setPositiveButton(R.string.dialog_cancel, null);
            builder.show();
            return true;
        }

    }

    /**
     * 删除数据（将ContentProvider和childs链表的数据都删除掉）
     * childs数据删除是因为对话框并不能重新刷新视图，这样删除的
     * 数据还会显示，所以删除childs中的数据可以通过notifyDataSetChanged()
     * 刷新视图
     *
     * @param tv
     * @param groupPosition
     * @param childPosition
     */
    private void deleteDish(TextView tv, int groupPosition, int childPosition) {
        ContentResolver resolver = mContext.getContentResolver();
        int deleteId = -1;
        Cursor c = resolver.query(Uri.parse(CONTENT_COM_IMOOC_MENUPROVIDER), null, null, null, null);
        while (c.moveToNext()) {
            //通过DISH_NAME字段得到要删除的id
            if (tv.getText().toString().equals(c.getString(c.getColumnIndex(DISH_NAME)))) {
                break;
            }
        }
        deleteId = c.getInt(c.getColumnIndex(DISH_ID));
        //删除ContentProvider中数据
        int count = resolver.delete(Uri.parse(CONTENT_COM_IMOOC_MENUPROVIDER), null, new String[]{deleteId + ""});
        //删除掉childs中的数据
        this.getChilds().get(groupPosition).remove(childPosition);
        //刷新视图
        this.notifyDataSetChanged();
        c.close();
    }

}
