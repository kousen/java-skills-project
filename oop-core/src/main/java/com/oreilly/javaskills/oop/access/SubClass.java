
package com.oreilly.javaskills.oop.access;

public class SubClass extends BaseClass {
    public void accessMembers() {
        System.out.println("--- Inside SubClass (Same Package) ---");
        System.out.println(publicVar);       // OK: public is accessible everywhere
        System.out.println(protectedVar);    // OK: protected is accessible to subclasses
        System.out.println(packagePrivateVar); // OK: accessible within the same package
        // System.out.println(privateVar);   // ERROR: private is not accessible here
    }
}
