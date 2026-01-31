package com.google.android.exoplayer2.upstream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public final class RawResourceDataSource implements DataSource {
    private static final String RAW_RESOURCE_SCHEME = "rawresource";
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private InputStream inputStream;
    private final TransferListener<? super RawResourceDataSource> listener;
    private boolean opened;
    private final Resources resources;
    private Uri uri;

    public static class RawResourceDataSourceException extends IOException {
        public RawResourceDataSourceException(String message) {
            super(message);
        }

        public RawResourceDataSourceException(IOException e) {
            super(e);
        }
    }

    public static Uri buildRawResourceUri(int rawResourceId) {
        return Uri.parse("rawresource:///" + rawResourceId);
    }

    public RawResourceDataSource(Context context) {
        this(context, null);
    }

    public RawResourceDataSource(Context context, TransferListener<? super RawResourceDataSource> listener) {
        this.resources = context.getResources();
        this.listener = listener;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws NumberFormatException, IOException {
        try {
            this.uri = dataSpec.uri;
            if (!TextUtils.equals(RAW_RESOURCE_SCHEME, this.uri.getScheme())) {
                throw new RawResourceDataSourceException("URI must use scheme rawresource");
            }
            try {
                int resourceId = Integer.parseInt(this.uri.getLastPathSegment());
                this.assetFileDescriptor = this.resources.openRawResourceFd(resourceId);
                this.inputStream = new FileInputStream(this.assetFileDescriptor.getFileDescriptor());
                this.inputStream.skip(this.assetFileDescriptor.getStartOffset());
                long skipped = this.inputStream.skip(dataSpec.position);
                if (skipped < dataSpec.position) {
                    throw new EOFException();
                }
                if (dataSpec.length != -1) {
                    this.bytesRemaining = dataSpec.length;
                } else {
                    long assetFileDescriptorLength = this.assetFileDescriptor.getLength();
                    this.bytesRemaining = assetFileDescriptorLength != -1 ? assetFileDescriptorLength - dataSpec.position : -1L;
                }
                this.opened = true;
                if (this.listener != null) {
                    this.listener.onTransferStart(this, dataSpec);
                }
                return this.bytesRemaining;
            } catch (NumberFormatException e) {
                throw new RawResourceDataSourceException("Resource identifier must be an integer.");
            }
        } catch (IOException e2) {
            throw new RawResourceDataSourceException(e2);
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
                    throw new RawResourceDataSourceException(new EOFException());
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
            throw new RawResourceDataSourceException(e);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() throws RawResourceDataSourceException {
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
                        throw new RawResourceDataSourceException(e);
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
                throw new RawResourceDataSourceException(e2);
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
                    throw new RawResourceDataSourceException(e3);
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
