# ⚙️ Установка

Для начала, нам нужно все подготовить для затемнения наших классов API. Для этого добавьте следующие строчки в ваш **`gradle.build`** или **`pom.xml`** .

{% tabs %}
{% tab title="build.gradle" %}
```gradle
plugins {
    id 'com.github.johnrengelman.shadow' version '8.1.1'
}

apply plugin: 'com.github.johnrengelman.shadow'

shadowJar {
    configurations = [project.configurations.shadow]
    archiveClassifier.set('')
}

build.dependsOn(shadowJar)
```
{% endtab %}

{% tab title="pom.xml" %}
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.6.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>ru.amixoldev.api</pattern>
                        <shadedPattern>ru.amixoldev.api.shaded</shadedPattern>
                    </relocation>
                </relocations>
                <finalName>${project.artifactId}-${project.version}</finalName>
            </configuration>
        </execution>
    </executions>
</plugin>
```
{% endtab %}
{% endtabs %}

Далее добавляем зависимость.

{% tabs %}
{% tab title="build.gradle" %}
```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}


dependencies {
    implementation 'com.github.chi2l3s:CommandManagerAPI:1.3'
}
```
{% endtab %}

{% tab title="pom.xml" %}
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.chi2l3s</groupId>
    <artifactId>CommandManagerAPI</artifactId>
    <version>Tag</version>
</dependency>
```
{% endtab %}
{% endtabs %}
