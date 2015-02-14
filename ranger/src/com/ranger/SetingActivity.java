package com.ranger;

import com.dd.CircularProgressButton;
import com.esri.android.map.MapView;
import com.ranger.ui.SwipeDismissTouchListener;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class SetingActivity extends Activity {

	MapView mMapView;
	ArcMap arcmap;

	ImageView imageView;
	RelativeLayout.LayoutParams imageView_params;

	ScrollView scrollView;
	RelativeLayout.LayoutParams scrollView_params;
	int old_img_margin;
	int old_scroll_margin;

	int margin = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting);

		imageView = (ImageView) findViewById(R.id.imageView1);
		imageView_params = (RelativeLayout.LayoutParams) imageView
				.getLayoutParams();
		old_img_margin = imageView_params.topMargin;

		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		scrollView_params = (RelativeLayout.LayoutParams) scrollView
				.getLayoutParams();
		old_scroll_margin = scrollView_params.topMargin;
		// scrollView.setScrollViewListener(this);

		// scrollView.setOnTouchListener(new TouchListener());

		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.slide_up);
		scrollView.startAnimation(animation);

		final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayout1);

		final CircularProgressButton circularButton1 = (CircularProgressButton) findViewById(R.id.circularProgressButton1);
		circularButton1.setIndeterminateProgressMode(true);
		circularButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (circularButton1.getProgress() == 0) {
					simulateErrorProgress(circularButton1);
				} else {
					circularButton1.setProgress(0);
				}
			}
		});

		Cards car = new Cards(this, linearLayout, scrollView);
		car.getView();
	}

	// @Override
	// public void onScrollChanged(ObservableScrollView scrollView, int x, int
	// y,
	// int oldx, int oldy) {
	// Log.d("-------->", "x:" + x + ",y:" + y);
	// if (y == 0) {
	// imageView_params.topMargin += margin;
	// imageView.setLayoutParams(imageView_params);
	// scrollView_params.topMargin += margin;
	// scrollView.setLayoutParams(scrollView_params);
	// }
	// }

	class TouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				imageView_params.topMargin = old_img_margin;
				imageView.setLayoutParams(imageView_params);
				scrollView_params.topMargin = old_scroll_margin;
				scrollView.setLayoutParams(scrollView_params);
			}
			return false;
		}
	}

	private void simulateSuccessProgress(final CircularProgressButton button) {
		ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
		widthAnimation.setDuration(1500);
		widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		widthAnimation
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						Integer value = (Integer) animation.getAnimatedValue();
						button.setProgress(value);
					}
				});
		widthAnimation.start();
	}

	private void simulateErrorProgress(final CircularProgressButton button) {
		ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
		widthAnimation.setDuration(1500);
		widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		widthAnimation
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						Integer value = (Integer) animation.getAnimatedValue();
						button.setProgress(value);
						if (value == 99) {
							button.setProgress(-1);
						}
					}
				});
		widthAnimation.start();
	}
}
