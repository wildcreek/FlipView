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
import android.widget.TextView;

/**
 * @version 1.0
 * @author hzc
 * @date 2015-6-25
 */
public class MainActivity extends Activity implements View.OnClickListener {
	private LinearLayout ll_infos;
	private ViewGroup mContainer;
	private LinearLayout ll_motto;
	private LinearLayout ll_day;
	private LinearLayout ll_people;
	private LinearLayout ll_rank;
	private TextView tv_motto;
	private String [] dayStrs;
	private String [] peopleStrs;
	private String [] rankStrs;
	private int [] currentIndexs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		ll_infos = (LinearLayout) findViewById(R.id.ll_infos);
		mContainer = (ViewGroup) findViewById(R.id.container);
		ll_motto = (LinearLayout) findViewById(R.id.ll_motto);
		
		ll_day = (LinearLayout) findViewById(R.id.ll_day);
		ll_people = (LinearLayout) findViewById(R.id.ll_people);
		ll_rank = (LinearLayout) findViewById(R.id.ll_rank);
		tv_motto = (TextView) findViewById(R.id.tv_motto);

		ll_day.setOnClickListener(this);
		ll_people.setOnClickListener(this);
		ll_rank.setOnClickListener(this);
		ll_motto.setOnClickListener(this);

		// Since we are caching large views, we want to keep their cache
		// between each animation
		mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
		
		//初始化数据
		initData();
	}

	private void initData() {
		dayStrs = new String[] {"真理惟一可靠的标准就是永远自相符合。 —— 欧文",
				"理想犹如天上的星星,我们犹如水手,虽不能到达天上,但是我们的航程可凭他指引  —— 舒尔茨",
				"时间是一切财富中最宝贵的财富。 —— 德奥弗拉斯多"};
		peopleStrs = new String[] {"竞争人数1","竞争人数2","竞争人数3","竞争人数4"};
		rankStrs = new String[] {"目前排名1","目前排名2","目前排名3","目前排名4","目前排名5"};
		currentIndexs = new int[]{0,0,0};
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
				centerX, centerY, 0.0f, true);
		rotation.setDuration(350);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(position));

		mContainer.startAnimation(rotation);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_day:
			applyRotation(0, 0, 90);
			if(currentIndexs[0] != dayStrs.length){
				tv_motto.setText(dayStrs[currentIndexs[0]]);
				currentIndexs[0]++;
			}else{
				currentIndexs[0] = 0;
				tv_motto.setText(dayStrs[currentIndexs[0]]);
				currentIndexs[0]++;
			}
			
			Log.e("Main", "点击了ll_day");
			break;
		case R.id.ll_people:
			applyRotation(0, 0, 90);
			if(currentIndexs[1] != peopleStrs.length){
				tv_motto.setText(peopleStrs[currentIndexs[1]]);
				currentIndexs[1]++;
			}else{
				currentIndexs[1] =0;
				tv_motto.setText(peopleStrs[currentIndexs[1]]);
				currentIndexs[1]++;
			}
			Log.e("Main", "点击了ll_people");
			break;
		case R.id.ll_rank:
			applyRotation(0, 0, 90);
			if(currentIndexs[2] != rankStrs.length){
				tv_motto.setText(rankStrs[currentIndexs[2]]);
				currentIndexs[2]++;
			}else{
				currentIndexs[2] = 0;
				tv_motto.setText(rankStrs[currentIndexs[2]]);
				currentIndexs[2]++;
			}
			Log.e("Main", "点击了ll_rank");
			break;
		case R.id.ll_motto:
			applyRotation(-1, 0, 90);
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

			if (mPosition > -1) {//点击正面条目
				ll_infos.setVisibility(View.GONE);
				ll_motto.setVisibility(View.VISIBLE);
				ll_motto.requestFocus();

				rotation = new Rotate3dAnimation(-90, 0, centerX, centerY,
						0.0f, false);
			} else {//点击后面条目
				ll_motto.setVisibility(View.GONE);
				ll_infos.setVisibility(View.VISIBLE);
				ll_infos.requestFocus();

				rotation = new Rotate3dAnimation(-90, 0, centerX, centerY,
						0.0f, false);
			}

			rotation.setDuration(350);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mContainer.startAnimation(rotation);
		}
	}

  
}
