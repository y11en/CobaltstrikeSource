package beacon.dns;

import common.CommonUtils;
import dns.DNSServer;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {
   protected Map checks = new HashMap();

   public boolean contains(String var1, String var2) {
      if (!CommonUtils.isNumber(var1)) {
         return true;
      } else {
         CacheManager.Entry var3 = (CacheManager.Entry)this.checks.get(var1);
         return var3 == null ? false : var3.items.containsKey(var2);
      }
   }

   public DNSServer.Response get(String var1, String var2) {
      if (!CommonUtils.isNumber(var1)) {
         return DNSServer.A(0L);
      } else {
         CacheManager.Entry var3 = (CacheManager.Entry)this.checks.get(var1);
         return (DNSServer.Response)var3.items.get(var2);
      }
   }

   public void add(String var1, String var2, DNSServer.Response var3) {
      CacheManager.Entry var4 = (CacheManager.Entry)this.checks.get(var1);
      if (var4 == null) {
         var4 = new CacheManager.Entry();
         this.checks.put(var1, var4);
      }

      var4.items.put(var2, var3);
   }

   public void purge(String var1) {
      CacheManager.Entry var2 = (CacheManager.Entry)this.checks.get(var1);
      if (var2 != null) {
         if (var2.txcount >= 15L) {
            this.checks.remove(var1);
         } else {
            ++var2.txcount;
         }

      }
   }

   private static class Entry {
      public Map items;
      public long txcount;

      private Entry() {
         this.items = new HashMap();
         this.txcount = 0L;
      }

      // $FF: synthetic method
      Entry(Object var1) {
         this();
      }
   }
}
