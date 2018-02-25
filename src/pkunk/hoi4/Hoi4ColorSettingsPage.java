package pkunk.hoi4;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class Hoi4ColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Operator", Hoi4SyntaxHighlighter.OPERATOR),
            new AttributesDescriptor("Keyword", Hoi4SyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("Id", Hoi4SyntaxHighlighter.KEY),
            new AttributesDescriptor("Tag", Hoi4SyntaxHighlighter.TAG),
            new AttributesDescriptor("Braces", Hoi4SyntaxHighlighter.BRACE),
            new AttributesDescriptor("String", Hoi4SyntaxHighlighter.STRING),
            new AttributesDescriptor("Commentary", Hoi4SyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Bad Character", Hoi4SyntaxHighlighter.BAD_CHARACTER),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new Hoi4SyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "###########################\n" +
                "# Test Events\n" +
                "###########################\n" +
                "\n" +
                "add_namespace = test\n" +
                "\n" +
                "# Test Event 1\n" +
                "country_event = {\n" +
                "\tid = test.1\n" +
                "\ttitle = test.1.t\n" +
                "\tdesc = test.1.d\n" +
                "\t\n" +
                "\ttrigger = {\n" +
                "\t\talways = no\n" +
                "\t\ttag = FRA\n" +
                "\t\tdate > 1936.2.1\n" +
                "\t}\n" +
                "\n" +
                "\tmean_time_to_happen = { days = 2 }\n" +
                "\n" +
                "\timmediate = {\n" +
                "\t\tset_province_name = { id = 3529 name=\"LOL_TEST\" }\n" +
                "\t}\n" +
                "\t\n" +
                "\toption = {\n" +
                "\t\tname = test.1.a\n" +
                "\t\t\n" +
                "\t\treset_province_name = 3529\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "# Test Event 2\n" +
                "country_event = {\n" +
                "\tid = test.2\n" +
                "\ttitle = test.2.t\n" +
                "\tdesc = test.2.d\n" +
                "\t\n" +
                "\tis_triggered_only = yes\n" +
                "\t\n" +
                "\timmediate = { FROM = { add_political_power = 100 } }\n" +
                "\n" +
                "\toption = {\n" +
                "\t\tname = test.2.a\n" +
                "\t\tFROM = { add_political_power = 100 }\n" +
                "\t\tevent_target:yalta_partner = {\n" +
                "\t\t\tadd_political_power = 100\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "# Test Event 3\n" +
                "country_event = {\n" +
                "\tid = test.3\n" +
                "\ttitle = test.3.t\n" +
                "\tdesc = test.3.d\n" +
                "\t\n" +
                "\tis_triggered_only = yes\n" +
                "\n" +
                "\timmediate = {\n" +
                "\t\tadd_autonomy_score = {\n" +
                "\t\t\tvalue = 10\n" +
                "\t\t\tlocalization = \"TEST_EVENT_3_IMMEDIATE: $VAL|+=2$\"\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\t\n" +
                "\toption = {\n" +
                "\t\tname = test.3.a\n" +
                "\t\tRAJ = {\n" +
                "\t\t\tadd_autonomy_score = {\n" +
                "\t\t\t\tvalue = 5\n" +
                "\t\t\t\tlocalization = \"TEST_EVENT_3_OPTION: $VAL|+=2$\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Hoi4";
    }
}
