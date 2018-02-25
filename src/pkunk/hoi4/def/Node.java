package pkunk.hoi4.def;

public class Node {

    private String id;
    private Type type;
    private Scope scope;
    protected boolean isCondition;
    protected boolean isAction;

    protected Node(String id, Type type, Scope scope) {
        this.id = id;
        this.type = type;
        this.scope = scope;
        this.isCondition = isCondition;
        this.isAction = isAction;
    }

    public static Node newCondition(String id, Scope scope, Type type) {
        Node node = new Node(id, type, scope);
        node.isCondition = true;
        return node;
    }

    public static BlockNode newBlockCondition(String id, Scope scope) {
        BlockNode node = new BlockNode(id, scope, Scope.NONE);
        node.isCondition = true;
        return node;
    }

    public static Node newAction(String id, Scope scope, Type type) {
        Node node = new Node(id, type, scope);
        node.isAction = true;
        return node;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Scope getScope() {
        return scope;
    }

    public boolean isCondition() {
        return isCondition;
    }

    public boolean isAction() {
        return isAction;
    }
}
