package com.example.playground;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout indicator;

    private List<String> images;
    SliderAdapter adapter;
    private String TAG = "_Main";
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        images = new ArrayList<>();
        images.add("https://wowslider.com/sliders/demo-77/data1/images/field175959_1920.jpg");
        images.add("https://soliloquywp.com/wp-content/uploads/2016/08/11-Website-Slider-Best-Practices-That-You-Must-Follow.png");
        images.add("https://soliloquywp.com/wp-content/uploads/2013/05/action-backlit-beach-1046896-1200x450_c.jpg");
        adapter = new SliderAdapter(this, images);
        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        viewPager.setAdapter(adapter);
        indicator.setupWithViewPager(viewPager, true);
        indicator.getTabAt(0).view.setVisibility(View.GONE);
        indicator.getTabAt(images.size() + 1).view.setVisibility(View.GONE);
        viewPager.setCurrentItem(1);
        setScrollSpeed(1500);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int jumpPosition = -1;

            @Override
            public void onPageScrolled(int position,
                                       float positionOffset,
                                       int positionOffsetPixels) {
                // We do nothing here.
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    // prepare to jump to the last page
                    jumpPosition = adapter.getRealCount();

                    //TODO: indicator.setActive(adapter.getRealCount() - 1)
                } else if (position == adapter.getRealCount() + 1) {
                    //prepare to jump to the first page
                    jumpPosition = 1;

                    //TODO: indicator.setActive(0)
                } else {
                    //TODO: indicator.setActive(position - 1)
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Let's wait for the animation to complete then do the jump.
                if (jumpPosition >= 0
                        && state == ViewPager.SCROLL_STATE_IDLE) {
                    // Jump without animation so the user is not
                    // aware what happened.
                    viewPager.setCurrentItem(jumpPosition, false);
                    Log.d(TAG, "listener: set" + jumpPosition);
                    // Reset jump position.
                    jumpPosition = -1;
                }
            }
        });
    }

    private void setScrollSpeed(int duration) {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
            scroller.setFixedDuration(duration);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run: curentPos: " + viewPager.getCurrentItem());
                    if (viewPager.getCurrentItem() >= images.size()) {
                        viewPager.setCurrentItem(1, false);
                        Log.d(TAG, "run: set" + 1);
//                        indicator.getTabAt(1).select();
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
//                        indicator.getTabAt(viewPager.getCurrentItem()).select();
                        Log.d(TAG, "run: set" + viewPager.getCurrentItem());
                    }
                }
            });
        }
    }

    private void setTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 4000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null)
            timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimer();
    }
}
