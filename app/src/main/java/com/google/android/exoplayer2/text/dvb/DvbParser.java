package com.google.android.exoplayer2.text.dvb;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class DvbParser {
    private static final int DATA_TYPE_24_TABLE_DATA = 32;
    private static final int DATA_TYPE_28_TABLE_DATA = 33;
    private static final int DATA_TYPE_2BP_CODE_STRING = 16;
    private static final int DATA_TYPE_48_TABLE_DATA = 34;
    private static final int DATA_TYPE_4BP_CODE_STRING = 17;
    private static final int DATA_TYPE_8BP_CODE_STRING = 18;
    private static final int DATA_TYPE_END_LINE = 240;
    private static final int OBJECT_CODING_PIXELS = 0;
    private static final int OBJECT_CODING_STRING = 1;
    private static final int PAGE_STATE_NORMAL = 0;
    private static final int REGION_DEPTH_4_BIT = 2;
    private static final int REGION_DEPTH_8_BIT = 3;
    private static final int SEGMENT_TYPE_CLUT_DEFINITION = 18;
    private static final int SEGMENT_TYPE_DISPLAY_DEFINITION = 20;
    private static final int SEGMENT_TYPE_OBJECT_DATA = 19;
    private static final int SEGMENT_TYPE_PAGE_COMPOSITION = 16;
    private static final int SEGMENT_TYPE_REGION_COMPOSITION = 17;
    private static final String TAG = "DvbParser";
    private static final byte[] defaultMap2To4 = {0, 7, 8, 15};
    private static final byte[] defaultMap2To8 = {0, 119, -120, -1};
    private static final byte[] defaultMap4To8 = {0, 17, 34, 51, 68, 85, 102, 119, -120, -103, -86, -69, -52, -35, -18, -1};
    private Bitmap bitmap;
    private final Canvas canvas;
    private final ClutDefinition defaultClutDefinition;
    private final DisplayDefinition defaultDisplayDefinition;
    private final Paint defaultPaint = new Paint();
    private final Paint fillRegionPaint;
    private final SubtitleService subtitleService;

    public DvbParser(int subtitlePageId, int ancillaryPageId) {
        this.defaultPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.defaultPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.defaultPaint.setPathEffect(null);
        this.fillRegionPaint = new Paint();
        this.fillRegionPaint.setStyle(Paint.Style.FILL);
        this.fillRegionPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        this.fillRegionPaint.setPathEffect(null);
        this.canvas = new Canvas();
        this.defaultDisplayDefinition = new DisplayDefinition(719, 575, 0, 719, 0, 575);
        this.defaultClutDefinition = new ClutDefinition(0, generateDefault2BitClutEntries(), generateDefault4BitClutEntries(), generateDefault8BitClutEntries());
        this.subtitleService = new SubtitleService(subtitlePageId, ancillaryPageId);
    }

    public void reset() {
        this.subtitleService.reset();
    }

    public List<Cue> decode(byte[] data, int limit) {
        int color;
        ParsableBitArray dataBitArray = new ParsableBitArray(data, limit);
        while (dataBitArray.bitsLeft() >= 48 && dataBitArray.readBits(8) == 15) {
            parseSubtitlingSegment(dataBitArray, this.subtitleService);
        }
        if (this.subtitleService.pageComposition == null) {
            return Collections.emptyList();
        }
        DisplayDefinition displayDefinition = this.subtitleService.displayDefinition != null ? this.subtitleService.displayDefinition : this.defaultDisplayDefinition;
        if (this.bitmap == null || displayDefinition.width + 1 != this.bitmap.getWidth() || displayDefinition.height + 1 != this.bitmap.getHeight()) {
            this.bitmap = Bitmap.createBitmap(displayDefinition.width + 1, displayDefinition.height + 1, Bitmap.Config.ARGB_8888);
            this.canvas.setBitmap(this.bitmap);
        }
        List<Cue> cues = new ArrayList<>();
        SparseArray<PageRegion> pageRegions = this.subtitleService.pageComposition.regions;
        for (int i = 0; i < pageRegions.size(); i++) {
            PageRegion pageRegion = pageRegions.valueAt(i);
            int regionId = pageRegions.keyAt(i);
            RegionComposition regionComposition = this.subtitleService.regions.get(regionId);
            int baseHorizontalAddress = pageRegion.horizontalAddress + displayDefinition.horizontalPositionMinimum;
            int baseVerticalAddress = pageRegion.verticalAddress + displayDefinition.verticalPositionMinimum;
            int clipRight = Math.min(regionComposition.width + baseHorizontalAddress, displayDefinition.horizontalPositionMaximum);
            int clipBottom = Math.min(regionComposition.height + baseVerticalAddress, displayDefinition.verticalPositionMaximum);
            this.canvas.clipRect(baseHorizontalAddress, baseVerticalAddress, clipRight, clipBottom, Region.Op.REPLACE);
            ClutDefinition clutDefinition = this.subtitleService.cluts.get(regionComposition.clutId);
            if (clutDefinition == null && (clutDefinition = this.subtitleService.ancillaryCluts.get(regionComposition.clutId)) == null) {
                clutDefinition = this.defaultClutDefinition;
            }
            SparseArray<RegionObject> regionObjects = regionComposition.regionObjects;
            for (int j = 0; j < regionObjects.size(); j++) {
                int objectId = regionObjects.keyAt(j);
                RegionObject regionObject = regionObjects.valueAt(j);
                ObjectData objectData = this.subtitleService.objects.get(objectId);
                if (objectData == null) {
                    objectData = this.subtitleService.ancillaryObjects.get(objectId);
                }
                if (objectData != null) {
                    Paint paint = objectData.nonModifyingColorFlag ? null : this.defaultPaint;
                    paintPixelDataSubBlocks(objectData, clutDefinition, regionComposition.depth, regionObject.horizontalPosition + baseHorizontalAddress, regionObject.verticalPosition + baseVerticalAddress, paint, this.canvas);
                }
            }
            if (regionComposition.fillFlag) {
                if (regionComposition.depth == 3) {
                    color = clutDefinition.clutEntries8Bit[regionComposition.pixelCode8Bit];
                } else if (regionComposition.depth == 2) {
                    color = clutDefinition.clutEntries4Bit[regionComposition.pixelCode4Bit];
                } else {
                    color = clutDefinition.clutEntries2Bit[regionComposition.pixelCode2Bit];
                }
                this.fillRegionPaint.setColor(color);
                this.canvas.drawRect(baseHorizontalAddress, baseVerticalAddress, regionComposition.width + baseHorizontalAddress, regionComposition.height + baseVerticalAddress, this.fillRegionPaint);
            }
            Bitmap cueBitmap = Bitmap.createBitmap(this.bitmap, baseHorizontalAddress, baseVerticalAddress, regionComposition.width, regionComposition.height);
            cues.add(new Cue(cueBitmap, baseHorizontalAddress / displayDefinition.width, 0, baseVerticalAddress / displayDefinition.height, 0, regionComposition.width / displayDefinition.width, regionComposition.height / displayDefinition.height));
            this.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
        return cues;
    }

    private static void parseSubtitlingSegment(ParsableBitArray data, SubtitleService service) {
        int segmentType = data.readBits(8);
        int pageId = data.readBits(16);
        int dataFieldLength = data.readBits(16);
        int dataFieldLimit = data.getBytePosition() + dataFieldLength;
        if (dataFieldLength * 8 > data.bitsLeft()) {
            Log.w(TAG, "Data field length exceeds limit");
            data.skipBits(data.bitsLeft());
            return;
        }
        switch (segmentType) {
            case 16:
                if (pageId == service.subtitlePageId) {
                    PageComposition current = service.pageComposition;
                    PageComposition pageComposition = parsePageComposition(data, dataFieldLength);
                    if (pageComposition.state != 0) {
                        service.pageComposition = pageComposition;
                        service.regions.clear();
                        service.cluts.clear();
                        service.objects.clear();
                        break;
                    } else if (current != null && current.version != pageComposition.version) {
                        service.pageComposition = pageComposition;
                        break;
                    }
                }
                break;
            case 17:
                PageComposition pageComposition2 = service.pageComposition;
                if (pageId == service.subtitlePageId && pageComposition2 != null) {
                    RegionComposition regionComposition = parseRegionComposition(data, dataFieldLength);
                    if (pageComposition2.state == 0) {
                        regionComposition.mergeFrom(service.regions.get(regionComposition.id));
                    }
                    service.regions.put(regionComposition.id, regionComposition);
                    break;
                }
                break;
            case 18:
                if (pageId == service.subtitlePageId) {
                    ClutDefinition clutDefinition = parseClutDefinition(data, dataFieldLength);
                    service.cluts.put(clutDefinition.id, clutDefinition);
                    break;
                } else if (pageId == service.ancillaryPageId) {
                    ClutDefinition clutDefinition2 = parseClutDefinition(data, dataFieldLength);
                    service.ancillaryCluts.put(clutDefinition2.id, clutDefinition2);
                    break;
                }
                break;
            case 19:
                if (pageId == service.subtitlePageId) {
                    ObjectData objectData = parseObjectData(data);
                    service.objects.put(objectData.id, objectData);
                    break;
                } else if (pageId == service.ancillaryPageId) {
                    ObjectData objectData2 = parseObjectData(data);
                    service.ancillaryObjects.put(objectData2.id, objectData2);
                    break;
                }
                break;
            case 20:
                if (pageId == service.subtitlePageId) {
                    service.displayDefinition = parseDisplayDefinition(data);
                    break;
                }
                break;
        }
        data.skipBytes(dataFieldLimit - data.getBytePosition());
    }

    private static DisplayDefinition parseDisplayDefinition(ParsableBitArray data) {
        int horizontalPositionMinimum;
        int horizontalPositionMaximum;
        int verticalPositionMinimum;
        int verticalPositionMaximum;
        data.skipBits(4);
        boolean displayWindowFlag = data.readBit();
        data.skipBits(3);
        int width = data.readBits(16);
        int height = data.readBits(16);
        if (displayWindowFlag) {
            horizontalPositionMinimum = data.readBits(16);
            horizontalPositionMaximum = data.readBits(16);
            verticalPositionMinimum = data.readBits(16);
            verticalPositionMaximum = data.readBits(16);
        } else {
            horizontalPositionMinimum = 0;
            horizontalPositionMaximum = width;
            verticalPositionMinimum = 0;
            verticalPositionMaximum = height;
        }
        return new DisplayDefinition(width, height, horizontalPositionMinimum, horizontalPositionMaximum, verticalPositionMinimum, verticalPositionMaximum);
    }

    private static PageComposition parsePageComposition(ParsableBitArray data, int length) {
        int timeoutSecs = data.readBits(8);
        int version = data.readBits(4);
        int state = data.readBits(2);
        data.skipBits(2);
        int remainingLength = length - 2;
        SparseArray<PageRegion> regions = new SparseArray<>();
        while (remainingLength > 0) {
            int regionId = data.readBits(8);
            data.skipBits(8);
            int regionHorizontalAddress = data.readBits(16);
            int regionVerticalAddress = data.readBits(16);
            remainingLength -= 6;
            regions.put(regionId, new PageRegion(regionHorizontalAddress, regionVerticalAddress));
        }
        return new PageComposition(timeoutSecs, version, state, regions);
    }

    private static RegionComposition parseRegionComposition(ParsableBitArray data, int length) {
        int id = data.readBits(8);
        data.skipBits(4);
        boolean fillFlag = data.readBit();
        data.skipBits(3);
        int width = data.readBits(16);
        int height = data.readBits(16);
        int levelOfCompatibility = data.readBits(3);
        int depth = data.readBits(3);
        data.skipBits(2);
        int clutId = data.readBits(8);
        int pixelCode8Bit = data.readBits(8);
        int pixelCode4Bit = data.readBits(4);
        int pixelCode2Bit = data.readBits(2);
        data.skipBits(2);
        int remainingLength = length - 10;
        SparseArray<RegionObject> regionObjects = new SparseArray<>();
        while (remainingLength > 0) {
            int objectId = data.readBits(16);
            int objectType = data.readBits(2);
            int objectProvider = data.readBits(2);
            int objectHorizontalPosition = data.readBits(12);
            data.skipBits(4);
            int objectVerticalPosition = data.readBits(12);
            remainingLength -= 6;
            int foregroundPixelCode = 0;
            int backgroundPixelCode = 0;
            if (objectType == 1 || objectType == 2) {
                foregroundPixelCode = data.readBits(8);
                backgroundPixelCode = data.readBits(8);
                remainingLength -= 2;
            }
            regionObjects.put(objectId, new RegionObject(objectType, objectProvider, objectHorizontalPosition, objectVerticalPosition, foregroundPixelCode, backgroundPixelCode));
        }
        return new RegionComposition(id, fillFlag, width, height, levelOfCompatibility, depth, clutId, pixelCode8Bit, pixelCode4Bit, pixelCode2Bit, regionObjects);
    }

    private static ClutDefinition parseClutDefinition(ParsableBitArray data, int length) {
        int[] clutEntries;
        int y;
        int cr;
        int cb;
        int t;
        int clutId = data.readBits(8);
        data.skipBits(8);
        int remainingLength = length - 2;
        int[] clutEntries2Bit = generateDefault2BitClutEntries();
        int[] clutEntries4Bit = generateDefault4BitClutEntries();
        int[] clutEntries8Bit = generateDefault8BitClutEntries();
        while (remainingLength > 0) {
            int entryId = data.readBits(8);
            int entryFlags = data.readBits(8);
            int remainingLength2 = remainingLength - 2;
            if ((entryFlags & 128) != 0) {
                clutEntries = clutEntries2Bit;
            } else if ((entryFlags & 64) != 0) {
                clutEntries = clutEntries4Bit;
            } else {
                clutEntries = clutEntries8Bit;
            }
            if ((entryFlags & 1) != 0) {
                y = data.readBits(8);
                cr = data.readBits(8);
                cb = data.readBits(8);
                t = data.readBits(8);
                remainingLength = remainingLength2 - 4;
            } else {
                y = data.readBits(6) << 2;
                cr = data.readBits(4) << 4;
                cb = data.readBits(4) << 4;
                t = data.readBits(2) << 6;
                remainingLength = remainingLength2 - 2;
            }
            if (y == 0) {
                cr = 0;
                cb = 0;
                t = 255;
            }
            int a2 = (byte) (255 - (t & 255));
            int r = (int) (y + (1.402d * (cr - 128)));
            int g = (int) ((y - (0.34414d * (cb - 128))) - (0.71414d * (cr - 128)));
            int b = (int) (y + (1.772d * (cb - 128)));
            clutEntries[entryId] = getColor(a2, Util.constrainValue(r, 0, 255), Util.constrainValue(g, 0, 255), Util.constrainValue(b, 0, 255));
        }
        return new ClutDefinition(clutId, clutEntries2Bit, clutEntries4Bit, clutEntries8Bit);
    }

    private static ObjectData parseObjectData(ParsableBitArray data) {
        int objectId = data.readBits(16);
        data.skipBits(4);
        int objectCodingMethod = data.readBits(2);
        boolean nonModifyingColorFlag = data.readBit();
        data.skipBits(1);
        byte[] topFieldData = null;
        byte[] bottomFieldData = null;
        if (objectCodingMethod == 1) {
            int numberOfCodes = data.readBits(8);
            data.skipBits(numberOfCodes * 16);
        } else if (objectCodingMethod == 0) {
            int topFieldDataLength = data.readBits(16);
            int bottomFieldDataLength = data.readBits(16);
            if (topFieldDataLength > 0) {
                topFieldData = new byte[topFieldDataLength];
                data.readBytes(topFieldData, 0, topFieldDataLength);
            }
            if (bottomFieldDataLength > 0) {
                bottomFieldData = new byte[bottomFieldDataLength];
                data.readBytes(bottomFieldData, 0, bottomFieldDataLength);
            } else {
                bottomFieldData = topFieldData;
            }
        }
        return new ObjectData(objectId, nonModifyingColorFlag, topFieldData, bottomFieldData);
    }

    private static int[] generateDefault2BitClutEntries() {
        int[] entries = {0, -1, ViewCompat.MEASURED_STATE_MASK, -8421505};
        return entries;
    }

    private static int[] generateDefault4BitClutEntries() {
        int[] entries = new int[16];
        entries[0] = 0;
        for (int i = 1; i < entries.length; i++) {
            if (i < 8) {
                entries[i] = getColor(255, (i & 1) != 0 ? 255 : 0, (i & 2) != 0 ? 255 : 0, (i & 4) != 0 ? 255 : 0);
            } else {
                entries[i] = getColor(255, (i & 1) != 0 ? 127 : 0, (i & 2) != 0 ? 127 : 0, (i & 4) != 0 ? 127 : 0);
            }
        }
        return entries;
    }

    private static int[] generateDefault8BitClutEntries() {
        int[] entries = new int[256];
        entries[0] = 0;
        for (int i = 0; i < entries.length; i++) {
            if (i < 8) {
                entries[i] = getColor(63, (i & 1) != 0 ? 255 : 0, (i & 2) != 0 ? 255 : 0, (i & 4) != 0 ? 255 : 0);
            } else {
                switch (i & 136) {
                    case 0:
                        entries[i] = getColor(255, ((i & 1) != 0 ? 85 : 0) + ((i & 16) != 0 ? 170 : 0), ((i & 2) != 0 ? 85 : 0) + ((i & 32) != 0 ? 170 : 0), ((i & 64) != 0 ? 170 : 0) + ((i & 4) != 0 ? 85 : 0));
                        break;
                    case 8:
                        entries[i] = getColor(127, ((i & 1) != 0 ? 85 : 0) + ((i & 16) != 0 ? 170 : 0), ((i & 2) != 0 ? 85 : 0) + ((i & 32) != 0 ? 170 : 0), ((i & 64) != 0 ? 170 : 0) + ((i & 4) != 0 ? 85 : 0));
                        break;
                    case 128:
                        entries[i] = getColor(255, ((i & 1) != 0 ? 43 : 0) + 127 + ((i & 16) != 0 ? 85 : 0), ((i & 2) != 0 ? 43 : 0) + 127 + ((i & 32) != 0 ? 85 : 0), ((i & 64) != 0 ? 85 : 0) + ((i & 4) != 0 ? 43 : 0) + 127);
                        break;
                    case 136:
                        entries[i] = getColor(255, ((i & 1) != 0 ? 43 : 0) + ((i & 16) != 0 ? 85 : 0), ((i & 2) != 0 ? 43 : 0) + ((i & 32) != 0 ? 85 : 0), ((i & 64) != 0 ? 85 : 0) + ((i & 4) != 0 ? 43 : 0));
                        break;
                }
            }
        }
        return entries;
    }

    private static int getColor(int a2, int r, int g, int b) {
        return (a2 << 24) | (r << 16) | (g << 8) | b;
    }

    private static void paintPixelDataSubBlocks(ObjectData objectData, ClutDefinition clutDefinition, int regionDepth, int horizontalAddress, int verticalAddress, Paint paint, Canvas canvas) {
        int[] clutEntries;
        if (regionDepth == 3) {
            clutEntries = clutDefinition.clutEntries8Bit;
        } else if (regionDepth == 2) {
            clutEntries = clutDefinition.clutEntries4Bit;
        } else {
            clutEntries = clutDefinition.clutEntries2Bit;
        }
        paintPixelDataSubBlock(objectData.topFieldData, clutEntries, regionDepth, horizontalAddress, verticalAddress, paint, canvas);
        paintPixelDataSubBlock(objectData.bottomFieldData, clutEntries, regionDepth, horizontalAddress, verticalAddress + 1, paint, canvas);
    }

    private static void paintPixelDataSubBlock(byte[] pixelData, int[] clutEntries, int regionDepth, int horizontalAddress, int verticalAddress, Paint paint, Canvas canvas) {
        byte[] clutMapTable4ToX;
        byte[] clutMapTable2ToX;
        ParsableBitArray data = new ParsableBitArray(pixelData);
        int column = horizontalAddress;
        int line = verticalAddress;
        byte[] clutMapTable2To4 = null;
        byte[] clutMapTable2To8 = null;
        while (data.bitsLeft() != 0) {
            int dataType = data.readBits(8);
            switch (dataType) {
                case 16:
                    if (regionDepth == 3) {
                        clutMapTable2ToX = clutMapTable2To8 == null ? defaultMap2To8 : clutMapTable2To8;
                    } else if (regionDepth == 2) {
                        clutMapTable2ToX = clutMapTable2To4 == null ? defaultMap2To4 : clutMapTable2To4;
                    } else {
                        clutMapTable2ToX = null;
                    }
                    column = paint2BitPixelCodeString(data, clutEntries, clutMapTable2ToX, column, line, paint, canvas);
                    data.byteAlign();
                    break;
                case 17:
                    if (regionDepth == 3) {
                        clutMapTable4ToX = 0 == 0 ? defaultMap4To8 : null;
                    } else {
                        clutMapTable4ToX = null;
                    }
                    column = paint4BitPixelCodeString(data, clutEntries, clutMapTable4ToX, column, line, paint, canvas);
                    data.byteAlign();
                    break;
                case 18:
                    column = paint8BitPixelCodeString(data, clutEntries, null, column, line, paint, canvas);
                    break;
                case 32:
                    clutMapTable2To4 = buildClutMapTable(4, 4, data);
                    break;
                case 33:
                    clutMapTable2To8 = buildClutMapTable(4, 8, data);
                    break;
                case 34:
                    clutMapTable2To8 = buildClutMapTable(16, 8, data);
                    break;
                case 240:
                    column = horizontalAddress;
                    line += 2;
                    break;
            }
        }
    }

    private static int paint2BitPixelCodeString(ParsableBitArray parsableBitArray, int[] iArr, byte[] bArr, int i, int i2, Paint paint, Canvas canvas) {
        boolean z = false;
        do {
            int bits = 0;
            int bits2 = 0;
            bits2 = 0;
            bits2 = 0;
            bits2 = 0;
            int bits3 = parsableBitArray.readBits(2);
            if (!parsableBitArray.readBit()) {
                bits = 1;
                bits2 = bits3;
            } else if (parsableBitArray.readBit()) {
                bits = parsableBitArray.readBits(3) + 3;
                bits2 = parsableBitArray.readBits(2);
            } else if (!parsableBitArray.readBit()) {
                switch (parsableBitArray.readBits(2)) {
                    case 0:
                        z = true;
                        break;
                    case 1:
                        bits = 2;
                        break;
                    case 2:
                        bits = parsableBitArray.readBits(4) + 12;
                        bits2 = parsableBitArray.readBits(2);
                        break;
                    case 3:
                        bits = parsableBitArray.readBits(8) + 29;
                        bits2 = parsableBitArray.readBits(2);
                        break;
                }
            }
            if (bits != 0 && paint != null) {
                if (bArr != null) {
                    bits2 = bArr[bits2];
                }
                paint.setColor(iArr[bits2]);
                canvas.drawRect(i, i2, i + bits, i2 + 1, paint);
            }
            i += bits;
        } while (!z);
        return i;
    }

    private static int paint4BitPixelCodeString(ParsableBitArray parsableBitArray, int[] iArr, byte[] bArr, int i, int i2, Paint paint, Canvas canvas) {
        boolean z = false;
        do {
            int bits = 0;
            int bits2 = 0;
            bits2 = 0;
            bits2 = 0;
            bits2 = 0;
            int bits3 = parsableBitArray.readBits(4);
            if (bits3 != 0) {
                bits = 1;
                bits2 = bits3;
            } else if (!parsableBitArray.readBit()) {
                int bits4 = parsableBitArray.readBits(3);
                if (bits4 != 0) {
                    bits = bits4 + 2;
                    bits2 = 0;
                } else {
                    z = true;
                }
            } else if (!parsableBitArray.readBit()) {
                bits = parsableBitArray.readBits(2) + 4;
                bits2 = parsableBitArray.readBits(4);
            } else {
                switch (parsableBitArray.readBits(2)) {
                    case 0:
                        bits = 1;
                        break;
                    case 1:
                        bits = 2;
                        break;
                    case 2:
                        bits = parsableBitArray.readBits(4) + 9;
                        bits2 = parsableBitArray.readBits(4);
                        break;
                    case 3:
                        bits = parsableBitArray.readBits(8) + 25;
                        bits2 = parsableBitArray.readBits(4);
                        break;
                }
            }
            if (bits != 0 && paint != null) {
                if (bArr != null) {
                    bits2 = bArr[bits2];
                }
                paint.setColor(iArr[bits2]);
                canvas.drawRect(i, i2, i + bits, i2 + 1, paint);
            }
            i += bits;
        } while (!z);
        return i;
    }

    private static int paint8BitPixelCodeString(ParsableBitArray data, int[] clutEntries, byte[] bArr, int column, int line, Paint paint, Canvas canvas) {
        boolean endOfPixelCodeString = false;
        do {
            int runLength = 0;
            int bits = 0;
            int peek = data.readBits(8);
            if (peek != 0) {
                runLength = 1;
                bits = peek;
            } else if (!data.readBit()) {
                int peek2 = data.readBits(7);
                if (peek2 != 0) {
                    runLength = peek2;
                    bits = 0;
                } else {
                    endOfPixelCodeString = true;
                }
            } else {
                runLength = data.readBits(7);
                bits = data.readBits(8);
            }
            if (runLength != 0 && paint != null) {
                if (bArr != null) {
                    bits = bArr[bits];
                }
                paint.setColor(clutEntries[bits]);
                canvas.drawRect(column, line, column + runLength, line + 1, paint);
            }
            column += runLength;
        } while (!endOfPixelCodeString);
        return column;
    }

    private static byte[] buildClutMapTable(int length, int bitsPerEntry, ParsableBitArray data) {
        byte[] clutMapTable = new byte[length];
        for (int i = 0; i < length; i++) {
            clutMapTable[i] = (byte) data.readBits(bitsPerEntry);
        }
        return clutMapTable;
    }

    private static final class SubtitleService {
        public final int ancillaryPageId;
        public DisplayDefinition displayDefinition;
        public PageComposition pageComposition;
        public final int subtitlePageId;
        public final SparseArray<RegionComposition> regions = new SparseArray<>();
        public final SparseArray<ClutDefinition> cluts = new SparseArray<>();
        public final SparseArray<ObjectData> objects = new SparseArray<>();
        public final SparseArray<ClutDefinition> ancillaryCluts = new SparseArray<>();
        public final SparseArray<ObjectData> ancillaryObjects = new SparseArray<>();

        public SubtitleService(int subtitlePageId, int ancillaryPageId) {
            this.subtitlePageId = subtitlePageId;
            this.ancillaryPageId = ancillaryPageId;
        }

        public void reset() {
            this.regions.clear();
            this.cluts.clear();
            this.objects.clear();
            this.ancillaryCluts.clear();
            this.ancillaryObjects.clear();
            this.displayDefinition = null;
            this.pageComposition = null;
        }
    }

    private static final class DisplayDefinition {
        public final int height;
        public final int horizontalPositionMaximum;
        public final int horizontalPositionMinimum;
        public final int verticalPositionMaximum;
        public final int verticalPositionMinimum;
        public final int width;

        public DisplayDefinition(int width, int height, int horizontalPositionMinimum, int horizontalPositionMaximum, int verticalPositionMinimum, int verticalPositionMaximum) {
            this.width = width;
            this.height = height;
            this.horizontalPositionMinimum = horizontalPositionMinimum;
            this.horizontalPositionMaximum = horizontalPositionMaximum;
            this.verticalPositionMinimum = verticalPositionMinimum;
            this.verticalPositionMaximum = verticalPositionMaximum;
        }
    }

    private static final class PageComposition {
        public final SparseArray<PageRegion> regions;
        public final int state;
        public final int timeOutSecs;
        public final int version;

        public PageComposition(int timeoutSecs, int version, int state, SparseArray<PageRegion> regions) {
            this.timeOutSecs = timeoutSecs;
            this.version = version;
            this.state = state;
            this.regions = regions;
        }
    }

    private static final class PageRegion {
        public final int horizontalAddress;
        public final int verticalAddress;

        public PageRegion(int horizontalAddress, int verticalAddress) {
            this.horizontalAddress = horizontalAddress;
            this.verticalAddress = verticalAddress;
        }
    }

    private static final class RegionComposition {
        public final int clutId;
        public final int depth;
        public final boolean fillFlag;
        public final int height;
        public final int id;
        public final int levelOfCompatibility;
        public final int pixelCode2Bit;
        public final int pixelCode4Bit;
        public final int pixelCode8Bit;
        public final SparseArray<RegionObject> regionObjects;
        public final int width;

        public RegionComposition(int id, boolean fillFlag, int width, int height, int levelOfCompatibility, int depth, int clutId, int pixelCode8Bit, int pixelCode4Bit, int pixelCode2Bit, SparseArray<RegionObject> regionObjects) {
            this.id = id;
            this.fillFlag = fillFlag;
            this.width = width;
            this.height = height;
            this.levelOfCompatibility = levelOfCompatibility;
            this.depth = depth;
            this.clutId = clutId;
            this.pixelCode8Bit = pixelCode8Bit;
            this.pixelCode4Bit = pixelCode4Bit;
            this.pixelCode2Bit = pixelCode2Bit;
            this.regionObjects = regionObjects;
        }

        public void mergeFrom(RegionComposition otherRegionComposition) {
            if (otherRegionComposition != null) {
                SparseArray<RegionObject> otherRegionObjects = otherRegionComposition.regionObjects;
                for (int i = 0; i < otherRegionObjects.size(); i++) {
                    this.regionObjects.put(otherRegionObjects.keyAt(i), otherRegionObjects.valueAt(i));
                }
            }
        }
    }

    private static final class RegionObject {
        public final int backgroundPixelCode;
        public final int foregroundPixelCode;
        public final int horizontalPosition;
        public final int provider;
        public final int type;
        public final int verticalPosition;

        public RegionObject(int type, int provider, int horizontalPosition, int verticalPosition, int foregroundPixelCode, int backgroundPixelCode) {
            this.type = type;
            this.provider = provider;
            this.horizontalPosition = horizontalPosition;
            this.verticalPosition = verticalPosition;
            this.foregroundPixelCode = foregroundPixelCode;
            this.backgroundPixelCode = backgroundPixelCode;
        }
    }

    private static final class ClutDefinition {
        public final int[] clutEntries2Bit;
        public final int[] clutEntries4Bit;
        public final int[] clutEntries8Bit;
        public final int id;

        public ClutDefinition(int id, int[] clutEntries2Bit, int[] clutEntries4Bit, int[] clutEntries8bit) {
            this.id = id;
            this.clutEntries2Bit = clutEntries2Bit;
            this.clutEntries4Bit = clutEntries4Bit;
            this.clutEntries8Bit = clutEntries8bit;
        }
    }

    private static final class ObjectData {
        public final byte[] bottomFieldData;
        public final int id;
        public final boolean nonModifyingColorFlag;
        public final byte[] topFieldData;

        public ObjectData(int id, boolean nonModifyingColorFlag, byte[] topFieldData, byte[] bottomFieldData) {
            this.id = id;
            this.nonModifyingColorFlag = nonModifyingColorFlag;
            this.topFieldData = topFieldData;
            this.bottomFieldData = bottomFieldData;
        }
    }
}
