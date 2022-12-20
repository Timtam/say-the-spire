import java.util.ArrayList;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.screens.compendium.PotionViewScreen;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import basemod.ReflectionHacks;
import sayTheSpire.localization.LocalizationContext;
import sayTheSpire.ui.elements.PotionElement;
import sayTheSpire.ui.positions.CategoryListPosition;
import sayTheSpire.Output;

public class PotionViewScreenPatch {

    public static PotionElement findPotionInList(PotionViewScreen screen, String listName) {
        ArrayList<AbstractPotion> list = (ArrayList<AbstractPotion>) ReflectionHacks.getPrivate(screen,
                PotionViewScreen.class, listName);
        if (list == null)
            return null;
        int listCount = list.size();
        for (int p = 0; p < listCount; p++) {
            AbstractPotion potion = list.get(p);
            if (potion.hb.justHovered) {
                String key;
                switch (listName) {
                case "commonPotions":
                    key = "common";
                    break;
                case "uncommonPotions":
                    key = "uncommon";
                    break;
                case "rarePotions":
                    key = "rare";
                    break;
                default:
                    key = "unknown";
                }
                CategoryListPosition position = null;
                LocalizationContext localization = Output.localization.getContext("ui.screens.PotionViewScreen");
                String localizedType = localization.localize("types." + key);
                if (localizedType == null)
                    localizedType = key;
                localization.put("category", localizedType);
                String category = localization.localize("categoryLabel");
                position = new CategoryListPosition(p, listCount, category);
                return new PotionElement(potion, PotionElement.PotionLocation.COMPENDIUM, position);
            }
        }
        return null;
    }

    public static PotionElement getJustHoveredPotion(PotionViewScreen screen) {
        PotionElement potion = findPotionInList(screen, "commonPotions");
        if (potion == null)
            potion = findPotionInList(screen, "uncommonPotions");
        if (potion == null)
            potion = findPotionInList(screen, "rarePotions");
        if (potion != null) {
            return potion;
        }
        return null;
    }

    @SpirePatch(clz = PotionViewScreen.class, method = "update")
    public static class UpdatePatch {

        public static void Postfix(PotionViewScreen __instance) {
            PotionElement potion = getJustHoveredPotion(__instance);
            if (potion != null) {
                Output.setUI(potion);
            }
        }
    }
}
