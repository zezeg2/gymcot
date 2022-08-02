# 헬스장 커뮤니티 프로젝트

## 요구사항

### ✣ Security

- 회원가입 방식

	- UserRequestDto를 통해 json형식으로 객체를 받아 User 데이터베이스에 저장
	- OAuth2를 통해 인증된 사용자를 데이터 베이스에 저장

- Spring Security를 이용한 인증, 인가을 사용한다

	- JWT 토큰을 이용한 인증
	- JWT 토큰이 만기되면 재인증 필요
	- 헤더에 remember-me 를 true로 명시할 시 JWT 토큰이 만기되더라도 자동으로 새로운 JWT 토큰 발행 
	- 권한은 3단계로 구분한다(member, manager, admin)
		- member -> 일반 유저
		- manager -> 헬스장(업주) 등록한 유저
		- admin -> 모든 유저 관리(권한 승격 및 시설등록 등의 권한)

- Security Filter Chain(Authentication, Authorization)에서 예외 발생시  json 형식의 response 

	```json
	{
	  "code": "",
	  "message": "",
	  "detail": {
	    "field": "",
	    "reason": ""
	  }
	}
	```

	- Authentication -> AuthenticationFailureHandler 구현
	- Authorization -> AuthenticationEntrypoint 구현

### ✣ API 기능 요구사항

### User

- UserApiController(Interface)
	- 공동으로 사용하는 의존성 주입
	- 공동으로 사용하는 메서드 정의 -> Authentication 객체로 부터 user_id를 가져올 수 있도록

- CommonController(/) : 권한에 관계없이  공통으로 사용하는 API

	| url      | authorization | return         | Description | method |
	| -------- | ------------- | -------------- | ----------- | ------ |
	| "/", "." | ALL           | String "index" | 테스트용    | get    |
	| "/join"  | AlLL          | void           | 회원가입    | post   |

- MemberApiController("/api/v1/member") : Member 권한을 가지고 있는 User에 대한 컨트롤러

	| url            | authorization | return          | Description               | method | 비고                       |
	| -------------- | ------------- | --------------- | ------------------------- | ------ | -------------------------- |
	| "/"            | ALL           | void            | Member 정보 업데이트      | put    |                            |
	| "/"            | ALL           | UserResponseDto | Member 정보 가져오기(Dto) | get    |                            |
	| "/gym/{gymId}" | ALL           | void            | 헬스장 등록               | put    | Manager가 approve상태 변경 |
	| "/attend"      | ALL           | void            | 출석 상태 변경            | put    |                            |
	| "delete"       | ALL           | void            | 회원 탈퇴                 | delete |                            |

	

- ManagerApiController ("/api/v1/manager") :  Manager 권한을 가지고 있는 User에 대한 컨트롤러  

	| url                           | authorization | return                | Description                                                  | method | 비고                       |
	| ----------------------------- | ------------- | --------------------- | ------------------------------------------------------------ | ------ | -------------------------- |
	| "/"                           | Manager       | void                  | Manager 정보 업데이트                                        | put    |                            |
	| "/"                           | Manager       | UserResponseDto       | Manager 정보 가져오기(Dto)                                   | get    |                            |
	| "/approve-member/{username}"" | Manager       | void                  | 자신의 헬스장에 등록한 User의 enrolled 필드 수정             | put    | Manager가 approve상태 변경 |
	| "/expel-member/{username}"    | Manager       | void                  | 자신의 헬스장에 등록한 User의 gym 필드, enrolled필드 수정 -> 강제 탈퇴 기능 | put    |                            |
	| "/waiting-list"               | Manager       | List<UserResponseDto> | 자신의 헬스장에 등록했으나 승인되지 않은 유저의 리스트       | get    |                            |
	| "/enrolled-user-list"         | Manager       | List<UserResponseDto> | 자신의 헬스장에 등록 하고 승인된 유저의 리스트               | get    |                            |
	| "delete"                      | Manager       | void                  | 회원 탈퇴                                                    | delete |                            |

	

- AdminApiController ("/api/v1/admin") : Admin 권한을 가지고 있는 User에 대한 컨트롤러

	| url                        | authorization | return                | Description                | method | 비고                             |
	| -------------------------- | ------------- | --------------------- | -------------------------- | ------ | -------------------------------- |
	| "/"                        | Admin         | void                  | Admin 정보 업데이트        | put    |                                  |
	| "/"                        | Admin         | UserResponseDto       | Admin 정보 가져오기(Dto)   | get    |                                  |
	| "/"/cr/{username}/{role}"" | Admin         | void                  | User의 권한 변경           | put    | {role} : member,  manager, admin |
	| "/managers"                | Admin         | List<UserResponseDto> | Manager 권한의 유저 리스트 | get    |                                  |
	| "delete"                   | Admin         | void                  | admin 계정 삭제            | delete |                                  |

### GymController

- 헬스장 등록(manager)
- 헬스장 수정(manager)
- 헬스장 삭제(admin)
- 헬스장 승인상태 변경(admin)
- 등록된 헬스장 목록 조회(admin)
- 승인되지 않은 헬스장 목록 조회(admin)
- 등록된 헬스장 검색(member)





