#!/bin/sh
rm -rf dist/
rm -rf node_modules/
npm install
npm run build
rm ../backend/src/main/resources/static/ -rf
mkdir ../backend/src/main/resources/static/
touch ../backend/src/main/resources/static/.gitkeep
cp dist/* ../backend/src/main/resources/static/ -r
