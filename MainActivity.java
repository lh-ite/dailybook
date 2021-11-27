package com.example.dailybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MydatabaseHelper dbHelper;
    private EditText accoutEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button saveData=(Button) findViewById(R.id.save_date);
        dbHelper=new MydatabaseHelper(this,"dailyBook.db",null,1);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                String s=accoutEdit.getText().toString();
                editor.putString("name",s);
                editor.apply();
                Toast.makeText(MainActivity.this, "Create success", Toast.LENGTH_SHORT).show();
            }
        });


        Button login=(Button) findViewById(R.id.LoginButton);
        accoutEdit= (EditText) findViewById(R.id.UserNameEdit);
        login.setOnClickListener(new View.OnClickListener() {//登陆功能监听器
            @Override
            public void onClick(View view) {//点击事件
                SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);//设置一个获取preference的内容
                String name=pref.getString("name",""); //获取已经保存的sharedpreference文件内的内容
                String account=accoutEdit.getText().toString();
                Log.d("MainActivity.this","begin");
                if(account.equals(name))//判断登录框里输入的内容是否等于存入sharedpreference内容
                {
                    Intent on=new Intent(MainActivity.this,main.class);
                    startActivity(on);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"acount not availiable",Toast.LENGTH_SHORT).show();//异常情况判断
                }
            }
        });

    }
}

