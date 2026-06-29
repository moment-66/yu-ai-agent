# ====== 第一阶段：Node 构建前端 ======
FROM node:20-alpine AS builder

WORKDIR /build

# 复制 package.json 利用缓存
COPY package.json package-lock.json ./
RUN npm ci --production=false

# 复制源码并构建
COPY . .
RUN npm run build

# ====== 第二阶段：Nginx 运行 ======
FROM nginx:stable-alpine

# 删除默认配置
RUN rm -rf /usr/share/nginx/html/* /etc/nginx/conf.d/default.conf

# 复制构建产物
COPY --from=builder /build/dist /usr/share/nginx/html

# 复制自定义 Nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD wget -qO- http://localhost/ || exit 1
