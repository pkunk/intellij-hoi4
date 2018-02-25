package pkunk.hoi4;

import com.intellij.lexer.FlexAdapter;

public class Hoi4LexerAdapter extends FlexAdapter {
    public Hoi4LexerAdapter() {
        super(new _Hoi4Lexer(null));
    }
}
