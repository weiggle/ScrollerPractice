package github.weiggle.com.scrollerpractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import github.weiggle.com.weidget.CurtainView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurtainView view = new CurtainView(this);
        setContentView(view);

    }

}
