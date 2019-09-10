# Custodian

Custodian provides dependency updates & project governance automation for Gradle projects.

## Features

* Extract dependency and project metadata information from GitHub and BitBucket projects.
* Searches for dependency updates (Maven, GitHub Releases API, Docker Hub, ...)
* Creates PRs with changes

## Modules

Custodian has the following modules:

* `custodian-service`: core project library
* `custodian-cli`: Custodian command line application
* `sentinel-gradle-plugin`: gradle plugin to extract project information