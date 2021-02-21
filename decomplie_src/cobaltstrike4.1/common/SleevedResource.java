package common;

import dns.SleeveSecurity;

public class SleevedResource {
   private static SleevedResource singleton;
   private SleeveSecurity data;

   public static void Setup(byte[] array) {
      singleton = new SleevedResource(CommonUtils.readResource("resources/cobaltstrike.auth"));
   }

   public static byte[] readResource(String s) {
      return singleton._readResource(s);
   }

   private SleevedResource(byte[] array) {
      (this.data = new SleeveSecurity()).registerKey(array);
   }

   private byte[] _readResource(String s) {
      byte[] resource = CommonUtils.readResource(CommonUtils.strrep(s, "resources/", "sleeve/"));
      if (resource.length > 0) {
         System.currentTimeMillis();
         return this.data.decrypt(resource);
      } else {
         byte[] resource2 = CommonUtils.readResource(s);
         if (resource2.length == 0) {
            CommonUtils.print_error("Could not find sleeved resource: " + s + " [ERROR]");
         } else {
            CommonUtils.print_stat("Used internal resource: " + s);
         }

         return resource2;
      }
   }
}
