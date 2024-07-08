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
        stage('MakefileChecker'){
            steps{
                script{
                    if(fileExists('Makefile')){
                        sh 'make -s'
                    }
                    else{
                        echo 'Makefile couldnt be found'
                    }
                }
            }
        }
    }
}
