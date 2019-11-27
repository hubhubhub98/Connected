package com.changsdev.whoaressuproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.SupportMapFragment;

public class ScrollableMapFragment extends SupportMapFragment {
    private OnTouchListener listener;
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstance) {
        View layout = super.onCreateView(layoutInflater, viewGroup, savedInstance);
        TouchableWrapper frameLayout = new TouchableWrapper(getActivity());
        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent,null));
        ((ViewGroup) layout).addView(frameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return layout;
    }

    public void setListener(OnTouchListener listener) {
        this.listener = listener;
    }

    public interface OnTouchListener {
        void onActionDown();
        void onActionUp();
    }

    public class TouchableWrapper extends FrameLayout {
        public TouchableWrapper(Context context) {
            super(context);
        }
        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            if (listener == null) {
                return super.dispatchTouchEvent(event);
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    listener.onActionDown();
                    break;
                    case MotionEvent.ACTION_UP:
                        listener.onActionUp();
                        break;
            }
            return super.dispatchTouchEvent(event);
        }
    }

}
