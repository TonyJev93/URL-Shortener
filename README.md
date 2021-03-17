# URL Shortener

## 과제 내용

- 요구사항
    ```
    URL을 입력받아 짧게 줄여주고, Shortening된 URL을 입력하면 원래 URL로 리다이렉트하는 URL Shortening Service
    예) https://en.wikipedia.org/wiki/URL_shortening => http://localhost/JZfOQNro
    
    1. URL 입력폼 제공 및 결과 출력
    2. URL Shortening Key는 8 Character 이내로 생성되어야 합니다.
    3. 동일한 URL에 대한 요청은 동일한 Shortening Key로 응답해야 합니다.
    4. 동일한 URL에 대한 요청 수 정보를 가져야 한다(화면 제공 필수 아님)
    5. Shortening된 URL을 요청받으면 원래 URL로 리다이렉트 합니다.
    6. Database 사용은 필수 아님
     
    ```

-  가산점
    ```
    Unit test 및 Integration test 작성.
     ```

- 제출물

    ```
    소스코드가 담긴 github.
    github의 readme에는 해당 웹서버를 리눅스 기준으로 실행하기 위해 필요한 설치/빌드 방법이 작성되어 있어야 합니다.
    ```

----
## TODO
- 개발환경
- Test 방법
- Test 코드 범위
- 빌드 및 배포 방법 

    `주의 : ( docker-compose.yaml > Redis & SHORTEN_URL_REDIRECT_SERVER_DNS 서버 IP로 변경 필요 )`

## (+본인추가) 제한사항
- 입력값은 URL인 경우에만 입력 가능.

## Docker 설치
- CentOS
1. yum 패키지 업데이트

    ```shell script
    $ yum -y update
    ```

2. Docker 설치

    ```shell script
    $ yum -y install docker
    ```
   
3. Docker compose 설치

    ```shell script
    # docker-compose 설치
    $ sudo curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    
    # docker-compose 실행 권한 부여
    $ sudo chmod +x /usr/local/bin/docker-compose
   
    # 설치된 docker-compose 실행 확인
    $ docker-compose --version
   ```
4. Maven 설치

    ```shell script
    $ wget http://mirror.apache-kr.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
    $ wget https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
    $ tar -xvf apache-maven-3.6.3-bin.tar.gz
    $ ln -s apache-maven-3.6.3 maven
   ``` 
   ```
   $ vi ~/.bash_profile 
   
   (in vi editor)
       export MAVEN_HOME=/usr/local/maven
       PATH=$PATH:$HOME/bin:$MAVEN_HOME/bin
       export PATH
   
   $ source ~/.bash_profile
   ```   

## Git Clone
`sudo git clone https://github.com/TonyJev93/URL-Shortener.git`   

## Redis Docker 실행

```
$ docker  run --name redis -d -p 6379:6379 redis
```


## 주의사항

```
브라우저에 남아있는 캐시로 인해 Redirect가 잘못 될 수 있음.
```
