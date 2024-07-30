chmod +x ./detour-0.0.1-SNAPSHOT.jar
pid=$(pgrep -f detour)
# shellcheck disable=SC2070
if [ -n ${pid} ]
then
        sudo kill -9 ${pid}
        echo kill process ${pid}
else
        echo no process
fi
BUILD_ID=dontKillMe nohup java -jar -Dspring.config.location=/home/ubuntu/detour/application.yml detour-0.0.1-SNAPSHOT.jar > nohup.out 2>&1 &
echo "######프로젝트 실행 완료######"