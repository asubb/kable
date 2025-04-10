## Documentation

While new features are added update the documentation under `docs/` folder

- Have a following sections in different files:
    1. Functionality provided (one file per module)
    2. Create examples based on tests
    3. Requirements to run the application
    4. Have an README.md file with the glossary

- Keep a changelog of changes in the `docs/changelog.md` file. Leave a 3-4 sentences about the requirements and solution, with header and date it has added on.

## Common Libraries

- For logging prefer logback with Kotlin logging library `io.github.oshai:kotlin-logging-jvm`:
    - and notation like:
  ```kotlin
  log.info { "Message here" }
  log.error(e) { "Some error happened" }
  ```
- Testing libraries
    - kotest
    - `com.willowtreeapps.assertk:assertk-jvm` for assertions
    - `org.mockito.kotlin:mockito-kotlin` for mocks
    -

## Test development

- Use describe spec as a default format for tests.

