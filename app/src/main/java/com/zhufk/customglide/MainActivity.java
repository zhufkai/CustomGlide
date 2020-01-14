package com.zhufk.customglide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zhufk.my_glide.library.Glide;

public class MainActivity extends AppCompatActivity {

    private final String PATH1 = "http://img5.imgtn.bdimg.com/it/u=4246510199,2069483326&fm=206&gp=0.jpg";
    private final String PATH2 = "http://img4.imgtn.bdimg.com/it/u=16705507,1328875785&fm=206&gp=0.jpg";

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);
        imageView3 = findViewById(R.id.image3);
        imageView4 = findViewById(R.id.image4);
    }

    public void test1(View view) {
//        com.bumptech.glide.Glide.with(this).load(PATH1).into(imageView1);
        Glide.with(this).load(PATH1).into(imageView1);
    }

    public void test2(View view) {
        Glide.with(this).load(PATH1).into(imageView2);
    }

    public void test3(View view) {
        Glide.with(this).load(PATH1).into(imageView3);
    }

    public void test4(View view) {
        Glide.with(this).load(PATH2).into(imageView4);
    }
}
