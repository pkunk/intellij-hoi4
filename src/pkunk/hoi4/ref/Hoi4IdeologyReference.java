package pkunk.hoi4.ref;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pkunk.hoi4.psi.*;

import java.util.*;
import java.util.stream.Collectors;

public class Hoi4IdeologyReference extends PsiReferenceBase<Hoi4Key> implements PsiPolyVariantReference {

    protected final String name;

    public Hoi4IdeologyReference(@NotNull Hoi4Key key) {
        super(key, key.getTextRange().shiftLeft(key.getTextRange().getStartOffset()));
        this.name = key.getText();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        List<ResolveResult> results = new ArrayList<>();
        PsiFile elementFile = getElement().getContainingFile();
        Project project = elementFile.getProject();
        VirtualFile[] roots = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for (VirtualFile root : roots) {
            for (VirtualFile topLevelDir : root.getChildren()) {
                if ("common".equals(topLevelDir.getName())) {
                    for (VirtualFile commonDir : topLevelDir.getChildren()) {
                        if ("ideologies".equals(commonDir.getName())) {
                            for (VirtualFile files : commonDir.getChildren()) {
                                PsiFile file = PsiManager.getInstance(project).findFile(files);
                                if (file != null && file instanceof Hoi4File) {
                                    results.addAll(findInFile(file));
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return results.toArray(new ResolveResult[results.size()]);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        return element.getText() != null && element.getText().equals(getElement().getText());
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
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
                .map(Hoi4SetBlock::getKey)
                .filter(Objects::nonNull)
                .filter(key -> Objects.equals(name, key.getText()))
                .map(PsiElementResolveResult::new)
                .collect(Collectors.toList());
    }
}
