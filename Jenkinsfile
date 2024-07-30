pipeline{
  agent any

  tools {
      gradle 'gradle 8.8'
  }

  stages{
    stage('Ready'){
      steps{
        sh "echo 'Ready'"
        git branch: 'dev',
          credentialsId: 'detour_github',
          url: 'https://github.com/BE-Friend-Develop-Team/detour'
      }
    }

    stage('Test'){
      steps{
        sh "echo 'Test'"
        sh 'chmod +x /var/jenkins_home/workspace/detour_dev/gradlew'
        sh '/var/jenkins_home/workspace/detour_dev/gradlew clean test'
      }
    }

    stage('Build'){
      steps{
        sh "echo 'Build Jar'"
        sh '/var/jenkins_home/workspace/detour_dev/gradlew bootJar'
      }
    }

    stage('Deploy'){
      steps{
        script{
          withCredentials([sshUserPrivateKey(credentialsId: "pem-key", keyFileVariable: 'my_private_key_file')]) {
            def remote = [:]
            remote.name = "pem-key"
            remote.host = "${env.DEV_BACK_IP}"
            remote.user = "ubuntu"
            remote.allowAnyHosts = true
            remote.identityFile = my_private_key_file

            sh "echo 'Deploy AWS'"
            dir('detour/build/libs'){
                sh "scp -o StrictHostKeyChecking=no -i ${my_private_key_file} /var/jenkins_home/workspace/detour_dev/build/libs/*.jar ubuntu@${env.DEV_BACK_IP}:/home/ubuntu/detour"
                sh "scp -o StrictHostKeyChecking=no -i ${my_private_key_file} /var/jenkins_home/workspace/detour_dev/deploy.sh ec2-user@${env.DEV_BACK_IP}:/home/ubuntu/detour"
            }

            sh "ssh -o StrictHostKeyChecking=no -i ${my_private_key_file} ubuntu@${env.DEV_BACK_IP} 'cd /home/ubuntu/detour && ./deploy.sh'"
            sh "echo 'Spring Boot Running'"
          }
        }
      }
    }
  }
}