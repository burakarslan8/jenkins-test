pipeline {
    agent any

    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
        stage('UnixChecker'){
            steps{
                script{
                    if(isUnix() == true){
                        echo 'This is running on UNIX'
                    }
                }
            }
        }
        stage('ReadmeChecker'){
            steps{
                script{
                    if(fileExists('README.md')){
                        echo 'This file exists.'
                    }
                }
            }
        }
    }
}
