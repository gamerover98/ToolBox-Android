package it.uniba.magr.misurapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SlideViewIntroductionAdapter extends PagerAdapter {

    Context ctx;

    public SlideViewIntroductionAdapter(Context ctx){
        this.ctx = ctx;
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.introduction_screen, container,false);

        ImageView logo = view.findViewById(R.id.logo);
        ImageView index_1 = view.findViewById(R.id.index_1);
        ImageView index_2 = view.findViewById(R.id.index_2);
        ImageView index_3 = view.findViewById(R.id.index_3);


        TextView title_welcome= view.findViewById(R.id.title_welcome);
        TextView text_welcome= view.findViewById(R.id.text_welcome);

        ImageView next = view.findViewById(R.id.arrow_next);
        ImageView back = view.findViewById(R.id.arrow_back);

        Button button_start = view.findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Introduction_activity.viewPager.setCurrentItem(position + 1);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Introduction_activity.viewPager.setCurrentItem(position - 1);

            }
        });

        switch (position) {
            case 0:
                logo.setImageResource(R.drawable.ic_launcher);
                index_1.setImageResource(R.drawable.selected_ind);
                index_2.setImageResource(R.drawable.unselected_ind);
                index_3.setImageResource(R.drawable.unselected_ind);

                title_welcome.setText(R.string.text_slide);
                text_welcome.setText(R.string.text_desc);
                back.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                break;
            case 1:
                logo.setImageResource(R.drawable.ic_launcher);
                index_1.setImageResource(R.drawable.unselected_ind);
                index_2.setImageResource(R.drawable.selected_ind);
                index_3.setImageResource(R.drawable.unselected_ind);

                title_welcome.setText(R.string.text_slide_2);
                text_welcome.setText(R.string.text_desc_2);
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                break;
            case 2:
                logo.setImageResource(R.drawable.ic_launcher);
                index_1.setImageResource(R.drawable.unselected_ind);
                index_2.setImageResource(R.drawable.unselected_ind);
                index_3.setImageResource(R.drawable.selected_ind);

                title_welcome.setText(R.string.text_slide_3);
                text_welcome.setText(R.string.text_desc_3);
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                break;
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
