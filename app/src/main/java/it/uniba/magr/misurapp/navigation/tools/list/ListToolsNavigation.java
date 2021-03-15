package it.uniba.magr.misurapp.navigation.tools.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.Navigable;
import it.uniba.magr.misurapp.navigation.tools.list.card.ToolCard;
import lombok.Getter;

public class ListToolsNavigation implements Navigable {

    @Getter
    @SuppressLint("StaticFieldLeak")
    private static ListToolsNavigation instance;

    private Context context;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tools_list;
    }

    @Override
    @SuppressWarnings("squid:S2696")
    public void onAttach(@NotNull Context context) {
        instance = this;
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

    }

    public void performToolItemClick(View view) {

        FragmentActivity activity = (FragmentActivity) context;

        GridView gridView = activity.findViewById(R.id.tools_list_grid_view);
        assert gridView != null;

        int position = gridView.getPositionForView(view);
        ListToolsCardAdapter cardAdapter = (ListToolsCardAdapter) gridView.getAdapter();

        ToolCard toolCard = cardAdapter.getToolCard(position);
        toolCard.onClick(context);

    }

}
