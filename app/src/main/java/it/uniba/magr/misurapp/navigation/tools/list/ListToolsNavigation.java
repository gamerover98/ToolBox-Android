package it.uniba.magr.misurapp.navigation.tools.list;

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
import it.uniba.magr.misurapp.navigation.tools.list.card.MeasureCard;

public class ListToolsNavigation implements Navigable {

    private Context context;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_measure_list_layout;
    }

    @NotNull
    @Override
    public String getToolbarName(@NotNull Context rootContext) {
        return rootContext.getResources().getString(R.string.text_list_tools);
    }

    @Override
    public void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {

        context = activity;

        GridView gridView = activity.findViewById(R.id.tools_list_grid_view);
        assert gridView != null;

        gridView.setAdapter(new ListToolsCardAdapter(context));
        gridView.setOnItemClickListener(this :: itemClick);

    }

    private void itemClick(AdapterView<?> parent, View view, int position, long id) {

        GridView gridView = (GridView) parent;
        ListToolsCardAdapter cardAdapter = (ListToolsCardAdapter) gridView.getAdapter();

        MeasureCard measureCard = cardAdapter.getMeasureCard(position);
        measureCard.onClick(context);

    }

}
