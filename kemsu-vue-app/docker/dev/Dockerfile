FROM node:lts-alpine
WORKDIR /front
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 3000
CMD npm run serve -- --port 3000