package it.uniba.magr.misurapp.navigation.measure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.measure.card.LevelCard;
import it.uniba.magr.misurapp.navigation.measure.card.MeasureCard;
import it.uniba.magr.misurapp.navigation.measure.card.RulerCard;

public class MeasureListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup parent,
                             @Nullable Bundle bundle) {

        return inflater.inflate(R.layout.fragment_measure_list_layout,
                parent, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {

        super.onActivityCreated(bundle);
        assert getActivity() != null;

        GridView gridView = getActivity().findViewById(R.id.tools_list_grid_view);
        assert gridView != null;

        gridView.setAdapter(new MeasureCardAdapter());
        gridView.setOnItemClickListener(this :: itemClick);

    }

    private void itemClick(AdapterView<?> parent, View view, int position, long id) {

        assert getContext() != null;

        GridView gridView = (GridView) parent;
        MeasureCardAdapter cardAdapter = (MeasureCardAdapter) gridView.getAdapter();

        MeasureCard measureCard = cardAdapter.getMeasureCard(position);
        measureCard.onClick(getContext());

    }

    public class MeasureCardAdapter extends BaseAdapter {

        private final LinkedList<MeasureCard> cards = new LinkedList<>();

        public MeasureCardAdapter() {

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

                assert getContext() != null;
                LayoutInflater inflater = (LayoutInflater) getContext()
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

}
