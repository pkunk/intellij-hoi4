package pkunk.hoi4;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.xml.util.ColorIconCache;
import org.jetbrains.annotations.NotNull;
import pkunk.hoi4.psi.Hoi4Color;

import java.awt.*;
import java.util.Collection;

public class Hoi4LineMarkerProvider extends RelatedItemLineMarkerProvider {

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element,
                                            @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
        if (element instanceof Hoi4Color) {
            Color c = null;
            Hoi4Color hoi4Color = (Hoi4Color) element;
            if (hoi4Color.getRed() != null && hoi4Color.getGreen() != null && hoi4Color.getBlue() != null) {
                int r = Integer.valueOf(hoi4Color.getRed().getText());
                int g = Integer.valueOf(hoi4Color.getGreen().getText());
                int b = Integer.valueOf(hoi4Color.getBlue().getText());
                if (0 <= r && r < 256 && 0 <= g && g < 256 && 0 <= b && b < 256) {
                    //noinspection UseJBColor
                    c = new Color(r, g, b);
                }
            } else if (hoi4Color.getHue() != null && hoi4Color.getSaturation() != null && hoi4Color.getBrightness() != null) {
                //todo verify color space
                float h = hoi4Color.getHue().getNumber().getDouble().floatValue();
                float s = hoi4Color.getSaturation().getNumber().getDouble().floatValue();
                float v = hoi4Color.getBrightness().getNumber().getDouble().floatValue();
                if (0.0 <= h && h <= 1.0 && 0.0 <= s && s <= 1.0 && 0.0 <= v && v <= 1.0) {
                    c = Color.getHSBColor(h, s, v);
                }
            }
            if (c != null) {
                NavigationGutterIconBuilder<PsiElement> builder =
                        NavigationGutterIconBuilder.create(ColorIconCache.getIconCache().getIcon(c, 12))
                                .setTarget(element.getFirstChild());
                result.add(builder.createLineMarkerInfo(element));
            }
        }
    }
}
