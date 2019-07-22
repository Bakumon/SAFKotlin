package me.bakumon.safkotlin;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

public class DocumentFileHelper {
    public boolean canWrite(Context context, @Nullable Uri documentTreeUri) {
        if (documentTreeUri == null) {
            return false;
        }
        boolean isDocumentUri = DocumentFile.isDocumentUri(context, documentTreeUri);
        if (!isDocumentUri) {
            return false;
        }
        DocumentFile documentFile = DocumentFile.fromTreeUri(context, documentTreeUri);
        return documentFile != null && documentFile.exists() && documentFile.canWrite();
    }
}
