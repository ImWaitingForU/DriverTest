package com.iwfu.drivertest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.iwfu.drivertest.R;
import com.iwfu.drivertest.receiver.NetworkReceiver;
import com.iwfu.drivertest.utils.BaseActivityInterface;
import com.iwfu.drivertest.utils.JuheUtils;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity
		implements
			BaseActivityInterface,
			View.OnClickListener,
			RadioGroup.OnCheckedChangeListener,
			CompoundButton.OnCheckedChangeListener {

	private RadioGroup rgSubject;
	private RadioGroup rgModel;
	private TextView tvTypeRadom;
	private TextView tvTypeOrder;
	private Switch typeSwitcher;
	private Button btnStart;

	/* 要选择的科目驾照等,默认为科目1，c1驾照，随机测试 */
	public static int chooseSubject = 1;
	public static String chooseModel = "c1";
	public static String chooseType = "rand";

	private ConnectivityManager cm;
	private NetworkReceiver receiver;
	private static ProgressDialog loadingDialog;

	public static final int LOADING_FINISH = 0x111;

	private static MyHandler mHandler;
	/**
	 * 使用静态弱引用handler
	 */
	private static class MyHandler extends Handler {
		WeakReference<Activity> mWeakReference;

		public MyHandler(Activity activity) {
			mWeakReference = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity activity = (MainActivity) mWeakReference.get();
			if (activity != null) {
				if (msg.what == LOADING_FINISH) {
					loadingDialog.dismiss();
					activity.startActivity(new Intent(activity,
							TestingActivity.class));
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver();
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public void initView() {
		rgSubject = (RadioGroup) findViewById(R.id.rg_subject);
		rgModel = (RadioGroup) findViewById(R.id.rg_model);
		tvTypeRadom = (TextView) findViewById(R.id.tv_type1);
		tvTypeOrder = (TextView) findViewById(R.id.tv_type2);
		btnStart = (Button) findViewById(R.id.btn_start);
		typeSwitcher = (Switch) findViewById(R.id.type_switcher);

		btnStart.setOnClickListener(this);
		rgSubject.setOnCheckedChangeListener(this);
		rgModel.setOnCheckedChangeListener(this);
		typeSwitcher.setOnCheckedChangeListener(this);

		mHandler = new MyHandler(this);
	}

	private void registerReceiver() {
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		receiver = new NetworkReceiver(cm);
		registerReceiver(receiver, filter);
	}

	private void startTest() {
		// 启动加载dialog
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setCancelable(false);
		loadingDialog.setMessage("正在智能出题..");
		loadingDialog.show();
		new Thread(new MyRunnable()).start();
	}

	/**
	 * 使用静态内部类进行线程操作，防止内存泄漏
	 */
	static class MyRunnable implements Runnable {

		@Override
		public void run() {
			Object dataResult = JuheUtils.getQuestion(chooseSubject,
					chooseModel, chooseType);
			if (dataResult != null) {
				Message message = Message.obtain();
				message.what = LOADING_FINISH;
				mHandler.sendMessage(message);
			}
		}
	}

	@Override
	public void onClick(View v) {
		startTest();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rb_subject1 :
				chooseSubject = 1;
				break;
			case R.id.rb_subject4 :
				chooseSubject = 4;
				break;
			case R.id.rb_model1 :
				chooseModel = "c1";
				break;
			case R.id.rb_model2 :
				chooseModel = "c2";
				break;
			case R.id.rb_model3 :
				chooseModel = "a1";
				break;
			case R.id.rb_model4 :
				chooseModel = "a2";
				break;
			case R.id.rb_model5 :
				chooseModel = "b1";
				break;
			case R.id.rb_model6 :
				chooseModel = "b2";
				break;
			default :
				break;
		}
		Log.d("tag", chooseSubject + "-" + chooseModel + "-" + chooseType);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			Log.d("tag", "order");
			chooseType = "order";
		} else {
			Log.d("tag", "rand");
			chooseType = "rand";
		}
	}
}
