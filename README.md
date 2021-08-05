commons
=======

ru.mail.jira.plugins.commons

![Maven Publish Package](https://github.com/atlascommunity/commons/workflows/Maven%20Publish%20Package/badge.svg)

#Publishing 


export MAVEN_OPTS="--add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.text=ALL-UNNAMED --add-opens=java.desktop/java.awt.font=ALL-UNNAMED"
mvn -B -s settings.xml clean deploy