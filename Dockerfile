# # שלב 1: בנייה באמצעות Maven
# FROM maven:3.8.7-openjdk-17 AS build

# # הגדרת תיקיית העבודה
# WORKDIR /app

# # העתקת כל הקבצים לתיקיית העבודה
# COPY . .

# # הפעלת פקודת ה-Maven לבניית הפרויקט
# RUN mvn clean package -DskipTests

# # שלב 2: שימוש בתמונה רשמית של OpenJDK להרצת האפליקציה
# FROM openjdk:17-jdk-slim

# # הגדרת תיקיית העבודה
# WORKDIR /app

# # העתקת קובץ ה-JAR מהשלב הקודם
# COPY --from=build /app/target/user-login-0.0.1-SNAPSHOT.jar /app/user-login.jar

# # חשיפת הפורט שעליו האפליקציה רצה
# EXPOSE 8080

# # פקודת הרצה של קובץ ה-JAR
# ENTRYPOINT ["java", "-jar", "/app/user-login.jar"]



# שלב 1: בנייה באמצעות Maven
FROM maven:3.8.4-openjdk-17 AS build

# הגדרת תיקיית העבודה
WORKDIR /app

# העתקת כל הקבצים לתיקיית העבודה
COPY . .

# הפעלת פקודת ה-Maven לבניית הפרויקט
RUN mvn clean package -DskipTests

# שלב 2: שימוש בתמונה רשמית של OpenJDK להרצת האפליקציה
FROM openjdk:17-jdk-slim

# הגדרת תיקיית העבודה
WORKDIR /app

# העתקת קובץ ה-JAR מהשלב הקודם
COPY --from=build /app/target/user-login-0.0.1-SNAPSHOT.jar /app/user-login.jar

# חשיפת הפורט שעליו האפליקציה רצה
EXPOSE 8080

# פקודת הרצה של קובץ ה-JAR
ENTRYPOINT ["java", "-jar", "/app/user-login.jar"]
