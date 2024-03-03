# Build

$ mvn clean package
...

# Usage

```
$ java -jar main/target/main-1.0-SNAPSHOT.jar
file:.../dependent/target/dependent-1.0-SNAPSHOT.jar
file:.../external/target/external-1.0-SNAPSHOT.jar
file:.../main/target/main-1.0-SNAPSHOT.jar
Klasse wurde geladen von: file:/Users/kris/IdeaProjects/JavaTests/./external/target/external-1.0-SNAPSHOT.jar
New Hello from External!
Hello from Dependent!
```

Wir bauen uns eine Liste aller `jar`-Dateien im aktuellen Verzeichnis oder darunter.
Wir bauen uns einen `classLoader` und laden die Klasse `de.koehntopp.java.External` aus irgendeinem dieser Jars.
Wir rufen dann auf dieser Klasse `public static main(String[] args) mit leerer Argumentliste auf,
indem wir Reflection und `.invoke()` verwenden.

Die External-Klasse hat eine deklarierte Dependency auf `de.koehnntopp.java.Dependent`.
Wir wurden dort statisch `Dependent.sayHello()` auf.

Wir könnten den Stunt mit dem `classLoader` und der `.invoke()`-Methode wiederholen.
Weil wir das nicht tun, müssen wir die Depencency zu `Dependent` in der `pom.xml` von `External` deklarieren.

# Maven

## /pom.xml

Das Top-Level `pom.xml` deklariert die drei Untermodule,

```
    <modules>
        <module>main</module>
        <module>external</module>
        <module>dependent</module>
    </modules>
```

Es muß auch `<packaging>pom</packaging>` mit deklarieren, damit das funktioniert.

## main/pom.xml

Das `main/pom.xml` will einen `Main-Class: de.koehntopp.java.Main` Eintrag in die `MANIFEST.MF` haben. 
Für diese eine Zeile muß der ganze Quatsch in `<build/>` deklariert werden.

## external/pom.xml

Das `external/pom.xml` will eine Dependency auf `depdendent` deklarieren.
Dazu muß dort der `<dependencies><depdendency /></dependencies>`-Block erstellt werden.

## depdencies/pom.xml

Hier ist nix besonderes.
