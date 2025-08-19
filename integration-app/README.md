# Pull Request Fixture Module

This module provides test fixtures for the Danger modules report plugin. It consists of example files and directory
structures that simulate various Pull Request changes.

- The folders and files under this module are intentionally used to simulate PR diffs: added, updated, and removed
  files.
- There is no production logic here; these files are only fixtures to exercise the reporting pipeline.
- This module serves as an integration test environment for the Danger plugin:
    - The Dangerfile integrates the danger-modules-report SDK to verify its functionality
    - The module structure itself provides test scenarios without containing any production code

## Example Use Cases

- Testing report formatting with various file statuses (added/modified/deleted)
- Verifying module detection and grouping logic
- Validating line count calculations for changes

## Publishing and Running Danger

To publish the module locally and run Danger:

- Run the script: `./run_danger.sh` in `integration-app`
    - This will first publish the artifacts locally ([local-maven-repository](../build/local-maven-repository))
    - Then execute Danger with the Dangerfile.df.kts configuration