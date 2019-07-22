package me.bakumon.safkotlin;

import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DocumentTreeRequest {

    private static final String TAG = "DocumentTreeRequest";
    private BridgeFragment mBridgeFragment;

    public interface Callback {
        void result(@Nullable Uri uri);
    }

    public DocumentTreeRequest(AppCompatActivity activity) {
        mBridgeFragment = getOnResultFragment(activity.getSupportFragmentManager());
    }

    public DocumentTreeRequest(Fragment fragment) {
        mBridgeFragment = getOnResultFragment(fragment.getChildFragmentManager());
    }

    private BridgeFragment getOnResultFragment(FragmentManager fragmentManager) {
        BridgeFragment simpleOnResultFragment = findSimpleOnResultFragment(fragmentManager);
        if (simpleOnResultFragment == null) {
            simpleOnResultFragment = new BridgeFragment();
            fragmentManager
                    .beginTransaction()
                    .add(simpleOnResultFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return simpleOnResultFragment;
    }

    private BridgeFragment findSimpleOnResultFragment(FragmentManager fragmentManager) {
        return (BridgeFragment) fragmentManager.findFragmentByTag(TAG);
    }

    public void request(Callback callback) {
        mBridgeFragment.requestDocumentTree(callback);
    }
}
