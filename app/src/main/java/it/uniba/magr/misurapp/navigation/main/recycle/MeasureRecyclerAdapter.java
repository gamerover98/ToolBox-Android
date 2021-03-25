package it.uniba.magr.misurapp.navigation.main.recycle;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

import it.uniba.magr.misurapp.R;
import it.uniba.magr.misurapp.database.bean.Measure;
import it.uniba.magr.misurapp.navigation.main.entry.MeasureEntry;
import lombok.Getter;

public class MeasureRecyclerAdapter extends RecyclerView.Adapter<MeasureRecyclerHolder> {

    /**
     * The recycler view instance.
     */
    @Getter @NotNull
    private final RecyclerView recyclerView;

    @NotNull
    private final LinkedList<MeasureEntry> entries = new LinkedList<>();

    public MeasureRecyclerAdapter(@NotNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
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

        int imageID = entry.getImageID();
        Measure measure = entry.getMeasure();

        String title = measure.getTitle();
        String description = measure.getDescription();

        holder.setImageID(imageID);
        holder.setTitle(title);
        holder.setDescription(description);

    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    /**
     * Adds a measure into the recycler view.
     * @param iconId  The drawable icon id.
     * @param measure The not null measure instance.
     */
    public void addMeasureEntry(@DrawableRes int iconId, @NotNull Measure measure) {

        MeasureEntry entry = new MeasureEntry(iconId, measure);
        entries.add(entry);

    }

    /**
     * Removes a measure from the recycler view.
     * @param position The item position.
     * @throws IllegalArgumentException If the position is major or equals
     *                                  than the entries array size.
     */
    public void removeMeasureEntry(int position) {

        if (position >= getItemCount()) {

            throw new IllegalArgumentException("Out of bound array position: "
                    + position + "/" + entries.size());

        }

        entries.remove(position);

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

    /**
     * Remove all entries.
     */
    public void clear() {
        entries.clear();
    }

    public void onRowMoved(int fromPosition, int toPosition) {

        MeasureEntry e1 = entries.get(fromPosition);
        MeasureEntry e2 = entries.get(toPosition);

        entries.set(fromPosition, e2);
        entries.set(toPosition,   e1);

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
