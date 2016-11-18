package com.iwfu.drivertest.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iwfu.drivertest.R;
import com.iwfu.drivertest.bean.QuestionBean;
import com.iwfu.drivertest.utils.BaseActivityInterface;
import com.iwfu.drivertest.views.TestingTopBar;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TestingActivity extends AppCompatActivity
		implements
			BaseActivityInterface,
			View.OnClickListener,
			DialogInterface.OnClickListener {

	private View testLayout;
	private TestingTopBar mTestingTopBar;
	private TextView tvQuestion;
	private TextView tvAnswer;
	private TextView tvId;
	private ImageView ivPic;

	private TextView tvItem1;
	private TextView tvItem2;
	private TextView tvItem3;
	private TextView tvItem4;

	private List<QuestionBean> mQuestionBeanList;
	private static int currentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testing);
		initView();

		// 初次进入显示第一个题目
		String dataResult = getIntent().getStringExtra("dataResult");
		mQuestionBeanList = parseJson(dataResult);
		showQuestion(currentIndex);
	}

	/**
	 * 设置chooseTv和correctTv的背景
	 */
	private void setTvBackground(TextView chooseTv, String currentAnswer) {
		if (chooseTv == null) {
			tvItem1.setBackgroundColor(Color.TRANSPARENT);
			tvItem2.setBackgroundColor(Color.TRANSPARENT);
			tvItem3.setBackgroundColor(Color.TRANSPARENT);
			tvItem4.setBackgroundColor(Color.TRANSPARENT);
			return;
		}
		chooseTv.setBackgroundColor(Color.RED);
		TextView correctTv = (TextView) testLayout
				.findViewWithTag(currentAnswer);
		correctTv.setBackgroundColor(Color.GREEN);
	}

	/**
	 * 使用Gson解析Json数据
	 *
	 * @return 问题的集合
	 */
	private List<QuestionBean> parseJson(String json) {
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<QuestionBean>>() {
		}.getType();
		List<QuestionBean> questions = gson.fromJson(json, listType);
		return questions;
	}

	@Override
	public void initView() {
		mTestingTopBar = (TestingTopBar) findViewById(R.id.testingTopBar);
		testLayout = findViewById(R.id.test_layout);
		tvId = (TextView) testLayout.findViewById(R.id.tvId);
		tvQuestion = (TextView) testLayout.findViewById(R.id.tvQuestion);
		tvAnswer = (TextView) testLayout.findViewById(R.id.tvAnswer);
		ivPic = (ImageView) testLayout.findViewById(R.id.ivPic);
		Button btnNext = (Button) findViewById(R.id.btn_next);
		btnNext.setOnClickListener(this);

		// 设置顶部菜单
		mTestingTopBar.getIvLastQuestion().setOnClickListener(this);
		mTestingTopBar.getIvQuit().setOnClickListener(this);

		tvItem1 = (TextView) findViewById(R.id.tvItem1);
		tvItem2 = (TextView) findViewById(R.id.tvItem2);
		tvItem3 = (TextView) findViewById(R.id.tvItem3);
		tvItem4 = (TextView) findViewById(R.id.tvItem4);
		tvItem1.setTag("1");
		tvItem2.setTag("2");
		tvItem3.setTag("3");
		tvItem4.setTag("4");
		tvItem1.setOnClickListener(this);
		tvItem2.setOnClickListener(this);
		tvItem3.setOnClickListener(this);
		tvItem4.setOnClickListener(this);

	}

	private void showQuestion(int index) {
		QuestionBean bean = mQuestionBeanList.get(index);
		tvId.setText(bean.getId() + ".");
		tvQuestion.setText(bean.getQuestion());
		tvAnswer.setText("解析  " + parseAnswer(bean) + ":" + bean.getExplains());
		tvItem1.setText("A." + bean.getItem1());
		tvItem2.setText("B." + bean.getItem2());
		mTestingTopBar.setProgress(currentIndex, mQuestionBeanList);

		if (!bean.getItem3().equals("")) {
			tvItem3.setText("C." + bean.getItem3());
			tvItem4.setText("D." + bean.getItem4());
		} else {
			tvItem3.setText("");
			tvItem4.setText("");
		}

		if (bean.getUrl() != null && !bean.getUrl().equals("")) {
			ivPic.setVisibility(View.VISIBLE);
			Picasso.with(this).load(bean.getUrl())
					.placeholder(R.drawable.loading_failed).into(ivPic);
		} else {
			ivPic.setVisibility(View.GONE);
		}

		tvAnswer.setVisibility(View.GONE);
		setTvBackground(null, bean.getAnswer());
		// 如果这题还没有做,第一题/下一题
		if (bean.getChooseAnswer() == null) {
			setTvClickable(true);
			tvAnswer.setVisibility(View.GONE);
		} else {
			// 如果做了 按照正确的显示方式展示出
			TextView chooseTv = (TextView) testLayout.findViewWithTag(bean
					.getChooseAnswer());
			setTvBackground(chooseTv, bean.getAnswer());
			tvAnswer.setVisibility(View.VISIBLE);
		}
	}

	private void setTvClickable(boolean clickable) {
		tvItem1.setClickable(clickable);
		tvItem2.setClickable(clickable);
		tvItem3.setClickable(clickable);
		tvItem4.setClickable(clickable);
	}

	/**
	 * 将1234答案解析成ABCD
	 * 
	 * @param bean
	 * @return
	 */
	private String parseAnswer(QuestionBean bean) {
		String answer = null;
		switch (bean.getAnswer()) {
			case "1" :
				answer = "A";
				break;
			case "2" :
				answer = "B";
				break;
			case "3" :
				answer = "C";
				break;
			case "4" :
				answer = "D";
				break;
			default :
				break;
		}
		return answer;
	}

	/**
	 * 检查是否选对 选对后自动切换到下一题，选错则不切换，显示解析
	 */
	private void checkQuestion(int checkTvId) {
		String chooseAnswer;
		TextView chooseTv = (TextView) findViewById(checkTvId);
		if (chooseTv != null) {
			chooseAnswer = (String) chooseTv.getTag();
		} else {
			return;
		}

		String currentAnswer = mQuestionBeanList.get(currentIndex).getAnswer();
		mQuestionBeanList.get(currentIndex).setChooseAnswer(chooseAnswer); // 设置选择项
		setTvClickable(false); // 选择完不允许改变
		if (chooseAnswer.equals(currentAnswer)) {
			// 选对了
			chooseTv.setBackgroundColor(Color.GREEN);
			if ((currentIndex + 1) == mQuestionBeanList.size()) {
				Toast.makeText(TestingActivity.this, "最后一个了",
						Toast.LENGTH_SHORT).show();
				// TODO:结束弹框,统计结果
			} else {
				showQuestion(++currentIndex);
			}
		} else {
			// 选错了
			setTvBackground(chooseTv, currentAnswer);
			tvAnswer.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvItem1 || v.getId() == R.id.tvItem2
				|| v.getId() == R.id.tvItem3 || v.getId() == R.id.tvItem4) {
			checkQuestion(v.getId());
			return;
		}
		switch (v.getId()) {
			case R.id.iv_lastQuestion :
				if (currentIndex != 0) {
					showQuestion(--currentIndex);
				} else {
					Toast.makeText(TestingActivity.this, "当前已经是第一题了",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.iv_ttb_quit :
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("确定结束测试吗?").setNegativeButton("取消", null)
						.setPositiveButton("结束测试", this).show();
				break;
			case R.id.btn_next :
				if ((currentIndex + 1) == mQuestionBeanList.size()) {
					Toast.makeText(TestingActivity.this, "最后一题了",
							Toast.LENGTH_SHORT).show();
					// TODO:结束弹框,统计结果
				} else {
					showQuestion(++currentIndex);
				}
				break;
			default :
				break;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == Dialog.BUTTON_POSITIVE) {
			dialog.dismiss();
			finish();
			// TODO 结束测试，统计结果
		}
	}
}
