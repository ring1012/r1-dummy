package com.google.android.exoplayer2.upstream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public final class ContentDataSource implements DataSource {
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private InputStream inputStream;
    private final TransferListener<? super ContentDataSource> listener;
    private boolean opened;
    private final ContentResolver resolver;
    private Uri uri;

    public static class ContentDataSourceException extends IOException {
        public ContentDataSourceException(IOException cause) {
            super(cause);
        }
    }

    public ContentDataSource(Context context) {
        this(context, null);
    }

    public ContentDataSource(Context context, TransferListener<? super ContentDataSource> listener) {
        this.resolver = context.getContentResolver();
        this.listener = listener;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws IOException {
        try {
            this.uri = dataSpec.uri;
            this.assetFileDescriptor = this.resolver.openAssetFileDescriptor(this.uri, "r");
            this.inputStream = new FileInputStream(this.assetFileDescriptor.getFileDescriptor());
            long skipped = this.inputStream.skip(dataSpec.position);
            if (skipped < dataSpec.position) {
                throw new EOFException();
            }
            if (dataSpec.length != -1) {
                this.bytesRemaining = dataSpec.length;
            } else {
                this.bytesRemaining = this.inputStream.available();
                if (this.bytesRemaining == 0) {
                    this.bytesRemaining = -1L;
                }
            }
            this.opened = true;
            if (this.listener != null) {
                this.listener.onTransferStart(this, dataSpec);
            }
            return this.bytesRemaining;
        } catch (IOException e) {
            throw new ContentDataSourceException(e);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public int read(byte[] buffer, int offset, int readLength) throws IOException {
        if (readLength == 0) {
            return 0;
        }
        if (this.bytesRemaining == 0) {
            return -1;
        }
        try {
            int bytesToRead = this.bytesRemaining == -1 ? readLength : (int) Math.min(this.bytesRemaining, readLength);
            int bytesRead = this.inputStream.read(buffer, offset, bytesToRead);
            if (bytesRead == -1) {
                if (this.bytesRemaining != -1) {
                    throw new ContentDataSourceException(new EOFException());
                }
                return -1;
            }
            if (this.bytesRemaining != -1) {
                this.bytesRemaining -= bytesRead;
            }
            if (this.listener != null) {
                this.listener.onBytesTransferred(this, bytesRead);
                return bytesRead;
            }
            return bytesRead;
        } catch (IOException e) {
            throw new ContentDataSourceException(e);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() throws ContentDataSourceException {
        this.uri = null;
        try {
            try {
                if (this.inputStream != null) {
                    this.inputStream.close();
                }
                this.inputStream = null;
                try {
                    try {
                        if (this.assetFileDescriptor != null) {
                            this.assetFileDescriptor.close();
                        }
                    } catch (IOException e) {
                        throw new ContentDataSourceException(e);
                    }
                } finally {
                    this.assetFileDescriptor = null;
                    if (this.opened) {
                        this.opened = false;
                        if (this.listener != null) {
                            this.listener.onTransferEnd(this);
                        }
                    }
                }
            } catch (IOException e2) {
                throw new ContentDataSourceException(e2);
            }
        } catch (Throwable th) {
            this.inputStream = null;
            try {
                try {
                    if (this.assetFileDescriptor != null) {
                        this.assetFileDescriptor.close();
                    }
                    this.assetFileDescriptor = null;
                    if (this.opened) {
                        this.opened = false;
                        if (this.listener != null) {
                            this.listener.onTransferEnd(this);
                        }
                    }
                    throw th;
                } catch (IOException e3) {
                    throw new ContentDataSourceException(e3);
                }
            } finally {
                this.assetFileDescriptor = null;
                if (this.opened) {
                    this.opened = false;
                    if (this.listener != null) {
                        this.listener.onTransferEnd(this);
                    }
                }
            }
        }
    }
}
