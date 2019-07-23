package me.bakumon.safkotlin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class BridgeFragment extends Fragment {

    private static final int OPEN_DIRECTORY_REQUEST_CODE = 101;

    private DocumentTreeRequest.Callback mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void requestDocumentTree(DocumentTreeRequest.Callback callback) {
        mCallback = callback;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK || data == null) {
                denied();
                return;
            }
            Uri directoryUri = data.getData();
            final FragmentActivity fragmentActivity = getActivity();
            if (directoryUri == null || fragmentActivity == null) {
                denied();
                return;
            }
            // 保留权限
            final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            fragmentActivity.getContentResolver().takePersistableUriPermission(directoryUri, takeFlags);
            granted(directoryUri);
        }
    }

    private void granted(Uri uri) {
        if (mCallback != null) {
            mCallback.result(uri);
        }
    }

    private void denied() {
        if (mCallback != null) {
            mCallback.result(null);
        }
    }
}
