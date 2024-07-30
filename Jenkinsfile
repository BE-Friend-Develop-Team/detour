pipeline{
  agent any

  tools {
      gradle 'gradle'
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
        dir('detour') {
          sh './gradlew clean test'
        }
      }
    }

    stage('Build'){
      steps{
        sh "echo 'Build Jar'"
        dir('detour') {
          sh './gradlew bootJar'
        }
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
            dir('detour/build/libs'){ #.jar 파일 EC2로 전송
                sh "scp -o StrictHostKeyChecking=no -i ${my_private_key_file} *.jar ubuntu@${env.DEV_BACK_IP}:/home/ubuntu/detour"
                sh "scp -o StrictHostKeyChecking=no -i ${my_private_key_file} /path/to/deploy.sh ec2-user@${env.DEV_BACK_IP}:/home/ubuntu/detour"
            }
						# EC2에 접속하여 배포스크립트 실행
            sh "ssh -o StrictHostKeyChecking=no -i ${my_private_key_file} ubuntu@${env.DEV_BACK_IP} 'cd detour && ./deploy.sh'"
            sh "echo 'Spring Boot Running'"
          }
        }
      }
    }
  }
}