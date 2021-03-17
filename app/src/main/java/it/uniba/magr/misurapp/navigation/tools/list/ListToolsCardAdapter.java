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
import it.uniba.magr.misurapp.navigation.tools.list.card.LevelToolCard;
import it.uniba.magr.misurapp.navigation.tools.list.card.LuxmeterToolCard;
import it.uniba.magr.misurapp.navigation.tools.list.card.MagnetometerToolCard;
import it.uniba.magr.misurapp.navigation.tools.list.card.ToolCard;
import it.uniba.magr.misurapp.navigation.tools.list.card.RulerToolCard;

public class ListToolsCardAdapter extends BaseAdapter {

    @NotNull
    private final Context context;

    @NotNull
    private final LinkedList<ToolCard> cards = new LinkedList<>();

    public ListToolsCardAdapter(@NotNull Context context) {

        this.context = context;

        addTool(context, new RulerToolCard());
        addTool(context, new LevelToolCard());
        addTool(context, new MagnetometerToolCard());
        addTool(context, new LuxmeterToolCard());


    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return getToolCard(position).getImageID();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NotNull
    public ToolCard getToolCard(int position) {
        return cards.get(position);
    }

    @Override
    @SuppressLint("InflateParams")
    public View getView(int position, View gridView, ViewGroup parent) {

        ToolCard toolCard = getToolCard(position);

        if (gridView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            gridView = inflater.inflate(R.layout.tools_grid_image_layout, null);

        }

        ImageView imageView = gridView.findViewById(R.id.tool_grid_view_image_view);
        MaterialTextView titleTextView = gridView
                .findViewById(R.id.tool_grid_title_view_text);

        imageView.setImageResource(toolCard.getImageID());
        titleTextView.setText(toolCard.getTitleID());

        return gridView;

    }

    private void addTool(@NotNull Context context, @NotNull ToolCard toolCard) {

        if (toolCard.isSupported(context)) {

            synchronized (cards) {
                cards.add(toolCard);
            }

        }

    }

}
