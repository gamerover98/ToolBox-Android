package it.uniba.magr.misurapp.navigation.main.recycle;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.navigation.main.entry.MeasureEntry;
import it.uniba.magr.misurapp.navigation.main.entry.RulerMeasureEntry;
import lombok.Getter;

public class MeasureRecyclerAdapter extends RecyclerView.Adapter<MeasureRecyclerHolder> {

    @Getter @NotNull
    private final RecyclerView recyclerView;

    @NotNull
    private final LinkedList<MeasureEntry> entries = new LinkedList<>();

    public MeasureRecyclerAdapter(@NotNull RecyclerView recyclerView) {

        this.recyclerView = recyclerView;

        for (int i = 0 ; i < 20 ; i++) {
            entries.add(new RulerMeasureEntry());
        }

    }

    @NotNull
    @Override
    public MeasureRecyclerHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        int layoutHolderID = R.layout.main_measure_recycler_view_layout_holder;
        View measureHolderView = layoutInflater.inflate(layoutHolderID, viewGroup, false);

        measureHolderView.setFocusable(View.FOCUSABLE_AUTO);
        measureHolderView.setFocusableInTouchMode(true);
        measureHolderView.setClickable(true);

        return new MeasureRecyclerHolder(recyclerView, measureHolderView);

    }

    @Override
    public void onBindViewHolder(@NotNull MeasureRecyclerHolder holder, int position) {

        MeasureEntry entry = entries.get(position);

        holder.setImageID(entry.getImageID());
        holder.setTitle(entry.getTitle());
        holder.setDescription(entry.getDescription());



    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    /**
     * Gets the measure entry from the item position.
     * @param position The recycler view item position.
     * @return The MeasureEntry instance of the item's position.
     */
    @Nullable
    public MeasureEntry getMeasureEntry(int position) {
        return entries.get(position);
    }

    public void onRowMoved(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {

            for (int i = fromPosition ; i < toPosition - 1 ; i++) {

                synchronized (entries) {
                    Collections.swap(entries, i, i + 1);
                }

            }

        } else {

            for (int i = fromPosition - 1 ; i > toPosition ; i--) {

                synchronized (entries) {
                    Collections.swap(entries, i, i + 1);
                }

            }

        }

        updateMoving(fromPosition, toPosition);

    }

    public void onRowSelected(@NotNull MeasureRecyclerHolder holder) {
        setBackgroundCardDragged(holder);
    }

    public void onRowClear(MeasureRecyclerHolder holder) {
        setBackgroundCardNormal(holder);
    }

    /**
     * Update single entry data.
     * @param position The entry position.
     */
    public void update(int position) {
        notifyItemChanged(position);
    }

    /**
     * Update an entry moving.
     *
     * @param fromPosition The old entry position.
     * @param toPosition   The new entry position.
     */
    public void updateMoving(int fromPosition, int toPosition) {

        if (fromPosition == toPosition) {
            return;
        }

        notifyItemMoved(fromPosition, toPosition);

    }

    /**
     * Update the view if an item is added.
     * @param position The new entry position.
     */
    public void updateAdding(int position) {
        notifyItemInserted(position);
    }

    /**
     * Update the view if an item is removed.
     * @param position The old entry position.
     */
    public void updateRemoving(int position) {
        notifyItemRemoved(position);
    }

    /**
     * Update all the view.
     * <p>It may affect performances</p>
     */
    @SuppressWarnings("NotifyDataSetChanged")
    public void updateAll() {

        RulerMeasureEntry.COUNT = 0;
        notifyDataSetChanged();

    }

    private void setBackgroundCardDragged(@NotNull MeasureRecyclerHolder holder) {

        MaterialCardView backgroundCardView = holder.getBackgroundCardView();
        Context context = recyclerView.getContext();
        Resources.Theme theme = context.getTheme();
        Resources resources = context.getResources();

        int color = ResourcesCompat.getColor(resources,
                R.color.background_dragged, theme);

        backgroundCardView.setCardBackgroundColor(color);

    }

    private void setBackgroundCardNormal(@NotNull MeasureRecyclerHolder holder) {

        MaterialCardView backgroundCardView = holder.getBackgroundCardView();
        Context context = recyclerView.getContext();
        Resources.Theme theme = context.getTheme();
        Resources resources = context.getResources();

        int color = ResourcesCompat.getColor(resources,
                R.color.background_darker, theme);

        backgroundCardView.setCardBackgroundColor(color);

    }

}
