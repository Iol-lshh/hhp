git submodule add https://github.com/Iol-lshh/hhp-sub.git src/main/resources/properties

git submodule update --remote --merge

git config -f .gitmodules --remove-section submodule.src/main/resources/properties


