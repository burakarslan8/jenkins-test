pipeline {
    agent any

    parameters {
        string(name: 'TEST_PARAMETER', defaultValue:'0')
    }

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
    post{
        success{
            echo 'Success'
            emailext body: "The result is ${TEST_PARAMETER}",
            subject: 'Test Subject',
            to: 'burakarslan271@gmail.com'
        }
        unstable{
            echo 'Unstable'
        }
        failure{
            echo 'Failure'
        }
    }
}
