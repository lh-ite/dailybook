package com.example.dailybook;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class main extends AppCompatActivity {
    private List<dailybook> title_list=new ArrayList<>();//用来存放数据
    private MydatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button select=(Button) findViewById(R.id.selectfrom_database);
        Button ad = (Button) findViewById(R.id.add);
        Button fre=(Button) findViewById(R.id.fresh);
        EditText selectfrom=(EditText) findViewById(R.id.textView4);

        dbHelper = new MydatabaseHelper(this, "dailyBook.db", null, 2);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("DailyBook", null, null, null, null, null, null);//进行数据查询
        if (cursor.moveToFirst()) {//从查询到的数据第一个查起
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") String content=cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") int id=cursor.getInt(cursor.getColumnIndex("id"));
                dailybook d=new dailybook(id,title,time,content);
                title_list.add(d);//将得到的数据导入创建好的对象里
            } while (cursor.moveToNext());
        }
        cursor.close();

        Adapter_title adapter=new Adapter_title(main.this,R.layout.content,title_list);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        fre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(main.this,main.class);
                startActivity(in);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置list view的监听器
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {//list view的点击事件
                dailybook d=title_list.get(position);

                String content=d.getContent();
                String time=d.getTime();
                String title=d.getTitle();
                Integer idone=d.getId();
                String idtwo=Integer.toString(idone);
                Intent intent=new Intent(main.this,show_message.class);
                Intent intent1=new Intent(main.this,add_activity.class);
                intent.putExtra("extra_content",content);//使用intent讲数据传向show_message界面方便后面的处理
                intent.putExtra("extra_time",time);
                intent.putExtra("extra_title",title);
                intent.putExtra("extra_id",idtwo);
                startActivity(intent);
            }
        });

          ad.setOnClickListener(new View.OnClickListener() {//添加数据功能的监听器
            @Override
            public void onClick(View view) {//点击事件
                Intent in = new Intent(main.this, add_activity.class);
                startActivity(in);

            }
        });





          select.setOnClickListener(new View.OnClickListener() {//筛选功能的监听器
              @Override
              public void onClick(View view) {//点击事件
                  title_list.clear(); //将之前的array list列表清空用来装下面的查询结果
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                String message=selectfrom.getText().toString();
                Cursor cursor1=db.query("DailyBook",null,"time=?",new String[]{message},null,null,null);//按照筛选条件进行筛选
//                  Cursor cursor1=db.rawQuery("select * from DailyBook where time= ?",new String[] {message});
                  if (cursor1.moveToFirst()) {//依次获得需要获得的内容
                      do {
                          @SuppressLint("Range") Integer id=cursor1.getInt(cursor1.getColumnIndex("id"));
                          @SuppressLint("Range") String title = cursor1.getString(cursor1.getColumnIndex("title"));
                          @SuppressLint("Range") String time = cursor1.getString(cursor1.getColumnIndex("time"));
                          @SuppressLint("Range") String content=cursor1.getString(cursor1.getColumnIndex("content"));
                          Log.d("main.this","title="+title);
                          Log.d("main.this","time="+time);
                          Log.d("main.this","content="+content);
                          Log.d("main.this","id="+id);
                          dailybook d=new dailybook(id,title,time,content);
                          title_list.add(d);
                      } while (cursor1.moveToNext());
                  }
                  Adapter_title adapter=new Adapter_title(main.this,R.layout.content,title_list);
                  ListView listView = (ListView) findViewById(R.id.list_view);
                  listView.setAdapter(adapter);
                  cursor1.close();

              }
          });

    }

}