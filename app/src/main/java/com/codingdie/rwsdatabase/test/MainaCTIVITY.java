package com.codingdie.rwsdatabase.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.codingdie.rwsdatabase.R;

/**
 * Created by xupen on 2016/8/28.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_list);
    }
    public void testMultipleRead(){
        startActivity(new Intent(this,MultipleReadActivity.class));
    }
    public void testMultipleReadAndWrite(){
        startActivity(new Intent(this,MultipleReadAndWrite.class));
    }
}
