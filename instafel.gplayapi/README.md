# Instafel GPlayAPI

This project used to grab latest Instagram apks from Google Play

# How to run

First of all, you need to update properties file with your credentials (you need to crate a file named 'gplayapi.properties' in me.mamiiblt.instafel.gplayapi's [resource folder](https://github.com/mamiiblt/instafel-gplayapi/tree/main/src/main/resources/))

```properties
email = <Google account mail>
aas_token = aas_et/.....
```
> You need to get AAS Token from [Aurora's Authenticator](https://github.com/whyorean/Authenticator/releases/latest) app.

> Instafel GPlayapi needs github_pat, github_releases_link and telegram_bot_token for normally, you can customize for your jobs too.

You are ready to use Aurora Store's GPlayAPI now!

# How to build

You can build project with `./gradlew :instafel.gplayapi:build-jar` command. Output JAR will be saved in `/instafel.gplayapi/output/ifl-gplayapi-..-snapshot.jar` format 

# Don't Forget

Thanks [AuroraOSS/gplayapi](https://gitlab.com/AuroraOSS/gplayapi) for made this project possible.