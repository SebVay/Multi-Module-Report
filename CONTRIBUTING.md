# Contributing to Danger Modules Report

First off, thank you for considering contributing to Danger Modules Report! It's people like you that make open source great.

## Where do I start?

If you're new to the project, here are a few ways you can get started:

Remember, as a newcomer, your fresh perspective is invaluable in identifying areas that might be unclear or could be improved. Don't hesitate to point out things that seem confusing or overly complex.

*   **Issues**: Check out the [issue tracker](https://github.com/SebVay/danger-modules-report/issues) for bugs, feature requests, and other tasks.
*   **Documentation**: Help us improve the documentation by fixing typos, adding examples, or clarifying confusing sections.
*   **Code**: If you're feeling adventurous, you can dive into the code and start fixing bugs or implementing new features such as:
  * New Hosts (Bitbucket / GitLab)

To contribute, please fork the repository and open a pull request with your changes.

## Code style

This project uses [Spotless](https://github.com/diffplug/spotless) and [Detekt](https://detekt.github.io/detekt/) to enforce code style. 

`Detekt` & `Spotless` are run with `./gradlew codeQualityCheck`. Run that command before submitting a pull request to make sure your code conforms to the project's style.

➡️ Run `git config core.hooksPath .githooks` after cloning the repository: This will set up Git hooks that automatically run `./gradlew codeQualityCheck` on each push.

 * When `./gradlew codeQualityCheck` fails:
   * If it's a Spotless issue, runs `./gradlew spotlessApply` to reformat your changes automatically.
   * If it's a Detekt issue, check `codeQualityCheck` output and fix the underlying issue.

## Development Tips

### Testing the report locally

Before opening a pull request, you can test the report generation locally. This project provides a Docker-based environment to run Danger locally, regardless of your operating system.

1.  **Install Docker**: Make sure you have Docker installed on your machine.
2.  **Run the local script**: Execute the following command to run Danger locally:

    ```bash
    ./danger/local/run_danger_locally.sh
    ```

The report will be generated and displayed in the terminal and you'll be able to verify your changes there.

When you open a pull request, please make sure it's ready for review. Pull requests that are still a work in progress should be marked as a draft.

## License

By contributing to Danger Modules Report, you agree that your contributions will be licensed under the MIT License.
