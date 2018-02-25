package pkunk.hoi4.ref;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pkunk.hoi4.psi.Hoi4Country;
import pkunk.hoi4.psi.Hoi4File;
import pkunk.hoi4.psi.Hoi4Statement;
import pkunk.hoi4.psi.Hoi4TagDeclaration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Hoi4TagReference extends PsiReferenceBase<Hoi4Country> implements PsiPolyVariantReference {

    private final String name;

    public Hoi4TagReference(@NotNull Hoi4Country country) {
        super(country, country.getTextRange().shiftLeft(country.getTextRange().getStartOffset()));
        name = country.getText();
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
                if ("common".equals(common.getName())) {
                    for (VirtualFile country_tags : common.getChildren()) {
                        if ("country_tags".equals(country_tags.getName())) {
                            for (VirtualFile countries : country_tags.getChildren()) {
                                PsiFile file = PsiManager.getInstance(project).findFile(countries);
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
                .map(Hoi4Statement::getTagDeclaration)
                .filter(Objects::nonNull)
                .map(Hoi4TagDeclaration::getCountry)
                .filter(country -> Objects.equals(name, country.getText()))
                .map(PsiElementResolveResult::new)
                .collect(Collectors.toList());
    }
}
