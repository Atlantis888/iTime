package com.example.itime.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.itime.Datee;
import com.example.itime.DetailActivity;
import com.example.itime.NewActivity;
import com.example.itime.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.itime.DetailActivity.REQUEST_CODE1;
import static com.example.itime.DetailActivity.RESULT_DELETE;
import static com.example.itime.MainActivity.REQUEST_CODE;
import static com.example.itime.NewActivity.RESULT_DONE;

public class HomeFragment extends Fragment {

    int k;
    long ms;
    View view1;
    String string;
    private ListView listView;
    private ImageView view_background;
    private FloatingActionButton buttonnn;
    public List<Datee> dates=new ArrayList<>();
    public DateAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        dates.add(new Datee("移动软甲开发与安全","已选日期：2019/12/30  时间：11：22","iTime开发",R.drawable.a2,"只剩5天"));
        adapter=new DateAdapter(root.getContext(),R.layout.date_item,dates);
        listView=root.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        view_background=root.findViewById(R.id.view_background);
        view_background.setImageResource(R.drawable.backgrou);

        //为按钮添加点击事件
        buttonnn=root.findViewById(R.id.buttonnn);
        buttonnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Intent intent = new Intent(root.getContext(), NewActivity.class);
                 intent.putExtra("title","");
                 startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //为listview添加item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                k=i;
                view1=view;
                String str1=new String(string);
                Intent intent = new Intent(root.getContext(), DetailActivity.class);
                String first=((TextView)view.findViewById(R.id.title)).getText().toString();
                intent.putExtra("position",i);
                intent.putExtra("title",((TextView)view.findViewById(R.id.title)).getText().toString());
                intent.putExtra("endtime",((TextView)view.findViewById(R.id.endtime)).getText().toString());
                intent.putExtra("sttr",str1);
                intent.putExtra("ms",ms);
                Toast.makeText(HomeFragment.this.getContext(),first,Toast.LENGTH_SHORT).show();
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        return root;
    }

    class DateAdapter extends ArrayAdapter<Datee> {

        private int resourceId;

        public DateAdapter(Context context, int resource, List<Datee> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Datee date = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.image_view)).setImageURI(date.getUri());
            ((TextView) view.findViewById(R.id.title)).setText(date.getTitle());
            ((TextView) view.findViewById(R.id.beizhu)).setText(date.getBeizhu());
            ((TextView) view.findViewById(R.id.endtime)).setText(date.getEndtime());
            ((TextView) view.findViewById(R.id.daoshu)).setText(date.getDaoshu());
            return view;
        }
    }

    //接受传递过来的数据并更新dates和通知适配器更新界面
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case REQUEST_CODE:

                //新建item
                if(resultCode==RESULT_OK)
                {
                    String title=data.getStringExtra("title");
                    String endtime=data.getStringExtra("endtime");
                    String beizhu=data.getStringExtra("beizhu");
                    String daoshu=data.getStringExtra("daoshu");
                    String miaoshu=data.getStringExtra("miaoshu");
                    ms=Long.valueOf(miaoshu);
                    String str=data.getStringExtra("uri");
                    Uri uri= Uri.parse((String) str);
                    string=getFilePathFromURI(this.getContext(),uri);
                    dates.add(new Datee(title,endtime,beizhu,daoshu,uri));
                    adapter.notifyDataSetChanged();
                }

                //删除item
                if(resultCode==RESULT_DELETE)
                {
                    int i=data.getIntExtra("position",0);
                    dates.remove(i);
                    adapter.notifyDataSetChanged();
                }

                //启动修改页面并传递数据
                if(resultCode==REQUEST_CODE1)
                {
                    String trytry=((TextView)view1.findViewById(R.id.title)).getText().toString();
                    Intent intent=new Intent(HomeFragment.this.getContext(), NewActivity.class);
                    intent.putExtra("title",trytry);
                    intent.putExtra("beizhu",((TextView)view1.findViewById(R.id.beizhu)).getText().toString());
                    intent.putExtra("endtime",((TextView)view1.findViewById(R.id.endtime)).getText().toString());
                    intent.putExtra("string",string);
                    startActivityForResult(intent, REQUEST_CODE);
                }

                //修改完成
                if(resultCode==RESULT_DONE)
                {
                    String title=data.getStringExtra("title");
                    String beizhu=data.getStringExtra("beizhu");
                    String endtime=data.getStringExtra("endtime");
                    String string=data.getStringExtra("uri");
                    Uri uri=Uri.parse(string);
                    ((TextView)view1.findViewById(R.id.beizhu)).setText(beizhu);
                    ((TextView)view1.findViewById(R.id.title)).setText(title);
                    ((TextView)view1.findViewById(R.id.endtime)).setText(endtime);
                    ((ImageView)view1.findViewById(R.id.image_view)).setImageURI(uri);
                }
                break;
        }
    }

    //将uri转化为路径
    private String getFilePathFromURI(Context context, Uri contentUri) {
       File rootDataDir = context.getFilesDir();
       String fileName = getFileName(contentUri);
       if (!TextUtils.isEmpty(fileName)) {
              File copyFile = new File(rootDataDir + File.separator + fileName + ".jpg");
              copyFile(context, contentUri, copyFile);
              return copyFile.getAbsolutePath();
       }
       return null;
    }
    private String getFileName(Uri uri) {
       if (uri == null) return null;
       String fileName = null;
       String path = uri.getPath();
       int cut = path.lastIndexOf('/');
       if (cut != -1) {
           fileName = path.substring(cut + 1);
       }
       return System.currentTimeMillis() + fileName;
    }
    private void copyFile(Context context, Uri srcUri, File dstFile) {
        try {
              InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
              if (inputStream == null) return;
              OutputStream outputStream = new FileOutputStream(dstFile);
              copyStream(inputStream, outputStream);
              inputStream.close();
              outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int copyStream(InputStream input, OutputStream output) throws Exception, IOException {
        final int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
                        while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        out.write(buffer, 0, n);
                        count += n;
                        }
                        out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {

            }
            try {
                in.close();
            } catch (IOException e) {

            }
        }
        return count;
    }
}