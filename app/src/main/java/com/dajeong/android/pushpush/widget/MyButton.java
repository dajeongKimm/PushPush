package com.dajeong.android.pushpush.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


public class MyButton extends AppCompatButton {

//    public MyButton(Context context) {  //소스코드만 쓸때
//        super(context);
//    }

    public MyButton(Context context, AttributeSet attrs) { //AttributeSet xml에서 태그로 쓸수 있게 만들어줌.(속성을 넘겨줌)

        super(context, attrs);
        setBackgroundColor(Color.DKGRAY);
        setTextColor(Color.WHITE);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) { //defStyleAttr -> 테마 지정
        super(context, attrs, defStyleAttr);
        setBackgroundColor(Color.DKGRAY);
        setTextColor(Color.WHITE);
    }
}
