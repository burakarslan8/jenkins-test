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
            mail to: 'burakarslan271@gmail.com',
                subject: "Succeeded Pipeline: ${currentBuild.fullDisplayName}",
                body: "${env.BUILD_URL}"
        }
        unstable{
            echo 'Unstable'
        }
        failure{
            echo 'Failure'
        }
    }
}
