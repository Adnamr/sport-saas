# Stage 1: Build
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files first (for layer caching)
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build for production
RUN npm run build -- --configuration=production

# Stage 2: Runtime
FROM nginx:alpine AS runtime

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Copy built application from builder stage
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Run nginx
CMD ["nginx", "-g", "daemon off;"]
