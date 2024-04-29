FROM node:lts-alpine
WORKDIR /front
COPY package*.json ./
RUN npm install
COPY ./public ./public
COPY ./src ./src
COPY ./*.js ./
COPY ./*.json ./
EXPOSE 3000
CMD npm run serve -- --port 3000