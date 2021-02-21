package org.apache.james.mime4j.dom;

import java.util.Iterator;
import java.util.List;
import org.apache.james.mime4j.stream.Field;

public interface Header extends Iterable<Field> {
   void addField(Field var1);

   List<Field> getFields();

   Field getField(String var1);

   List<Field> getFields(String var1);

   Iterator<Field> iterator();

   int removeFields(String var1);

   void setField(Field var1);
}
