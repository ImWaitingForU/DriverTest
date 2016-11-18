package com.iwfu.drivertest.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iwfu.drivertest.R;
import com.iwfu.drivertest.bean.QuestionBean;

import java.util.List;

/**
 * Created by Iwfu on 2016/11/12 0012.
 * <p>
 * 测试时的顶部Bar,包括左侧的倒计时，中间显示进度，右侧的退出测试按钮
 */
public class TestingTopBar extends FrameLayout {

	private ImageView ivQuit;
	private TextView tvProgress;
	private ImageView ivLastQuestion;

	public TestingTopBar(Context context) {
		super(context);
	}

	public TestingTopBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		View view = LayoutInflater.from(context).inflate(
				R.layout.testing_top_bar, this, true);

		tvProgress = (TextView) view.findViewById(R.id.tv_ttb_progress);
		ivQuit = (ImageView) view.findViewById(R.id.iv_ttb_quit);
		ivLastQuestion = (ImageView) view.findViewById (R.id.iv_lastQuestion);


	}
	public TextView getTvProgress() {
		return tvProgress;
	}

	public void setTvProgress(TextView tvProgress) {
		this.tvProgress = tvProgress;
	}

	public ImageView getIvQuit() {
		return ivQuit;
	}

	public void setIvQuit(ImageView ivQuit) {
		this.ivQuit = ivQuit;
	}

	public ImageView getIvLastQuestion() {
		return ivLastQuestion;
	}

	public void setIvLastQuestion(ImageView ivLastQuestion) {
		this.ivLastQuestion = ivLastQuestion;
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
		tvProgress.setText("进度\n" + (currentIndex + 1) +"/"+list.size());
	}

}
