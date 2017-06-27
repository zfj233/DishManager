package com.zfj.android.dishmanager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.zfj.android.dishmanager.MainActivity.CONTENT_COM_IMOOC_MENUPROVIDER;
import static com.zfj.android.dishmanager.MainActivity.DISH_NAME;
import static com.zfj.android.dishmanager.MainActivity.DISH_TYPE;

/**
 * Created by zfj_ on 2017/6/9.
 */

public class AddDishUI extends Activity {
    private Spinner addTypeSp;
    private EditText addNameEdt;
    private Button saveBtn;
    private TextView backToMain;
    private String getSpinnerName;
    private List<String> spData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_layout);
        initView();
        loadLocalData();
        bindEvents();
    }

    private void loadLocalData() {
        Intent intent = getIntent();
        spData = intent.getStringArrayListExtra("groupList");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                spData);
        adapter.setDropDownViewResource(android.
                R.layout.simple_spinner_dropdown_item);
        addTypeSp.setAdapter(adapter);
    }

    private void bindEvents() {
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddDishUI.this, MainActivity.class));
            }
        });
        addTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSpinnerName = spData.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addNameEdt.getText().toString().equals("")) {
                    Toast.makeText(AddDishUI.this, R.string.plz_enter_dish_name, Toast.LENGTH_SHORT).show();
                } else {
                    addDishToProvider();
                }
            }
        });
    }

    private void addDishToProvider() {
        String enterName = addNameEdt.getText().toString();
        ContentValues dishValues = new ContentValues();
        dishValues.put(DISH_TYPE, getSpinnerName);
        dishValues.put(DISH_NAME, enterName);
        ContentResolver resolver = getContentResolver();
        resolver.insert(Uri.parse(CONTENT_COM_IMOOC_MENUPROVIDER), dishValues);
        Toast.makeText(AddDishUI.this, getString(R.string.add_success), Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        addTypeSp = (Spinner) findViewById(R.id.add_spinner);
        addNameEdt = (EditText) findViewById(R.id.add_dish_name);
        saveBtn = (Button) findViewById(R.id.save_btn);
        backToMain = (TextView) findViewById(R.id.back_to_main);
    }
}
