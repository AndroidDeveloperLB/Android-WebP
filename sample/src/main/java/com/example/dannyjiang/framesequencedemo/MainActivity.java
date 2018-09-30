package com.example.dannyjiang.framesequencedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.danny.framesSquencce.WebpImageView;

public class MainActivity extends AppCompatActivity {

    WebpImageView webpImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webpImageView = findViewById(R.id.webpImage);
        // add finish callback
        webpImageView.setFinishedListener(new WebpImageView.OnWebpFinishListener() {

            @Override
            public void onAnimationFinished() {
                Toast.makeText(MainActivity.this, "default webp animation finished",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationStart() {
                Toast.makeText(MainActivity.this, "default webp animation start",
                        Toast.LENGTH_SHORT).show();
            }
        });
        webpImageView.setNeutralDrawable(getResources().openRawResource(R.raw.ben_happy_talk_right));
        webpImageView.setDefaultAnimationCount(10);
    }

    public void defaultAnim(View view) {
        webpImageView.playAnimation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        webpImageView.destroy();
    }
}
