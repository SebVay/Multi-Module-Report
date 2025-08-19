# Get the absolute path of the script's directory
script_path="$(cd "$(dirname "$0")" && pwd)"

# Set project root directory one level up from script path
project_root="$script_path"/..

# Change to project root directory or exit if change fails
cd "$project_root" || exit

# Run gradle task to publish the artifacts locally
./gradlew danger-modules-report:publishAllPublicationsToRootProjectRepository

# Display message and wait for user input before exiting
read -n 1 -s -r -p "Danger report generated at '$script_path/output' - Press any key to exit."