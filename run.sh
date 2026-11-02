#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p bin
find src -name '*.java' -print > bin/sources.txt
javac --release 21 -encoding UTF-8 -d bin @bin/sources.txt
exec java -cp bin com.platzi.proyecto.MainClass "$@"
