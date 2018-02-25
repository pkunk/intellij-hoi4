package pkunk.hoi4.def;

public enum Type {
    ID,
    TAG,
    BOOLEAN,
    INTEGER,
    FLOAT,
    DATE,
    STRING,
    ARRAY,
    COLOR,
    BLOCK,
    ;

    @SuppressWarnings("RedundantIfStatement")
    public static boolean equals(Type t1, Type t2) {
        if (t1 == t2) {
            return true;
        }
        if (t1 == ID && t2 == TAG || t1 == TAG && t2 == ID) {
            return true;
        }
        return false;
    }
}
