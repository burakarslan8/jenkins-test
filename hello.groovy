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
            mail    to: 'burak.arslan@tokeninc.com',
                    subject: 'Jenkins pipeline results',
                    body: "The output is ${TEST_PARAMETER}"
        }
        unstable{
            echo 'Unstable'
        }
        failure{
            echo 'Failure'
        }
    }
}
