package de.koehntopp.java;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

// $ mvn clean package
// ...
// $ java -jar main/target/main-...jar
//
// Beispiel-Ausgabe:
// file:/Users/kris/IdeaProjects/JavaTests/./dependent/target/dependent-1.0-SNAPSHOT.jar
// file:/Users/kris/IdeaProjects/JavaTests/./external/target/external-1.0-SNAPSHOT.jar
// file:/Users/kris/IdeaProjects/JavaTests/./main/target/main-1.0-SNAPSHOT.jar
// Klasse wurde geladen von: file:/Users/kris/IdeaProjects/JavaTests/target/classes/
// New Hello from External!
// Hello from Dependent!

public class Main {
    public static void main(String[] args) throws Exception {
        // Start-Ordner
        File currentDir = new File(".");

        // Liste der jars, die von searchForJars() gefüllt wird
        List<URL> urls = new ArrayList<>();
        searchForJars(currentDir, urls);

        for (URL url: urls) {
            System.out.println(url);
        }

        // classLoader durchsucht die gefundenen jars in `urls`.
        URLClassLoader classLoader = new URLClassLoader(urls.toArray(new URL[0]), Main.class.getClassLoader());
        // Lade de.joehntopp.java.External
        Class<?> externalClass = Class.forName("de.koehntopp.java.External", true, classLoader);

        // Debug: Wir wollen drucken, von wo die Klasse geladen wurde.
        ProtectionDomain protectionDomain = externalClass.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URL location = (codeSource != null) ? codeSource.getLocation() : null;
        System.out.println("Klasse wurde geladen von: " + location);

        // Aufruf von External.main(String[] args)
        externalClass
                .getMethod("main", String[].class)
                .invoke(null, (Object) new String[]{});
    }

    private static void searchForJars(File dir, List<URL> urls) throws Exception {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchForJars(file, urls); // Rekursiver Aufruf für Unterverzeichnisse
                } else if (file.getName().endsWith(".jar")) {
                    urls.add(file.toURI().toURL());
                }
            }
        }
    }
}
