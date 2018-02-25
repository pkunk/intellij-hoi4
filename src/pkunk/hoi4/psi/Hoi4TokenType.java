package pkunk.hoi4.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import pkunk.hoi4.Hoi4Language;

public class Hoi4TokenType extends IElementType {

    public Hoi4TokenType(@NotNull String debugName) {
        super(debugName, Hoi4Language.INSTANCE);
    }

    @Override
    public String toString() {
        return "Hoi4TokenType." + super.toString();
    }
}
