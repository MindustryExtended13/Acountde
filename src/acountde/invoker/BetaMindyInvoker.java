package acountde.invoker;

import arc.Core;
import arc.util.OS;
import mindustry.Vars;
import mindustry.mod.Mods;
import betamindy.BetaMindy;

public class BetaMindyInvoker {
    public static Mods.LoadedMod mod;

    public static void setHack(boolean b) {
        if(b) {
            BetaMindy.uwu = true;
        } else {
            BetaMindy.uwu =
                    OS.username.equals("sunny") ||
                    OS.username.equals("anuke") ||
                    OS.username.equals("feeli");
        }
    }

    public static void load() {
        mod = Vars.mods.getMod(BetaMindy.class);
        setHack(Core.settings.getBool("betamindy-hack"));
    }
}