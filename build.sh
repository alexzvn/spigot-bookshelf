# default is home/codespace/.java/current/bin/java
rm test/plugins/Bookshelf.jar
mvn clean && mvn install
cp target/Bookshelf-v0.0.1.jar test/plugins/Bookshelf.jar
