- 원하는 위치로 이동하여
    - 서브모듈 생성
        - git submodule add <repository-url>
    - 서브모듈의 이름을 바꾸려면
      - git submodule add <repository-url> <directory-name>
    - 예
      1. `cd src/main/resources`
      2. `git submodule add https://github.com/Iol-lshh/hhp-sub.git properties`


git submodule init
git submodule update
git submodule update --remote --merge

git config -f .gitmodules --remove-section submodule.src/main/resources/properties

- 해당 서브모듈을 .gitmodules 파일에서 제거
  - git config -f .gitmodules --remove-section submodule.<submodule-name>
- .git/config 파일에서 서브모듈 관련 항목을 제거합니다
  - git config --remove-section submodule.<submodule-name>
- Git에서 서브모듈 캐시를 제거
  - git rm --cached <submodule-path> 
- 서브모듈과 관련된 디렉토리를 직접 삭제하고 커밋
  - rm -rf .git/modules/<submodule-path> rm -rf <submodule-path> 
- 변경 사항을 커밋
  - git add .gitmodules git commit -m "Removed submodule <submodule-name>" 


