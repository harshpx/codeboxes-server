# !/bin/bash

# Stop and remove the running codeboxes-server container (if it exists)
echo "[INFO] Performing an existing codeboxes-server docker container cleanup..."
docker stop codeboxes-server || true
docker rm -f codeboxes-server || true

# Free up port 8080 (if still in use) (fallback)
echo "[INFO] Freeing up port 8080 (if still in use)..."
# Use 'lsof' which is generally more reliable and available on Ubuntu
if command -v lsof >/dev/null 2>&1; then
  sudo lsof -t -i:8080 | xargs -r kill -9 || true
else
  # Fallback for systems without lsof, though Ubuntu usually has it
  if command -v fuser >/dev/null 2>&1; then
    fuser -k 8080/tcp || true
  else
    # Install psmisc if fuser is not found (for Alpine-like systems, less common on Ubuntu)
    sudo apt-get update && sudo apt-get install -y psmisc || true
    fuser -k 8080/tcp || true
  fi
fi
sleep 5

# Build the containers
echo "[INFO] Building codeboxes-server..."
docker build -t codeboxes-server ./server
echo "[INFO] Building code-runner..."
docker build -t code-runner ./code-runner

# start codeboxes-server container
echo "[INFO] Starting codeboxes-server container..."
docker run -d \
  --name codeboxes-server \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -p 8080:8080 codeboxes-server

echo "[INFO] Deployment complete."