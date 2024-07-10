pipeline{
    agent any
    parameters{
        booleanParam(name: 'IS_FAKE', defaultValue: 'false')
    }

    stages{
        stage('test'){
            steps{
                script{
                    if ($(IS_FAKE) == 'true'){
                        echo 'true'
                    }
                    else{
                        echo 'false'
                    }
                }
            }
        }
    }
}