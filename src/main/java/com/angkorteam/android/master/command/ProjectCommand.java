package com.angkorteam.android.master.command;

import com.angkorteam.android.master.Utilities;
import com.angkorteam.android.master.support.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.springframework.shell.standard.ShellOption.NULL;

@ShellComponent
public class ProjectCommand {

    @ShellMethod(key = "create", value = "create project")
    public String create(
            @ShellOption(help = "name") String name,
            @ShellOption(help = "applicationId") String applicationId,
            @ShellOption(help = "gradle version", defaultValue = GradleVersionProvider.SELECTED, valueProvider = GradleVersionProvider.class) String gradleVersion,
            @ShellOption(help = "compile sdk version", defaultValue = CompileSdkVersionProvider.SELECTED, valueProvider = CompileSdkVersionProvider.class) String compileSdkVersion,
            @ShellOption(help = "build tools version", defaultValue = BuildToolVersionProvider.SELECTED, valueProvider = BuildToolVersionProvider.class) String buildToolsVersion,
            @ShellOption(help = "min sdk version", defaultValue = MinSdkVersionProvider.SELECTED, valueProvider = MinSdkVersionProvider.class) String minSdkVersion,
            @ShellOption(help = "target sdk version", defaultValue = TargetSdkVersionProvider.SELECTED, valueProvider = TargetSdkVersionProvider.class) String targetSdkVersion,
            @ShellOption(help = "core ktx version", defaultValue = CoreKtxVersionProvider.SELECTED, valueProvider = CoreKtxVersionProvider.class) String coreKtxVersion,
            @ShellOption(help = "appcompat version", defaultValue = AppCompatVersionProvider.SELECTED, valueProvider = AppCompatVersionProvider.class) String appCompatVersion,
            @ShellOption(help = "material version", defaultValue = MaterialVersionProvider.SELECTED, valueProvider = MaterialVersionProvider.class) String materialVersion,
            @ShellOption(help = "navigation ktx version", defaultValue = NavigationKtxVersionProvider.SELECTED, valueProvider = NavigationKtxVersionProvider.class) String navigationKtxVersion,
            @ShellOption(help = "navigation compose version", defaultValue = NavigationComposeVersionProvider.SELECTED, valueProvider = NavigationComposeVersionProvider.class) String navigationComposeVersion,
            @ShellOption(help = "paging compose version", defaultValue = PagingComposeVersionProvider.SELECTED, valueProvider = PagingComposeVersionProvider.class) String pagingComposeVersion,
            @ShellOption(help = "activity compose version", defaultValue = ActivityComposeVersionProvider.SELECTED, valueProvider = ActivityComposeVersionProvider.class) String activityComposeVersion,
            @ShellOption(help = "hilt version", defaultValue = HiltVersionProvider.SELECTED, valueProvider = HiltVersionProvider.class) String hiltVersion,
            @ShellOption(help = "room version", defaultValue = RoomVersionProvider.SELECTED, valueProvider = RoomVersionProvider.class) String roomVersion,
            @ShellOption(help = "retrofit version", defaultValue = RetrofitVersionProvider.SELECTED, valueProvider = RetrofitVersionProvider.class) String retrofitVersion,
            @ShellOption(help = "okhttp version", defaultValue = OkHttpVersionProvider.SELECTED, valueProvider = OkHttpVersionProvider.class) String okHttpVersion,
            @ShellOption(help = "constraint layout compose version", defaultValue = ConstraintLayoutComposeVersionProvider.SELECTED, valueProvider = ConstraintLayoutComposeVersionProvider.class) String constraintLayoutComposeVersion,
            @ShellOption(help = "compose version", defaultValue = ComposeVersionProvider.SELECTED, valueProvider = ComposeVersionProvider.class) String composeVersion,
            @ShellOption(help = "glide version", defaultValue = GlideVersionProvider.SELECTED, valueProvider = GlideVersionProvider.class) String glideVersion,
            @ShellOption(help = "datastore version", defaultValue = DatastoreVersionProvider.SELECTED, valueProvider = DatastoreVersionProvider.class) String datastoreVersion,
            @ShellOption(help = "kotlin version", defaultValue = KotlinVersionProvider.SELECTED, valueProvider = KotlinVersionProvider.class) String kotlinVersion,
            @ShellOption(help = "hilt plugin version", defaultValue = HiltPluginVersionProvider.SELECTED, valueProvider = HiltPluginVersionProvider.class) String hiltPluginVersion,
            @ShellOption(help = "build tools gradle version", defaultValue = BuildToolGradleVersionProvider.SELECTED, valueProvider = BuildToolGradleVersionProvider.class) String buildToolGradleVersion,
            @ShellOption(help = "compose view model version", defaultValue = ViewModelComposeVersionProvider.SELECTED, valueProvider = ViewModelComposeVersionProvider.class) String viewModelComposeVersion,
            @ShellOption(help = "lifecycle ktx version", defaultValue = LifecycleKtxVersionProvider.SELECTED, valueProvider = LifecycleKtxVersionProvider.class) String lifecycleKtxVersion,
            @ShellOption(help = "android sdk dir", defaultValue = NULL) String sdkDir) throws Throwable {
        if (name.length() == 0) {
            return "invalid name";
        } else {
            if (name.matches("^[a-zA-Z0-9][a-zA-Z0-9]*([ _.-][a-zA-Z0-9]+)+[a-zA-Z0-9]$") || name.matches("[a-zA-Z0-9]*")) {
            } else {
                return "invalid name";
            }
        }

        File sdkDirFile = null;

        if (sdkDir == null || "".equals(sdkDir)) {
            if (SystemUtils.IS_OS_MAC) {
                String env = System.getenv("ANDROID_SDK_ROOT");
                if (env != null && !"".equals(env)) {
                    sdkDirFile = new File(env);
                    if (sdkDirFile.isDirectory()) {
                        sdkDir = FilenameUtils.normalize(sdkDirFile.getAbsolutePath(), true);
                    } else {
                        sdkDirFile = new File(FileUtils.getUserDirectoryPath(), "Library/Android/sdk");
                        if (sdkDirFile.isDirectory()) {
                            sdkDir = FilenameUtils.normalize(sdkDirFile.getAbsolutePath(), true);
                        }
                    }
                } else {
                    sdkDirFile = new File(FileUtils.getUserDirectoryPath(), "Library/Android/sdk");
                    if (sdkDirFile.isDirectory()) {
                        sdkDir = FilenameUtils.normalize(sdkDirFile.getAbsolutePath(), true);
                    }
                }
            } else if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_LINUX) {
                String env = System.getenv("ANDROID_SDK_ROOT");
                if (env != null && !"".equals(env)) {
                    sdkDirFile = new File(env);
                    if (sdkDirFile.isDirectory()) {
                        sdkDir = FilenameUtils.normalize(sdkDirFile.getAbsolutePath(), true);
                        if (SystemUtils.IS_OS_WINDOWS) {
                            sdkDir = StringUtils.replace(sdkDir, ":", "\\:");
                        }
                    }
                }
            }
        } else {
            sdkDirFile = new File(sdkDir);
            if (!sdkDirFile.isDirectory()) {
                sdkDir = null;
            }
        }

        if (sdkDir != null && !"".equals(sdkDir)) {
            System.out.println("Android SDK detected at " + sdkDirFile.getAbsolutePath());
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
                                Utilities.rebuildKtFile(workspace, sources + "/" + applicationIdFolder + key.substring(sources.length() + 1 + originalPkg), content, applicationId, name);
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
                                        coreKtxVersion, appCompatVersion, materialVersion, navigationComposeVersion, navigationKtxVersion, pagingComposeVersion, activityComposeVersion,
                                        hiltVersion, roomVersion, retrofitVersion, okHttpVersion, constraintLayoutComposeVersion,
                                        glideVersion, datastoreVersion, viewModelComposeVersion, lifecycleKtxVersion);
                            } else if (key.equals("build.gradle")) {
                                Utilities.rebuildBuildGradleFile(workspace, key, content, composeVersion, kotlinVersion, hiltPluginVersion, buildToolGradleVersion);
                            } else if (key.equals("local.properties")) {
                                Utilities.rebuildLocalPropertiesFile(workspace, key, content, sdkDir);
                            } else if (key.equals("settings.gradle")) {
                                Utilities.rebuildSettingsGradleFile(workspace, key, content, name);
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
