package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes.dex */
public final class LinkedHashTreeMap<K, V> extends AbstractMap<K, V> implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final Comparator<Comparable> NATURAL_ORDER;
    Comparator<? super K> comparator;
    private LinkedHashTreeMap<K, V>.EntrySet entrySet;
    final Node<K, V> header;
    private LinkedHashTreeMap<K, V>.KeySet keySet;
    int modCount;
    int size;
    Node<K, V>[] table;
    int threshold;

    static {
        $assertionsDisabled = !LinkedHashTreeMap.class.desiredAssertionStatus();
        NATURAL_ORDER = new Comparator<Comparable>() { // from class: com.google.gson.internal.LinkedHashTreeMap.1
            @Override // java.util.Comparator
            public int compare(Comparable a2, Comparable b) {
                return a2.compareTo(b);
            }
        };
    }

    public LinkedHashTreeMap() {
        this(NATURAL_ORDER);
    }

    public LinkedHashTreeMap(Comparator<? super K> comparator) {
        this.size = 0;
        this.modCount = 0;
        this.comparator = comparator == null ? NATURAL_ORDER : comparator;
        this.header = new Node<>();
        this.table = new Node[16];
        this.threshold = (this.table.length / 2) + (this.table.length / 4);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object key) {
        Node<K, V> node = findByObject(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return findByObject(key) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        Node<K, V> created = find(key, true);
        V result = created.value;
        created.value = value;
        return result;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Arrays.fill(this.table, (Object) null);
        this.size = 0;
        this.modCount++;
        Node<K, V> header = this.header;
        Node<K, V> e = header.next;
        while (e != header) {
            Node<K, V> next = e.next;
            e.prev = null;
            e.next = null;
            e = next;
        }
        header.prev = header;
        header.next = header;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object key) {
        Node<K, V> node = removeInternalByKey(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    Node<K, V> find(K k, boolean z) {
        Node<K, V> node;
        Comparator<? super K> comparator = this.comparator;
        Node<K, V>[] nodeArr = this.table;
        int iSecondaryHash = secondaryHash(k.hashCode());
        int length = iSecondaryHash & (nodeArr.length - 1);
        Node<K, V> node2 = nodeArr[length];
        int iCompare = 0;
        if (node2 != null) {
            Comparable comparable = comparator == NATURAL_ORDER ? (Comparable) k : null;
            while (true) {
                if (comparable != null) {
                    iCompare = comparable.compareTo(node2.key);
                } else {
                    iCompare = comparator.compare(k, node2.key);
                }
                if (iCompare == 0) {
                    return node2;
                }
                Node<K, V> node3 = iCompare < 0 ? node2.left : node2.right;
                if (node3 == null) {
                    break;
                }
                node2 = node3;
            }
        }
        if (!z) {
            return null;
        }
        Node<K, V> node4 = this.header;
        if (node2 == null) {
            if (comparator == NATURAL_ORDER && !(k instanceof Comparable)) {
                throw new ClassCastException(k.getClass().getName() + " is not Comparable");
            }
            node = new Node<>(node2, k, iSecondaryHash, node4, node4.prev);
            nodeArr[length] = node;
        } else {
            node = new Node<>(node2, k, iSecondaryHash, node4, node4.prev);
            if (iCompare < 0) {
                node2.left = node;
            } else {
                node2.right = node;
            }
            rebalance(node2, true);
        }
        int i = this.size;
        this.size = i + 1;
        if (i > this.threshold) {
            doubleCapacity();
        }
        this.modCount++;
        return node;
    }

    /* JADX WARN: Multi-variable type inference failed */
    Node<K, V> findByObject(Object obj) {
        if (obj == 0) {
            return null;
        }
        try {
            return find(obj, false);
        } catch (ClassCastException e) {
            return null;
        }
    }

    Node<K, V> findByEntry(Map.Entry<?, ?> entry) {
        Node<K, V> mine = findByObject(entry.getKey());
        boolean valuesEqual = mine != null && equal(mine.value, entry.getValue());
        if (valuesEqual) {
            return mine;
        }
        return null;
    }

    private boolean equal(Object a2, Object b) {
        return a2 == b || (a2 != null && a2.equals(b));
    }

    private static int secondaryHash(int h) {
        int h2 = h ^ ((h >>> 20) ^ (h >>> 12));
        return ((h2 >>> 7) ^ h2) ^ (h2 >>> 4);
    }

    void removeInternal(Node<K, V> node, boolean unlink) {
        if (unlink) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = null;
            node.next = null;
        }
        Node<K, V> left = node.left;
        Node<K, V> right = node.right;
        Node<K, V> originalParent = node.parent;
        if (left != null && right != null) {
            Node<K, V> adjacent = left.height > right.height ? left.last() : right.first();
            removeInternal(adjacent, false);
            int leftHeight = 0;
            Node<K, V> left2 = node.left;
            if (left2 != null) {
                leftHeight = left2.height;
                adjacent.left = left2;
                left2.parent = adjacent;
                node.left = null;
            }
            int rightHeight = 0;
            Node<K, V> right2 = node.right;
            if (right2 != null) {
                rightHeight = right2.height;
                adjacent.right = right2;
                right2.parent = adjacent;
                node.right = null;
            }
            adjacent.height = Math.max(leftHeight, rightHeight) + 1;
            replaceInParent(node, adjacent);
            return;
        }
        if (left != null) {
            replaceInParent(node, left);
            node.left = null;
        } else if (right != null) {
            replaceInParent(node, right);
            node.right = null;
        } else {
            replaceInParent(node, null);
        }
        rebalance(originalParent, false);
        this.size--;
        this.modCount++;
    }

    Node<K, V> removeInternalByKey(Object key) {
        Node<K, V> node = findByObject(key);
        if (node != null) {
            removeInternal(node, true);
        }
        return node;
    }

    private void replaceInParent(Node<K, V> node, Node<K, V> replacement) {
        Node<K, V> parent = node.parent;
        node.parent = null;
        if (replacement != null) {
            replacement.parent = parent;
        }
        if (parent != null) {
            if (parent.left == node) {
                parent.left = replacement;
                return;
            } else {
                if (!$assertionsDisabled && parent.right != node) {
                    throw new AssertionError();
                }
                parent.right = replacement;
                return;
            }
        }
        int index = node.hash & (this.table.length - 1);
        this.table[index] = replacement;
    }

    private void rebalance(Node<K, V> unbalanced, boolean insert) {
        for (Node<K, V> node = unbalanced; node != null; node = node.parent) {
            Node<K, V> left = node.left;
            Node<K, V> right = node.right;
            int leftHeight = left != null ? left.height : 0;
            int rightHeight = right != null ? right.height : 0;
            int delta = leftHeight - rightHeight;
            if (delta == -2) {
                Node<K, V> rightLeft = right.left;
                Node<K, V> rightRight = right.right;
                int rightRightHeight = rightRight != null ? rightRight.height : 0;
                int rightLeftHeight = rightLeft != null ? rightLeft.height : 0;
                int rightDelta = rightLeftHeight - rightRightHeight;
                if (rightDelta == -1 || (rightDelta == 0 && !insert)) {
                    rotateLeft(node);
                } else {
                    if (!$assertionsDisabled && rightDelta != 1) {
                        throw new AssertionError();
                    }
                    rotateRight(right);
                    rotateLeft(node);
                }
                if (insert) {
                    return;
                }
            } else if (delta == 2) {
                Node<K, V> leftLeft = left.left;
                Node<K, V> leftRight = left.right;
                int leftRightHeight = leftRight != null ? leftRight.height : 0;
                int leftLeftHeight = leftLeft != null ? leftLeft.height : 0;
                int leftDelta = leftLeftHeight - leftRightHeight;
                if (leftDelta == 1 || (leftDelta == 0 && !insert)) {
                    rotateRight(node);
                } else {
                    if (!$assertionsDisabled && leftDelta != -1) {
                        throw new AssertionError();
                    }
                    rotateLeft(left);
                    rotateRight(node);
                }
                if (insert) {
                    return;
                }
            } else if (delta == 0) {
                node.height = leftHeight + 1;
                if (insert) {
                    return;
                }
            } else {
                if (!$assertionsDisabled && delta != -1 && delta != 1) {
                    throw new AssertionError();
                }
                node.height = Math.max(leftHeight, rightHeight) + 1;
                if (!insert) {
                    return;
                }
            }
        }
    }

    private void rotateLeft(Node<K, V> root) {
        Node<K, V> left = root.left;
        Node<K, V> pivot = root.right;
        Node<K, V> pivotLeft = pivot.left;
        Node<K, V> pivotRight = pivot.right;
        root.right = pivotLeft;
        if (pivotLeft != null) {
            pivotLeft.parent = root;
        }
        replaceInParent(root, pivot);
        pivot.left = root;
        root.parent = pivot;
        root.height = Math.max(left != null ? left.height : 0, pivotLeft != null ? pivotLeft.height : 0) + 1;
        pivot.height = Math.max(root.height, pivotRight != null ? pivotRight.height : 0) + 1;
    }

    private void rotateRight(Node<K, V> root) {
        Node<K, V> pivot = root.left;
        Node<K, V> right = root.right;
        Node<K, V> pivotLeft = pivot.left;
        Node<K, V> pivotRight = pivot.right;
        root.left = pivotRight;
        if (pivotRight != null) {
            pivotRight.parent = root;
        }
        replaceInParent(root, pivot);
        pivot.right = root;
        root.parent = pivot;
        root.height = Math.max(right != null ? right.height : 0, pivotRight != null ? pivotRight.height : 0) + 1;
        pivot.height = Math.max(root.height, pivotLeft != null ? pivotLeft.height : 0) + 1;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        LinkedHashTreeMap<K, V>.EntrySet result = this.entrySet;
        if (result != null) {
            return result;
        }
        LinkedHashTreeMap<K, V>.EntrySet result2 = new EntrySet();
        this.entrySet = result2;
        return result2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        LinkedHashTreeMap<K, V>.KeySet result = this.keySet;
        if (result != null) {
            return result;
        }
        LinkedHashTreeMap<K, V>.KeySet result2 = new KeySet();
        this.keySet = result2;
        return result2;
    }

    static final class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        int height;
        final K key;
        Node<K, V> left;
        Node<K, V> next;
        Node<K, V> parent;
        Node<K, V> prev;
        Node<K, V> right;
        V value;

        Node() {
            this.key = null;
            this.hash = -1;
            this.prev = this;
            this.next = this;
        }

        Node(Node<K, V> parent, K key, int hash, Node<K, V> next, Node<K, V> prev) {
            this.parent = parent;
            this.key = key;
            this.hash = hash;
            this.height = 1;
            this.next = next;
            this.prev = prev;
            prev.next = this;
            next.prev = this;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry other = (Map.Entry) o;
            if (this.key == null) {
                if (other.getKey() != null) {
                    return false;
                }
            } else if (!this.key.equals(other.getKey())) {
                return false;
            }
            if (this.value == null) {
                if (other.getValue() != null) {
                    return false;
                }
            } else if (!this.value.equals(other.getValue())) {
                return false;
            }
            return true;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value != null ? this.value.hashCode() : 0);
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public Node<K, V> first() {
            Node<K, V> node = this;
            Node<K, V> child = node.left;
            while (child != null) {
                node = child;
                child = node.left;
            }
            return node;
        }

        public Node<K, V> last() {
            Node<K, V> node = this;
            Node<K, V> child = node.right;
            while (child != null) {
                node = child;
                child = node.right;
            }
            return node;
        }
    }

    private void doubleCapacity() {
        this.table = doubleCapacity(this.table);
        this.threshold = (this.table.length / 2) + (this.table.length / 4);
    }

    static <K, V> Node<K, V>[] doubleCapacity(Node<K, V>[] oldTable) {
        int oldCapacity = oldTable.length;
        Node<K, V>[] newTable = new Node[oldCapacity * 2];
        AvlIterator<K, V> iterator = new AvlIterator<>();
        AvlBuilder<K, V> leftBuilder = new AvlBuilder<>();
        AvlBuilder<K, V> rightBuilder = new AvlBuilder<>();
        for (int i = 0; i < oldCapacity; i++) {
            Node<K, V> root = oldTable[i];
            if (root != null) {
                iterator.reset(root);
                int leftSize = 0;
                int rightSize = 0;
                while (true) {
                    Node<K, V> node = iterator.next();
                    if (node == null) {
                        break;
                    }
                    if ((node.hash & oldCapacity) == 0) {
                        leftSize++;
                    } else {
                        rightSize++;
                    }
                }
                leftBuilder.reset(leftSize);
                rightBuilder.reset(rightSize);
                iterator.reset(root);
                while (true) {
                    Node<K, V> node2 = iterator.next();
                    if (node2 == null) {
                        break;
                    }
                    if ((node2.hash & oldCapacity) == 0) {
                        leftBuilder.add(node2);
                    } else {
                        rightBuilder.add(node2);
                    }
                }
                newTable[i] = leftSize > 0 ? leftBuilder.root() : null;
                newTable[i + oldCapacity] = rightSize > 0 ? rightBuilder.root() : null;
            }
        }
        return newTable;
    }

    static class AvlIterator<K, V> {
        private Node<K, V> stackTop;

        AvlIterator() {
        }

        void reset(Node<K, V> root) {
            Node<K, V> stackTop = null;
            for (Node<K, V> n = root; n != null; n = n.left) {
                n.parent = stackTop;
                stackTop = n;
            }
            this.stackTop = stackTop;
        }

        public Node<K, V> next() {
            Node<K, V> stackTop = this.stackTop;
            if (stackTop == null) {
                return null;
            }
            Node<K, V> stackTop2 = stackTop.parent;
            stackTop.parent = null;
            for (Node<K, V> n = stackTop.right; n != null; n = n.left) {
                n.parent = stackTop2;
                stackTop2 = n;
            }
            this.stackTop = stackTop2;
            return stackTop;
        }
    }

    static final class AvlBuilder<K, V> {
        private int leavesSkipped;
        private int leavesToSkip;
        private int size;
        private Node<K, V> stack;

        AvlBuilder() {
        }

        void reset(int targetSize) {
            int treeCapacity = (Integer.highestOneBit(targetSize) * 2) - 1;
            this.leavesToSkip = treeCapacity - targetSize;
            this.size = 0;
            this.leavesSkipped = 0;
            this.stack = null;
        }

        void add(Node<K, V> node) {
            node.right = null;
            node.parent = null;
            node.left = null;
            node.height = 1;
            if (this.leavesToSkip > 0 && (this.size & 1) == 0) {
                this.size++;
                this.leavesToSkip--;
                this.leavesSkipped++;
            }
            node.parent = this.stack;
            this.stack = node;
            this.size++;
            if (this.leavesToSkip > 0 && (this.size & 1) == 0) {
                this.size++;
                this.leavesToSkip--;
                this.leavesSkipped++;
            }
            for (int scale = 4; (this.size & (scale - 1)) == scale - 1; scale *= 2) {
                if (this.leavesSkipped == 0) {
                    Node<K, V> right = this.stack;
                    Node<K, V> center = right.parent;
                    Node<K, V> left = center.parent;
                    center.parent = left.parent;
                    this.stack = center;
                    center.left = left;
                    center.right = right;
                    center.height = right.height + 1;
                    left.parent = center;
                    right.parent = center;
                } else if (this.leavesSkipped == 1) {
                    Node<K, V> right2 = this.stack;
                    Node<K, V> center2 = right2.parent;
                    this.stack = center2;
                    center2.right = right2;
                    center2.height = right2.height + 1;
                    right2.parent = center2;
                    this.leavesSkipped = 0;
                } else if (this.leavesSkipped == 2) {
                    this.leavesSkipped = 0;
                }
            }
        }

        Node<K, V> root() {
            Node<K, V> stackTop = this.stack;
            if (stackTop.parent != null) {
                throw new IllegalStateException();
            }
            return stackTop;
        }
    }

    private abstract class LinkedTreeMapIterator<T> implements Iterator<T> {
        int expectedModCount;
        Node<K, V> lastReturned = null;
        Node<K, V> next;

        LinkedTreeMapIterator() {
            this.next = LinkedHashTreeMap.this.header.next;
            this.expectedModCount = LinkedHashTreeMap.this.modCount;
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.next != LinkedHashTreeMap.this.header;
        }

        final Node<K, V> nextNode() {
            Node<K, V> e = this.next;
            if (e == LinkedHashTreeMap.this.header) {
                throw new NoSuchElementException();
            }
            if (LinkedHashTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.next = e.next;
            this.lastReturned = e;
            return e;
        }

        @Override // java.util.Iterator
        public final void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            LinkedHashTreeMap.this.removeInternal(this.lastReturned, true);
            this.lastReturned = null;
            this.expectedModCount = LinkedHashTreeMap.this.modCount;
        }
    }

    final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return LinkedHashTreeMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new LinkedHashTreeMap<K, V>.LinkedTreeMapIterator<Map.Entry<K, V>>() { // from class: com.google.gson.internal.LinkedHashTreeMap.EntrySet.1
                {
                    LinkedHashTreeMap linkedHashTreeMap = LinkedHashTreeMap.this;
                }

                @Override // java.util.Iterator
                public Map.Entry<K, V> next() {
                    return nextNode();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return (o instanceof Map.Entry) && LinkedHashTreeMap.this.findByEntry((Map.Entry) o) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            Node<K, V> node;
            if (!(o instanceof Map.Entry) || (node = LinkedHashTreeMap.this.findByEntry((Map.Entry) o)) == null) {
                return false;
            }
            LinkedHashTreeMap.this.removeInternal(node, true);
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            LinkedHashTreeMap.this.clear();
        }
    }

    final class KeySet extends AbstractSet<K> {
        KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return LinkedHashTreeMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new LinkedHashTreeMap<K, V>.LinkedTreeMapIterator<K>() { // from class: com.google.gson.internal.LinkedHashTreeMap.KeySet.1
                {
                    LinkedHashTreeMap linkedHashTreeMap = LinkedHashTreeMap.this;
                }

                @Override // java.util.Iterator
                public K next() {
                    return nextNode().key;
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return LinkedHashTreeMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object key) {
            return LinkedHashTreeMap.this.removeInternalByKey(key) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            LinkedHashTreeMap.this.clear();
        }
    }

    private Object writeReplace() throws ObjectStreamException {
        return new LinkedHashMap(this);
    }
}
