package com.angkorteam.android.master.command;

import com.angkorteam.android.master.Utilities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@ShellComponent
public class ProjectCommand {

    @ShellMethod(key = "create", value = "create project")
    public String create(
            @ShellOption(help = "name") String name,
            @ShellOption(help = "applicationId") String applicationId,
            @ShellOption(help = "gradle version", defaultValue = "6.8.2") String gradleVersion,
            @ShellOption(help = "compile sdk version", defaultValue = "30") String compileSdkVersion,
            @ShellOption(help = "build tools version", defaultValue = "30.0.3") String buildToolsVersion,
            @ShellOption(help = "min sdk version", defaultValue = "21") String minSdkVersion,
            @ShellOption(help = "target sdk version", defaultValue = "30") String targetSdkVersion,
            @ShellOption(help = "core ktx version", defaultValue = "1.5.0-beta01") String coreKtxVersion,
            @ShellOption(help = "appcompat version", defaultValue = "1.3.0-beta01") String appcompatVersion,
            @ShellOption(help = "material version", defaultValue = "1.3.0") String materialVersion,
            @ShellOption(help = "navigation compose version", defaultValue = "1.0.0-alpha07") String navigationComposeVersion,
            @ShellOption(help = "paging compose version", defaultValue = "1.0.0-alpha07") String pagingComposeVersion,
            @ShellOption(help = "activity compose version", defaultValue = "1.3.0-alpha02") String activityComposeVersion,
            @ShellOption(help = "hilt version", defaultValue = "1.0.0-alpha03") String hiltVersion,
            @ShellOption(help = "room version", defaultValue = "2.3.0-beta02") String roomVersion,
            @ShellOption(help = "retrofit version", defaultValue = "2.9.0") String retrofitVersion,
            @ShellOption(help = "okhttp version", defaultValue = "4.9.1") String okhttpVersion,
            @ShellOption(help = "constraint layout compose version", defaultValue = "1.0.0-alpha02") String constraintLayoutComposeVersion,
            @ShellOption(help = "compose version", defaultValue = "1.0.0-alpha12") String composeVersion,
            @ShellOption(help = "glide version", defaultValue = "4.12.0") String glideVersion,
            @ShellOption(help = "datastore version", defaultValue = "1.0.0-alpha06") String datastoreVersion,
            @ShellOption(help = "kotlin version", defaultValue = "1.4.30") String kotlinVersion,
            @ShellOption(help = "hilt plugin version", defaultValue = "2.32-alpha") String hiltPluginVersion,
            @ShellOption(help = "build tools gradle version", defaultValue = "7.0.0-alpha08") String buildToolGradleVersion,
            @ShellOption(help = "android sdk dir") String sdkDir) throws Throwable {
        if (name.length() == 0) {
            return "invalid name";
        } else {
            if (name.matches("^[a-zA-Z0-9][a-zA-Z0-9]*([ _.-][a-zA-Z0-9]+)+[a-zA-Z0-9]$") || name.matches("[a-zA-Z0-9]*")) {
            } else {
                return "invalid name";
            }
        }

        if (!applicationId.matches("^[a-z][a-z0-9]*(\\.[a-z0-9]+)+[0-9a-z]$")) {
            return "invalid applicationId";
        }

        String sources = "app/src/main/java";
        String resources = "app/src/main/res";

        String applicationIdFolder = StringUtils.replace(applicationId, ".", "/");
        int originalPkg = "com.angkorteam.blueprint.android".length();

        File workspace = new File(FileUtils.getTempDirectory(), RandomStringUtils.randomAlphanumeric(10));
        workspace.mkdirs();

        try (InputStream stream = ProjectCommand.class.getResourceAsStream("/blueprint.zip")) {
            try (ZipInputStream zip = new ZipInputStream(stream)) {
                while (true) {
                    ZipEntry entry = zip.getNextEntry();
                    if (entry == null) {
                        break;
                    }
                    if (!entry.isDirectory()) {
                        byte[] content = Utilities.readEntry(zip);
                        String key = entry.getName();
                        if (key.startsWith(resources)) {
                            if (key.startsWith(resources + "/values") && key.endsWith("strings.xml")) {
                                Utilities.rebuildStringsXmlFile(workspace, key, content, name);
                            } else {
                                FileUtils.writeByteArrayToFile(new File(workspace, key), content);
                            }
                        } else if (key.startsWith(sources)) {
                            if (key.endsWith(".kt")) {
                                Utilities.rebuildKtFile(workspace, sources + "/" + applicationIdFolder + key.substring(sources.length() + 1 + originalPkg), content, applicationId);
                            } else {
                                throw new IllegalArgumentException(key + " is not supported");
                            }
                        } else {
                            if (key.equals("gradle/wrapper/gradle-wrapper.jar")
                                    || key.equals("gradlew.bat")
                                    || key.equals("gradlew")
                                    || key.equals("gradle.properties")
                                    || key.equals("app/.gitignore")
                                    || key.equals(".gitignore")
                                    || key.equals("app/proguard-rules.pro")) {
                                FileUtils.writeByteArrayToFile(new File(workspace, key), content);
                            } else if (key.equals("gradle/wrapper/gradle-wrapper.properties")) {
                                Utilities.rebuildGradleWrapperPropertiesFile(workspace, key, content, gradleVersion);
                            } else if (key.equals("app/build.gradle")) {
                                Utilities.rebuildAppBuildGradleFile(workspace, key, content, compileSdkVersion, buildToolsVersion, applicationId, minSdkVersion, targetSdkVersion,
                                        coreKtxVersion, appcompatVersion, materialVersion, navigationComposeVersion, pagingComposeVersion, activityComposeVersion,
                                        hiltVersion, roomVersion, retrofitVersion, okhttpVersion, constraintLayoutComposeVersion,
                                        glideVersion, datastoreVersion);
                            } else if (key.equals("build.gradle")) {
                                Utilities.rebuildBuildGradleFile(workspace, key, content, composeVersion, kotlinVersion, hiltPluginVersion, buildToolGradleVersion);
                            } else if (key.equals("local.properties")) {
                                Utilities.rebuildLocalPropertiesFile(workspace, key, content, sdkDir);
                            } else if (key.equals("settings.gradle")) {
                                Utilities.rebuildSettingsGradleFile(workspace, key, content, key);
                            } else if (key.equals("app/src/main/AndroidManifest.xml")) {
                                Utilities.rebuildAndroidManifestXmlFile(workspace, key, content, applicationId);
                            } else {
                                throw new IllegalArgumentException(key + " is not supported");
                            }
                        }
                    }
                    zip.closeEntry();
                }
            }
        }

        File zip = new File(System.getProperty("user.dir"), name + ".zip");
        Utilities.zip(workspace, name, zip);

        FileUtils.deleteDirectory(workspace);

        return zip.getName() + " was created";
    }

}
