# Image de base à partir de laquelle notre nouvelle image sera créée
FROM nginx:latest


RUN apt update
RUN apt install -y vim nano

# Répertoire dans lequel on se place
# WORKDIR repertoire_travail
WORKDIR /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

COPY dist/space-station-front/browser/ /usr/share/nginx/html/

EXPOSE 80
