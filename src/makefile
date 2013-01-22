JAVA_BUILD_OPTS = -source 1.6 -target 1.6

.PHONY: clean
clean:
	rm -Rf build
	rm -f Vertigo.jar

.PHONY: build_core
build_core:
	mkdir -p build
	javac ${JAVA_BUILD_OPTS} -cp "`find libs -name \*.jar | tr '\n' ':'`" -d build `find me/matej/Vertigo -maxdepth 1 -name \*.java` \
		`find me/matej/Vertigo/Entities -maxdepth 1 -name \*.java` \
		`find me/matej/Vertigo/Entities/Tools -maxdepth 1 -name \*.java` \
		`find me/matej/Vertigo/GameStates -maxdepth 1 -name \*.java` \
		`find me/matej/Vertigo/GUI -maxdepth 1 -name \*.java` \
		`find me/matej/Vertigo/WebService -maxdepth 1 -name \*.java` \
		`find me/matej/Vertigo/World -maxdepth 1 -name \*.java`

.PHONY: application
application: build_core
	echo "Name: Vertigo application" > application.manifest
	echo "Main-Class: me.matej.Vertigo.GameMain" >> application.manifest
	echo "Build-Date: `date`" >> application.manifest
	echo "Default-Args: " >> application.manifest
	echo "Class-Path: `find libs -name \*.jar | tr '\n' ' '`" >> application.manifest
	echo "" >> application.manifest
	
	jar -cfm Vertigo.jar application.manifest \
		me/matej/Vertigo/resources/click.wav \
		me/matej/Vertigo/resources/explosion.wav \
		me/matej/Vertigo/resources/Mario.png \
		me/matej/Vertigo/resources/Obstacles.json \
		-C . libs \
		-C build me/matej/Vertigo/Entities \
		-C build me/matej/Vertigo/Entities/Tools \
		-C build me/matej/Vertigo/GameStates \
		-C build me/matej/Vertigo/GUI \
		-C build me/matej/Vertigo/WebService \
		-C build me/matej/Vertigo/World \
		-C build me/matej/Vertigo
