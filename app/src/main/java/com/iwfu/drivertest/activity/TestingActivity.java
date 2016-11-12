package com.iwfu.drivertest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iwfu.drivertest.R;
import com.iwfu.drivertest.bean.QuestionBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_testing);

        String dataResult = getIntent ().getStringExtra ("dataResult");
        parseJson (dataResult);
    }

    /**
     * 使用Gson解析Json数据
     */
    private void parseJson (String json) {
        Gson gson = new Gson ();
        Type listType = new TypeToken<ArrayList<QuestionBean>> () {
        }.getType ();
        List<QuestionBean> questions = gson.fromJson (json, listType);
        for (Iterator iterator = questions.iterator (); iterator.hasNext (); ) {
            QuestionBean question = (QuestionBean) iterator.next ();
            Log.d ("tag", "迭代结果:" + question.toString ());
        }
    }
}
