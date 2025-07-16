
package com.oreilly.javaskills.oop.anotherpackage;

import com.oreilly.javaskills.oop.access.BaseClass;

public class DifferentPackageSubClass extends BaseClass {
    public void accessMembers() {
        System.out.println("--- Inside DifferentPackageSubClass ---");
        System.out.println(publicVar);       // OK: public is accessible everywhere
        System.out.println(protectedVar);    // OK: protected is accessible to subclasses in other packages
        // System.out.println(packagePrivateVar); // ERROR: package-private not accessible outside package
        // System.out.println(privateVar);   // ERROR: private is not accessible here
    }
}
