package it.uniba.magr.misurapp.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;

import java.util.LinkedList;

import it.uniba.magr.misurapp.R;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

        GridView gridView = getActivity().findViewById(R.id.measure_list_grid_view);
        assert gridView != null;

        gridView.setAdapter(new ImageAdapter());

    }

    public class ImageAdapter extends BaseAdapter {

        private final LinkedList<Card> cards = new LinkedList<>();

        public ImageAdapter() {
            cards.add(new Card(R.mipmap.icon_level_foreground, R.string.app_name));
        }

        @Override
        public int getCount() {
            return cards.size();
        }

        @Override
        public Object getItem(int position) {
            return cards.get(position).imageID;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        @SuppressLint("InflateParams")
        public View getView(int position, View gridView, ViewGroup parent) {

            if (gridView == null) {

                assert getContext() != null;
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                gridView = inflater.inflate(R.layout.measure_grid_view_image_layout, null);

            }

            ImageView imageView = gridView.findViewById(R.id.measure_grid_view_image_view);
            MaterialTextView textView = gridView.findViewById(R.id.measure_grid_view_text);
            Card card = cards.get(position);

            imageView.setImageResource(card.imageID);
            textView.setText(card.textID);

            return gridView;

        }

        @Getter
        @RequiredArgsConstructor
        private class Card {

            private final int imageID;
            private final int textID;

        }

    }

}
