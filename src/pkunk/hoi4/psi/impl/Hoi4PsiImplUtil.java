package pkunk.hoi4.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pkunk.hoi4.def.BlockNode;
import pkunk.hoi4.def.GlobalRegister;
import pkunk.hoi4.def.Scope;
import pkunk.hoi4.def.Type;
import pkunk.hoi4.psi.*;
import pkunk.hoi4.ref.*;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Hoi4PsiImplUtil {

    @NotNull
    public static Scope getCurrentScope(PsiElement psiElement) {
        Optional<Scope> scopeFromFs = getCurrentScopeFromFs(psiElement);
        if (!scopeFromFs.isPresent()) {
            // ignore all file file by default
            return Scope.ALL;
        }
        Hoi4SetBlock parent = Hoi4PsiImplUtil.getParentSetBlock(psiElement);
        if (parent == null) {
            return scopeFromFs.orElse(Scope.ALL);
        }
        if (parent.getStringKey().startsWith("event_target:")) {
            //todo extremely ugly hack, replace with proper support of "event_target"
            return Scope.ALL;
        }
        if (parent.getState() != null) {
            return Scope.STATE;
        } else if (parent.getCountry() != null) {
            return Scope.COUNTRY;
        }
        BlockNode parentNode = GlobalRegister.NODES.stream()
                .filter(n -> n instanceof BlockNode)
                .map(n -> (BlockNode)n)
                .filter(n -> n.getId().equalsIgnoreCase(parent.getStringKey()))
                .findFirst()
                .orElse(null);
        if (parentNode == null || parentNode.getToScope() == Scope.INHERITED) {
            return getCurrentScope(parent);
        }
        return parentNode.getToScope();
    }

    @NotNull
    private static Optional<Scope> getCurrentScopeFromFs(PsiElement psiElement) {
        Optional<Scope> result = Optional.empty();
        PsiFile elementFile = psiElement.getContainingFile();
        PsiDirectory parent = elementFile.getParent();
        Project project = elementFile.getProject();
        PsiManager psiManager = PsiManager.getInstance(project);
        if (parent != null && psiManager.isInProject(parent)) {
            switch (parent.getName()) {
                case "events": {
                    result = Optional.of(Scope.EVENTS);
                    break;
                }
                case "national_focus": {
                    parent = parent.getParentDirectory();
                    if (parent != null && psiManager.isInProject(parent) && "common".equals(parent.getName())) {
                        result = Optional.of(Scope.FOCUS_TREES);
                    }
                    break;
                }
            }
        }
        //todo add ContentSourceRoot validation?
        return result;
    }

    @NotNull
    public static Type getValueType(Hoi4Statement statement) {
        if (statement.getSetBlock() != null) {
            return Type.BLOCK;
        }
        if (statement.getExpression() != null) {
            Hoi4Value value = statement.getExpression().getValue();
            if (value.getKey() != null) {
                return Type.ID;
            }
            if (value.getNumber() != null) {
                Hoi4Number number = value.getNumber();
                if (number.getIntValue() != null) {
                    return Type.INTEGER;
                }
                if (number.getFloat() != null) {
                    return Type.FLOAT;
                }
            }
            if (value.getDateValue() != null) {
                return Type.DATE;
            }
            if (value.getBoolean() != null) {
                return Type.BOOLEAN;
            }
            if (value.getStringValue() != null) {
                return Type.STRING;
            }
            if (value.getArray() != null) {
                return Type.ARRAY;
            }
        }
        if (statement.getTagDeclaration() != null) {
            return Type.STRING;
        }
        if (statement.getRandomArray() != null) {
            return Type.BLOCK;
        }
        if (statement.getColor() != null) {
            return Type.COLOR;
        }
        assert false;
        return null;
    }

    @NotNull
    public static Double getDouble(Hoi4Number number) {
        if (number.getFloat() != null) {
            return Double.valueOf(number.getFloat().getText());
        }
        if (number.getIntValue() != null) {
            return Integer.valueOf(number.getIntValue().getText()).doubleValue();
        }
        assert false;
        return null;
    }

    @NotNull
    public static PsiElement getElementKey(Hoi4SetBlock block) {
        Hoi4Key key = block.getKey();
        if (key != null) {
            return key.getVariable();
        }
        Hoi4LOp lop = block.getLOp();
        if (lop != null) {
            return lop;
        }
        Hoi4State state = block.getState();
        if (state != null) {
            return state.getIntValue();
        }
        Hoi4Country country = block.getCountry();
        if (country != null) {
            return country.getTag();
        }
        Hoi4Bookmark bookmark = block.getBookmark();
        if (bookmark != null) {
            return bookmark.getDate();
        }
        PsiElement string = block.getString();
        if (string != null) {
            return string;
        }
        assert false;
        return null;
    }

    @NotNull
    public static String getStringKey(Hoi4SetBlock block) {
        String text = getElementKey(block).getText();
        if (text.length() > 1 && text.startsWith("\"")) {
            text = text.substring(1, text.length() - 1 );
        }
        return text;
    }

    @NotNull
    public static String getStringKey(Hoi4Expression expression) {
        Hoi4Key key = expression.getKey();
        if (key != null) {
            return key.getVariable().getText();
        }
        Hoi4XOp xop = expression.getXOp();
        if (xop != null) {
            return xop.getXor().getText().toUpperCase(Locale.ROOT);
        }
        assert false;
        return null;
    }

    @NotNull
    public static List<String> getChildStringKeys(Hoi4SetBlock block) {
        List<Hoi4Statement> statements = block.getBlock().getStatementList();
        return statements.stream()
                .map(s -> {
                    if (s.getExpression() != null) {
                        return getStringKey(s.getExpression());
                    } else if (s.getSetBlock() != null) {
                        return getStringKey(s.getSetBlock());
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @NotNull
    public static List<Pair<String, PsiElement>> getChildPairKeys(Hoi4SetBlock block) {
        List<Hoi4Statement> statements = block.getBlock().getStatementList();
        return statements.stream()
                .map(s -> {
                    if (s.getExpression() != null) {
                        Hoi4Expression expression = s.getExpression();
                        return Pair.create(getStringKey(expression), (PsiElement) expression);
                    } else if (s.getSetBlock() != null) {
                        Hoi4SetBlock setBlock = s.getSetBlock();
                        return Pair.create(getStringKey(setBlock), (PsiElement) setBlock);
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    public static Hoi4SetBlock getParentSetBlock(PsiElement psiElement) {
        PsiElement parent = psiElement.getParent();
        do {
            if (parent instanceof Hoi4SetBlock) {
                return (Hoi4SetBlock) parent;
            }
            if (parent instanceof Hoi4File) {
                return null;
            }
            parent = parent.getParent();
        } while (parent != null && !(parent instanceof PsiFile));
        return null;
    }

    @NotNull
    public static Hoi4TagReference getReference(Hoi4Country country) {
        return new Hoi4TagReference(country);
    }

    public static String getName(Hoi4Country country) {
        return country.getTag().getText();
    }

    public static Hoi4Country setName(Hoi4Country country, String newName) {
        // do nothing
        return country;
    }

    public static PsiElement getNameIdentifier(Hoi4Country country) {
        return country;
    }

    @Nullable
    public static Hoi4StateReference getReference(Hoi4IntValue intValue) {
        PsiElement parent = intValue.getParent();
        if (parent instanceof Hoi4State) {
            return new Hoi4StateReference(intValue);
        }
        if (parent instanceof Hoi4Number) {
            parent = parent.getParent();
            if (parent instanceof Hoi4Value) {
                parent = parent.getParent();
                if (parent instanceof Hoi4Expression) {
                    Hoi4Expression expression = (Hoi4Expression) parent;
                    String stringKey = expression.getStringKey();
                    switch (stringKey) {
                        case "state":
                        case "has_full_control_of_state":
                        case "controls_state":
                        case "owns_state":
                        case "add_state_core":
                        case "remove_state_core":
                        case "set_capital":
                        case "add_state_claim":
                        case "remove_state_claim":
                        case "set_state_owner":
                        case "set_state_controller":
                        case "transfer_state":
                        case "goto_state":
                            return new Hoi4StateReference(intValue);
                        case "id":
                            Hoi4SetBlock setBlock = expression.getParentSetBlock();
                            if (setBlock != null && "state".equals(setBlock.getStringKey())) {
                                return new Hoi4StateReference(intValue);
                            }
                            break;
                    }
                }
            }
        } else if (parent instanceof Hoi4Array) {
            parent = parent.getParent();
            if (parent instanceof Hoi4Value) {
                parent = parent.getParent();
                if (parent instanceof Hoi4Expression) {
                    Hoi4Expression expression = (Hoi4Expression) parent;
                    String stringKey = expression.getStringKey();
                    switch (stringKey) {
                        case "states":
                        case "prioritize":
                            return new Hoi4StateReference(intValue);
                    }
                }
            }
        }
        return null;
    }

    public static String getName(Hoi4IntValue intValue) {
        return intValue.getInteger().getText();
    }

    public static Hoi4IntValue setName(Hoi4IntValue intValue, String newName) {
        // do nothing
        return intValue;
    }

    public static PsiElement getNameIdentifier(Hoi4IntValue intValue) {
        return intValue.getInteger();
    }

    @Nullable
    public static PsiReferenceBase<Hoi4Key> getReference(Hoi4Key key) {
        PsiElement parent = key.getParent();
        if (parent instanceof Hoi4Expression) {
//            Scope scope = getCurrentScope(parent);
//            if (scope == Scope.COUNTRY) {
                Hoi4IdeologyReference ideologyReference = new Hoi4IdeologyReference(key);
                if (ideologyReference.multiResolve(false).length > 0) {
                    return ideologyReference;
                }
//            } else if (scope == Scope.STATE) {
                Hoi4BuildingReference buildingReference = new Hoi4BuildingReference(key);
                if (buildingReference.multiResolve(false).length > 0) {
                    return buildingReference;
                }
//            }
            Hoi4ScriptedTriggerReference triggerReference = new Hoi4ScriptedTriggerReference(key);
            if (triggerReference.multiResolve(false).length > 0) {
                return triggerReference;
            }
            Hoi4ScriptedEffectReference effectReference = new Hoi4ScriptedEffectReference(key);
            if (effectReference.multiResolve(false).length > 0) {
                return effectReference;
            }
        } else if (parent instanceof Hoi4Value) {
            parent = parent.getParent();
            if (parent instanceof Hoi4Expression) {
                Hoi4Expression expression = (Hoi4Expression) parent;
                String stringKey = expression.getStringKey();
                switch (stringKey) {
                    case "ideology":
                        Hoi4IdeologyReference ideologyReference = new Hoi4IdeologyReference(key);
                        if (ideologyReference.multiResolve(false).length > 0) {
                            return ideologyReference;
                        }
                        return new Hoi4SubIdeologyReference(key);
                    case "has_government":
                    case "ruling_party":
                        return new Hoi4IdeologyReference(key);
                    case "type": {
                        Hoi4SetBlock block = expression.getParentSetBlock();
                        if (block != null) {
                            switch (block.getStringKey()) {
                                case "set_building_level":
                                case "damage_building":
                                case "remove_building":
                                case "add_building_construction":
                                    return new Hoi4BuildingReference(key);
                            }
                        }
                        break;
                    }

                }
            }
        } else if (parent instanceof Hoi4SetBlock) {
            parent = parent.getParent();
            if (parent instanceof Hoi4Statement) {
                parent = parent.getParent();
                if (parent instanceof Hoi4File) {
                    PsiDirectory dir = ((Hoi4File) parent).getParent();
                    if (dir != null) {
                        switch (dir.getName()) {
                            case "scripted_triggers":
                                return new Hoi4ScriptedTriggerReference(key);
                            case "scripted_effects":
                                return new Hoi4ScriptedEffectReference(key);
                        }
                    }
                } else if (parent instanceof Hoi4Block) {
                    Hoi4IdeologyReference ideologyReference = new Hoi4IdeologyReference(key);
                    if (ideologyReference.multiResolve(false).length > 0) {
                        return ideologyReference;
                    }
                    Hoi4SubIdeologyReference subIdeologyReference = new Hoi4SubIdeologyReference(key);
                    if (subIdeologyReference.multiResolve(false).length > 0) {
                        return subIdeologyReference;
                    }
                    Hoi4BuildingReference buildingReference = new Hoi4BuildingReference(key);
                    if (buildingReference.multiResolve(false).length > 0) {
                        return buildingReference;
                    }
                }
            }
        }
        return null;
    }

    public static String getName(Hoi4Key key) {
        return key.getVariable().getText();
    }

    public static Hoi4Key setName(Hoi4Key key, String newName) {
        // do nothing
        return key;
    }

    public static PsiElement getNameIdentifier(Hoi4Key key) {
        return key.getVariable();
    }
}
