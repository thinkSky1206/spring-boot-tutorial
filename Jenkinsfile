node("master") {

    stage('Configure') {
    env.PATH = "${tool 'maven-3.5.0'}/bin:${env.PATH}"
    }

    stage('Checkout') {
        git 'https://github.com/thinkSky1206/spring-boot-tutorial.git'
    }

    stage('Build') {
     configFileProvider([configFile(fileId: 'cece87f9-8459-4936-ba6d-6afadc79d557', variable: 'MAVEN_SETTINGS')]) {
      sh 'mvn -s $MAVEN_SETTINGS clean package'
    }


    }


}