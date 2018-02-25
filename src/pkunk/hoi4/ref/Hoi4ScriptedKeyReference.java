package pkunk.hoi4.ref;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pkunk.hoi4.psi.Hoi4File;
import pkunk.hoi4.psi.Hoi4Key;
import pkunk.hoi4.psi.Hoi4SetBlock;
import pkunk.hoi4.psi.Hoi4Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Hoi4ScriptedKeyReference extends PsiReferenceBase<Hoi4Key> implements PsiPolyVariantReference {

    private final String name;
    private final String dirName;

    protected Hoi4ScriptedKeyReference(@NotNull Hoi4Key key, @NotNull String dirName) {
        super(key, key.getTextRange().shiftLeft(key.getTextRange().getStartOffset()));
        this.name = key.getText();
        this.dirName = dirName;
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
                        if (dirName.equals(commonDir.getName())) {
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
    private List<ResolveResult> findInFile(@NotNull PsiFile containingFile) {
        PsiElement[] children = containingFile.getChildren();
        return Arrays.stream(children)
                .filter(psiElement -> psiElement instanceof Hoi4Statement)
                .map(psiElement -> (Hoi4Statement) psiElement)
                .map(Hoi4Statement::getSetBlock)
                .filter(Objects::nonNull)
                .map(Hoi4SetBlock::getKey)
                .filter(Objects::nonNull)
                .filter(key -> Objects.equals(name, key.getText()))
                .map(PsiElementResolveResult::new)
                .collect(Collectors.toList());
    }
}
