JAVAC = javac
CLASS_FILES = ./*.class
# view/*.class model/Businesses/*.class model/Reviews/*.class model/Users/*.class loaders/*.class

#Default: $(CLASS_FILES)
.DEFAULT_GOAL = build

build: 
	$(JAVAC) -d out *.java

.PHONY: client
client: 
	java -cp out Client

.PHONY: server
server: 
	java -cp out Server

.PHONY: clean
clean:
	@rm -r out