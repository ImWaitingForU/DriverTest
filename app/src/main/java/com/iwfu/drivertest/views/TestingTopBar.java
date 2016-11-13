package com.iwfu.drivertest.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iwfu.drivertest.R;
import com.iwfu.drivertest.bean.QuestionBean;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Iwfu on 2016/11/12 0012.
 * <p>
 * 测试时的顶部Bar,包括左侧的倒计时，中间显示进度，右侧的退出测试按钮
 */
public class TestingTopBar extends FrameLayout {

	private ImageView ivQuit;
	private TextView tvTime;
	private TextView tvProgress;

	private MyHandler handler;
	private class MyHandler extends Handler {

		WeakReference<Context> mReference;

		public MyHandler(Context context) {
			mReference = new WeakReference<>(context);
		}

		@Override
		public void handleMessage(Message msg) {
			if (mReference != null) {

			}
		}
	}

	public TestingTopBar(Context context) {
		super(context);
	}

	public TestingTopBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		View view = LayoutInflater.from(context).inflate(
				R.layout.testing_top_bar, this, true);

		tvTime = (TextView) view.findViewById(R.id.tv_ttb_time);
		tvProgress = (TextView) view.findViewById(R.id.tv_ttb_progress);
		ivQuit = (ImageView) view.findViewById(R.id.iv_ttb_quit);

		handler = new MyHandler(context);
	}

	/**
	 * 开启倒计时
	 */
	public void startCountDown() {

	}

	/**
	 * 设置时间
	 *
	 * @param time
	 *            String表示的时间
	 */
	public void setTime(String time) {
		tvTime.setText(time);
	}

	/**
	 * 设置进度
	 *
	 * @param currentIndex
	 *            当前题号
	 * @param list
	 *            题目集合
	 */
	public void setProgress(int currentIndex, List<QuestionBean> list) {
		tvProgress.setText("进度\n" + (currentIndex + 1) / list.size());
	}

	/**
	 * 设置点击结束按钮
	 */
	public void setOnQuitButtonPressed(OnClickListener listener) {
		ivQuit.setOnClickListener(listener);
	}
}
