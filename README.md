📌 **2024.07.17 ~ 2024.08.14**

<div align="center">
  
# [2024] 🛫detour

『 ${\textsf{\color{green}detour}}$ - 당신의 여행을 더 특별하게 만드는 방법 』

현대인의 바쁜 일상 속에서 여유를 찾고 여행을 더욱 특별하게 즐길 수 있도록 돕는 여행 일정 관리 프로젝트 👒 🛍


![동물_누끼 (1)](https://github.com/user-attachments/assets/d8c82ddb-f083-482a-a9ee-302e980eb652)


⏰ 맞춤형 여행 계획 : 다른 사용자의 취향과 관심사를 내 여행에 적용하고 여행 경로를 참고할 수 있습니다. 
<br>

📜 커뮤니티 기능 : 여행자들 간의 여행 경험을 공유하고 추천할 수 있는 커뮤니티 공간을 제공합니다. 
<br>

🧚🏻‍♀️ 일행과의 일정 공유 : 여행을 함께하는 일행을 초대하여 해당 일정을 함께 수정하고 관리할 수 있습니다!
<br>

💌 추억 기록 : 여행의 순간을 사진과 글로 기록하고 나만의 여행을 저장해 보세요!
   
</div>
<br>

## 목차
- [👩‍👩‍👧👨‍👦 Team 🥓](#team)
- [🎨 Tech Stack](#tech-stack)
- [🎯 MVP](#rngus)
- [🛫 Features](#features)
- [🛠 Trouble Shooting](#dlskdud)
- [📑 Technical Documentation](#tech)
- [🌌 Environment Variable](#ghksrud)

<br>

<div id="team">

# 👩‍👩‍👧👨‍👦 Team 

<table>
  <tbody>
    <tr>
      <td align="center">
        <a href="https://github.com/LeeChangHyeong">
          <img src="https://avatars.githubusercontent.com/u/71262367?v=4" width="100px;" alt=""/><br />
          <sub><b> 리더 : 이창형 </b></sub>
        </a><br />
        <a href="https://changbroblog.tistory.com/">🐼</a>
      </td>
      <td align="center">
        <a href="https://github.com/sihyun615">
          <img src="https://avatars.githubusercontent.com/u/126032302?v=4" width="100px;" alt=""/><br />
          <sub><b> 부리더 : 박시현 </b></sub>
        </a><br />
        <a href="https://enjoydev.tistory.com/">👾</a>
      </td>
       <td align="center">
        <a href="https://github.com/eunsaemsaem">
          <img src="https://avatars.githubusercontent.com/u/142576710?v=4" width="100px;" alt=""/><br />
          <sub><b> 팀원 : 이은샘 </b></sub>
        </a><br />
        <a href="https://saemmeas-coding.tistory.com/">🙈</a>
      </td>
      <td align="center">
        <a href="https://github.com/LeeNaYoung240">
          <img src="https://avatars.githubusercontent.com/u/107848521?v=4" width="100px;" alt=""/><br />
          <sub><b> 팀원 : 이나영 </b></sub>
        </a><br />
        <a href="https://leenayoung240.github.io/ ">🍀</a>
      </td>
      <td align="center">
        <a href="https://github.com/wondo8449">
          <img src="https://avatars.githubusercontent.com/u/54055270?v=4" width="100px;" alt=""/><br />
          <sub><b> 팀원 : 김예찬 </b></sub>
        </a><br />
        <a href="https://velog.io/@wondo8449/posts">🐶</a>
      </td>
    </tr>
  </tbody>
</table>


<details>
<summary>이창형 </summary>
<div markdown="1">

- user CRUD
- social login (Kakao)
- redis 구현 및 연결 (랭킹 기능)
- 검색 기능(좋아요, 최신순 정렬), 페이지네이션
- 이메일 인증

</div>
</details>


<details>
<summary>박시현</summary>
<div markdown="1">

- schedule CRUD
- dailyplan CRUD
- comment CRUD
- like CRUD
- invitation CRUD
- CI/CD (Jenkins)

</div>
</details>

<details>
<summary>이은샘</summary>
<div markdown="1">
  
- comment CRUD
- marker CRUD
- withdrawal

</div>
</details>

<details>
<summary>이나영</summary>
<div markdown="1"> 
  
- marker CRUD
- review CRUD
- S3를 이용한 파일 업로드
- 분산 락(Redisson)
  
</div>
</details>

<details>
<summary>김예찬</summary>
<div markdown="1">

- place CRUD
- 배포

</div>
</details>


<br>

<div id="tech-stack">

# 🎨 Tech Stack

| Type         | Tech                                                                                                                | Version                                                                                             | Link |
| ------------ | ------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- | ---- |
| **IDE**      | ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) |                                                                                                     |      |
| **Framework**| ![Spring Boot](https://img.shields.io/badge/SpringBoot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) | 3.3.2                                                                                               |      |
| **Language** | ![Java](https://img.shields.io/badge/Java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)           | JDK 17                                                                                              |      |
| **Database** | ![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white) <br/> ![Redis](https://img.shields.io/badge/redis-%23DC382D.svg?style=for-the-badge&logo=redis&logoColor=white) <br/> | 8.0.35 <br/> 7.2.5 <br/> |      |
| **DevOps**   | ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) <br/> ![Jenkins](https://img.shields.io/badge/jenkins-%23D24939.svg?style=for-the-badge&logo=jenkins&logoColor=white) <br/> ![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazonwebservices&logoColor=white)  | 27.1.1 <br/> 2.462.1 <br/> AWS (EC2, S3, Route53, RDS, IAM, VPC)                               |      |
| **Security** | ![Spring Security](https://img.shields.io/badge/SpringSecurity-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) <br/> ![OAuth 2.0](https://img.shields.io/badge/OAuth%202.0-%234E9A06.svg?style=for-the-badge&logo=oauth&logoColor=white) |                                                                                                     |      |                                                                                           |
| **Collaborative Tools** | ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) <br/> ![Slack](https://img.shields.io/badge/slack-%234A154B.svg?style=for-the-badge&logo=slack&logoColor=white)  <br/> ![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)  <br/> ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)  <br/> ![Cacoo](https://img.shields.io/badge/cacoo-4479A1.svg?style=for-the-badge&logo=c&logoColor=white) |  | [🔗 Notion](https://www.notion.so/BE-Friend-638a73c5210640f0a29a94c04b7aa964?pvs=4) 
| **Video**   |  <img align="left" alt="SOKURI_CODE  YouTube" width="48px" src="https://img.icons8.com/color/48/000000/youtube-play.png" /> | | [🔗 Youtube](https://youtu.be/QC6cOnhgit0) |

     





<br>

<div id="rngus">
  
# 🎯 MVP

> 영상 재생 : 사진 클릭
- 🔥 Jenkins + Docker Out of Docker를 통한 CI/CD 구현
  
- 🔥 RDS를 통한 안정적인 데이터베이스 관리

<details>
<summary>🔥 AWS S3를 통한 이미지 저장</summary>
<div markdown="1">

![s3](https://github.com/user-attachments/assets/8db79a37-d112-4005-9843-cfc61ff2ac9b)

</div>
</details>




<details>
<summary>🔥 Redission을 활용한 동시성 제어</summary>
<div markdown="1">

![레디슨4](https://github.com/user-attachments/assets/ba435b02-7969-4e46-a723-8e43d4c6e68a)


</div>
</details>

<details>
<summary>🔥 Redis 캐싱 통한 랭킹 시스템 구현</summary>
<div markdown="1">

![랭킹1](https://github.com/user-attachments/assets/9fe01e29-a258-4f06-b570-1ac4decceb93)


</div>
</details>

<details>
<summary>🔥 SMTP를 활용한 회원가입 시 이메일 인증 기능 구현</summary>
<div markdown="1">

![image](https://github.com/user-attachments/assets/d6857a84-0e9c-4491-ab18-755db5aaa78c)


</div>
</details>
 

<br>


<div id="features">


# 🛫 Features

- 사용자 관련 기능
  
  - 회원가입 : 인증 코드를 통한 이메일 인증 기능
    
    - 일반 사용자
      
    - 관리자
      
  - 로그인
    
    - 일반 로그인
      
    - 카카오 로그인
  - 로그아웃
    
  - 회원 정보 수정
    
    - 닉네임
      
    - 이메일
    - 비밀번호
      
  - 탈퇴
    
- 일정 관리 기능
  
  - 일정 생성 - 각 날짜에 대한 마커 생성 (장소 등록)
    
  - 일정 수정
    
    - 제목
      
    - 대표 이미지
      
      - 마커
        
        - 파일 및 글 작성/수정
          
        - 파일 삭제
        - drag & drop을 통한 순서 변경
        - 삭제
          
      - 일행 초대 및 취소
  - 일정 조회
    
      - 랭킹
        
      - 최신순 / 좋아요순
      - 내 일정 모음
      - 내가 좋아요한 일정 모음
  - 일정 검색
    
  - 일정 삭제
    
- 리뷰 작성, 평균 점수 조회
  
- 사용자 간 커뮤니케이션 기능
  
  - 댓글 작성, 수정, 삭제
    
  - 일정에 대한 좋아요 등록 및 취소



<br>

<div id="dlskdud">

# 🛠 Trouble Shooting

<details>
<summary>💢 메인페이지(랭킹) 조회가 느려요</summary>
<div markdown="1"> 


**🔥 문제 발생**

유저테스트를 하는 와중에, 메인화면 조회하는게 더 빨랐으면 하는 피드백을 받음.

![image](https://github.com/user-attachments/assets/4b98bb92-09ef-4924-831b-6d70f14bb11b)

**🌠 해결 과정**

- 메인화면의 랭킹 조회 부분을 사용자가 메인 페이지에 접근할시에 매번 DB에 접근해 랭킹 데이터를 들고오는 것이 아니라 Redis에 랭킹 정보를 저장해놓고 Redis 서버에 접근하여 게시물 정보를 들고오도록 수정
- 기존에 메인 화면에 12개의 게시물을 보여주려 할때 12번의 api를 호출하여 게시글의 모든 데이터를
들고 왔지만 한 번의 api로 썸네일에 필요한 부분의 게시글 데이터만 들고올 수 있도록 변경함.

- 이러한 수정으로 인해 응답속도가 **기존 대비 85%이상 줄어든 것을 확인** 가능

**👺 게시글의 모든 데이터를 들고오고 응답속도가 느렸던 수정 전 페이지**
![image](https://github.com/user-attachments/assets/f1770f99-b921-48c8-9a3f-44b6e8ad2bd0)
![image](https://github.com/user-attachments/assets/a20de94c-201a-4568-8a03-1cefddf059bb)

- 호출도 많고 응답속도가 하나당 1초가 넘어가는 것 확인 가능

**✅ 수정 후**
 ![image](https://github.com/user-attachments/assets/949900d5-8a8f-4e37-96be-a83f92c455c1)

- api 호출도 한 번으로 줄었고 **응답속도는 1.08s → 130ms로 약 88%** 가 감소한 것 확인 가능

</div>
</details>

<details>
<summary>💔 좋아요를 눌렀는데 좋아요가 누락돼요</summary>
<div markdown="1"> 
  
**🤦‍♂️ 문제 발생 :**

- 여러 사용자가 같은 자원에 대해 좋아요를 누를 때 중복된 좋아요나 누락된 좋아요가 발생하여 좋아요 수가 정확히 반영되지 않는 문제 발생

**👏 해결 과정**

- 기존의 코드에 Redisson의 분산 락을 설정하여 여러 프로세스나 서버가 동시에 같은 리소스에 접근할 때 오직 하나의 프로세스만이 리소스에 접근할 수 있도록 보장함. 사용자가 좋아요를 누를 때 분산 락을 통해 한 프로세스만이 좋아요를 처리할 수 있고, 다른 프로세스는 락이 해제될 때가지 대기하게 됨.

- 즉, 여러 사용자가 좋아요를 동시에 누를 때 좋아요를 정확하게 처리

### ♻ 테스트 코드 실행 결과

🔒 **❌ 락이 적용되지 않은 경우 :** 총 좋아요 수를 1000개를 입력했는데 일치하지 않는 것 확인 가능
![image](https://github.com/user-attachments/assets/3ec7ecf6-daa7-4178-988f-f2867d495326)
![image](https://github.com/user-attachments/assets/a1f85db4-8183-4999-a000-8f3c81fc87db)

🔒  **⭕ 락이 적용된 경우:** 총 좋아요 수 1000개를 입력했을 때 1000개의 결과로 일치하는 것 확인 가능
![image](https://github.com/user-attachments/assets/be5ee43d-e649-4956-ac3b-d0319f2e3b5f)
![image](https://github.com/user-attachments/assets/42b2bbcd-849a-4733-8410-45d78132ebae)


</div>
</details>


<details>
<summary>💦 가끔 게시글 수정이 안돼요</summary>
<div markdown="1"> 
  
**😨 기존 방식에서의 문제점 인식**

- 기존 저희 어플리케이션 랭킹 시스템은 특정 시간(10분)별 조회수로 산정됨.
  
- 저희 어플리케이션은 10분마다 스케쥴의 필드에 있는 시간별 조회수를 통해 랭킹을 세우고 모든 스케쥴 entity의 시간별 조회수를 0으로 초기화 해줌.(다시 시간별 조회수를 측정하기 위해)

- 이때 table에 lock이 걸리고 table에 있는 entity를 변경하려할때 동작이 무시됨.

 **✅ 해결 과정 : 랭킹 계산을 담당하는 테이블을 따로 분리**


- 스케쥴 entity에서 시간별 조회수 필드를 빼고 스케쥴별로 시간별 조회수를 따로 계산하고 저장하고 삭제하는 역할을 가지는 테이블을 만들어 해결함.

**💡 선택 이유**


- 시간별 조회수 필드를 빼고 스케쥴 별로 시간별 조회수를 따로 계산하는 테이블을 만들면 메인 테이블 (스케쥴 테이블)에서 전체 테이블이 동시에 업데이트 될일이 없어 주기적으로 테이블 자체가 lock이 걸리는 상황이 사라지기 때문에 선택하여 오류를 해결함.
</div>
</details>


<br>

<div id="tech">


# 📑 Technical Documentation

<details>
<summary>🏗 Architecture</summary>
<div markdown="1">
  
## 🏗

![image](https://github.com/user-attachments/assets/32005285-0efc-449d-b7f6-f7d033d6d482)



</div>
</details>

</div>
</details>

<details>
<summary>🧬 ERD DIAGRAM</summary>
<div markdown="1">
  

![image](https://github.com/user-attachments/assets/00f43a8d-f1c6-40ba-b277-b33813302ea5)



</div>
</details>


<details>
<summary> 🔨 API 명세서</summary>
<div markdown="1">
  
  <details>
  <summary> User </summary>
  <div markdown="1">
  
  ![image](https://github.com/user-attachments/assets/8a537967-552f-4b93-a2c1-0d7fa174f2c3)



  </div>
  </details>

<details>
<summary> Schedule </summary>
<div markdown="1">
  
![image](https://github.com/user-attachments/assets/618f64c1-9b76-465c-a3ae-cacf262e75dc)


</div>
  </details>

<details>
<summary> DailyPlan </summary>
<div markdown="1">
  
![image](https://github.com/user-attachments/assets/2013ac6b-3647-4996-a672-98ed0924bd31)


</div>
</details>

  <details>
<summary> Marker </summary>
<div markdown="1">
  
![image](https://github.com/user-attachments/assets/1170e6fe-a658-44e4-a3e1-a29130b46f74)



</div>
</details>

  <details>
<summary> Place </summary>
<div markdown="1">
  
![image](https://github.com/user-attachments/assets/3b16493b-9023-496f-8078-b3ec24be2146)


</div>
  </details>


  <details>
<summary> Like </summary>
<div markdown="1">
  
![image](https://github.com/user-attachments/assets/a84227f1-8ff6-4502-a6cd-bec625b3ca0e)



</div>
</details>

  <details>
<summary> Invitation </summary>
<div markdown="1">
  

![image](https://github.com/user-attachments/assets/b0ae98e0-4dd7-479d-adc1-52ef9de9d321)


</div>
</details>

  <details>
<summary> Comment </summary>
<div markdown="1">
  

![image](https://github.com/user-attachments/assets/82b36ff5-3294-4923-82d4-209b115b2d20)


</div>
</details>

  <details>
<summary> Review </summary>
<div markdown="1">
  
![image](https://github.com/user-attachments/assets/bf450161-b1cb-49fa-8a55-00a40104788c)


</div>
</details>

</div>
</details>



</div>
</details>


<details>
<summary>💬 코드 리뷰 pn rule</summary>
<div markdown="1">
  
## 💬 코드 리뷰 Pn rule

- **P1: 꼭 반영해주세요 (Request changes)**

리뷰어는 PR의 내용이 서비스에 중대한 오류를 발생할 수 있는 가능성을 잠재하고 있는 등 중대한 코드 수정이 반드시 필요하다고 판단되는 경우, P1 태그를 통해 리뷰 요청자에게 수정을 요청합니다. 리뷰 요청자는 p1 태그에 대해 리뷰어의 요청을 반영하거나, 반영할 수 없는 합리적인 의견을 통해 리뷰어를 설득할 수 있어야 합니다.

- **P2: 적극적으로 고려해주세요 (Request changes)**

작성자는 P2에 대해 수용하거나 만약 수용할 수 없는 상황이라면 적합한 의견을 들어 토론할 것을 권장합니다.

- **P3: 웬만하면 반영해 주세요 (Comment)**

작성자는 P3에 대해 수용하거나 만약 수용할 수 없는 상황이라면 반영할 수 없는 이유를 들어 설명하거나 다음에 반영할 계획을 명시적으로(JIRA 티켓 등으로) 표현할 것을 권장합니다. Request changes 가 아닌 Comment 와 함께 사용됩니다.

- **P4: 반영해도 좋고 넘어가도 좋습니다 (Approve)**

작성자는 P4에 대해서는 아무런 의견을 달지 않고 무시해도 괜찮습니다. 해당 의견을 반영하는 게 좋을지 고민해 보는 정도면 충분합니다.

- **P5: 그냥 사소한 의견입니다 (Approve)**

작성자는 P5에 대해 아무런 의견을 달지 않고 무시해도 괜찮습니다.

- **Q: 코드에 관한 질문입니다 (Comment)**

작성자는 질문에 대해 답변해주는 것을 권장합니다.

ex) P5 - 코드 확인했어요 고생하셨습니다!

![image](https://github.com/user-attachments/assets/0ab10e46-622d-46da-8ea1-cfcefd5073f3)

</div>
</details>

</div>
</details>


<details>
<summary>🌠 Commit Rule</summary>
<div markdown="1">
  
## 🌠 Commit Rule
- **[#이슈번호] '작업 타입' : '작업 내용'**
> ex)  
> [#36] ✨ feat : 회원가입 기능 추가
> - 구체적인 내용1
> - 구체적인 내용2
> - 구체적인 내용3
> - 구체적인 내용이 있을 경우을 아래에 작성
> - 여러 줄의 메시지를 작성할 땐 "-"로 구분

<br>

| 작업 타입 | 작업내용 |
| --- | --- |
| ✨ feat | 새로운 기능을 추가 |
| 🎉 add | 없던 파일을 생성함, 초기 세팅 |
| 🐛 bugfix | 버그 수정 |
| ♻️ refactor | 코드 리팩토링 |
| 🩹 fix | 코드 수정 |
| 🚚 move | 파일 옮김/정리 |
| 🔥 del | 기능/파일을 삭제 |
| 🍻 test | 테스트 코드를 작성 |
| 🎨 readme | readme 수정 |
| 🙈 gitfix | gitignore 수정 |
| 🔨script | package.json 변경(npm 설치 등) |


</div>
</details>



<br>

<div id="ghksrud">
  
# 🌌 Environment Variable
```env
ACCESS_TOKEN_EXPIRATION=6000000
ADMIN_TOKEN={admin_token}
APP_PASSWORD={password}
AWS_ACCESS_KEY={aws_access_key}
AWS_BUCKET_NAME={bucket_name}
AWS_REGION={region}
AWS_SECRET_KEY={aws_secret_key}
CACHE_REDIS_HOST={redis_host}
CACHE_REDIS_PORT={port}
DB_HOST={host}
DEFAULT_IMAGE_URL={url_image}
JWT_SECRET_KEY={jwt_secret_key}
MYSQL_DATABASE={database}
MYSQL_ROOT_PASSWORD={password}
PASSWORD={password}
REDIS_PASSWORD={redis_password}
REFRESH_TOKEN_EXPIRATION=12000000
SOCIAL_KAKAO_CLIENT_ID={kakao_client_id}
SOCIAL_KAKAO_CLIENT_SECRET={kakao_client_secret}
SOCIAL_KAKAO_REDIRECT_URI={kakao_redirect_url}
USERNAME={admin}
```


