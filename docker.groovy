pipeline{
    agent {
        docker { image 'alpine'}
    }
    
    stages{
        stage('CheckVersion'){
            sh 'alpine -v'
        }
    }
}