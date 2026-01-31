package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class DefaultAllocator implements Allocator {
    private static final int AVAILABLE_EXTRA_CAPACITY = 100;
    private int allocatedCount;
    private Allocation[] availableAllocations;
    private int availableCount;
    private final int individualAllocationSize;
    private final byte[] initialAllocationBlock;
    private final Allocation[] singleAllocationReleaseHolder;
    private int targetBufferSize;
    private final boolean trimOnReset;

    public DefaultAllocator(boolean trimOnReset, int individualAllocationSize) {
        this(trimOnReset, individualAllocationSize, 0);
    }

    public DefaultAllocator(boolean trimOnReset, int individualAllocationSize, int initialAllocationCount) {
        Assertions.checkArgument(individualAllocationSize > 0);
        Assertions.checkArgument(initialAllocationCount >= 0);
        this.trimOnReset = trimOnReset;
        this.individualAllocationSize = individualAllocationSize;
        this.availableCount = initialAllocationCount;
        this.availableAllocations = new Allocation[initialAllocationCount + 100];
        if (initialAllocationCount > 0) {
            this.initialAllocationBlock = new byte[initialAllocationCount * individualAllocationSize];
            for (int i = 0; i < initialAllocationCount; i++) {
                int allocationOffset = i * individualAllocationSize;
                this.availableAllocations[i] = new Allocation(this.initialAllocationBlock, allocationOffset);
            }
        } else {
            this.initialAllocationBlock = null;
        }
        this.singleAllocationReleaseHolder = new Allocation[1];
    }

    public synchronized void reset() {
        if (this.trimOnReset) {
            setTargetBufferSize(0);
        }
    }

    public synchronized void setTargetBufferSize(int targetBufferSize) {
        boolean targetBufferSizeReduced = targetBufferSize < this.targetBufferSize;
        this.targetBufferSize = targetBufferSize;
        if (targetBufferSizeReduced) {
            trim();
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Allocator
    public synchronized Allocation allocate() {
        Allocation allocation;
        this.allocatedCount++;
        if (this.availableCount > 0) {
            Allocation[] allocationArr = this.availableAllocations;
            int i = this.availableCount - 1;
            this.availableCount = i;
            allocation = allocationArr[i];
            this.availableAllocations[this.availableCount] = null;
        } else {
            allocation = new Allocation(new byte[this.individualAllocationSize], 0);
        }
        return allocation;
    }

    @Override // com.google.android.exoplayer2.upstream.Allocator
    public synchronized void release(Allocation allocation) {
        this.singleAllocationReleaseHolder[0] = allocation;
        release(this.singleAllocationReleaseHolder);
    }

    @Override // com.google.android.exoplayer2.upstream.Allocator
    public synchronized void release(Allocation[] allocations) {
        if (this.availableCount + allocations.length >= this.availableAllocations.length) {
            this.availableAllocations = (Allocation[]) Arrays.copyOf(this.availableAllocations, Math.max(this.availableAllocations.length * 2, this.availableCount + allocations.length));
        }
        for (Allocation allocation : allocations) {
            Assertions.checkArgument(allocation.data == this.initialAllocationBlock || allocation.data.length == this.individualAllocationSize);
            Allocation[] allocationArr = this.availableAllocations;
            int i = this.availableCount;
            this.availableCount = i + 1;
            allocationArr[i] = allocation;
        }
        this.allocatedCount -= allocations.length;
        notifyAll();
    }

    @Override // com.google.android.exoplayer2.upstream.Allocator
    public synchronized void trim() {
        int lowIndex;
        int highIndex;
        int targetAllocationCount = Util.ceilDivide(this.targetBufferSize, this.individualAllocationSize);
        int targetAvailableCount = Math.max(0, targetAllocationCount - this.allocatedCount);
        if (targetAvailableCount < this.availableCount) {
            if (this.initialAllocationBlock != null) {
                int highIndex2 = this.availableCount - 1;
                int highIndex3 = highIndex2;
                int lowIndex2 = 0;
                while (lowIndex2 <= highIndex3) {
                    Allocation lowAllocation = this.availableAllocations[lowIndex2];
                    if (lowAllocation.data == this.initialAllocationBlock) {
                        lowIndex = lowIndex2 + 1;
                        highIndex = highIndex3;
                    } else {
                        Allocation highAllocation = this.availableAllocations[highIndex3];
                        if (highAllocation.data != this.initialAllocationBlock) {
                            highIndex = highIndex3 - 1;
                            lowIndex = lowIndex2;
                        } else {
                            lowIndex = lowIndex2 + 1;
                            this.availableAllocations[lowIndex2] = highAllocation;
                            highIndex = highIndex3 - 1;
                            this.availableAllocations[highIndex3] = lowAllocation;
                        }
                    }
                    highIndex3 = highIndex;
                    lowIndex2 = lowIndex;
                }
                targetAvailableCount = Math.max(targetAvailableCount, lowIndex2);
                if (targetAvailableCount < this.availableCount) {
                }
            }
            Arrays.fill(this.availableAllocations, targetAvailableCount, this.availableCount, (Object) null);
            this.availableCount = targetAvailableCount;
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Allocator
    public synchronized int getTotalBytesAllocated() {
        return this.allocatedCount * this.individualAllocationSize;
    }

    @Override // com.google.android.exoplayer2.upstream.Allocator
    public int getIndividualAllocationLength() {
        return this.individualAllocationSize;
    }
}
