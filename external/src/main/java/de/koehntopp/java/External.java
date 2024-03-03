package de.koehntopp.java;

public class External {
    public static void main(String[] args) {
        System.out.println("New Hello from External!");
        Dependent.sayHello(); // Ruft die Methode aus der Dependent-Klasse auf
    }
}
