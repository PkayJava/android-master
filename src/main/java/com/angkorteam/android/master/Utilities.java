package com.angkorteam.android.master;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Utilities {

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

    public static void rebuildKtFile(File output, String name, byte[] content, String pkg) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${pkg}", pkg);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static void rebuildStringsXmlFile(File output, String name, byte[] content, String app_name) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${app_name}", app_name);
        FileUtils.write(new File(output, name), plain, StandardCharsets.UTF_8);
    }

    public static void rebuildAppBuildGradleFile(File output, String name, byte[] content, String compile_sdk_version, String build_tools_version, String pkg, String min_sdk_version, String target_sdk_version,
                                                 String core_ktx_version, String appcompat_version, String material_version, String navigation_compose_version, String paging_compose_version, String activity_compose_version,
                                                 String hiltVersion, String room_version, String retrofit_version, String okhttp_version, String constraint_layout_compose_version,
                                                 String glide_version, String datastore_version) throws IOException {
        String plain = new String(content, StandardCharsets.UTF_8);
        plain = StringUtils.replace(plain, "${compile_sdk_version}", compile_sdk_version);
        plain = StringUtils.replace(plain, "${build_tools_version}", build_tools_version);
        plain = StringUtils.replace(plain, "${pkg}", pkg);
        plain = StringUtils.replace(plain, "${min_sdk_version}", min_sdk_version);
        plain = StringUtils.replace(plain, "${target_sdk_version}", target_sdk_version);
        plain = StringUtils.replace(plain, "${core_ktx_version}", core_ktx_version);
        plain = StringUtils.replace(plain, "${appcompat_version}", appcompat_version);
        plain = StringUtils.replace(plain, "${material_version}", material_version);
        plain = StringUtils.replace(plain, "${navigation_compose_version}", navigation_compose_version);
        plain = StringUtils.replace(plain, "${paging_compose_version}", paging_compose_version);
        plain = StringUtils.replace(plain, "${activity_compose_version}", activity_compose_version);
        plain = StringUtils.replace(plain, "${hilt_version}", hiltVersion);
        plain = StringUtils.replace(plain, "${room_version}", room_version);
        plain = StringUtils.replace(plain, "${retrofit_version}", retrofit_version);
        plain = StringUtils.replace(plain, "${okhttp_version}", okhttp_version);
        plain = StringUtils.replace(plain, "${constraint_layout_compose_version}", constraint_layout_compose_version);
        plain = StringUtils.replace(plain, "${glide_version}", glide_version);
        plain = StringUtils.replace(plain, "${datastore_version}", datastore_version);
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

}
