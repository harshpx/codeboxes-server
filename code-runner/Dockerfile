
### -------------------- Using Debian image --------------------###
# # Select the base image
# FROM debian:bullseye-slim

# # Prevent interactive prompts during package installation
# ENV DEBIAN_FRONTEND=noninteractive

# # Install necessary packages
# RUN apt-get update && apt-get install -y \
#     curl \
#     gnupg \
#     build-essential \
#     openjdk-17-jdk \
#     nodejs \
#     npm \
#     python3 \
#     coreutils \
#     ca-certificates

# # # Install Node.js 20 from NodeSource
# # RUN curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
# #     apt-get install -y nodejs

# # Clean up
# RUN apt-get clean && rm -rf /var/lib/apt/lists/*

# # Set the working directory
# WORKDIR /app

# # Copy scripts into the container's working directory
# COPY entrypoint.sh run-js.js run-java.sh run-cpp.sh run-python.py /app/

# # Make the scripts executable
# RUN chmod +x /app/entrypoint.sh /app/run-js.js /app/run-java.sh /app/run-cpp.sh /app/run-python.py

# # entrypoint script to run when the container starts
# ENTRYPOINT ["/app/entrypoint.sh"]


### -------------------- Using Alpine image --------------------###
# Use Alpine as base
FROM alpine:3.20

# Install packages
RUN apk add --no-cache \
    bash \
    openjdk17 \
    nodejs \
    python3 \
    g++ \
    gcc \
    go \
    libc6-compat \
    libstdc++ \
    coreutils \
    ca-certificates

# clean up unnecessary files
RUN rm -rf /var/cache/apk/* /usr/share/man /usr/share/doc /tmp/*

# Set working directory
WORKDIR /app

# Copy all script files
COPY entrypoint.sh run-js.js run-py.py run-java.sh run-cpp.sh run-c.sh run-go.sh /app/

# Make scripts executable
RUN chmod +x /app/entrypoint.sh /app/run-js.js /app/run-py.py /app/run-java.sh /app/run-cpp.sh /app/run-c.sh /app/run-go.sh

# Use entrypoint
ENTRYPOINT ["/app/entrypoint.sh"]
