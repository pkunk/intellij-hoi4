package pkunk.hoi4;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class Hoi4FileType extends LanguageFileType {

    public static final Hoi4FileType INSTANCE = new Hoi4FileType();

    public Hoi4FileType() {
        super(Hoi4Language.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Hoi4 file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Hoi4 content file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "txt";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }
}
