@echo off

REM Installation instructions: https://help.github.com/articles/using-jekyll-with-pages/

IF EXIST Gemfile.lock del Gemfile.lock

bundle exec jekyll serve --watch
