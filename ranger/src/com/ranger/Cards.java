package com.ranger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ranger.ui.SwipeDismissTouchListener;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;

public class Cards {
	private LayoutInflater mLayoutInflater;
	private ViewGroup dismissableContainer;
	private Context context = null;

	public Cards(Context context, ViewGroup dismissable, ScrollView scrollView) {
		this.context = context;
		this.dismissableContainer = dismissable;
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void refresh() {
		int child_count = dismissableContainer.getChildCount();
		dismissableContainer.removeViews(1, child_count - 2);
	}

	public void refreshIndex() {

	}

	/**
	 * ���ListView��Item���֡�
	 */
	public void getView() {

		for (int i = 0; i < 8; i++) {

			int resID = context.getResources().getIdentifier("item", "layout",
					context.getPackageName());

			final View dismissableView = mLayoutInflater.inflate(resID, null);

			// viewHolder(dismissableView, key, mMap);

			dismissableView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
				}
			});
			// Create a generic swipe-to-dismiss touch listener.
			dismissableView.setOnTouchListener(new SwipeDismissTouchListener(
					dismissableView, null,
					new SwipeDismissTouchListener.DismissCallbacks() {
						@Override
						public boolean canDismiss(Object token) {
							return true;
						}

						@Override
						public void onDismiss(View view, Object token) {
							dismissableContainer.removeView(dismissableView);
						}
					}));

			dismissableContainer.addView(dismissableView, 1);

			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.slide_up);
			dismissableView.startAnimation(animation);
		}

		// ���ǵ��׳���
		// try {
		// Class<?> c = (Class<?>) Class.forName("com.apn.ui."+key_name);
		// try {
		// Constructor<?> constructor = c.getConstructor(View.class,
		// Map.class); //���캯������б��class����
		// try {
		// constructor.newInstance(view,item);
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// } catch (InvocationTargetException e) {
		// e.printStackTrace();
		// }
		//
		// } catch (InstantiationException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// } catch (NoSuchMethodException e) {
		// e.printStackTrace();
		// }
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }

	}

}
