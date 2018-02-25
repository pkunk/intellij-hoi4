package pkunk.hoi4.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import pkunk.hoi4.Hoi4FileType;
import pkunk.hoi4.Hoi4Language;

public class Hoi4File extends PsiFileBase {

    public Hoi4File(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, Hoi4Language.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return Hoi4FileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Hoi4 File";
    }
}
