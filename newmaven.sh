mvn -B archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DgroupId=$1 -DartifactId=$2
cd $2
echo $2