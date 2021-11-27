package com.example.dailybook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class add_activity extends AppCompatActivity {
    private MydatabaseHelper dbHelper;
    public static final int CHOOSE_PHOTO=2;
    public static final int TAKE_PHOTO=1;
    private ImageView picture;
    private  String id;
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_active);

        EditText Title= (EditText) findViewById(R.id.edit_title);
        EditText Writer=(EditText) findViewById(R.id.edit_writer);
        EditText Time=(EditText) findViewById(R.id.edit_time);
        EditText Content=(EditText) findViewById(R.id.edit_content);
        Button addtodatabase=(Button) findViewById(R.id.addto_database);
        Button addp=(Button) findViewById(R.id.addto_pic);
        Button ret=(Button)findViewById(R.id.returnback1);
        picture=findViewById(R.id.edit_pic);

        dbHelper=new MydatabaseHelper(this,"dailyBook.db",null,2);
        addtodatabase.setOnClickListener(new View.OnClickListener() {//添加内容的监听器
            @Override
            public void onClick(View view) {//设置点击事件
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                String get_title=Title.getText().toString();//获取编辑框内需要添加的数据
                String get_time=Time.getText().toString();
                String get_content=Content.getText().toString();
                String get_writer=Writer.getText().toString();
                values.put("writer",get_writer);//讲需要向数据库内赋的内容放入value
                values.put("content",get_content);
                values.put("time",get_time);
                values.put("title",get_title);
                values.put("photeid",imagePath);
                db.insert("DailyBook",null,values);//执行insert语句
                values.clear();
                Toast.makeText(add_activity.this, "add success", Toast.LENGTH_SHORT).show();
                Intent in=new Intent(add_activity.this,main.class);
                startActivity(in);
                Log.d("add_activity","get_title="+get_title);
            }
        });

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(add_activity.this,main.class);
                startActivity(in);
            }
        });
        addp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(add_activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(add_activity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else
                {
                    Log.d("show_message.this","begin");
                    openAlbum();
                }
            }
        });
    }
    private void openAlbum()
    {
        Intent inten=new Intent("android.intent.action.GET_CONTENT");
        inten.setType("image/*");
        Log.d("add_activity.this","start");
        startActivityForResult(inten,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)
    {
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    openAlbum();
                }
                else
                {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("show_message.this", "are you sure");
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (requestCode != RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) //获得到图片地址并且传给展示图片函数
    {
        imagePath=null;
        Uri uri=data.getData();
        Log.d("show_message.this", "get the imagepath");
        if(DocumentsContract.isDocumentUri(this,uri))
        {
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority()))
            {
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
                Log.d("show_message.this", "imagePath1="+imagePath);
            }
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority()))
            {
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
                Log.d("show_message.this", "imagePath2");
            }
        }
        else if("content".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath=getImagePath(uri,null);
            Log.d("show_message.this", "imagePath3");
        }
        else if("file".equalsIgnoreCase(uri.getScheme()))
        {
            imagePath=uri.getPath();
        }
        Log.d("show_message.this","begin display imageaaaa");
//        SQLiteDatabase db=dbHelper.getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put("photeid",imagePath);
//        db.update("DailyBook",values,"content=?",new String[] {});
        displayImage(imagePath);

    }

    private  void handleImageBeforeKitKat(Intent data)//获得到图片地址并且传给展示图片函数
    {
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        Log.d("show_message.this","imagePath4");
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("photeid",imagePath);
        db.update("DailyBook",values,"id=?",new String[] {id});
        displayImage(imagePath);
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) //获取图片路径的函数
    {
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }

        return path;
    }

    private void displayImage(String imagePath) //将先前获得到的地址传入，展示图片
    {
        if(imagePath!=null)
        {
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);

        }
        else
        {
            Toast.makeText(this, "faild to get image", Toast.LENGTH_SHORT).show();
        }
    }
}
