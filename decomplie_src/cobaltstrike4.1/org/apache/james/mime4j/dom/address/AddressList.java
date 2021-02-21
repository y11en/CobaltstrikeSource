package org.apache.james.mime4j.dom.address;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AddressList extends AbstractList<Address> implements Serializable {
   private static final long serialVersionUID = 1L;
   private final List<? extends Address> addresses;

   public AddressList(List<? extends Address> addresses, boolean dontCopy) {
      if (addresses != null) {
         this.addresses = (List)(dontCopy ? addresses : new ArrayList(addresses));
      } else {
         this.addresses = Collections.emptyList();
      }

   }

   public int size() {
      return this.addresses.size();
   }

   public Address get(int index) {
      return (Address)this.addresses.get(index);
   }

   public MailboxList flatten() {
      boolean groupDetected = false;
      Iterator i$ = this.addresses.iterator();

      while(i$.hasNext()) {
         Address addr = (Address)i$.next();
         if (!(addr instanceof Mailbox)) {
            groupDetected = true;
            break;
         }
      }

      if (!groupDetected) {
         List<Mailbox> mailboxes = this.addresses;
         return new MailboxList(mailboxes, true);
      } else {
         List<Mailbox> results = new ArrayList();
         Iterator i$ = this.addresses.iterator();

         while(i$.hasNext()) {
            Address addr = (Address)i$.next();
            addr.addMailboxesTo(results);
         }

         return new MailboxList(results, false);
      }
   }
}
