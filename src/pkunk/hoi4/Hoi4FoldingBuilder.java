package pkunk.hoi4;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pkunk.hoi4.psi.Hoi4File;
import pkunk.hoi4.psi.Hoi4SetBlock;
import pkunk.hoi4.psi.Hoi4Statement;
import pkunk.hoi4.psi.impl.Hoi4PsiImplUtil;

import java.util.Arrays;
import java.util.Objects;

public class Hoi4FoldingBuilder extends FoldingBuilderEx {

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        if (root instanceof Hoi4File) {
            Hoi4File hoi4File = (Hoi4File) root;
            Hoi4Statement[] statements = PsiTreeUtil.getChildrenOfType(hoi4File, Hoi4Statement.class);
            if (statements != null) {
                return Arrays.stream(statements)
                        .map(Hoi4Statement::getSetBlock)
                        .filter(Objects::nonNull)
                        .map(b -> new FoldingDescriptor(b, b.getTextRange()))
                        .toArray(FoldingDescriptor[]::new);

            }
        }
        return new FoldingDescriptor[0];
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return false;
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode astNode) {
        PsiElement psi = astNode.getPsi();
        if (psi instanceof Hoi4SetBlock) {
            return Hoi4PsiImplUtil.getStringKey((Hoi4SetBlock) psi) + " = {...}";
        }
        return "...";
    }
}
