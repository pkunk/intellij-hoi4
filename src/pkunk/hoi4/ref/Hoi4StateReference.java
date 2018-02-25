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

public class Hoi4StateReference extends PsiReferenceBase<Hoi4IntValue> implements PsiPolyVariantReference {

    private final String name;

    public Hoi4StateReference(@NotNull Hoi4IntValue state) {
        super(state, state.getTextRange().shiftLeft(state.getTextRange().getStartOffset()));
        name = state.getText();
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        List<ResolveResult> results = new ArrayList<>();
        PsiFile elementFile = getElement().getContainingFile();
        Project project = elementFile.getProject();
        VirtualFile[] roots = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for (VirtualFile root : roots) {
            for (VirtualFile common : root.getChildren()) {
                if ("history".equals(common.getName())) {
                    for (VirtualFile history_files : common.getChildren()) {
                        if ("states".equals(history_files.getName())) {
                            for (VirtualFile state_files : history_files.getChildren()) {
                                PsiFile file = PsiManager.getInstance(project).findFile(state_files);
                                if (file != null && file instanceof Hoi4File) {
                                    results.addAll(findInFile(file));
                                }
                            }
                        }
                    }
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
                .filter(sb -> sb.getKey() != null && "state".equals(sb.getKey().getText()))
                .map(Hoi4SetBlock::getBlock)
                .map(Hoi4Block::getStatementList)
                .flatMap(Collection::stream)
                .map(Hoi4Statement::getExpression)
                .filter(Objects::nonNull)
                .filter(ex -> ex.getKey() != null && "id".equals(ex.getKey().getText()))
                .map(Hoi4Expression::getValue)
                .map(Hoi4Value::getNumber)
                .filter(Objects::nonNull)
                .map(Hoi4Number::getIntValue)
                .filter(Objects::nonNull)
                .filter(state -> Objects.equals(name, state.getText()))
                .map(PsiElementResolveResult::new)
                .collect(Collectors.toList());
    }
}
