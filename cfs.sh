size=${#1} 
name = "HW"
if [[ $size == 1 ]]; then
	name = $name"0"$1
else
	name = $name$1
fi
name = $name"-0036478527"
mkdir $name
cd $name
mkdir src
mkdir src/main
mkdir src/main/java
mkdir src/test
mkdir src/test/java
cp -r ../ant-build-config/. ./