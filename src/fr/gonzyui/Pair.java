package fr.gonzyui;

public class Pair<K, V> {
    private K key;

    private V value;

    private static final String OPEN_BRACE = "{";

    private static final String COMMA = ",";

    private static final String CLOSE_BRACE = "}";

    public Pair() {}

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Pair<K, V> setPair(K key, V value) {
        this.key = key;
        this.value = value;
        return this;
    }

    public K getKey() {
        return this.key;
    }

    public Pair<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    public V getValue() {
        return this.value;
    }

    public Pair<K, V> setValue(V value) {
        this.value = value;
        return this;
    }

    public String toString() {
        return "{" + this.key + "," + this.value + "}";
    }

    public String toTuple() {
        return "{" + this.key + "," + this.value + "}";
    }

    public static void main(String[] args) {
        System.out.println((new Pair()).setPair("Susan Boyle", Integer.valueOf(1)).toString());
        System.out.println((new Pair()).setKey("Susan Boyle").setValue(Integer.valueOf(1)).toString());
        System.out.println((new Pair("Susan Boyle", Integer.valueOf(1))).toString());
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return true;
    }

    public int hashCode() {
        return (this.key != null) ? this.key.hashCode() : 0;
    }
}
