pipeline {
    agent any

    parameters{
        // both recipient and sender email addresses must be verified in AWS SES
        string(name: 'recipientEmail', defaultValue: 'example@tokeninc.com')
        string(name: 'senderEmail', defaultValue: 'example@tokeninc.com')
        booleanParam(name: 'IS_FAKE', defaultValue: 'false')
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
    // email notification
    post{
        success{
            // TODO: the script should be in the workspace of the pipeline

            script{
                echo 'Success'
                // build time before post actions
                Float buildTime = currentBuild.duration/1000
                if (IS_FAKE == 'false'){

                    sh """
                        chmod +x email_notifier.py
                        python3 email_notifier.py ${senderEmail} ${recipientEmail} ${JOB_NAME} ${BUILD_NUMBER} ${currentBuild.currentResult} ${currentBuild.duration}
                    """
                    echo 'Email sent!'
                }
                else {
                    echo 'Email not sent because it is a fake build.'
                }
                buildTime = currentBuild.duration/1000
                echo "Build time including post actions: ${buildTime}s"
            }
            
        }
        failure{
            // TODO: the script should be in the workspace of the pipeline
            
            script{
                echo 'Failure'
                // build time before post actions
                Float buildTime = currentBuild.duration/1000
                if (IS_FAKE == 'false'){

                    sh """
                        chmod +x email_notifier.py
                        python3 email_notifier.py ${senderEmail} ${recipientEmail} ${JOB_NAME} ${BUILD_NUMBER} ${currentBuild.currentResult} ${currentBuild.duration}
                    """
                    echo 'Email sent!'
                }
                else {
                    echo 'Email not sent because it is a fake build.'
                }
                buildTime = currentBuild.duration/1000
                echo "Build time including post actions: ${buildTime}s"
            }
        }
    }
}
