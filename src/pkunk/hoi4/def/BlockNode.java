package pkunk.hoi4.def;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class BlockNode extends Node {

    private Scope toScope;

    private boolean conditions;
    private boolean actions;

    private Set<Child> children = new LinkedHashSet<>();

    protected BlockNode(String id, Scope fromScope, Scope toScope) {
        super(id, Type.BLOCK, fromScope);
        this.toScope = toScope;
    }

    public static BlockNode newBlock(String id, Scope fromScope, Scope toScope) {
        return new BlockNode(id, fromScope, toScope);
    }


    public static BlockNode newConditionScope(String id, Scope fromScope, Scope toScope) {
        return new BlockNode(id, fromScope, toScope)
                .withConditions(true);
    }

    public static BlockNode newActionScope(String id, Scope fromScope, Scope toScope) {
        return new BlockNode(id, fromScope, toScope)
                .withActions(true)
                .withChild("limit", Type.BLOCK);
    }

    public static BlockNode newConditionActionScope(String id, Scope fromScope, Scope toScope) {
        return new BlockNode(id, fromScope, toScope)
                .withConditions(true)
                .withActions(true)
                .withChild("limit", Type.BLOCK);
    }

    public BlockNode withConditions(boolean conditions) {
        this.conditions = true;
        return this;
    }

    public BlockNode withActions(boolean actions) {
        this.actions = actions;
        return this;
    }

    public BlockNode withChild(String key, Type type) {
        Child c = new Child(key, type, false, false);
        this.children.remove(c);
        this.children.add(c);
        return this;
    }

    public BlockNode withMandatoryChild(String key, Type type) {
        Child c = new Child(key, type, true, false);
        this.children.remove(c);
        this.children.add(c);
        return this;
    }

    public BlockNode withMultiChild(String key, Type type) {
        Child c = new Child(key, type, false, true);
        this.children.remove(c);
        this.children.add(c);
        return this;
    }

    public BlockNode withMandatoryMultiChild(String key, Type type) {
        Child c = new Child(key, type, true, true);
        this.children.remove(c);
        this.children.add(c);
        return this;
    }

    public Scope getToScope() {
        return toScope;
    }

    public boolean hasConditions() {
        return conditions;
    }

    public boolean hasActions() {
        return actions;
    }

    public Set<Child> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public static class Child {
        private String key;
        private Type type;
        private boolean mandatory;
        private boolean multiple;
        private Child(String key, Type type, boolean mandatory, boolean multiple) {
            this.key = key;
            this.type = type;
            this.mandatory = mandatory;
            this.multiple = multiple;
        }

        public String getKey() {
            return key;
        }

        public Type getType() {
            return type;
        }

        public boolean isMandatory() {
            return mandatory;
        }

        public boolean isMultiple() {
            return multiple;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Child)) return false;
            Child child = (Child) o;
            return Objects.equals(key, child.key) &&
                    type == child.type;
        }

        @Override
        public int hashCode() {

            return Objects.hash(key, type);
        }
    }
}
