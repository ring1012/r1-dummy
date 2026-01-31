package rx.internal.util;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LinkedArrayList {
    final int capacityHint;
    Object[] head;
    int indexInTail;
    volatile int size;
    Object[] tail;

    public LinkedArrayList(int capacityHint) {
        this.capacityHint = capacityHint;
    }

    public void add(Object o) {
        if (this.size == 0) {
            this.head = new Object[this.capacityHint + 1];
            this.tail = this.head;
            this.head[0] = o;
            this.indexInTail = 1;
            this.size = 1;
            return;
        }
        if (this.indexInTail == this.capacityHint) {
            Object[] t = new Object[this.capacityHint + 1];
            t[0] = o;
            this.tail[this.capacityHint] = t;
            this.tail = t;
            this.indexInTail = 1;
            this.size++;
            return;
        }
        this.tail[this.indexInTail] = o;
        this.indexInTail++;
        this.size++;
    }

    public Object[] head() {
        return this.head;
    }

    public Object[] tail() {
        return this.tail;
    }

    public int size() {
        return this.size;
    }

    public int indexInTail() {
        return this.indexInTail;
    }

    public int capacityHint() {
        return this.capacityHint;
    }

    List<Object> toList() {
        int cap = this.capacityHint;
        int s = this.size;
        List<Object> list = new ArrayList<>(s + 1);
        Object[] h = head();
        int j = 0;
        int k = 0;
        while (j < s) {
            list.add(h[k]);
            j++;
            k++;
            if (k == cap) {
                k = 0;
                h = (Object[]) h[cap];
            }
        }
        return list;
    }

    public String toString() {
        return toList().toString();
    }
}
