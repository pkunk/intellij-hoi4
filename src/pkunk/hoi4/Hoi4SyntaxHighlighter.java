package pkunk.hoi4;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import pkunk.hoi4.psi.Hoi4Types;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class Hoi4SyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey OPERATOR =
            createTextAttributesKey("HOI4_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey KEYWORD =
            createTextAttributesKey("HOI4_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey BRACE =
            createTextAttributesKey("HOI4_BRACE", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey KEY =
            createTextAttributesKey("HOI4_KEY", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);
    public static final TextAttributesKey TAG =
            createTextAttributesKey("HOI4_TAG", DefaultLanguageHighlighterColors.CONSTANT);
    public static final TextAttributesKey COMMENT =
            createTextAttributesKey("HOI4_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey STRING =
            createTextAttributesKey("HOI4_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey BAD_CHARACTER =
            createTextAttributesKey("HOI4_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[]{OPERATOR};
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] BRACE_KEYS = new TextAttributesKey[]{BRACE};
    private static final TextAttributesKey[] KEY_KEYS = new TextAttributesKey[]{KEY};
    private static final TextAttributesKey[] TAG_KEYS = new TextAttributesKey[]{TAG};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new Hoi4LexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType iElementType) {
        if (iElementType.equals(Hoi4Types.EQUAL)
                || iElementType.equals(Hoi4Types.LESS)
                || iElementType.equals(Hoi4Types.MORE)) {
            return OPERATOR_KEYS;
        } else if (iElementType.equals(Hoi4Types.LBRACE)
                || iElementType.equals(Hoi4Types.RBRACE)) {
            return BRACE_KEYS;
        } else if (iElementType.equals(Hoi4Types.YES)
                || iElementType.equals(Hoi4Types.NO)
                || iElementType.equals(Hoi4Types.NOT)
                || iElementType.equals(Hoi4Types.OR)
                || iElementType.equals(Hoi4Types.AND)
                || iElementType.equals(Hoi4Types.XOR)
                || iElementType.equals(Hoi4Types.IF)
                || iElementType.equals(Hoi4Types.ELSE)
                ) {
            return KEYWORD_KEYS;
        } else if (iElementType.equals(Hoi4Types.VARIABLE)) {
            return KEY_KEYS;
        } else if (iElementType.equals(Hoi4Types.TAG)) {
            return TAG_KEYS;
        } else if (iElementType.equals(Hoi4Types.COMMENT)) {
            return COMMENT_KEYS;
        } else if (iElementType.equals(Hoi4Types.STRING)) {
            return STRING_KEYS;
        } else if (iElementType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}
