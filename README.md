 <h1>📚 인재는 두 배로, 채용시간은 절반으로! Jobkok</h1>
<div  align="center">
  <img width="40%" src="https://user-images.githubusercontent.com/113500789/231082566-b8dc6797-5daf-49cb-b072-a9e67216318e.jpg" alt="roobits">
</div>
</br>

- **팀 명 :**  SSAK3조
- **프로젝트 명 :** JOBKOK
- **프로젝트 기간 :** 2023.02.26 - 2023.04.10
- **한줄 소개 :** 채용관리 서비스
- **테이블 명세서 & ERD :** [테이블 명세서 & ERD](https://www.erdcloud.com/d/zf9iDizMrS2riH2BJ)
- **API명세서 :** [API 명세서](https://documenter.getpostman.com/view/24197090/2s93K1qzkt#intro)
<div><h1> 👨‍👨‍1. 팀원 소개 </h1></div>
<div align = center> 
<table border = 1>
  <th>오태경</th>
  <th>권지수</th>
  <th>제원석</th>
  <th>주찬혁</th>
    <tr>
      <td><a href="https://github.com/Blooossom"><img src="https://avatars.githubusercontent.com/u/113500789?v=4" width=160/></a>
      </td>
      <td><a href="https://github.com/jisoooit"><img src="https://avatars.githubusercontent.com/u/77223718?v=4" width=160/></a>
      </td>
      <td><a href="https://github.com/WONSEOKJE"><img src="https://avatars.githubusercontent.com/u/113500970?v=4" width=160/></a>
      </td>
      <td><a href="https://github.com/crossbell8368"><img src="https://avatars.githubusercontent.com/u/50852143?v=4" width=160/></a>
      </td>
    </tr>
  <tr>
    <td align = center>Redis 이용한 성능 향상<br>JWT 토큰 관리<br>로그인 등 인증  구현</td>
    <td align = center>CI/CD 구축<br>메일링 서비스 구현<br>사전 테스트 코드 작성</td>
    <td align = center>테이블 설계<br>코드 리팩토링<br>인재 가점 관리기능 구현<br>Redis 이용한 성능 향상</td>
    <td align = center>RDS 구축<br>사용자 정의 예외처리<br>채용폼 검색 기능 구현<br>HTTPS 로드 밸런싱</td>
  </tr>
</table>
</div>
<div> <h1> 2. 프로젝트 소개 </h1> </div>

  

## 📖 Intro
> 채용관리에 어려움을 겪는 기업을 위한 채용 관리 서비스로
> 
> 채용 공고를 작성할 시간이 없고, 빠르고 편하게 인재관리를 진행할 수 있게 도와주는
> 
> 기업연계 프로젝트 잡콕입니다!

<br>

## 👨‍👨‍협업 방식
- 매주 월요일, 수요일은 오프라인 미팅으로 온라인으로 진행하며 겪었던 트러블 슈팅 공유와 코드 리뷰를 진행합니다.
- 개발 과정에서 발생하는 에러와 예외는 Slack을 통해 공유하며 협력하여 해결할 수 있도록 합니다.
- 모든 Pull Request는 다른 팀원 1명 이상의 리뷰를 거쳐 Merge 합니다.
- Notion을 활용하여 BE 파트 내부 회의, FE, UXUI와 같은 타 파트 회의록을 작성하여 관리합니다.

<br>

## ✨ 구현 내용
|첫화면 및 로그인 페이지|회원가입 페이지|
|:---:|:---:|
|<img width="100%" alt="첫 화면 gif" src="https://user-images.githubusercontent.com/113500789/231084886-abdac5a5-6b54-4c6b-a106-4f571e280fc4.gif"/>|<img width="100%" alt="로그인 gif" src="https://user-images.githubusercontent.com/113500789/231083483-3acfdbb4-f551-4700-997c-2d3119dc3204.gif"/>|
|- 검색<br/>- 로그인<br/>- 회원가입<br/>- 로그아웃<br/>- 책 등록<br/>- 내 책장<br/>- 마이페이지| 자체 회원가입으로 다음과 같이 구현<br/>- email input<br/>- 지역 input<br/>- nickname input<br/>- password input<br/>- sign-up 버튼<br/>- email input<br/>- password input<br/>- login 버튼|
|**인재 관리**|**채용폼 작성**|
|<img width="100%" alt="마이 룸 생성 gif" src="https://user-images.githubusercontent.com/113500789/231083985-71da428c-ab08-401c-9686-f28bfceb9cac.gif"/>|<img width="100%" src="https://user-images.githubusercontent.com/113500789/231084103-be1057cd-8017-41f6-8400-f6da0b9bcf71.gif"/>|
|책 키워드 검색 → 키워드가 들어간 책들 목록 →유저 등록한 책 목록 → 유저가 클릭한 책 상세페이지를 통한 검색 구현<br/>- 책 키워드 검색창<br/>- 키워드가 들어간 책 리스트<br/>- 유저가 등록한 책 리스트<br/>- 클릭된 책 상세페이지 이동 <br/>책 한줄평 작성, 조회, 수정, |-책 검색 API<br/>- 책 등급 선택|
|**지원서 작성**|**추천 인재**|
|<img width="100%" src="https://user-images.githubusercontent.com/113500789/231084187-acaeea89-bd53-4df6-a637-840451cf1b90.gif"/>|<img width="100%" src="https://user-images.githubusercontent.com/113500789/231089561-97ffc9b0-ad45-46f7-a7a9-5b050cbca405.gif"/>|
|-책 수정|- 책 정보<br/>- 등록한 유저 정보<br/>- 등록한 유저가 등록한 책<br/>- 교환 요청: 쪽지 보내기|
|**채용 단계 수정**|**인재 상세 페이지**|
|<img width="100%" src="https://user-images.githubusercontent.com/113500789/231084352-f4703130-8f56-4599-af19-a8b0baa6b8ec.gif"/>|<img width="100%" src="https://user-images.githubusercontent.com/113500789/231084418-4b5da0bf-931c-46c8-b589-4b9f1e1a8bb9.gif"/>|
|- 주기적으로 새 메세지 조회<br/>- 알림버튼을 클릭해 쪽지 페이지 진입<br/>- 쪽지페이지에서 쪽지 개별 페이지 진입|-받은메세지, 보낸 메세지 조회, 삭제|
|**알림 센터 메시지 보내기**|
|<img width="100%" src="https://user-images.githubusercontent.com/113500789/231084465-0efefd8d-e0eb-48ae-bc48-2a422bc68f68.gif"/>|
|- 책 등록, 조회, 수정, 삭제|

## 🔧 Skills

| Git | Github | Slack |
| :---: | :---: | :---: |
| <img alt="git logo" src="https://git-scm.com/images/logos/logomark-orange@2x.png" width="65" height="65" > | <img alt="github logo" src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png" width="65" height="65"> | <img alt="Slack logo" src="https://user-images.githubusercontent.com/113500789/231091759-8a7df6f6-4b24-49b4-bc65-0cb5cc459716.png" height="65" width="65"> |

| Java | mySQL | JWT | AWS | Spring | Spring<br>Boot | JPA | Spring<br>Security | Redis |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| <div style="display: flex; align-items: flex-start;"><img src="https://techstack-generator.vercel.app/java-icon.svg" alt="icon" width="65" height="65" /></div> | <div style="display: flex; align-items: flex-start;"><img src="https://techstack-generator.vercel.app/mysql-icon.svg" alt="icon" width="65" height="65" /></div> | <div style="display: flex; align-items: flex-start;"><img src="https://blog.kakaocdn.net/dn/cqbtEQ/btrZISJO4rM/psTAeZ2SeJr4mw2z80gt00/img.png" alt="icon" width="65" height="65" /></div> | <div style="display: flex; align-items: flex-start;"><img src="https://techstack-generator.vercel.app/aws-icon.svg" alt="icon" width="65" height="65" /></div> |<div style="display: flex; align-items: flex-start;"><img alt="spring logo" src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" height="50" width="50" ></div> |<div style="display: flex; align-items: flex-start;"> <img alt="spring-boot logo" src="https://t1.daumcdn.net/cfile/tistory/27034D4F58E660F616" width="65" height="65" ></div> |<div style="display: flex; align-items: flex-start;"> <img alt="JPA logo" src="https://blog.kakaocdn.net/dn/pDeWq/btrEdsKjNw0/ribKZh6VkOzOKack7yHikk/img.png" width="65" height="65" ></div>  |<div style="display: flex; align-items: flex-start;"> <img alt="Spring Security logo" src="https://velog.velcdn.com/images/shawnhansh/post/8c5ab467-22b0-4a1a-97a9-5d6c732687bb/image.png" width="65" height="65" ></div> |<div style="display: flex; align-items: flex-start;"><img src="https://user-images.githubusercontent.com/113500789/231090458-82a7c6da-8f9e-45cd-a776-78a8cc25ff21.png" alt="icon" width="65" height="65" /></div> |
<br/>
