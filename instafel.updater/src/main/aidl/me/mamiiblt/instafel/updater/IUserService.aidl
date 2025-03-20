// IUserService.aidl
package me.mamiiblt.instafel.updater;

interface IUserService {

    void destroy() = 16777114; // Destroy method defined by Shizuku server

    String executeShellCommand(String command) = 0;
}
