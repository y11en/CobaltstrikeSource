package org.apache.james.mime4j.field;

import java.util.HashMap;
import java.util.Map;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.dom.FieldParser;
import org.apache.james.mime4j.dom.field.ParsedField;
import org.apache.james.mime4j.stream.Field;

public class DelegatingFieldParser implements FieldParser<ParsedField> {
   private final FieldParser<? extends ParsedField> defaultParser;
   private final Map<String, FieldParser<? extends ParsedField>> parsers;

   public DelegatingFieldParser(FieldParser<? extends ParsedField> defaultParser) {
      this.defaultParser = defaultParser;
      this.parsers = new HashMap();
   }

   public void setFieldParser(String name, FieldParser<? extends ParsedField> parser) {
      this.parsers.put(name.toLowerCase(), parser);
   }

   public FieldParser<? extends ParsedField> getParser(String name) {
      FieldParser<? extends ParsedField> field = (FieldParser)this.parsers.get(name.toLowerCase());
      return field == null ? this.defaultParser : field;
   }

   public ParsedField parse(Field rawField, DecodeMonitor monitor) {
      FieldParser<? extends ParsedField> parser = this.getParser(rawField.getName());
      return parser.parse(rawField, monitor);
   }
}
