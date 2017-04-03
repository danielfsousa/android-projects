package com.danisousa.draganddraw;

import android.graphics.Point;
import android.graphics.PointF;

import java.io.Serializable;

public class Box implements Serializable {

    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }
}
