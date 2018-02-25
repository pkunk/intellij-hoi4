package pkunk.hoi4;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pkunk.hoi4.psi.Hoi4Country;
import pkunk.hoi4.psi.Hoi4IntValue;
import pkunk.hoi4.psi.Hoi4Key;
import pkunk.hoi4.psi.Hoi4Types;
import pkunk.hoi4.ref.*;

public class Hoi4FindUsagesProvider implements FindUsagesProvider {

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(new Hoi4LexerAdapter(),
                TokenSet.create(Hoi4Types.COUNTRY, Hoi4Types.INT_VALUE, Hoi4Types.KEY),
                TokenSet.create(Hoi4Types.COMMENT),
                TokenSet.EMPTY);
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement && psiElement.getReference() != null;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof Hoi4Country) {
            return "tag";
        } else if (element instanceof Hoi4IntValue && element.getReference() instanceof Hoi4StateReference) {
            return "state";
        } else if (element instanceof Hoi4Key && element.getReference() instanceof Hoi4IdeologyReference) {
            return "ideology";
        } else if (element instanceof Hoi4Key && element.getReference() instanceof Hoi4BuildingReference) {
            return "building";
        } else if (element instanceof Hoi4Key && element.getReference() instanceof Hoi4ScriptedTriggerReference) {
            return "scripted_trigger";
        } else if (element instanceof Hoi4Key && element.getReference() instanceof Hoi4ScriptedEffectReference) {
            return "scripted_effect";
        } else {
            return "";
        }
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof Hoi4Country) {
            return getNodeText(element, true);
        } else if (element instanceof Hoi4IntValue && element.getReference() instanceof Hoi4StateReference) {
            return getNodeText(element, true);
        } else if (element instanceof Hoi4Key && element.getReference() != null) {
            return getNodeText(element, true);
        } else {
            return "";
        }
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof Hoi4Country) {
            if (useFullName) {
                PsiElement refElement = ((Hoi4Country) element).getReference().resolve();
                if (refElement != null) {
                    if (refElement.getParent() != null) {
                        return refElement.getParent().getText();
                    }
                }
            }
            return element.getText();
        } else if (element instanceof Hoi4IntValue && element.getReference() instanceof Hoi4StateReference) {
            if (useFullName) {
                Hoi4StateReference reference = ((Hoi4IntValue) element).getReference();
                if (reference != null) {
                    PsiElement refElement = reference.resolve();
                    if (refElement != null) {
                        PsiFile containingFile = refElement.getContainingFile();
                        if (containingFile != null) {
                            return containingFile.getName();
                        }
                    }
                }
            }
            return element.getText();
        } else if (element instanceof Hoi4Key && element.getReference() != null) {
            return element.getText();
        } else {
            return "";
        }
    }
}
