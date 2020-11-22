package it.uniba.magr.misurapp.navigation.tools.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.tools.card.LevelCard;
import it.uniba.magr.misurapp.navigation.tools.card.MeasureCard;
import it.uniba.magr.misurapp.navigation.tools.card.RulerCard;

public class ListToolsCardAdapter extends BaseAdapter {

    @NotNull
    private final Context context;

    @NotNull
    private final LinkedList<MeasureCard> cards = new LinkedList<>();

    public ListToolsCardAdapter(@NotNull Context context) {

        this.context = context;

        cards.add(new LevelCard());
        cards.add(new RulerCard());

    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position).getImageID();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NotNull
    public MeasureCard getMeasureCard(int position) {
        return cards.get(position);
    }

    @Override
    @SuppressLint("InflateParams")
    public View getView(int position, View gridView, ViewGroup parent) {

        MeasureCard measureCard = cards.get(position);

        if (gridView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            gridView = inflater.inflate(R.layout.measure_grid_view_image_layout, null);

        }

        ImageView imageView = gridView.findViewById(R.id.measure_grid_view_image_view);
        MaterialTextView titleTextView = gridView
                .findViewById(R.id.measure_grid_title_view_text);
        MaterialTextView descTextView = gridView
                .findViewById(R.id.measure_grid_description_text_view);

        imageView.setImageResource(measureCard.getImageID());
        titleTextView.setText(measureCard.getTitleID());
        descTextView.setText(measureCard.getDescriptionID());

        return gridView;

    }

}
