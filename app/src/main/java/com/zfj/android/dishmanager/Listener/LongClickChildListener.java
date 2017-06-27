package com.zfj.android.dishmanager.Listener;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.zfj.android.dishmanager.R;
import com.zfj.android.dishmanager.adapter.LocalDishAdapter;

import java.util.List;

/**
 * Created by zfj_ on 2017/6/9.
 */

public class LongClickChildListener implements AdapterView.OnItemLongClickListener {
    Context mContext;
    List<List<String>> childs;

    public LongClickChildListener(Context context, List<List<String>> childs) {
        mContext = context;
        this.childs = childs;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
        int childPos = (int) view.getTag(R.id.child_item_tv);
        if (childPos == -1) {
            return true;
        } else {
            final TextView tv = (TextView) view.findViewById(R.id.child_item_tv);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setMessage(mContext.getString(R.string.ask_is_del1) + tv.getText().toString() + mContext.getString(R.string.ask_is_del2))
                    .setNegativeButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteDish(tv);
                        }
                    })
                    .setPositiveButton(R.string.dialog_cancel, null);
            builder.show();
            return true;
        }

    }

    private void deleteDish(TextView tv) {
        ContentResolver resolver = mContext.getContentResolver();
        int deleteId = -1;
        Cursor c = resolver.query(Uri.parse("content://com.imooc.menuprovider"), null, null, null, null);
        while (c.moveToNext()) {
            if (tv.getText().toString().equals(c.getString(c.getColumnIndex("dish_name")))) {
                break;
            }
        }
        deleteId = c.getInt(c.getColumnIndex("dish_id"));

        int count = resolver.delete(Uri.parse("content://com.imooc.menuprovider"), null, new String[]{deleteId + ""});
        //Toast.makeText(mContext,count+"",Toast.LENGTH_SHORT).show();
        c.close();
    }
}
