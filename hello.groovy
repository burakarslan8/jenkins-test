pipeline {
    agent any

    parameters{
        // both recipient and sender email addresses must be verified in AWS SES
        string(name: 'recipientEmail', defaultValue: 'example@tokeninc.com')
        string(name: 'senderEmail', defaultValue: 'example@tokeninc.com')
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
            script{
                echo 'Success'
                def subject = "Pipeline: ${JOB_NAME} - Build #${BUILD_NUMBER} - ${currentBuild.currentResult} | ${currentBuild.duration}"
                print subject
                mail(
                    to: "${recipientEmail}",
                    subject: subject,
                    body: "The build is successful",
                    replyTo: "${senderEmail}",
                    from: "${senderEmail}",
                )
                echo 'Email Sent!'
            }
            
        }
        unstable{
            script{
                echo 'Unstable'
                def subject = "Pipeline: ${JOB_NAME} - Build #${BUILD_NUMBER} - ${currentBuild.currentResult} | ${currentBuild.duration}"
                print subject
                mail(
                    to: "${recipientEmail}",
                    subject: subject,
                    body: "The build is unstable",
                    replyTo: "${senderEmail}",
                    from: "${senderEmail}",
                )
                echo 'Email Sent!'
            }
        }
        failure{
            script{
                echo 'Failure'
                def subject = "Pipeline: ${JOB_NAME} - Build #${BUILD_NUMBER} - ${currentBuild.currentResult} | ${currentBuild.duration}"
                print subject
                mail(
                    to: "${recipientEmail}",
                    subject: subject,
                    body: "The build has failed",
                    replyTo: "${senderEmail}",
                    from: "${senderEmail}",
                )
                echo 'Email Sent!'
            }
        }
    }
}
