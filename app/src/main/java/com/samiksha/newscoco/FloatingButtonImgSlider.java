package com.samiksha.newscoco;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

public class FloatingButtonImgSlider extends AppCompatActivity {

    SliderLayout sliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_button_img_slider);

        sliderLayout = findViewById(R.id.imageSlider);
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderLayout.setScrollTimeInSec(2);

        setSliderViews();


    }

    private void setSliderViews() {
        for (int i = 0; i <= 2; i++) {
            DefaultSliderView defaultsliderView = new DefaultSliderView(this);

            switch (i) {
                case 0:
                    defaultsliderView.setImageDrawable(R.drawable.sam1);
                    defaultsliderView.setDescription("FIRST IMAGE");
                    break;
                case 1:
                    defaultsliderView.setImageDrawable(R.drawable.karan);
                    defaultsliderView.setDescription("SECOND IMAGE");
                    break;
                case 2:
                    defaultsliderView.setImageDrawable(R.drawable.burhan);
                    defaultsliderView.setDescription("THIRD IMAGE");
                    break;


            }

            defaultsliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            defaultsliderView.setDescription("" + (i + 1));
            final int final1 = 1;
            defaultsliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {

                }
            });
            sliderLayout.addSliderView(defaultsliderView);


        }


    }
}
