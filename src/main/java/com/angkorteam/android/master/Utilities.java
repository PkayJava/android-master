package com.angkorteam.android.master;

import com.angkorteam.android.master.command.ProjectCommand;
import com.angkorteam.android.master.support.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Utilities {

    public static void main(String[] args) throws IOException {
        String name = "video-capture";
        String applicationId = "video.hello.com";
        String gradleVersion = GradleVersionProvider.SELECTED;
        String compileSdkVersion = CompileSdkVersionProvider.SELECTED;
        String buildToolsVersion = BuildToolsVersionProvider.SELECTED;
        String minSdkVersion = MinSdkVersionProvider.SELECTED;
        String targetSdkVersion = TargetSdkVersionProvider.SELECTED;
        String coreKtxVersion = CoreKtxVersionProvider.SELECTED;
        String appCompatVersion = AppCompatVersionProvider.SELECTED;
        String materialVersion = MaterialVersionProvider.SELECTED;
        String navigationComposeVersion = NavigationComposeVersionProvider.SELECTED;
        String navigationKtxVersion = NavigationKtxVersionProvider.SELECTED;
        String pagingComposeVersion = PagingComposeVersionProvider.SELECTED;
        String activityComposeVersion = ActivityComposeVersionProvider.SELECTED;
        String accompanistVersion = AccompanistGlideVersionProvider.SELECTED;
        String hiltVersion = HiltVersionProvider.SELECTED;
        String roomVersion = RoomVersionProvider.SELECTED;
        String commonsIOVersion = CommonsIOVersionProvider.SELECTED;
        String okHttpVersion = OkHttpVersionProvider.SELECTED;
        String constraintLayoutComposeVersion = ConstraintLayoutComposeVersionProvider.SELECTED;
        String datastoreVersion = DatastoreVersionProvider.SELECTED;
        String viewModelComposeVersion = ViewModelComposeVersionProvider.SELECTED;
        String lifecycleKtxVersion = LifecycleKtxVersionProvider.SELECTED;
        String composeVersion = ComposeVersionProvider.SELECTED;
        String kotlinVersion = KotlinVersionProvider.SELECTED;
        String hiltPluginVersion = HiltPluginVersionProvider.SELECTED;
        String buildToolGradleVersion = BuildToolsGradleVersionProvider.SELECTED;
        String gsonVersion = GsonVersionProvider.SELECTED;

        Map<String, String> params = new HashMap<>();

        params.put("name", name);
        params.put("applicationId", applicationId);
        params.put("gradleVersion", gradleVersion);
        params.put("compileSdkVersion", compileSdkVersion);
        params.put("buildToolsVersion", buildToolsVersion);
        params.put("minSdkVersion", minSdkVersion);
        params.put("targetSdkVersion", targetSdkVersion);
        params.put("coreKtxVersion", coreKtxVersion);
        params.put("appCompatVersion", appCompatVersion);
        params.put("materialVersion", materialVersion);
        params.put("navigationComposeVersion", navigationComposeVersion);
        params.put("navigationKtxVersion", navigationKtxVersion);
        params.put("pagingComposeVersion", pagingComposeVersion);
        params.put("activityComposeVersion", activityComposeVersion);
        params.put("accompanistVersion", accompanistVersion);
        params.put("hiltVersion", hiltVersion);
        params.put("roomVersion", roomVersion);
        params.put("commonsIOVersion", commonsIOVersion);
        params.put("gsonVersion", gsonVersion);
        params.put("okHttpVersion", okHttpVersion);
        params.put("constraintLayoutComposeVersion", constraintLayoutComposeVersion);
        params.put("datastoreVersion", datastoreVersion);
        params.put("viewModelComposeVersion", viewModelComposeVersion);
        params.put("lifecycleKtxVersion", lifecycleKtxVersion);
        params.put("composeVersion", composeVersion);
        params.put("kotlinVersion", kotlinVersion);
        params.put("hiltPluginVersion", hiltPluginVersion);
        params.put("buildToolGradleVersion", buildToolGradleVersion);

        File workspace = generate(params);

        File dest = new File("/Users/macbook/github/PkayJava", name);

        FileUtils.copyDirectory(workspace, dest);
        FileUtils.deleteDirectory(workspace);
    }

    private static byte[] buffer = new byte[1024];

    public static void zip(File sourceFolder, String name, File outputZip) throws IOException {
        try (FileOutputStream stream = new FileOutputStream(outputZip)) {
            try (ZipOutputStream zip = new ZipOutputStream(stream)) {
                zipFile(sourceFolder, name, zip);
            }
        }
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    public static void rebuildKtFile(File output, String name, byte[] content, String pkg, String app_name) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${pkg}", pkg);
        plain = StringUtils.replace(plain, "${app_name}", app_name);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static void rebuildStringsXmlFile(File output, String name, byte[] content, String app_name) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${app_name}", app_name);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static void rebuildAppBuildGradleFile(File output, String name, byte[] content, String compile_sdk_version, String build_tools_version, String pkg, String min_sdk_version, String target_sdk_version,
                                                 String core_ktx_version, String appcompat_version, String material_version, String navigation_compose_version, String navigation_ktx_version, String paging_compose_version, String activity_compose_version, String accompanist_version,
                                                 String hilt_version, String room_version, String commons_io_version, String okhttp_version, String constraint_layout_compose_version,
                                                 String datastore_version, String view_model_compose_version,
                                                 String lifecycle_ktx_version, String gson_version) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${compile_sdk_version}", compile_sdk_version);
        plain = StringUtils.replace(plain, "${build_tools_version}", build_tools_version);
        plain = StringUtils.replace(plain, "${pkg}", pkg);
        plain = StringUtils.replace(plain, "${min_sdk_version}", min_sdk_version);
        plain = StringUtils.replace(plain, "${target_sdk_version}", target_sdk_version);
        plain = StringUtils.replace(plain, "${core_ktx_version}", core_ktx_version);
        plain = StringUtils.replace(plain, "${appcompat_version}", appcompat_version);
        plain = StringUtils.replace(plain, "${material_version}", material_version);
        plain = StringUtils.replace(plain, "${navigation_ktx_version}", navigation_ktx_version);
        plain = StringUtils.replace(plain, "${navigation_compose_version}", navigation_compose_version);
        plain = StringUtils.replace(plain, "${paging_compose_version}", paging_compose_version);
        plain = StringUtils.replace(plain, "${accompanist_version}", accompanist_version);
        plain = StringUtils.replace(plain, "${activity_compose_version}", activity_compose_version);
        plain = StringUtils.replace(plain, "${hilt_version}", hilt_version);
        plain = StringUtils.replace(plain, "${room_version}", room_version);
        plain = StringUtils.replace(plain, "${commons_io_version}", commons_io_version);
        plain = StringUtils.replace(plain, "${okhttp_version}", okhttp_version);
        plain = StringUtils.replace(plain, "${constraint_layout_compose_version}", constraint_layout_compose_version);
        plain = StringUtils.replace(plain, "${datastore_version}", datastore_version);
        plain = StringUtils.replace(plain, "${view_model_compose_version}", view_model_compose_version);
        plain = StringUtils.replace(plain, "${lifecycle_ktx_version}", lifecycle_ktx_version);
        plain = StringUtils.replace(plain, "${gson_version}", gson_version);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static void rebuildSettingsGradleFile(File output, String name, byte[] content, String project_name) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${project_name}", project_name);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static void rebuildAndroidManifestXmlFile(File output, String name, byte[] content, String pkg) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${pkg}", pkg);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static void rebuildGradleWrapperPropertiesFile(File output, String name, byte[] content, String gradle_version) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${gradle_version}", gradle_version);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static void rebuildLocalPropertiesFile(File output, String name, byte[] content, String sdk_dir) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${sdk.dir}", sdk_dir);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static void rebuildBuildGradleFile(File output, String name, byte[] content, String compose_version, String kotlin_version, String hilt_plugin_version, String build_tools_gradle_version) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${compose_version}", compose_version);
        plain = StringUtils.replace(plain, "${kotlin_version}", kotlin_version);
        plain = StringUtils.replace(plain, "${hilt_plugin_version}", hilt_plugin_version);
        plain = StringUtils.replace(plain, "${build_tools_gradle_version}", build_tools_gradle_version);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static byte[] readEntry(ZipInputStream zip) throws IOException {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        int len;
        while ((len = zip.read(buffer)) > 0) {
            o.write(buffer, 0, len);
        }
        o.close();
        return o.toByteArray();
    }

    public static File generate(Map<String, String> params) throws IOException {
        String name = params.get("name");
        String sdkDir = params.get("sdkDir");
        String applicationId = params.get("applicationId");

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

        String sources = "app/src/main/java";
        String resources = "app/src/main/res";

        String applicationIdFolder = StringUtils.replace(applicationId, ".", "/");
        int originalPkg = "com.angkorteam.blueprint.android".length();

        File workspace = new File(FileUtils.getTempDirectory(), RandomStringUtils.randomAlphanumeric(10));
        workspace.mkdirs();

        read(params, originalPkg, applicationIdFolder, "/template", workspace, resources, sources);

        return workspace;
    }

    protected static void read(Map<String, String> params, int originalPkg, String applicationIdFolder, String path, File workspace, String resources, String sources) throws IOException {
        try (InputStream stream = ProjectCommand.class.getResourceAsStream(path)) {
            if (stream instanceof ByteArrayInputStream) {
                List<String> names = IOUtils.readLines(stream, StandardCharsets.UTF_8);
                if (!names.isEmpty()) {
                    for (String name : names) {
                        read(params, originalPkg, applicationIdFolder, path + "/" + name, workspace, resources, sources);
                    }
                }
            } else if (stream instanceof BufferedInputStream) {
                byte[] content = IOUtils.toByteArray(stream);
                String key = path.substring("/template/".length());
                if (key.startsWith(resources)) {
                    if (key.startsWith(resources + "/values") && key.endsWith("strings.xml")) {
                        Utilities.rebuildStringsXmlFile(workspace, key, content, params.get("name"));
                    } else {
                        FileUtils.writeByteArrayToFile(new File(workspace, key), content);
                    }
                } else if (key.startsWith(sources)) {
                    if (key.endsWith(".kt") || key.endsWith(".java")) {
                        Utilities.rebuildKtFile(workspace, sources + "/" + applicationIdFolder + key.substring(sources.length() + 1 + originalPkg), content, params.get("applicationId"), params.get("name"));
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
                        Utilities.rebuildGradleWrapperPropertiesFile(workspace, key, content, params.get("gradleVersion"));
                    } else if (key.equals("app/build.gradle")) {
                        Utilities.rebuildAppBuildGradleFile(workspace, key, content, params.get("compileSdkVersion"), params.get("buildToolsVersion"), params.get("applicationId"), params.get("minSdkVersion"), params.get("targetSdkVersion"),
                                params.get("coreKtxVersion"), params.get("appCompatVersion"), params.get("materialVersion"), params.get("navigationComposeVersion"), params.get("navigationKtxVersion"), params.get("pagingComposeVersion"), params.get("activityComposeVersion"), params.get("accompanistVersion"),
                                params.get("hiltVersion"), params.get("roomVersion"), params.get("commonsIOVersion"), params.get("okHttpVersion"), params.get("constraintLayoutComposeVersion"),
                                params.get("datastoreVersion"), params.get("viewModelComposeVersion"), params.get("lifecycleKtxVersion"),
                                params.get("gsonVersion"));
                    } else if (key.equals("build.gradle")) {
                        Utilities.rebuildBuildGradleFile(workspace, key, content, params.get("composeVersion"), params.get("kotlinVersion"), params.get("hiltPluginVersion"), params.get("buildToolGradleVersion"));
                    } else if (key.equals("local.properties")) {
                        Utilities.rebuildLocalPropertiesFile(workspace, key, content, params.get("sdkDir"));
                    } else if (key.equals("settings.gradle")) {
                        Utilities.rebuildSettingsGradleFile(workspace, key, content, params.get("name"));
                    } else if (key.equals("app/src/main/AndroidManifest.xml")) {
                        Utilities.rebuildAndroidManifestXmlFile(workspace, key, content, params.get("applicationId"));
                    } else {
                        throw new IllegalArgumentException(key + " is not supported");
                    }
                }
            }
        }
    }

}
