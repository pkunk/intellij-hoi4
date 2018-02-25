package pkunk.hoi4;

import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Hoi4CodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

    @NotNull
    @Override
    public Language getLanguage() {
        return Hoi4Language.INSTANCE;
    }

    @Nullable
    @Override
    public CommonCodeStyleSettings getDefaultCommonSettings() {
        CodeStyleSettings codeStyleSettings = new CodeStyleSettings();
        CommonCodeStyleSettings.IndentOptions indentOptions = codeStyleSettings.getIndentOptions();
        assert indentOptions != null;
        indentOptions.USE_TAB_CHARACTER = true;
        indentOptions.SMART_TABS = true;
        return codeStyleSettings;
    }

    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        return null;
    }
}
