package it.uniba.magr.misurapp.navigation.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;
import it.uniba.magr.misurapp.navigation.tools.card.MeasureCard;

public class ToolsNavigation implements Navigable {

    private Context context;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_measure_list_layout;
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        context = activity;

        GridView gridView = activity.findViewById(R.id.tools_list_grid_view);
        assert gridView != null;

        gridView.setAdapter(new ToolsCardAdapter(context));
        gridView.setOnItemClickListener(this :: itemClick);

    }

    private void itemClick(AdapterView<?> parent, View view, int position, long id) {

        GridView gridView = (GridView) parent;
        ToolsCardAdapter cardAdapter = (ToolsCardAdapter) gridView.getAdapter();

        MeasureCard measureCard = cardAdapter.getMeasureCard(position);
        measureCard.onClick(context);

    }

}
