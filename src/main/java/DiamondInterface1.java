@FunctionalInterface
public interface DiamondInterface1 {
    static void staticMethod() {
        //implentation ...
    }

    default void defaultMethod() {
       //implementation ...
    }

    String get();
}