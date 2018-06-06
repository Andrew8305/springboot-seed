# SpringBoot-Seed

## Introduction

> An example project based on SpringBoot2 integrated with oauth2 ( token persisted on redis ), swagger2, restful API, h2db, wechat miniapp, druid, mybatis ( generator and pagehelper included).

## Preparation
> install plugin `lombok` if you are using IDEA

> Create mysql database named `seed` by `db/schema.sql` & `db/data.sql`, then Run SpringBoot Application

## Features


#### druid
> see `http://localhost:8000/druid/login.html`

> username `root` and password `root` is same as mysql database

#### h2db console
> see `http://localhost:8000/h2`

> active when spring.profiles.active=dev

#### mybatis generator
> run `mvn mybatis-generator:generate`

#### swagger
> see `http://localhost:8000/swagger-ui.html `

#### oauth2
>use `curl http://localhost:8000/oauth/token -X POST -u client:security -d "grant_type=password&username=admin&password=admin"` to get access token, for example : `69aaaeb8-49c2-410d-8253-ad6c003c6091`

>then we can use `curl -X PUT -H "Authorization: Bearer 69aaaeb8-49c2-410d-8253-ad6c003c6091" --header "Content-Type: application/json" -d "{  \"newPassword\": \"new\",  \"oldPassword\": \"admin\"}" "http://localhost:8000/user/password"` to modify password

>also `curl http://localhost:8000/user/1 -X DELETE -H "Authorization: Bearer 69aaaeb8-49c2-410d-8253-ad6c003c6091"` to access other authenticated url

>also with authorization_code `http://localhost:8000/oauth/authorize?response_type=code&client_id=client&redirect_uri=http://www.baidu.com`

#### remote service & client

> `https://github.com/ustcwudi/springboot-service`
> `https://github.com/ustcwudi/vue-seed`
