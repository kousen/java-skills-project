
package com.oreilly.javaskills.oop.anotherpackage;

import com.oreilly.javaskills.oop.access.BaseClass;

@SuppressWarnings("unused")
public class DifferentPackageClass {
    public void accessMembers() {
        BaseClass base = new BaseClass();
        System.out.println("--- Inside DifferentPackageClass ---");
        System.out.println(base.publicVar);           // OK: public is accessible everywhere
        // System.out.println(base.protectedVar);    // ERROR: protected not accessible without subclassing
        // System.out.println(base.packagePrivateVar); // ERROR: package-private not accessible outside package
        // System.out.println(base.privateVar);       // ERROR: private is not accessible here
    }
}
