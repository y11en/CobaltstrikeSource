package aggressor;

import aggressor.dialogs.ConnectDialog;
import aggressor.ui.UseSynthetica;
import common.Authorization;
import common.License;
import common.Requirements;
import sleep.parser.ParserConfig;

import javax.swing.*;

public class Aggressor {
   public static final String VERSION = "4.1 (20200625) " + (License.isTrial() ? "Trial" : "Licensed");
   public static final String VERSION_SHORT = "4.1";
   public static MultiFrame frame = null;

   public static MultiFrame getFrame() {
      return frame;
   }

   public static void main(String[] var0) {
      JOptionPane.showMessageDialog(null,"hello world");
      ParserConfig.installEscapeConstant('c', "\u0003");
      ParserConfig.installEscapeConstant('U', "\u001f");
      ParserConfig.installEscapeConstant('o', "\u000f");
      (new UseSynthetica()).setup();
      Requirements.checkGUI();
      License.checkLicenseGUI(new Authorization());
      frame = new MultiFrame();
      (new ConnectDialog(frame)).show();
   }
}
