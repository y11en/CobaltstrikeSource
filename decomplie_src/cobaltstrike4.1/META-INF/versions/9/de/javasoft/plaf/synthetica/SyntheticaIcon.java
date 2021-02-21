package de.javasoft.plaf.synthetica;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthIcon;

public abstract class SyntheticaIcon implements SynthIcon, Icon {
   public static int getIconWidth(Icon icon, SynthContext context) {
      if (icon == null) {
         return 0;
      } else {
         return icon instanceof SynthIcon ? ((SynthIcon)icon).getIconWidth(context) : icon.getIconWidth();
      }
   }

   public static int getIconHeight(Icon icon, SynthContext context) {
      if (icon == null) {
         return 0;
      } else {
         return icon instanceof SynthIcon ? ((SynthIcon)icon).getIconHeight(context) : icon.getIconHeight();
      }
   }

   public static void paintIcon(Icon icon, SynthContext context, Graphics g, int x, int y, int w, int h) {
      if (icon instanceof SynthIcon) {
         ((SynthIcon)icon).paintIcon(context, g, x, y, w, h);
      } else if (icon != null) {
         icon.paintIcon(context.getComponent(), g, x, y);
      }

   }

   public abstract int getIconHeight(SynthContext var1);

   public abstract int getIconWidth(SynthContext var1);

   public abstract void paintIcon(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6);

   public void paintIcon(Component c, Graphics g, int x, int y) {
      this.paintIcon((SynthContext)null, g, x, y, 0, 0);
   }

   public int getIconWidth() {
      return this.getIconWidth((SynthContext)null);
   }

   public int getIconHeight() {
      return this.getIconHeight((SynthContext)null);
   }
}
