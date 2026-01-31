package com.google.android.exoplayer2.upstream;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public final class AssetDataSource implements DataSource {
    private final AssetManager assetManager;
    private long bytesRemaining;
    private InputStream inputStream;
    private final TransferListener<? super AssetDataSource> listener;
    private boolean opened;
    private Uri uri;

    public static final class AssetDataSourceException extends IOException {
        public AssetDataSourceException(IOException cause) {
            super(cause);
        }
    }

    public AssetDataSource(Context context) {
        this(context, null);
    }

    public AssetDataSource(Context context, TransferListener<? super AssetDataSource> listener) {
        this.assetManager = context.getAssets();
        this.listener = listener;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public long open(DataSpec dataSpec) throws IOException {
        try {
            this.uri = dataSpec.uri;
            String path = this.uri.getPath();
            if (path.startsWith("/android_asset/")) {
                path = path.substring(15);
            } else if (path.startsWith(MqttTopic.TOPIC_LEVEL_SEPARATOR)) {
                path = path.substring(1);
            }
            this.inputStream = this.assetManager.open(path, 1);
            long skipped = this.inputStream.skip(dataSpec.position);
            if (skipped < dataSpec.position) {
                throw new EOFException();
            }
            if (dataSpec.length != -1) {
                this.bytesRemaining = dataSpec.length;
            } else {
                this.bytesRemaining = this.inputStream.available();
                if (this.bytesRemaining == 2147483647L) {
                    this.bytesRemaining = -1L;
                }
            }
            this.opened = true;
            if (this.listener != null) {
                this.listener.onTransferStart(this, dataSpec);
            }
            return this.bytesRemaining;
        } catch (IOException e) {
            throw new AssetDataSourceException(e);
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
                    throw new AssetDataSourceException(new EOFException());
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
            throw new AssetDataSourceException(e);
        }
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public Uri getUri() {
        return this.uri;
    }

    @Override // com.google.android.exoplayer2.upstream.DataSource
    public void close() throws AssetDataSourceException {
        this.uri = null;
        try {
            try {
                if (this.inputStream != null) {
                    this.inputStream.close();
                }
            } catch (IOException e) {
                throw new AssetDataSourceException(e);
            }
        } finally {
            this.inputStream = null;
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd(this);
                }
            }
        }
    }
}
