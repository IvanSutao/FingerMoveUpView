package code.ivan.com.slideviewinorout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import code.ivan.com.FingerMoveUpView.FingerMoveUpView;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RelativeLayout rootview = findViewById(R.id.rl_rootview);
//                rootview.addView(new FingerMoveUpView(MainActivity.this));
//            }
//        });
    }
}
