package com.danisousa.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    public static final String EXTRA_TIME = "com.danisousa.crimminalintent.time";

    private TimePicker mTimePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setIs24HourView(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setMinute(calendar.get(Calendar.MINUTE));
        } else {
            mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }

        return new TimePickerDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newHour, newMinutes;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            newHour = mTimePicker.getHour();
                            newMinutes = mTimePicker.getMinute();
                        } else {
                            newHour = mTimePicker.getCurrentHour();
                            newMinutes = mTimePicker.getCurrentMinute();
                        }

                        Date date = new GregorianCalendar(year, month, day, newHour, newMinutes).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    public static TimePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
