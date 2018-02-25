package pkunk.hoi4.ref;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import org.jetbrains.annotations.NotNull;
import pkunk.hoi4.psi.Hoi4Block;
import pkunk.hoi4.psi.Hoi4Key;
import pkunk.hoi4.psi.Hoi4SetBlock;
import pkunk.hoi4.psi.Hoi4Statement;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Hoi4SubIdeologyReference extends Hoi4IdeologyReference {

    public Hoi4SubIdeologyReference(@NotNull Hoi4Key key) {
        super(key);
    }

    @NotNull
    protected List<ResolveResult> findInFile(@NotNull PsiFile containingFile) {
        PsiElement[] children = containingFile.getChildren();
        return Arrays.stream(children)
                .filter(psiElement -> psiElement instanceof Hoi4Statement)
                .map(psiElement -> (Hoi4Statement) psiElement)
                .map(Hoi4Statement::getSetBlock)
                .filter(Objects::nonNull)
                .filter(sb -> sb.getKey() != null && "ideologies".equals(sb.getKey().getText()))
                .map(Hoi4SetBlock::getBlock)
                .map(Hoi4Block::getStatementList)
                .flatMap(Collection::stream)
                .map(Hoi4Statement::getSetBlock)
                .filter(Objects::nonNull)
                .map(Hoi4SetBlock::getBlock)
                .map(Hoi4Block::getStatementList)
                .flatMap(Collection::stream)
                .map(Hoi4Statement::getSetBlock)
                .filter(Objects::nonNull)
                .filter(sb -> sb.getKey() != null && "types".equals(sb.getKey().getText()))
                .map(Hoi4SetBlock::getBlock)
                .map(Hoi4Block::getStatementList)
                .flatMap(Collection::stream)
                .map(Hoi4Statement::getSetBlock)
                .filter(Objects::nonNull)
                .map(Hoi4SetBlock::getKey)
                .filter(Objects::nonNull)
                .filter(key -> Objects.equals(name, key.getText()))
                .map(PsiElementResolveResult::new)
                .collect(Collectors.toList());
    }
}
