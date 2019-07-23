package me.bakumon.safkotlin;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import org.jetbrains.annotations.NotNull;

public class DocumentTreeRequest {

    private static final String TAG = "DocumentTreeRequest";
    private BridgeFragment mBridgeFragment;

    public interface Callback {
        void result(@Nullable Uri uri);
    }

    public DocumentTreeRequest(@NotNull AppCompatActivity activity) {
        mBridgeFragment = getOnResultFragment(activity.getSupportFragmentManager());
    }

    public DocumentTreeRequest(@NotNull Fragment fragment) {
        mBridgeFragment = getOnResultFragment(fragment.getChildFragmentManager());
    }

    private BridgeFragment getOnResultFragment(FragmentManager fragmentManager) {
        BridgeFragment bridgeFragment = findSimpleOnResultFragment(fragmentManager);
        if (bridgeFragment == null) {
            bridgeFragment = new BridgeFragment();
            fragmentManager
                    .beginTransaction()
                    .add(bridgeFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return bridgeFragment;
    }

    private BridgeFragment findSimpleOnResultFragment(@NotNull FragmentManager fragmentManager) {
        return (BridgeFragment) fragmentManager.findFragmentByTag(TAG);
    }

    public void request(Callback callback) {
        mBridgeFragment.requestDocumentTree(callback);
    }
}
