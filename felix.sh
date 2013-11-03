rm -rf felix-cache/*
rm -rf bundle/*
cp $FELIX_HOME/bundle/* bundle/
cp bundles/*/target/scala-2.10/*.jar bundle/
cp scalamodules/target/scala-2.10//*.jar bundle/

java -Dfelix.config.properties=file:src/main/resources/felix.config -jar $FELIX_HOME/bin/felix.jar -b bundle