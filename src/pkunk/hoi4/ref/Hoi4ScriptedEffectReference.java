package pkunk.hoi4.ref;

import org.jetbrains.annotations.NotNull;
import pkunk.hoi4.psi.Hoi4Key;

public class Hoi4ScriptedEffectReference extends Hoi4ScriptedKeyReference {

    public Hoi4ScriptedEffectReference(@NotNull Hoi4Key key) {
        super(key, "scripted_effects");
    }
}
