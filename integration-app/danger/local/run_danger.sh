# Color sugaring
green=$(tput setaf 2)
yellow=$(tput setaf 3)
reset=$(tput sgr0)

# Get the absolute path of the script's directory
script_path="$(cd "$(dirname "$0")" && pwd)"

# Set project root directory one level up from script path
project_root="$script_path"/../../..

# Change to project root directory
cd "$project_root" || exit

echo "${green}Publishing 'danger-modules-report' at build/local-maven-repository ${reset}"

# Run gradle task to publish the artifacts locally
./gradlew danger-modules-report:publishAllPublicationsToRootProjectRepository

# Clean any previous temp container used for extracting output
docker rm danger-module-report-image >/dev/null 2>&1 || true

docker build -f integration-app/danger/local/Dockerfile -t danger-module-report-image . || {
    echo "${yellow}Error: Docker build failed for image 'danger-module-report-image'."
    echo "One of the reasons may be that your docker engine is not running.${reset}"
    read -n 1 -s -r -p "Press any key to exit."
    exit 1
}

# Display message and wait for user input before exiting
echo "${green}Danger report generated - Check the command line output${reset}"
read -n 1 -s -r -p "Press any key to exit."