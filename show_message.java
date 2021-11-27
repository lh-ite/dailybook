package com.example.dailybook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class show_message extends AppCompatActivity {
    private MydatabaseHelper dbHelper;
    public static final int CHOOSE_PHOTO=2;
    public static final int TAKE_PHOTO=1;
    private ImageView picture;
    private Uri imageuri;
    private  String id;
    private String address;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String get_title,get_time,get_content;
        dbHelper=new MydatabaseHelper(show_message.this,"dailyBook.db",null,2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_message);
        Intent intent=getIntent();
        String content=intent.getStringExtra("extra_content");//获取上一个界面传来的数据
        String title=intent.getStringExtra("extra_title");
        String time=intent.getStringExtra("extra_time");
        id=intent.getStringExtra("extra_id");
        Button re=(Button)findViewById(R.id.returnback);



        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor a=db.query("DailyBook",null,"id=?",new String[] {id},null,null,null,null);//获取点击后那个数据对应的内容
        if (a.moveToFirst()) {
            do {
                address=a.getString(a.getColumnIndex("photeid"));
            } while (a.moveToNext());
        }
        a.close();

//        Integer id1=Integer.valueOf(id);
        EditText show_title=(EditText) findViewById(R.id.edit_title);
        EditText show_content=(EditText) findViewById(R.id.edit_content);
        EditText show_time=(EditText) findViewById(R.id.edit_time);
        Button addpic=(Button)findViewById(R.id.add_pic);
        picture=(ImageView)findViewById(R.id.img1) ;

        show_time.setText(time);
        show_content.setText(content);
        show_title.setText(title);
        displayImage(address);

        Button change=(Button) findViewById(R.id.changeto_database);
        Button delete=(Button) findViewById(R.id.delete_database);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//修改数据库内容的点击事件
                String get_title1=show_title.getText().toString();
                String get_time1=show_time.getText().toString();
                String get_content1=show_content.getText().toString();
                Log.d("show_message.this","get_title1="+get_title1);
                Log.d("show_message.this","get_content2="+get_time1);
                Log.d("show_message.this","get_time3="+get_content1);
                SQLiteDatabase db=dbHelper.getWritableDatabase();

                ContentValues values=new ContentValues();
                values.put("content",get_content1);
                values.put("title",get_title1);
                values.put("time",get_time1);
                Log.d("show_message.this","get_title="+get_title1);
                Log.d("show_message.this","get_content="+get_content1);
                Log.d("show_message.this","get_time="+get_time1);
                db.update("DailyBook",values,"id=?",new String[] {id});
               // db.execSQL("UPDATE DailyBook SET content=?,time=?,title=?",new String[]{get_content1,get_time1,get_title1});
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.delete("DailyBook","id=?",new String []{id});
                Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }
        });

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(show_message.this,main.class);
                startActivity(in);
            }
        });

        addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(show_message.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)//申请访问SD卡权限
                {
                    ActivityCompat.requestPermissions(show_message.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else
                {
                    Log.d("show_message.this","begin");
                    openAlbum();//open Album方法
                }
//                    File outpuImage=new File(getExternalCacheDir(),"output_image.jpg");
//                    try{
//                        if(outpuImage.exists()){
//                            outpuImage.delete();
//                        }
//                        outpuImage.createNewFile();
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                    if(Build.VERSION.SDK_INT>=24){
//                        imageuri= FileProvider.getUriForFile(show_message.this,"com.example.dailybook.fileprovider",outpuImage);
//                    }
//                    else
//                    {
//                        imageuri=Uri.fromFile(outpuImage);
//                    }
//                    Intent intent2=new Intent("android.media.action.IMAGE_CAPTURE");
//                    intent2.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
//                    startActivityForResult(intent2,TAKE_PHOTO);
            }
        });
    }
//    @Override
//    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case TAKE_PHOTO:
//                if (requestCode == RESULT_OK) {
//                    try {
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageuri));
//                        picture.setImageBitmap(bitmap);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }
//}
    private void openAlbum()
    {
        Intent inten=new Intent("android.intent.action.GET_CONTENT");
        inten.setType("image/*");
        Log.d("show_message.this","start");
        startActivityForResult(inten,CHOOSE_PHOTO);//给第二个参数传入CHoose,选择完图片回到start。。方法
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)//获取权限
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
    private void handleImageOnKitKat(Intent data) //获得到图片地址并且传给展示图片函数（安卓4.4以上的版本）
    {
        String imagePath=null;
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
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("photeid",imagePath);
        db.update("DailyBook",values,"id=?",new String[] {id});
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
