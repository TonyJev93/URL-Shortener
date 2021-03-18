
# URL Shortener  

- 2021-03-18 : init, 최초 작성
  
## 0. Project Requirements
- Apache Maven 3.6.3
- jdk 1.8 +
- Spring Boot 2.4.3
- Redis
- Lombok
- Docker
- Docker Compose

## 1. 설치 방법 (Linux CentOS 기준)

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
	  $ sudo chmod +x /usr/local/bin/docker-compose # 설치된 docker-compose 실행 확인  
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
5. Git Clone
	```
	$ git clone https://github.com/TonyJev93/URL-Shortener.git
	```


## 2. 빌드 및 배포

### 0.  Redis Docker 실행

WAS 서버 기동 전 Redis 기동이 필수적으로 선행되어야 한다.
``` 
$ docker  run --name redis -d -p 63790:6379 redis  
```  
> **port**는 임의로 **63790**으로 지정하였으며, 
> 바꾸게 되면 아래 설명할 **docker-compose.yaml** 의 
> **환경변수(LOCAL_REDIS_PORT)** 또한 바꿔 주어야 한다.



### 1. Git clone 받은 프로젝트 Path로 이동
```
$ cd {git-path}/URL-Shortener
```

### 2.  빌드 & 배포 스크립트 실행
```
$ ./buildAndDeploy.sh
```

#### 2. 1. 스크립트 구성
**1. ./build.sh**
- **역할** : 메이븐 의존성 설치 및 도커 이미지 생성
	
* **빌드 순서**
	1. **mvn clean** 
	2. **mvn install** 
	3. **docker-compose down** : 기존 도커 컨테이너 다운
	4. **docker rm container_id/ docker rmi  image_id** : 컨테이너/이미지 삭제
	5. **docker build** : 도커 이미지 생성 >> './Dockerfile'  기준, (Image name = **tonyjev/urlshortener**)
	
**2. ./deploy.sh**

* **배포**
	 ```
	# docker compose 파일 수정
	$ vi ./docker-compose.yaml
	```

* **docker-compose.yaml**

	> **Container Service Name** : urlshortener.service
	> **Image** : tonyjev/urlshortener
	
	> **Environment ( 중요 ! )**
	  - LOCAL_REDIS_HOST=**localhost**
      - LOCAL_REDIS_PORT=**63790**
      - SHORTEN_URL_REDIRECT_SERVER_DNS=http://**localhost**:8080
      - SHORTEN_URL_REDIRECT_SERVICE_PATH=shorten-url/redirect
      - SHORTEN_URL_MAX_LENGTH=8

위 환경 변수 중 **localhost**를 WAS를 구동할 **서버의 IP 주소로 변경**하여 사용한다.
또한, Redis의 Port는 위 Docker를 이용하여 띄운 Redis의 Port와 일치하도록 한다.

```
	# docker compose 실행
	$ docker-compose up -d

	# 컨테이너 Loading 여부 확인
	$ docker ps
		- IMAGE : tonyjev/urlshortener 로 띄워진 컨테이너 존재여부 확인

	# WAS 서버 log 확인
	$ docker logs -f $(docker ps -f ancestor=tonyjev/urlshortener -q)
```

> 빌드 완료 후에는 **./deploy.sh** 혹은 **docker-compose up -d** 명령어를 통해 배포만으로 서버구동 가능


### 스크립트로 빌드/배포 안될 경우 (Docker / Docker Compose 설치 필요 없음)

```
$ mvn clean
$ mvn package
$ cd ./target
$ nohup java -jar urlshortener-0.0.1-SNAPSHOT.jar &
```


## 3. 테스트 방법

### 테스트 순서
1. Redis 구동
2. 서버 빌드 및 배포
3. Web page 접속
	```
	# Server IP = 본인 서버 IP, 8080 Port 이용
	웹 접속 경로 : http://{{Server IP}}:8080
	```
4. 입력 Form 안에 URL 입력 후 **'Get Shorten URL'** 버튼 클릭
	> 주의 : 입력 URL은 **http:// or https://** 가 포함된 **완전한 URL의 형식**을 입력하여야 함. **(Validation Check)**
	> ex) http://www.naver.com
5. 출력된 단축 URL 주소창에 입력 ( **주의** : 기존 브라우저에 남아있는 캐시로 인해 Redirecting이 정상 작동하지 않을 수 있음. **Secret Mode**에서 수행하는 것을 추천)
6. 기존 URL로 Redirect 완료

### 테스트를 통해 확인되어야 할 사항

####  * URL 단축 서비스
	1. 입력 URL의 Validation Check 여부 : URL 형식이 아닌 경우 Error Msg 전달
	2. 단축 URL 출력 여부 : 올바른 URL 입력 시 단축 URL이 출력
	3. 동일 URL 요청 : 동일 URL로 요청한 경우 동일한 단축 URL 이 출력
	4. 요청 횟수 확인 : 동일 URL로 요청한 경우 요청 횟수가 증가

####  * 리다이렉팅 서비스
	1. 단축 URL 접속 시 Redirect 여부 : 단축 URL 입력 시 기존 URL로 이동
	2. 존재하지 않는 단축 URL 접속 시 에러처리 여부


## 4. 핵심문제 해결전략

### 1) URL 단축 구현

 주어진 URL을 특정 길이 내의 값으로 단축 하는 로직을 구현한다. 확인된 방법은 **3 가지**가 있었다.

> 참고 : [https://dzone.com/articles/url-shortener-detailed-explanation](https://dzone.com/articles/url-shortener-detailed-explanation)
1) Hash Function
2) UUID
3) **Base62 Encoding**(선택)

**1번과 2번 방식의 단점**은 결과값을 특정 길이로 절삭하는 과정에서 **중복된 결과값이 발생**할 수 있으며, 이에 따른 추가적인 변경 로직을 수행하여야 한다는 점이 있었다.

**3번 방식**의 경우 **기존 URL**과 맵핑되는 **1씩 증가하는 숫자**(Key ID)를 **Base62 Encoding**을 통해 변환하여 얻어진 결과를 **단축 URL**로 정의 하므로서 아래와 같은 맵핑 구조가 생긴다.

```
기존 URL - Key ID - 단축 URL
```

즉, Key ID를 통해 기존 URL과 단축 URL에 동시에 접근 할 수 있게 된다.

Base62 Encoding을 통해 얻은 단축 URL이 8자리가 넘기위해서는 Key ID가 62의 8승이라는 수만큼 증가하여야 하며 그 동안의 단축 URL의 중복은 발생하지 않는다.
(물론, **62의 8승 까지 꽉 찬 경우** 다시 1부터 시작하여 기존 저장된 단축 URL에 덮어 씌어지는 것을 방지할 수 없는 단점이 존재)

### 2) 영속성 처리

기존 URL과 단축 URL의 맵핑관계를 저장하기 위한 저장장소가 필요하다.
이를 위해 **DB 또는 캐시** 이용을 선택할 수 있다.

데이터 구조가 비교적 간단하며, 기존 URL과 단축 URL간의 데이터가 변경될 일이 없기 때문에 
조회 성능이 뛰어난 **캐시 서버(Redis)를 이용**하였다.

Redis 서버를 통해 빠른 데이터 접근 및 저장 뿐만 아니라 **TTL**을 통한 만료 기능을 이용하여 **저장공간을 더욱 효율적으로 사용**할 수 있다.

## 5. 소스 구조

* **패키지 구성**
	- **presentation.api** : Controller, Client로부터 오는 요청에 대해 처리(요청 데이터 유효성 검증), 서비스와의 연계 수행.
	- **application** : Service, 특정 기능을 수행하는 서비스로 구성, Domain과 Infra 를 연결지어주며 협력이 가능하도록 함.
	- **domain** : Model, 협력의 주체, 영속성 대상이 됨(Repository), POJO로 구성.
	- **infrastructure** : 외부 요소(DB, API 등), 협력에 필요한 외부 자원을 공급해주는 역할 수행.
	- **global** : 공통 기능(config, exception, util ..)
	- **resources.templates** : View, Mustache 템플릿 엔진 이용.

* **API 구성**
	* **Get Shorten URL**  (단축 URL 요청)
		* Url Path : /shorten-url
		* HttpMethod : Post
		* Response Status : OK (200)
	* **Redirect URL** (URL 리다이랙팅)
		* Url Path : /shorten-url/redirect/{shortenUrl}
		* HttpMethod : Get
		* Response Status : Permanent Redirect (308)



* **개발 원칙 : OOP**
	* **SRP** : 단일 책임, 한 클래스에서 여러 역할을 수행하지 않도록 분리
	* **OCP** : 개방-폐쇄, 인터페이스 의존을 통한 서비스 간 의존성 제거
	* **LSP**
	* **ISP** : 인터페이스 분리, 서비스 Layer의 세분화를 통한 인터페이스의 응집성을 높힘
	* **DIP** : 의존성 역전, 서비스 간 인터페이스 참조를 이용하여 패키지 간 순환 참조 제거
	```
	URL 단축 처리하는 Class (ShortenUrlGenerator)를 인터페이스로 구성하므로서,
	Base62 Encoding 외의 방식으로 교체 시 서비스 Layer에 영향을 주지 않고 유연하게 변경 가능.
	```

- **Test Code**
	- 구현 범위 : MVC Test, Service Test, Unit Test
	- MVC Test
		1. UrlShortenerControllerTest
		2. RedirectControllerTest
	- Service Test
		- GetShortenUrlServiceTest
	- Unit Test
		-  Base62UtilTest



## 7. 고찰

 **1. Spring Redis Data 사용**
 
 Spirng에서 제공하는 Redis Data를 이용하여 영속성을 구현하였다. Redis 서버를 이번 Project를 통해 처음 사용하게 되며, 더욱  쉬운 방법으로 Redis를 사용 할 수 있는 방법이 Redis Data를 사용하는 것이었다.

사용법이 쉽고 제공해주는 기능들이 많다는 장점이 있지만, Redis에 Model을 저장하는 과정에서 Class 정보를 같이 저장하는 Redis Data의 특징 때문에 직렬화를 필수적으로 implement해야 했다. 이 때, 기존 Model의 클래스 정보가 변경될 경우 역직렬화 과정에서 Exception이 발생할 수 있어 'serialVersionUID'을 꼭 명시해주어야 하며, 이를 통해 Class내 field 의 추가/제거로 인한 역직렬화 Error는 방지할 수 있지만 기존 field의 Data Type을 변경할 경우 동일하게 역직렬화 Exception이 발생하게 된다.
 
따라서, Redis Data 라이브러리를 이용하기 위해 저장 대상이 될 Model의 속성 변화가 빈번하지 않는다는 전제를 가지고 있어야 한다.  이번 프로젝트의 경우 단축 URL과 관련하여 추가적인 속성의 발생에 대해 무시할 수 없기 때문에 적합한 선택은 아닌 것 같다. Redis 사용이 숙달 된 이후 Redis Template을 이용한 데이터 관리를 직접 하는것이 올바른 선택일 것으로 보인다.


**2. Controller vs RestController**

 짧은 시간내에 단순 API가 아닌 화면을 제공해주는 서버를 개발해야 하는 상황에서 JQuery 또는 Vue.js를 이용하여 Model And View 를 리턴하여 Data Binding  하는 것이 아닌 단순 데이터만을 반환하여 즉각적으로 Rendering 해주는 Client를 개발 할 수 없었다.(클라이언트에 대한 기술 부족) 하지만 시간이 길게 주어졌다면 Vue.js를 이용하여 구현하였을 것 같다.
 
 이로 인해 html을 이용하였고 요청 데이터 포맷을 json이 아닌 form의 형식으로 하였고, Controller 또한 RestController 가 아닌 그냥 Controller 를 이용하여 Model에 데이터를 싣어 보내도록 개발하였다. 기존 RestController 개발에 익숙하여 Model을 이용한 Data Return을 어떻게 해야  효율적인지와 Exception 처리를 어떤 방식으로 화면에 보여줄지에 대해 미숙하게 수행하였다. 또한, RestDocs를 이용한 API 문서 작성을 계획하였으나 기본 Controller의 경우 RestDocs를 활용하기 어렵다는 것을 깨닫았다.

 그러나 본 프로젝트의 목적이 서버와 화면 연계가 아닌 실질적인 기능 수행이었기 때문에, 화면과 요청/응답 처리에 대해 에너지를 크게 소비하지 않았다.
