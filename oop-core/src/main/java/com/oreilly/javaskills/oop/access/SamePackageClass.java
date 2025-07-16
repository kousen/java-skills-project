
package com.oreilly.javaskills.oop.access;

public class SamePackageClass {
    public void accessMembers() {
        BaseClass base = new BaseClass();
        System.out.println("--- Inside SamePackageClass (Not a Subclass) ---");
        System.out.println(base.publicVar);       // OK: public is accessible everywhere
        System.out.println(base.protectedVar);    // OK: protected is accessible within the same package
        System.out.println(base.packagePrivateVar); // OK: accessible within the same package
        // System.out.println(base.privateVar);   // ERROR: private is not accessible here
    }
}
