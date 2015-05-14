mvn -B archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DgroupId=$1 -DartifactId=$2
git add $2
git commit -m "Addded "$2
git push -u origin master
cd $2
echo $2
