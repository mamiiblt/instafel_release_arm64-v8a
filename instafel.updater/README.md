# Instafel Updater

Instafel Updater is an automated update tool based on **Shizuku & Root**. This tool allows you to automatically download and install updates without the need for manual intervention. By leveraging Shizuku, updates are installed seamlessly in the background, ensuring your Instafel is always up-to-date with minimal effort.

## How It Works

Instafel Updater uses **WorkManager** to perform periodic checks for updates. This ensures that the app stays up-to-date without requiring manual checks from the user. In addition, a **Foreground Service** is utilized to prevent the process from being terminated by the Android system during the update process. Once an update is detected, the update package is downloaded and installed automatically using the **pm** (Package Manager) command. By combining these technologies, the Instafel Updater provides a fully automated update service for Instafel, making the update process completely hands-free.

## Installation

To install Instafel Updater, you can download the latest builds from the [Releases](https://github.com/mamiiblt/instafel-updater/releases) section of this repository.
## Libraries

A special thank you to the creators of the following libraries, whose contributions made this project possible:

- [Shizuku](https://shizuku.rikka.app/): A powerful tool for accessing Android system services.
- [WorkManager](https://developer.android.com/reference/androidx/work/WorkManager): A library for managing background tasks in a reliable way.
- [OkHttp](https://square.github.io/okhttp/): A high-performance HTTP client used for checking updates.
- [Material-You-Preferences](https://github.com/TTTT55/Material-You-Preferences): A library for creating modern, Material You-style preference screens.

## Contributions

If you'd like to contribute to this project, feel free to submit a pull request or open an issue if you encounter any bugs. Contributions are always welcome!

Developed with ❤️ by mamiiblt
