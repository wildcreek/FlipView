package com.example.activity;

import com.example.flipview.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * @version 1.0
 * @author hzc
 * @date 2015-6-25
 */
public class MainActivity extends Activity implements View.OnClickListener {
	private LinearLayout ll_infos;
	private ViewGroup mContainer;
	private LinearLayout ll_motto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		ll_infos = (LinearLayout) findViewById(R.id.ll_infos);
		mContainer = (ViewGroup) findViewById(R.id.container);
		ll_motto = (LinearLayout) findViewById(R.id.ll_motto);

		// Prepare the ListView
		ll_infos.setClickable(true);
		ll_infos.setFocusable(true);
		ll_infos.setOnClickListener(this);

		// Prepare the ImageView
		ll_motto.setClickable(true);
		ll_motto.setFocusable(true);
		ll_motto.setOnClickListener(this);

		// Since we are caching large views, we want to keep their cache
		// between each animation
		mContainer
				.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
	}

	/**
	 * Setup a new 3D rotation on the container view.
	 * 
	 * @param position
	 *            the item that was clicked to show a picture, or -1 to show the
	 *            list
	 * @param start
	 *            the start angle at which the rotation must begin
	 * @param end
	 *            the end angle of the rotation
	 */
	private void applyRotation(int position, float start, float end) {
		// Find the center of the container
		final float centerX = mContainer.getWidth() / 2.0f;
		final float centerY = mContainer.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 310.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(position));

		mContainer.startAnimation(rotation);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_infos:
			applyRotation(0, 0, 90);
			Log.e("Main", "点击了ll_infos");
			break;
		case R.id.ll_motto:
			applyRotation(-1, 180, 90);
			Log.e("Main", "点击了ll_motto");
			break;
		default:
			break;
		}
		
	}
 
	/**
	 * This class listens for the end of the first half of the animation. It
	 * then posts a new action that effectively swaps the views when the
	 * container is rotated 90 degrees and thus invisible.
	 */
	private final class DisplayNextView implements Animation.AnimationListener {
		private final int mPosition;

		private DisplayNextView(int position) {
			mPosition = position;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			mContainer.post(new SwapViews(mPosition));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * This class is responsible for swapping the views and start the second
	 * half of the animation.
	 */
	private final class SwapViews implements Runnable {
		private final int mPosition;

		public SwapViews(int position) {
			mPosition = position;
		}

		public void run() {
			final float centerX = mContainer.getWidth() / 2.0f;
			final float centerY = mContainer.getHeight() / 2.0f;
			Rotate3dAnimation rotation;

			if (mPosition > -1) {
				ll_infos.setVisibility(View.GONE);
				ll_motto.setVisibility(View.VISIBLE);
				ll_motto.requestFocus();

				rotation = new Rotate3dAnimation(90, 180, centerX, centerY,
						310.0f, false);
			} else {
				ll_motto.setVisibility(View.GONE);
				ll_infos.setVisibility(View.VISIBLE);
				ll_infos.requestFocus();

				rotation = new Rotate3dAnimation(90, 0, centerX, centerY,
						310.0f, false);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mContainer.startAnimation(rotation);
		}
	}

  
}
