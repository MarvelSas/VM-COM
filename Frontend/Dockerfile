FROM node:18-alpine AS builder

WORKDIR /app
COPY package*.json ./

RUN npm install -g @angular/cli
RUN npm install

COPY . .
RUN ng build

FROM nginx:latest
COPY --from=builder /app/dist/frontend /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
