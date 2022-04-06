# PlaceStom
![Build Status](https://img.shields.io/github/workflow/status/WeiiswurstDev/PlaceStom/Java%20CI%20with%20Gradle?style=for-the-badge)
![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/weiiswurstdev/PlaceStom?style=for-the-badge)
![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/WeiiswurstDev_PlaceStom?server=https%3A%2F%2Fsonarcloud.io&sonarVersion=8.9&style=for-the-badge)
![GitHub](https://img.shields.io/github/license/WeiiswurstDev/PlaceStom?style=for-the-badge)

PlaceStom is a [Minestom](https://github.com/Minestom/Minestom) implementation of [r/place](https://www.reddit.com/r/place/).

## Running PlaceStom
1. Clone the repository to your computer
2. Run ``gradlew shadowJar`` in the folder
3. Copy the built jar from ``build/libs`` to a new folder (this is where your server will run)
4. Run the server with ``java -jar PlaceStom-??.jar``.

## Current features
* Placing and removing blocks on the canvas
* Loading and storing the canvas in an [H2 database](https://h2database.com/html/main.html)
* Checking for updates
* Admin commands:
  * No cooldown for the admin (Permission: placestom.noCooldown)
  * Change cooldown for everyone (Permission: placestom.setCooldown)
  * Clear a chunk (/clearchunk, `placestom.clearchunk`)
  * Generate a server icon from the canvas around you (/generateservericon, `placestom.servericon`)
  * (You need an extension like [JustPermissions](https://github.com/JustDoom/JustPermissions) to give players permissions)

## Upcoming features
* Storing the player position (or maybe in an extension?)
* Storing player statistics (blocks placed in total, ...)
* A better console?

## Contributing
We always want to see contributors! To contribute:
* Fork the repository into your GitHub account
* Clone the fork into your favourite IDE, e.g. [IntelliJ IDEA](https://www.jetbrains.com/idea/)
* Make a change
* Try it locally: Run the main method in Place
* Happy with it? Push it to your fork, then create a pull request
