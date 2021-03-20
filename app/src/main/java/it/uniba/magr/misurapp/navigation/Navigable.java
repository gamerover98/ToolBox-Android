package it.uniba.magr.misurapp.navigation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.LayoutRes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Create your own navigable interface to attach into your navigable application.
 * <p>Let's see /res/navigation/nav_graph.xml file</p>
 */
@SuppressWarnings("unused") // unused default methods arguments.
public interface Navigable {

    /**
     * @return The fragment layout resource ID.
     */
    @LayoutRes
    int getLayoutId();

    /**
     * @param rootContext The root context of the activity.
     * @return The interface name for the toolbar title.
     */
    @NotNull
    String getToolbarName(@NotNull Context rootContext);

    /**
     * The first method called from the fragment lifecycle.
     * @param context The parent context where the fragment is placed.
     */
    default void onAttach(@NotNull Context context) {
        // nothing to do
    }

    /**
     * The second method called from the fragment lifecycle.
     * Within you implementation, you should initialize essential components
     * of fragment that you want to retain when the fragment
     * is paused or stopped, then resumed.
     *
     * @param bundle The context bundle.
     */
    default void onCreate(@Nullable Bundle bundle) {
        // nothing to do
    }

    /**
     * The third method called from the fragment lifecycle.
     * Called when the navigation fragment view is created.
     *
     * See method onCreateView() in the {@link androidx.fragment.app.Fragment} class.
     *
     * @param context The activity/fragment context.
     * @param bundle The context bundle.
     */
    default void onCreateView(@NotNull Context context, @Nullable Bundle bundle) {
        // nothing to do
    }

    /**
     * Called after onCreateView() method.
     *
     * @param activity The activity instance where the fragment is placed.
     * @param bundle The context bundle.
     */
    default void onActivityCreated(@NotNull Activity activity, @Nullable Bundle bundle) {
        // nothing to do
    }

    /**
     * Called after onActivityCreated() method,
     * when the Fragment is visible to the user.
     */
    default void onStart() {
        // nothing to do
    }

    /**
     * Called after fragment onStart() method,
     * when the fragment is visible to the user and actively running.
     */
    default void onResume() {
        // nothing to do
    }

    /**
     * The system calls this method as the first indication that the user
     * is leaving the fragment (though it doesn't always means the fragment
     * is being destroyed). This is usually where you should commit any changes
     * that should be persisted beyond the current user session (because
     * the user might not come back).
     */
    default void onPause() {
        // nothing to do
    }

    /**
     * Called when the Fragment is no longer started.
     * This is generally tied to Activity.onStop of
     * the containing Activity's lifecycle.
     */
    default void onStop() {
        // nothing to do
    }

    /**
     * Called when the view hierarchy associated with
     * the fragment is being removed.
     *
     * This method can return to the onCreateView() fragment method.
     */
    default void onDestroyView() {
        // nothing to do
    }

    /**
     * Called between onDestroyView() and onDetach() methods.
     *
     * This is the first method called when the fragment is going to
     * be detached and deleted.
     */
    default void onDestroy() {
        // nothing to do
    }

    /**
     * Called when the fragment is being disassociated from the activity.
     * This is the latest method of a fragment lifecycle.
     */
    default void onDetach() {
        // nothing to do
    }

    /**
     * Called when the user clicks the UI.
     * @param event The not null motion event.
     */
    default void onTouchEvent(@NotNull MotionEvent event) {
        // nothing to do
    }

}
