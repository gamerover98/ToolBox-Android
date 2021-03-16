package it.uniba.magr.misurapp.navigation.main.recycle;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import it.uniba.magr.misurapp.R;
import lombok.Getter;

public class MeasureRecyclerHolder extends RecyclerView.ViewHolder {

    @Getter @NotNull
    private final RecyclerView recyclerView;

    @Getter @NotNull
    private final MaterialCardView backgroundCardView;

    @NotNull
    private final AppCompatImageView iconView;

    @NotNull
    private final MaterialTextView titleView;

    @NotNull
    private final MaterialTextView descriptionView;

    public MeasureRecyclerHolder(@NotNull RecyclerView recyclerView, @NotNull View itemView) {

        super(itemView);

        this.recyclerView       = recyclerView;
        this.backgroundCardView = itemView.findViewById(R.id.main_measure_recycler_card);
        this.iconView           = itemView.findViewById(R.id.main_measure_recycler_view_icon);
        this.titleView          = itemView.findViewById(R.id.main_measure_recycler_view_title);
        this.descriptionView    = itemView.findViewById(R.id.main_measure_recycler_view_description);

    }

    @IdRes
    public int getImageID() {
        return iconView.getId();
    }

    @NotNull
    public String getTitle() {
        return titleView.getText().toString();
    }

    @NotNull
    public String getDescription() {
        return descriptionView.getText().toString();
    }

    public void setImageID(@DrawableRes int imageID) {
        iconView.setImageResource(imageID);
    }

    public void setTitle(@NotNull String title) {
        titleView.setText(title);
    }

    public void setDescription(@NotNull String description) {
        descriptionView.setText(description);
    }

}
