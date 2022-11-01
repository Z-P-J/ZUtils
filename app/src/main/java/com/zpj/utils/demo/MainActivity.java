package com.zpj.utils.demo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.zpj.utils.ContextUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "context=" + ContextUtils.getApplicationContext(), Toast.LENGTH_SHORT).show();
    }

}
