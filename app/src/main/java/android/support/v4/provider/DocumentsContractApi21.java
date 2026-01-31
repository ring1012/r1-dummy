package android.support.v4.provider;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.util.ArrayList;

@RequiresApi(21)
@TargetApi(21)
/* loaded from: classes.dex */
class DocumentsContractApi21 {
    private static final String TAG = "DocumentFile";

    DocumentsContractApi21() {
    }

    public static Uri createFile(Context context, Uri self, String mimeType, String displayName) {
        return DocumentsContract.createDocument(context.getContentResolver(), self, mimeType, displayName);
    }

    public static Uri createDirectory(Context context, Uri self, String displayName) {
        return createFile(context, self, "vnd.android.document/directory", displayName);
    }

    public static Uri prepareTreeUri(Uri treeUri) {
        return DocumentsContract.buildDocumentUriUsingTree(treeUri, DocumentsContract.getTreeDocumentId(treeUri));
    }

    public static Uri[] listFiles(Context context, Uri self) throws Exception {
        ContentResolver resolver = context.getContentResolver();
        Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(self, DocumentsContract.getDocumentId(self));
        ArrayList<Uri> results = new ArrayList<>();
        Cursor c = null;
        try {
            c = resolver.query(childrenUri, new String[]{"document_id"}, null, null, null);
            while (c.moveToNext()) {
                String documentId = c.getString(0);
                Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(self, documentId);
                results.add(documentUri);
            }
        } catch (Exception e) {
            Log.w(TAG, "Failed query: " + e);
        } finally {
            closeQuietly(c);
        }
        return (Uri[]) results.toArray(new Uri[results.size()]);
    }

    public static Uri renameTo(Context context, Uri self, String displayName) {
        return DocumentsContract.renameDocument(context.getContentResolver(), self, displayName);
    }

    private static void closeQuietly(AutoCloseable closeable) throws Exception {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
            }
        }
    }
}
