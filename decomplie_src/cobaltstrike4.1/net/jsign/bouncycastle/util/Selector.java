package net.jsign.bouncycastle.util;

public interface Selector<T> extends Cloneable {
   boolean match(T var1);

   Object clone();
}
