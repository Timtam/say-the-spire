import java.util.ArrayList;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.neow.NeowUnlockScreen;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import sayTheSpire.Output;

public class NeowUnlockScreenPatch {

    public static void handleOpen(ArrayList<AbstractUnlock> bundle) {
        if (bundle.size() <= 0)
            return;
        switch (bundle.get(0).type) {
        case CARD:
            for (AbstractUnlock unlock : bundle) {
                Output.text(unlock.card.name, false);
            }
            break;
        case RELIC:
            for (AbstractUnlock unlock : bundle) {
                Output.text(unlock.relic.name, false);
            }
            break;
        case CHARACTER:
            Output.text(bundle.get(0).player.title, false);
            break;
        }
    }

    @SpirePatch(clz = NeowUnlockScreen.class, method = "open", paramtypez = { ArrayList.class, boolean.class })
    public static class OpenPatch {

        public static void postfix(NeowUnlockScreen __instance, ArrayList<AbstractUnlock> bundle, Boolean isVictory) {
            handleOpen(bundle);
        }
    }

    @SpirePatch(clz = NeowUnlockScreen.class, method = "reOpen")
    public static class ReOpenPatch {

        public static void postfix(NeowUnlockScreen __instance) {
            handleOpen(__instance.unlockBundle);
        }
    }
}
