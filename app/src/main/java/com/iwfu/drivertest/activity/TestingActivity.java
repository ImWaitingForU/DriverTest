package com.iwfu.drivertest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
			BaseActivityInterface {

	private TestingTopBar mTestingTopBar;
	private TextView tvQuestion;
	private TextView tvAnswer;
	private TextView tvId;
	private ImageView ivPic;
	private RadioGroup rgItems;
	private RadioButton rbItem1;
	private RadioButton rbItem2;
	private RadioButton rbItem3;
	private RadioButton rbItem4;

	private List<QuestionBean> mQuestionBeanList;
	private static int currentIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testing);
		initView();

		// 初始化数据
		String dataResult = getIntent().getStringExtra("dataResult");
		mQuestionBeanList = parseJson(dataResult);
		QuestionBean bean = mQuestionBeanList.get(currentIndex);

		changeQuestion(bean);

	}

	/**
	 * 切换问题
	 */
	private void changeQuestion(QuestionBean bean) {
		tvId.setText(bean.getId() + ".");
		tvQuestion.setText(bean.getQuestion());
		tvAnswer.setText(bean.getId() + ".");
		tvAnswer.setVisibility(View.GONE);
		mTestingTopBar.setProgress(currentIndex, mQuestionBeanList);

		if (bean.getItem3().equals("") && bean.getItem4().equals("")) {
			rbItem1.setText("A." + bean.getItem1());
			rbItem2.setText("B." + bean.getItem2());
			rbItem3.setText("C." + bean.getItem3());
			rbItem4.setText("D." + bean.getItem4());
		}

		if (bean.getUrl() != null) {
			ivPic.setVisibility(View.VISIBLE);
			Picasso.with(this).load(bean.getUrl())
					.placeholder(R.drawable.loading_failed).resize(800, 500)
					.into(ivPic);
		} else {
			ivPic.setVisibility(View.GONE);
		}
	}

	/**
	 * 选对后自动切换到下一题，选错则不切换，显示解析
	 */
	private void nextQuestion(int checkedId) {
		switch (checkedId) {
			case 1 :
				break;
			case 2 :
				break;
			case 3 :
				break;
			case 4 :
				break;
		}
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
		// for (Iterator iterator = questions.iterator(); iterator.hasNext();) {
		// QuestionBean question = (QuestionBean) iterator.next();
		// Log.d("tag", "迭代结果:" + question.getId());
		// }
		return questions;
	}

	@Override
	public void initView() {
		mTestingTopBar = (TestingTopBar) findViewById(R.id.testingTopBar);
		View testLayout = findViewById(R.id.test_layout);
		tvId = (TextView) testLayout.findViewById(R.id.tvId);
		tvQuestion = (TextView) testLayout.findViewById(R.id.tvQuestion);
		tvAnswer = (TextView) testLayout.findViewById(R.id.tvAnswer);
		ivPic = (ImageView) testLayout.findViewById(R.id.ivPic);
		rgItems = (RadioGroup) testLayout.findViewById(R.id.rgItems);
		rbItem1 = (RadioButton) testLayout.findViewById(R.id.rbItem1);
		rbItem2 = (RadioButton) testLayout.findViewById(R.id.rbItem2);
		rbItem3 = (RadioButton) testLayout.findViewById(R.id.rbItem3);
		rbItem4 = (RadioButton) testLayout.findViewById(R.id.rbItem4);

		rgItems.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 如果答案正确跳到下一题，并弹出解析

			}
		});

	}

}
