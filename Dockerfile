# Étape 1 : Utiliser une image de base Maven pour construire le projet
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier tout le code source et le compiler
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Utiliser une image Tomcat pour exécuter le WAR
FROM tomcat:10-jdk17
WORKDIR /usr/local/tomcat/webapps/

# Installer ffmpeg et ffprobe
#RUN apt-get update && apt-get install -y ffmpeg && rm -rf /var/lib/apt/lists/*


# Copier le WAR généré dans le répertoire webapps de Tomcat
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Exposer le port Tomcat par défaut
EXPOSE 8080

# Lancer Tomcat
CMD ["catalina.sh", "run"]
