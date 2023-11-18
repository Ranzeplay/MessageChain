# syntax = docker/dockerfile:1

FROM node:18-alpine as build
ENV NODE_ENV="production"
WORKDIR /src

COPY --link . .
RUN npm i
RUN npm run build
RUN npm prune --omit=dev


FROM nginx:stable-alpine as prod
WORKDIR /
COPY --from=build /src/build /usr/share/nginx/html
COPY ./nginx/site.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
