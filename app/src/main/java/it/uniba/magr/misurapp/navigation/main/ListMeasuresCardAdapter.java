package it.uniba.magr.misurapp.navigation.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.main.card.MeasureCard;
import it.uniba.magr.misurapp.navigation.main.card.RulerMeasureCard;

public class ListMeasuresCardAdapter extends BaseAdapter {

    @NotNull
    private final Context context;

    @NotNull
    private final LinkedList<MeasureCard> cards = new LinkedList<>();

    public ListMeasuresCardAdapter(@NotNull Context context) {

        this.context = context;
        addCard(new RulerMeasureCard());

    }

    /**
     * Add a card into the list adapter.
     *
     * @param card The not null instance of the measure card.
     * @return True if it will be added.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean addCard(@NotNull MeasureCard card) {

        synchronized (cards) {
            return cards.add(card);
        }

    }

    /**
     * Remove a card from the list adapter.
     *
     * @param card The not null instance of the measure card.
     * @return True if it will be removed.
     */
    @SuppressWarnings("UnusedReturnValue")
    public boolean removeCard(@NotNull MeasureCard card) {

        synchronized (cards) {
            return cards.remove(card);
        }

    }

    /**
     * Gets a list of cards with a specific title.
     *
     * @param title The not null title string.
     * @return A not modifiable list of cards.
     */
    @NotNull
    public List<MeasureCard> getCards(@NotNull String title) {

        List<MeasureCard> result = new LinkedList<>();

        synchronized (cards) {

            cards.stream()
                    .filter(c -> c.getTitle().equals(title))
                    .forEach(result :: add);

        }

        return Collections.unmodifiableList(result);

    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return getMeasureCard(position).getImageID();
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

        MeasureCard measureCard = getMeasureCard(position);

        if (gridView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            gridView = inflater.inflate(R.layout.measures_grid_image_layout, null);

        }

        ImageView imageView = gridView.findViewById(R.id.measure_grid_view_image_view_icon);
        MaterialTextView titleTextView = gridView
                .findViewById(R.id.measure_grid_view_text_view_title);
        MaterialTextView descTextView = gridView
                .findViewById(R.id.measure_grid_view_text_view_description);

        imageView.setImageResource(measureCard.getImageID());
        titleTextView.setText(measureCard.getTitle());
        descTextView.setText(measureCard.getDescription());

        return gridView;

    }

}
