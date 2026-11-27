# Image de base à partir de laquelle notre nouvelle image sera créée
FROM nginx:latest

RUN apt update

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY dist/space-station-front/browser/ /usr/share/nginx/html/

EXPOSE 80
