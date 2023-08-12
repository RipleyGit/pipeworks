#!/bin/bash

FIND_STR="SNAPSHOT"
for file in $(ls */pom.xml)
do
      POM_PATH=$(pwd)/$file
      COUNT_SNAPSHOT=$(grep -c $FIND_STR $POM_PATH)
      if [ "$COUNT_SNAPSHOT" -eq 0 ];
        then
          echo """$POM_PATH"" 路径下的 pom.xml 文件不包含 SNAPSHOT"
      else
          echo """$POM_PATH"" 路径下的 pom.xml 文件包含 SNAPSHOT，且有 ""$COUNT_SNAPSHOT"" 处，行号以及内容如下："
          grep -nC 1 $FIND_STR $POM_PATH
      fi
done

for file in $(ls pom.xml)
do
      POM_PATH=$(pwd)/$file
      COUNT_SNAPSHOT=$(grep -c $FIND_STR $POM_PATH)
      if [ "$COUNT_SNAPSHOT" -eq 0 ];
        then
          echo """$POM_PATH"" 路径下的 pom.xml 文件不包含 SNAPSHOT"
      else
          echo """$POM_PATH"" 路径下的 pom.xml 文件包含 SNAPSHOT，且有 ""$COUNT_SNAPSHOT"" 处，行号以及内容如下："
          grep -nC 1 $FIND_STR $POM_PATH
      fi
done