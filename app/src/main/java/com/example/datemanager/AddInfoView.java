package com.example.datemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Locale;

public class AddInfoView extends LinearLayout {
    public Context context;
    public RelativeLayout remindView, timeRollerView;
    public RollerView rollerViewLeft, rollerViewRight;
    public CalendarView dateCalendarView;
    public RemindModeView remindModeView;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public Switch switchRemind;
    public Spinner spinnerRemindMode;
    public Button btCancel, btConfirm;
    public AnimatedTextView txtDateStart, txtDateEnd, txtTimeStart, txtTimeEnd;
    public TextView txtCurrentDate, txtCurrentTime;
    public EditText inputTitle, inputRemark;
    public MatterInfo matterInfo;
    public boolean isActivate, isDateCalendarViewActivated, isTimeRollerViewActivated;

    public AddInfoView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    @SuppressLint("ClickableViewAccessibility, SetTextI18n")
    private void init(){
        inflate(context, R.layout.window_addinfoview, this);
        switchRemind = findViewById(R.id.switchRemind);
        remindView = findViewById(R.id.aligned_view4);
        timeRollerView = findViewById(R.id.timeRollerView);
        rollerViewLeft = findViewById(R.id.rollerViewLeft);
        rollerViewRight = findViewById(R.id.rollerViewRight);
        dateCalendarView = findViewById(R.id.dateCalendarView);
        spinnerRemindMode = findViewById(R.id.spinnerRemindMode);
        remindModeView = findViewById(R.id.remindModeView);
        btCancel = findViewById(R.id.btCancel);
        btConfirm = findViewById(R.id.btConfirm);
        txtDateStart = findViewById(R.id.txtDateStart);
        txtDateEnd = findViewById(R.id.txtDateEnd);
        txtTimeStart = findViewById(R.id.txtTimeStart);
        txtTimeEnd = findViewById(R.id.txtTimeEnd);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        inputTitle = findViewById(R.id.inputTitle);
        inputRemark = findViewById(R.id.inputRemark);
        dateCalendarView.setVisibility(GONE);
        switchRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    AnimationManager.expand(remindView, 5);
                } else {
                    AnimationManager.collapse(remindView, 2);
                }
            }
        });
        spinnerRemindMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                remindModeView.selectMode(RemindMode.values()[adapterView.getSelectedItemPosition()]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        txtDateStart.setOnActionUpListener(new AnimatedTextView.OnActionUpListener() {
            @Override
            public void onActionUp() {
                if (!isDateCalendarViewActivated || txtCurrentDate == txtDateStart)
                    isDateCalendarViewActivated = !isDateCalendarViewActivated;
                enableDateView(txtDateStart.getText().toString(), isDateCalendarViewActivated);
                txtCurrentDate = txtDateStart;
            }
        });
        txtDateEnd.setOnActionUpListener(new AnimatedTextView.OnActionUpListener() {
            @Override
            public void onActionUp() {
                if (!isDateCalendarViewActivated || txtCurrentDate == txtDateEnd)
                    isDateCalendarViewActivated = !isDateCalendarViewActivated;
                enableDateView(txtDateEnd.getText().toString(), isDateCalendarViewActivated);
                txtCurrentDate = txtDateEnd;
            }
        });
        txtTimeStart.setOnActionUpListener(new AnimatedTextView.OnActionUpListener() {
            @Override
            public void onActionUp() {
                if (!isTimeRollerViewActivated || txtCurrentTime == txtTimeStart)
                    isTimeRollerViewActivated = !isTimeRollerViewActivated;
                String[] temp = txtTimeStart.getText().toString().split(":");
                enableTimeView(temp[0], temp[1], isTimeRollerViewActivated);
                txtCurrentTime = txtTimeStart;
            }
        });
        txtTimeEnd.setOnActionUpListener(new AnimatedTextView.OnActionUpListener() {
            @Override
            public void onActionUp() {
                if (!isTimeRollerViewActivated || txtCurrentTime == txtTimeEnd)
                    isTimeRollerViewActivated = !isTimeRollerViewActivated;
                String[] temp = txtTimeEnd.getText().toString().split(":");
                enableTimeView(temp[0], temp[1], isTimeRollerViewActivated);
                txtCurrentTime = txtTimeEnd;
            }
        });
        dateCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String month = (i1 + 1) < 10 ? "0" + (i1 + 1) : String.valueOf(i1 + 1);
                String day = i2 < 10 ? "0" + i2 : String.valueOf(i2);
                String date = String.format(Locale.TAIWAN, "%d/%s/%s", i, month, day);
                txtCurrentDate.setText(date);
                checkDate(txtCurrentDate);
            }
        });
        RollerView.OnTextChangedListener textChangedListener = () -> {
            if (txtCurrentTime != null) {
                txtCurrentTime.setText(rollerViewLeft.getCurrentText() + ":" + rollerViewRight.getCurrentText());
                checkTime(txtCurrentTime);
            }
        };
        rollerViewLeft.setOnTextChangedListener(textChangedListener);
        rollerViewRight.setOnTextChangedListener(textChangedListener);
        btCancel.setOnTouchListener(new OnTouchListener() {
            private boolean isOutside;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        isOutside = false;
                        btCancel.setBackgroundResource(R.drawable.background_rectangle_focus);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isOutside = motionEvent.getX() > btCancel.getWidth() || motionEvent.getX() < 0 ||
                                motionEvent.getY() > btCancel.getHeight() || motionEvent.getY() < 0;
                        break;
                    case MotionEvent.ACTION_UP:
                        btCancel.setBackgroundResource(R.drawable.background_rectangle_unfocus);
                        if (!isOutside)
                            closeView();
                        break;
                }

                return true;
            }
        });
        btConfirm.setOnTouchListener(new OnTouchListener() {
            private boolean isOutside;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        isOutside = false;
                        btConfirm.setBackgroundResource(R.drawable.background_rectangle_focus);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isOutside = motionEvent.getX() > btConfirm.getWidth() || motionEvent.getX() < 0 ||
                                motionEvent.getY() > btConfirm.getHeight() || motionEvent.getY() < 0;
                        break;
                    case MotionEvent.ACTION_UP:
                        btConfirm.setBackgroundResource(R.drawable.background_rectangle_unfocus);
                        if (!isOutside) {
                            addData();
                            closeView();
                        }
                        break;
                }

                return true;
            }
        });
    }

    private void checkDate(TextView currentTextView){
        if (currentTextView == txtDateStart &&
                !DataManager.dateStartBefore(txtDateStart.getText().toString(), txtDateEnd.getText().toString())){
            txtDateEnd.setText(txtDateStart.getText());
        } else if (currentTextView == txtDateEnd &&
                DataManager.dateEndAfter(txtDateStart.getText().toString(), txtDateEnd.getText().toString())){
            txtDateStart.setText(txtDateEnd.getText());
        }
    }

    private void checkTime(TextView currentTextView){
        if (currentTextView == txtTimeStart &&
                !DataManager.timeStartBefore(txtTimeStart.getText().toString(), txtTimeEnd.getText().toString())){
            txtTimeEnd.setText(txtTimeStart.getText());
        } else if (currentTextView == txtTimeEnd &&
                DataManager.timeEndAfter(txtTimeStart.getText().toString(), txtTimeEnd.getText().toString())){
            txtTimeStart.setText(txtTimeEnd.getText());
        }
    }

    private void enableDateView(String date, boolean isEnable){
        dateCalendarView.setDate(new DateTime(date, "00:00").getTimeInMillis());
        timeRollerView.setVisibility(GONE);
        dateCalendarView.setVisibility(VISIBLE);
    }

    private void enableTimeView(String hour, String minute, boolean isEnable){
        setRollerIndex(hour, minute);
        dateCalendarView.setVisibility(GONE);
        if (isEnable)
            AnimationManager.expand(timeRollerView, 4);
        else
            AnimationManager.collapse(timeRollerView, 2);
    }

    private void setRollerIndex(String left, String right){
        rollerViewLeft.setIndex(left);
        rollerViewRight.setIndex(right);
    }

    private void addData(){
        String dateStart = txtDateStart.getText().toString();
        String dateEnd = txtDateEnd.getText().toString();
        DateTime dateTimeStart = new DateTime(dateStart, txtTimeStart.getText().toString());
        DateTime dateTimeEnd = new DateTime(dateEnd, txtTimeEnd.getText().toString());
        RemindInfo remindInfo = switchRemind.isChecked() ? remindModeView.getRemindInfo() : new RemindInfo(-1, -1);
        String title = inputTitle.getText().toString().equals("") ? "我的活動" :  inputTitle.getText().toString();
        if (matterInfo == null)
            DataManager.getInstance().addData(new MatterInfo(dateTimeStart, dateTimeEnd, title, inputRemark.getText().toString(), remindInfo), true);
        else {
            matterInfo.setInfo(dateTimeStart, dateTimeEnd, title, inputRemark.getText().toString(), remindInfo);
            DataManager.getInstance().resetReminder(matterInfo);
        }
    }

    public void showView(Activity activity, String date){
        if (isActivate) closeView();

        setView(date);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        activity.addContentView(this, params);
        isActivate = true;
    }

    public void showView(Activity activity, MatterInfo matterInfo){
        if (isActivate) closeView();

        setView(matterInfo);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        activity.addContentView(this, params);
        isActivate = true;
    }

    private void setView(String date){
        txtDateStart.setText(date);
        txtDateEnd.setText(date);
    }

    private void setView(MatterInfo matterInfo){
        this.matterInfo = matterInfo;
        inputTitle.setText(matterInfo.topic);
        txtDateStart.setText(matterInfo.getDateStart());
        txtDateEnd.setText(matterInfo.getDateEnd());
        txtTimeStart.setText(matterInfo.getTimeStart());
        txtTimeEnd.setText(matterInfo.getTimeEnd());
        RemindInfo remindInfo = matterInfo.remindInfo;
        if (matterInfo.remindInfo.firstRemind == -1)
            switchRemind.setChecked(false);
        else{
            spinnerRemindMode.setSelection(remindInfo.mode.ordinal(), false);
            remindModeView.selectMode(remindInfo.mode);
            switch (remindInfo.mode){
                case GENERAL:
                    remindModeView.generalInfoView.setNumber(remindInfo.firstRemind);
                    break;
                case REPEAT:
                    remindModeView.repeatInfoView1.setNumber(remindInfo.firstRemind);
                    remindModeView.repeatInfoView2.setNumber(remindInfo.remindInterval);
                    break;
            }
        }
        inputRemark.setText(matterInfo.remark);
    }

    public void closeView(){
        if (!isActivate) return;

        resetView();
        ((ViewGroup) getParent()).removeView(this);
        isActivate = false;
    }

    @SuppressLint("SetTextI18n")
    private void resetView(){
        this.matterInfo = null;
        inputTitle.setText("");
        txtTimeStart.setText("08:00");
        txtTimeEnd.setText("09:00");
        dateCalendarView.setVisibility(GONE);
        timeRollerView.setVisibility(GONE);
        switchRemind.setChecked(true);
        spinnerRemindMode.setSelection(0, false);
        remindModeView.resetView();
        inputRemark.setText("");
    }

}
