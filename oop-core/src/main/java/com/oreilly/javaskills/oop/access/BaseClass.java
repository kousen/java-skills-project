
package com.oreilly.javaskills.oop.access;

@SuppressWarnings({"FieldCanBeLocal", "unused", "FieldMayBeFinal"})
public class BaseClass {
    // Accessible from anywhere
    public String publicVar = "I am public";

    // Accessible only within this package and by subclasses
    protected String protectedVar = "I am protected";

    // Accessible only within this package (default/package-private)
    String packagePrivateVar = "I am package-private";

    // Accessible only within this class
    private String privateVar = "I am private";

    public void showVariables() {
        System.out.println("--- Inside BaseClass ---");
        System.out.println(publicVar);
        System.out.println(protectedVar);
        System.out.println(packagePrivateVar);
        System.out.println(privateVar); // All are accessible here
    }
}
