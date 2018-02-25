package pkunk.hoi4;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pkunk.hoi4.def.BlockNode;
import pkunk.hoi4.def.GlobalRegister;
import pkunk.hoi4.def.Node;
import pkunk.hoi4.def.Scope;
import pkunk.hoi4.psi.*;
import pkunk.hoi4.ref.Hoi4StateReference;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Predicate.isEqual;

public class Hoi4Annotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof Hoi4SetBlock) {
            Hoi4SetBlock block = (Hoi4SetBlock) element;
            Scope scope = block.getCurrentScope();
            annotateBlock(block, holder, scope, Collections.emptyList());
        } else if (element instanceof Hoi4IntValue) {
            if (element.getReference() instanceof Hoi4StateReference) {
                Annotation a = holder.createInfoAnnotation(element, null);
                a.setTextAttributes(Hoi4SyntaxHighlighter.TAG);
            }
        } else if (element instanceof Hoi4Key) {
            PsiReference reference = element.getReference();
            if (reference != null) {
                Annotation a = holder.createInfoAnnotation(element, null);
                a.setTextAttributes(Hoi4SyntaxHighlighter.TAG);
                if (reference instanceof PsiPolyVariantReference) {
                    if (((PsiPolyVariantReference) reference).multiResolve(false).length == 0) {
                        holder.createErrorAnnotation(element, "Unknown reference \"" + element.getText() + "\"");
                    }
                }
            }
        } else if (element instanceof Hoi4BomMark) {
            Annotation a = holder.createInfoAnnotation(element, "File starts with BOM");
            a.setTextAttributes(DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE);
        }
    }

    private void annotateBlock(
            @NotNull Hoi4SetBlock block,
            @NotNull AnnotationHolder holder,
            @NotNull Scope scope,
            @NotNull List<Pair<String, PsiElement>> branchKeys
    ) {
        final String key = block.getStringKey();
        final PsiElement keyElement = block.getElementKey();
        List<Pair<String, PsiElement>> childPairKeys = block.getChildPairKeys();

        if ("limit".equals(key)) {
            Annotation a = holder.createInfoAnnotation(keyElement, null);
            a.setTextAttributes(Hoi4SyntaxHighlighter.KEYWORD);
        }

        if ("else".equals(key)) {
            Hoi4SetBlock parentBlock = block.getParentSetBlock();
            if (parentBlock != null) {
                parentBlock = block.getParentSetBlock();
                if (parentBlock != null) {
                    childPairKeys = childPairKeys.stream()
                            .filter(p -> !Objects.equals(p.first, "if"))
                            .filter(p -> !Objects.equals(p.first, "else"))
                            .filter(p -> !Objects.equals(p.first, "limit"))
                            .collect(Collectors.toList());
                    childPairKeys.addAll(branchKeys);
                    annotateBlock(parentBlock, holder, scope, childPairKeys);
                }
            }
            return;
        } else if ("if".equals(key)) {
            long limitCount = childPairKeys.stream().map(p -> p.first).filter(isEqual("limit")).count();
            if (limitCount < 1L) {
                holder.createErrorAnnotation(keyElement, "Missing mandatory field \"limit\"");
                return;
            }
            long elseCount = childPairKeys.stream().map(p -> p.first).filter(isEqual("else")).count();
            if (elseCount > 1L) {
                holder.createErrorAnnotation(keyElement, "Duplicated field \"else\"");
                return;
            }
            Hoi4SetBlock parentBlock = block.getParentSetBlock();
            if (parentBlock != null) {
                childPairKeys = childPairKeys.stream()
                        .filter(p -> !Objects.equals(p.first, "if"))
                        .filter(p -> !Objects.equals(p.first, "else"))
                        .filter(p -> !Objects.equals(p.first, "limit"))
                        .collect(Collectors.toList());
                childPairKeys.addAll(branchKeys);
                annotateBlock(parentBlock, holder, scope, childPairKeys);
            }
            return;
        }

        childPairKeys = childPairKeys.stream()
                .filter(p -> !Objects.equals(p.first, "if"))
                .filter(p -> !Objects.equals(p.first, "else"))
                .collect(Collectors.toList());
        childPairKeys.addAll(branchKeys);

        BlockNode blockNode =  GlobalRegister.NODES.stream()
                .filter(b -> b instanceof BlockNode)
                .map(b -> (BlockNode)b)
                .filter(b -> b.getScope() == Scope.ALL || b.getScope() == Scope.INHERITED || b.getScope() == scope)
                .filter(b -> key.equals(b.getId()))
                .findFirst()
                .orElse(null);
        if (blockNode == null) {
            return;
        }

        for (BlockNode.Child child : blockNode.getChildren()) {
            if (child.isMandatory()) {
                if (childPairKeys.stream().map(p -> p.first).noneMatch(isEqual(child.getKey()))) {
                    holder.createErrorAnnotation(keyElement, "Missing mandatory field \"" + child.getKey() + "\"");
                }
            } else if (!child.isMultiple()) {
                Set fields = childPairKeys.stream()
                        .map(p -> p.first)
                        .filter(isEqual(child.getKey()))
                        .collect(Collectors.toSet());
                if (fields.size() > 1) {
                    holder.createErrorAnnotation(keyElement, "Duplicated field \"" + child.getKey() + "\"");
                }
            }
        }
        Set<String> toRemove = blockNode.getChildren().stream().map(BlockNode.Child::getKey).collect(Collectors.toSet());
        childPairKeys.removeIf(k -> toRemove.contains(k.first));
        toRemove.clear();

        if (blockNode.hasConditions()) {
            Scope toScope = blockNode.getToScope() != Scope.INHERITED ? blockNode.getToScope() : scope;
            for (Pair<String, PsiElement> pair : childPairKeys) {
                Node conditionNode = getConditionNode(toScope, pair.first);
                if (conditionNode != null) {
                    toRemove.add(pair.first);
                }
            }
        }
        childPairKeys.removeIf(k -> toRemove.contains(k.first));
        toRemove.clear();

        if (blockNode.hasActions()) {
            for (Pair<String, PsiElement> pair : childPairKeys) {
                Node actionNode = GlobalRegister.ACTIONS.get(pair.first.toLowerCase(Locale.ROOT));
                if (actionNode != null) {
                    toRemove.add(pair.first);
                }
            }
        }
        childPairKeys.removeIf(k -> toRemove.contains(k.first));
        toRemove.clear();

        //kind of hack, eventually everything should have proper scope
        if (scope == Scope.ALL) {
            return;
        }

        for (Pair<String, PsiElement> pair : childPairKeys) {
            if (blockNode.hasConditions() || blockNode.hasActions()) {
                if (pair.second instanceof Hoi4SetBlock) {
                    Hoi4SetBlock setBlock = (Hoi4SetBlock) pair.getSecond();
                    if (setBlock.getState() != null || setBlock.getCountry() != null) {
                        continue;
                    }
                    if (setBlock.getStringKey().startsWith("event_target:")) {
                        //todo extremely ugly hack, replace with proper support of "event_target"
                        continue;
                    }
                } else if (pair.second instanceof Hoi4Expression) {
                    Hoi4Expression expression = (Hoi4Expression) pair.getSecond();
                    if (expression.getKey() != null && expression.getKey().getReference() != null) {
                        continue;
                    }
                }
            }
            holder.createWarningAnnotation(pair.second, "Unknown field: " + pair.first);
        }
    }

    @Nullable
    private Node getConditionNode(Scope scope, String nodeName) {
        if (scope == Scope.NONE) {
            return null;
        }
        Node conditionNode = GlobalRegister.CONDITIONS_MAP.getOrDefault(scope, Collections.emptyMap())
                .get(nodeName.toLowerCase(Locale.ROOT));
        if (conditionNode == null) {
            conditionNode = GlobalRegister.CONDITIONS_MAP.getOrDefault(Scope.INHERITED, Collections.emptyMap())
                    .get(nodeName.toLowerCase(Locale.ROOT));
        }
        if (conditionNode == null) {
            conditionNode = GlobalRegister.CONDITIONS_MAP.getOrDefault(Scope.ALL, Collections.emptyMap())
                    .get(nodeName.toLowerCase(Locale.ROOT));
        }
        /// add ALL support
        return conditionNode;
    }
}
