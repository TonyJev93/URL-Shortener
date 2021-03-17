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

## (+본인추가) 제한사항
- 입력값은 URL인 경우에만 입력 가능.

## Docker 설치
- CentOS
1. yum 패키지 업데이트

    ```
    $ yum -y update
    ```

2. Docker 설치

    ```
    $ yum -y install docker
    ```

## Redis Docker 실행

```
$ docker  run --name redis -d -p 6379:6379 redis
```

## 주의사항

```
브라우저에 남아있는 캐시로 인해 Redirect가 잘못 될 수 있음.
```
