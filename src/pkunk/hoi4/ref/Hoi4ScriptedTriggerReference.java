package pkunk.hoi4.ref;

import org.jetbrains.annotations.NotNull;
import pkunk.hoi4.psi.Hoi4Key;

public class Hoi4ScriptedTriggerReference extends Hoi4ScriptedKeyReference {

    public Hoi4ScriptedTriggerReference(@NotNull Hoi4Key key) {
        super(key, "scripted_triggers");
    }
}
