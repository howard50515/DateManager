package com.example.datemanager;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RemindInfoView extends RelativeLayout {
    public Context context;
    public String txtLeft, txtRight, inputHint;
    public TextView txtViewLeft, txtViewRight;
    public EditText inputNumber;

    public RemindInfoView(Context context) {
        super(context);
        this.context = context;
    }

    public RemindInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RemindInfoView);
        getValues(typedArray);
        initView();
    }

    private void getValues(TypedArray typedArray) {
        txtLeft = typedArray.getString(R.styleable.RemindInfoView_txtLeft);
        txtRight = typedArray.getString(R.styleable.RemindInfoView_txtRight);
        inputHint = typedArray.getString(R.styleable.RemindInfoView_inputHint);
        typedArray.recycle();
    }

    private void initView() {
        inflate(context, R.layout.component_remindinfoview, this);
        setGravity(Gravity.CENTER);
        txtViewLeft = findViewById(R.id.txtLeft);
        txtViewRight = findViewById(R.id.txtRight);
        inputNumber = findViewById(R.id.inputNumber);
        txtViewLeft.setText(txtLeft);
        txtViewRight.setText(txtRight);
    }

    public int getNumber(){
        String str = inputNumber.getText().toString();
        return str.equals("") ? 0 : Integer.parseInt(str);
    }

    public void setNumber(int number){
        inputNumber.setText(String.valueOf(number));
    }
}
