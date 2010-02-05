CHATSERVER_DIR=/opt/apps/chatServer
JAVA_HOME=/usr/java/jdk1.5.0_21
OLDCP=${CLASSPATH}
CLASSPATH=${CHATSERVER_DIR}/classes:${CHATSERVER_DIR}/libs/antlr.jar:${CHATSERVER_DIR}/libs/asm.jar:${CHATSERVER_DIR}/libs/cglib.jar:${CHATSERVER_DIR}/libs/commons-beanutils.jar:${CHATSERVER_DIR}/libs/commons-chain.jar:${CHATSERVER_DIR}/libs/commons-collections.jar:${CHATSERVER_DIR}/libs/commons-configuration.jar:${CHATSERVER_DIR}/libs/commons-dbcp.jar:${CHATSERVER_DIR}/libs/commons-fileupload.jar:${CHATSERVER_DIR}/libs/commons-io.jar:${CHATSERVER_DIR}/libs/commons-lang.jar:${CHATSERVER_DIR}/libs/commons-logging.jar:${CHATSERVER_DIR}/libs/commons-logging_1.jar:${CHATSERVER_DIR}/libs/commons-pool.jar:${CHATSERVER_DIR}/libs/commons-validator.jar:${CHATSERVER_DIR}/libs/dom4j.jar:${CHATSERVER_DIR}/libs/ejb3-persistence.jar:${CHATSERVER_DIR}/libs/gson-1.3-b2.jar:${CHATSERVER_DIR}/libs/hibernate-annotations.jar:${CHATSERVER_DIR}/libs/hibernate-commons-annotations.jar:${CHATSERVER_DIR}/libs/hibernate.jar:${CHATSERVER_DIR}/libs/javaee.jar:${CHATSERVER_DIR}/libs/jta.jar:${CHATSERVER_DIR}/libs/log4j.jar:${CHATSERVER_DIR}/libs/mysql-connector.jar:${CHATSERVER_DIR}/libs/ojdbc14_g.jar:${CHATSERVER_DIR}/libs/spring.jar:${CHATSERVER_DIR}/libs/xpp3_min.jar:${CHATSERVER_DIR}/libs/xstream.jar:
${JAVA_HOME}/bin/java -Xms512m -Xmx1024m -cp $CLASSPATH net.ds3783.chatserver.Main
CLASSPATH=${OLDCP}
