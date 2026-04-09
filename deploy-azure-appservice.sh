#!/usr/bin/env bash
set -euo pipefail

# Usage:
# ./deploy-azure-appservice.sh <resource-group> <app-name> <region> [plan-name]
#
# Example:
# ./deploy-azure-appservice.sh sickles-rg sicklesscript-blog eastus sickles-plan

RG="${1:-}"
APP_NAME="${2:-}"
LOCATION="${3:-}"
PLAN_NAME="${4:-sicklesscript-plan}"

if [[ -z "$RG" || -z "$APP_NAME" || -z "$LOCATION" ]]; then
  echo "Usage: $0 <resource-group> <app-name> <region> [plan-name]"
  exit 1
fi

echo "Building application..."
mvn clean package -DskipTests

echo "Creating resource group (if needed)..."
az group create --name "$RG" --location "$LOCATION" >/dev/null

echo "Creating App Service plan (Linux, B1)..."
az appservice plan create \
  --name "$PLAN_NAME" \
  --resource-group "$RG" \
  --is-linux \
  --sku B1 >/dev/null

echo "Creating Java 17 web app..."
az webapp create \
  --resource-group "$RG" \
  --plan "$PLAN_NAME" \
  --name "$APP_NAME" \
  --runtime "JAVA|17-java17" >/dev/null

echo "Configuring startup command..."
az webapp config set \
  --resource-group "$RG" \
  --name "$APP_NAME" \
  --startup-file "java -jar /home/site/wwwroot/target/sicklesscript-0.0.1-SNAPSHOT.jar" >/dev/null

echo "Deploying packaged app..."
az webapp deploy \
  --resource-group "$RG" \
  --name "$APP_NAME" \
  --src-path "target/sicklesscript-0.0.1-SNAPSHOT.jar" \
  --type jar >/dev/null

echo "Deployment complete: https://$APP_NAME.azurewebsites.net/posts"
