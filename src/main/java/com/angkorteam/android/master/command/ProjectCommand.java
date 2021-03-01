package com.angkorteam.android.master.command;

import com.angkorteam.android.master.Utilities;
import com.angkorteam.android.master.support.*;
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
            @ShellOption(help = "gradle version", defaultValue = GradleVersionProvider.V_6_8_2, valueProvider = GradleVersionProvider.class) String gradleVersion,
            @ShellOption(help = "compile sdk version", defaultValue = CompileSdkVersionProvider.V_30, valueProvider = CompileSdkVersionProvider.class) String compileSdkVersion,
            @ShellOption(help = "build tools version", defaultValue = BuildToolVersionProvider.V_30_0_3, valueProvider = BuildToolVersionProvider.class) String buildToolsVersion,
            @ShellOption(help = "min sdk version", defaultValue = MinSdkVersionProvider.V_21, valueProvider = MinSdkVersionProvider.class) String minSdkVersion,
            @ShellOption(help = "target sdk version", defaultValue = TargetSdkVersionProvider.V_30, valueProvider = TargetSdkVersionProvider.class) String targetSdkVersion,
            @ShellOption(help = "core ktx version", defaultValue = CoreKtxVersionProvider.V_1_3_2, valueProvider = CoreKtxVersionProvider.class) String coreKtxVersion,
            @ShellOption(help = "appcompat version", defaultValue = AppCompatVersionProvider.V_1_2_0, valueProvider = AppCompatVersionProvider.class) String appCompatVersion,
            @ShellOption(help = "material version", defaultValue = MaterialVersionProvider.V_1_3_0, valueProvider = MaterialVersionProvider.class) String materialVersion,
            @ShellOption(help = "navigation compose version", defaultValue = NavigationComposeVersionProvider.V_1_0_0_ALPHA08, valueProvider = NavigationComposeVersionProvider.class) String navigationComposeVersion,
            @ShellOption(help = "paging compose version", defaultValue = PagingComposeVersionProvider.V_1_0_0_ALPHA08, valueProvider = PagingComposeVersionProvider.class) String pagingComposeVersion,
            @ShellOption(help = "activity compose version", defaultValue = ActivityComposeVersionProvider.V_1_3_0_ALPHA03, valueProvider = ActivityComposeVersionProvider.class) String activityComposeVersion,
            @ShellOption(help = "hilt version", defaultValue = HiltVersionProvider.V_1_0_0_ALPHA03, valueProvider = HiltVersionProvider.class) String hiltVersion,
            @ShellOption(help = "room version", defaultValue = RoomVersionProvider.V_2_3_0_BETA02, valueProvider = RoomVersionProvider.class) String roomVersion,
            @ShellOption(help = "retrofit version", defaultValue = RetrofitVersionProvider.V_2_9_0, valueProvider = RetrofitVersionProvider.class) String retrofitVersion,
            @ShellOption(help = "okhttp version", defaultValue = OkHttpVersionProvider.V_4_9_1, valueProvider = OkHttpVersionProvider.class) String okHttpVersion,
            @ShellOption(help = "constraint layout compose version", defaultValue = ConstraintLayoutComposeVersionProvider.V_1_0_0_ALPHA03, valueProvider = ConstraintLayoutComposeVersionProvider.class) String constraintLayoutComposeVersion,
            @ShellOption(help = "compose version", defaultValue = ComposeVersionProvider.V_1_0_0_BETA01, valueProvider = ComposeVersionProvider.class) String composeVersion,
            @ShellOption(help = "glide version", defaultValue = GlideVersionProvider.V_4_12_0, valueProvider = GlideVersionProvider.class) String glideVersion,
            @ShellOption(help = "datastore version", defaultValue = DatastoreVersionProvider.V_1_0_0_ALPHA07, valueProvider = DatastoreVersionProvider.class) String datastoreVersion,
            @ShellOption(help = "kotlin version", defaultValue = KotlinVersionProvider.V_1_4_30, valueProvider = KotlinVersionProvider.class) String kotlinVersion,
            @ShellOption(help = "hilt plugin version", defaultValue = HiltPluginVersionProvider.V_2_32_ALPHA, valueProvider = HiltPluginVersionProvider.class) String hiltPluginVersion,
            @ShellOption(help = "build tools gradle version", defaultValue = BuildToolGradleVersionProvider.V_7_0_0_ALPHA08, valueProvider = BuildToolGradleVersionProvider.class) String buildToolGradleVersion,
            @ShellOption(help = "compose view model version", defaultValue = ViewModelComposeVersionProvider.V_1_0_0_ALPHA02, valueProvider = ViewModelComposeVersionProvider.class) String viewModelComposeVersion,
            @ShellOption(help = "lifecycle ktx version", defaultValue = LifecycleKtxVersionProvider.V_2_3_0, valueProvider = LifecycleKtxVersionProvider.class) String lifecycleKtxVersion,
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
                                        coreKtxVersion, appCompatVersion, materialVersion, navigationComposeVersion, pagingComposeVersion, activityComposeVersion,
                                        hiltVersion, roomVersion, retrofitVersion, okHttpVersion, constraintLayoutComposeVersion,
                                        glideVersion, datastoreVersion, viewModelComposeVersion, lifecycleKtxVersion);
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
