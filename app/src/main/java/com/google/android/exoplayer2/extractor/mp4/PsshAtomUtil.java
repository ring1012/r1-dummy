package com.google.android.exoplayer2.extractor.mp4;

import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.nio.ByteBuffer;
import java.util.UUID;

/* loaded from: classes.dex */
public final class PsshAtomUtil {
    private static final String TAG = "PsshAtomUtil";

    private PsshAtomUtil() {
    }

    public static byte[] buildPsshAtom(UUID uuid, byte[] data) {
        int psshBoxLength = data.length + 32;
        ByteBuffer psshBox = ByteBuffer.allocate(psshBoxLength);
        psshBox.putInt(psshBoxLength);
        psshBox.putInt(Atom.TYPE_pssh);
        psshBox.putInt(0);
        psshBox.putLong(uuid.getMostSignificantBits());
        psshBox.putLong(uuid.getLeastSignificantBits());
        psshBox.putInt(data.length);
        psshBox.put(data);
        return psshBox.array();
    }

    public static UUID parseUuid(byte[] atom) {
        Pair<UUID, byte[]> parsedAtom = parsePsshAtom(atom);
        if (parsedAtom == null) {
            return null;
        }
        return (UUID) parsedAtom.first;
    }

    public static byte[] parseSchemeSpecificData(byte[] atom, UUID uuid) {
        Pair<UUID, byte[]> parsedAtom = parsePsshAtom(atom);
        if (parsedAtom == null) {
            return null;
        }
        if (uuid != null && !uuid.equals(parsedAtom.first)) {
            Log.w(TAG, "UUID mismatch. Expected: " + uuid + ", got: " + parsedAtom.first + ".");
            return null;
        }
        return (byte[]) parsedAtom.second;
    }

    private static Pair<UUID, byte[]> parsePsshAtom(byte[] atom) {
        ParsableByteArray atomData = new ParsableByteArray(atom);
        if (atomData.limit() < 32) {
            return null;
        }
        atomData.setPosition(0);
        int atomSize = atomData.readInt();
        if (atomSize != atomData.bytesLeft() + 4) {
            return null;
        }
        int atomType = atomData.readInt();
        if (atomType != Atom.TYPE_pssh) {
            return null;
        }
        int atomVersion = Atom.parseFullAtomVersion(atomData.readInt());
        if (atomVersion > 1) {
            Log.w(TAG, "Unsupported pssh version: " + atomVersion);
            return null;
        }
        UUID uuid = new UUID(atomData.readLong(), atomData.readLong());
        if (atomVersion == 1) {
            int keyIdCount = atomData.readUnsignedIntToInt();
            atomData.skipBytes(keyIdCount * 16);
        }
        int dataSize = atomData.readUnsignedIntToInt();
        if (dataSize != atomData.bytesLeft()) {
            return null;
        }
        byte[] data = new byte[dataSize];
        atomData.readBytes(data, 0, dataSize);
        return Pair.create(uuid, data);
    }
}
