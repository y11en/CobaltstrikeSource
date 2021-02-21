package net.jsign.bouncycastle.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CollectionStore<T> implements Store<T>, Iterable<T> {
   private Collection<T> _local;

   public CollectionStore(Collection<T> var1) {
      this._local = new ArrayList(var1);
   }

   public Collection<T> getMatches(Selector<T> var1) {
      if (var1 == null) {
         return new ArrayList(this._local);
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = this._local.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var1.match(var4)) {
               var2.add(var4);
            }
         }

         return var2;
      }
   }

   public Iterator<T> iterator() {
      return this.getMatches((Selector)null).iterator();
   }
}
