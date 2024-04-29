FROM node:lts-alpine
WORKDIR /front
COPY package*.json ./

RUN npm install
COPY ./public ./public
COPY ./src ./src
COPY ./*.js ./
COPY ./*.json ./

VOLUME ./dist

ENTRYPOINT npm run build