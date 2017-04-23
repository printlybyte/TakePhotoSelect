package com.laifeng.takephotoselect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 0;
    private Button mBtnTest;
    private TextView mTxtTest;
    private RecyclerView mRecyclerTest;
    private List<String> mlist = new ArrayList<>();
    private CommonAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    };

    public void Multiselect(View view) {
        mTxtTest.setText("");
        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
                .multiSelect(false)
                // 是否记住上次选中记录
                .rememberSelected(false)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5")).build();

        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    public void Single(View view) {
        mTxtTest.setText("");
        ImgSelConfig config = new ImgSelConfig.Builder(this, loader)
                // 是否多选
                .multiSelect(true)//是否记住上次所选位置
                .btnText("Confirm")
                // 确定按钮背景色
                //.btnBgColor(Color.parseColor(""))
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.mipmap.ic_launcher)
                .title("Images")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#3F51B5"))
                .allImagesText("All Images")
                .needCrop(true)
                .cropSize(1, 1, 200, 200)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(9)
                .build();

        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Back_photo(data);
        }
    }

    /*
    * 选择返回的图片做处理
    * @parems liuguodong
    * */
    private void Back_photo(Intent data) {
        List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
        for (int i = 0; i < pathList.size(); i++) {
            mlist.add(pathList.get(i));
        }
        mAdapter = new CommonAdapter<String>(this, R.layout.item_test, mlist) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, String s, final int position) {
                holder.setText(R.id.item_txt_test, s + " : " + holder.getAdapterPosition() + " , " + holder.getLayoutPosition());
                ImageView image = holder.getView(R.id.item_img_test);
                Glide.with(MainActivity.this).load(mlist.get(position)).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(image);
                holder.setOnClickListener(R.id.item_img_test, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        mRecyclerTest.setAdapter(mAdapter);
    }

/**
 * @name: liuguodong
 * @time:
 *
 *
 **/
    private void initView() {
        mBtnTest = (Button) findViewById(R.id.btn_test);
        mBtnTest.setOnClickListener(this);
        mTxtTest = (TextView) findViewById(R.id.txt_test);
        mRecyclerTest = (RecyclerView) findViewById(R.id.recycler_test);
        mRecyclerTest.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test:
                Single(v);
                break;
        }
    }
}