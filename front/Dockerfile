FROM node:20-alpine AS builder

WORKDIR /front

COPY package*.json .
RUN npm install
# COPY . ./front 会导致文件在 /front/front/ 目录
COPY . .

EXPOSE 5173

CMD ["npm", "run", "dev"]