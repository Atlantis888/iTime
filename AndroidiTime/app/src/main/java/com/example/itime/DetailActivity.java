package com.example.itime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Formatter;

public class DetailActivity extends AppCompatActivity {

    public static final int RESULT_DELETE = 111;
    public static final int REQUEST_CODE1 = 777;
    long qq;
    String pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final TextView third=this.findViewById(R.id.third);
        final ImageView photo;
        TextView first,second;
        FloatingActionButton deletebutton,editbutton;
        photo=this.findViewById(R.id.photo);
        first=this.findViewById(R.id.first);
        second=this.findViewById(R.id.second);
        deletebutton=findViewById(R.id.deletebutton);
        editbutton=this.findViewById(R.id.editbutton);
        long mm=getIntent().getLongExtra("ms",0);
        final int position=getIntent().getIntExtra("position",0);
        final String title=getIntent().getStringExtra("title");
        final String endtime=getIntent().getStringExtra("endtime");
        String str=getIntent().getStringExtra("sttr");
        Uri uri=Uri.parse(str);
        photo.setImageURI(uri);
        first.setText(title);
        second.setText(endtime);
        String ss=stringForTime(mm);
        third.setText(ss);
        qq=mm;
        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent();
                setResult(REQUEST_CODE1, intent);
                DetailActivity.this.finish();
            }
        }) ;

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("是否删除该计时？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent();
                        intent.putExtra("position",position);
                        setResult(RESULT_DELETE, intent);
                        DetailActivity.this.finish();
                    }
                });
                builder.show();
            }
        }) ;
        @SuppressLint("HandlerLeak")
        final Handler handler = new Handler ( ) {
            public void handleMessage(Message msg) {
                //计算剩余时间
                third.setText(pp);
                super.handleMessage (msg);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Message msg = new Message ( );
                        qq=qq-1000;
                        pp=stringForTime(qq);
                        handler.sendMessage (msg);
                        Thread.sleep (1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace ( );
                }
            }
        }).start();

    }
    public static String stringForTime(long timeMs){
        long totalSeconds = timeMs/1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds/60)%60;
        long hours = totalSeconds/3600;
        return new Formatter().format("剩余时间：\n%02d小时%02d分钟%02d秒",hours,minutes,seconds).toString();
    }


}
