rm -rd bundle
mkdir bundle
cp bundles/*/target/scala-2.10/*jar bundle/

java -Dfelix.config.properties=file:src/main/resources/felix.config -jar $FELIX_HOME/bin/felix.jar -b ./bundle