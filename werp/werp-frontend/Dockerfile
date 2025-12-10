# Payara Micro já traz servlet‑container e JSF (Jakarta Faces) incluídos
FROM payara/micro:6.2025.6-jdk21

# Copia o WAR gerado pelo Maven/Gradle
COPY target/werp-frontend.war $DEPLOY_DIR/ROOT.war

EXPOSE 8080
