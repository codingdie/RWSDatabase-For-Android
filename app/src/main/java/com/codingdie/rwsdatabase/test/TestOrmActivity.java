package com.codingdie.rwsdatabase.test;

import android.app.Activity;
import android.os.Bundle;
import com.codingdie.rwsdatabase.R;
import com.codingdie.rwsdatabase.orm.cache.ClassCache;
import com.codingdie.rwsdatabase.orm.cache.model.ClassInfo;
import com.codingdie.rwsdatabase.orm.cache.model.TestClass;
import com.google.gson.Gson;

public class TestOrmActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_orm);
        long cur = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            ClassInfo.newInstance(TestClass.class);
        }
        System.out.println(System.currentTimeMillis() - cur);
        cur = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            ClassCache.getInstance().get(TestClass.class);
        }
        System.out.println(System.currentTimeMillis() - cur);

    }
}
