package pkunk.hoi4;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pkunk.hoi4.psi.Hoi4Block;
import pkunk.hoi4.psi.Hoi4SetBlock;
import pkunk.hoi4.psi.Hoi4Types;

public class Hoi4PairedBraceMatcher implements PairedBraceMatcher {

    private final BracePair[] pairs = new BracePair[] {
            new BracePair(Hoi4Types.LBRACE, Hoi4Types.RBRACE, true)
    };

    @NotNull
    @Override
    public BracePair[] getPairs() {
        return pairs;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        PsiElement element = file.findElementAt(openingBraceOffset);
        if (element == null || element instanceof PsiFile) return openingBraceOffset;
        PsiElement parent = element.getParent();
        if (parent instanceof Hoi4Block) {
            parent = parent.getParent();
            if (parent instanceof Hoi4SetBlock) {
                return parent.getTextRange().getStartOffset();
            }
        }
        return openingBraceOffset;
    }
}
