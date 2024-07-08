pipeline{
    agent {
        docker { image 'alpine'}
    }
    
    parameters {
        string name: 'name', description: 'Enter your name', defaultValue: 'Burak'
    }
    
    stages{
        stage('Welcomer'){
            steps{
                echo 'Welcome $name'
            }
        }
    }
}