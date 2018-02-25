package pkunk.hoi4.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import pkunk.hoi4.psi.Hoi4NamedElement;

public abstract class Hoi4NamedElementImpl extends ASTWrapperPsiElement implements Hoi4NamedElement {

    public Hoi4NamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}
