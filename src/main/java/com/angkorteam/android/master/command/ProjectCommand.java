package com.angkorteam.android.master.command;

import com.angkorteam.android.master.Utilities;
import com.angkorteam.android.master.support.AccompanistGlideVersionProvider;
import com.angkorteam.android.master.support.ActivityComposeVersionProvider;
import com.angkorteam.android.master.support.AppCompatVersionProvider;
import com.angkorteam.android.master.support.BuildToolGradleVersionProvider;
import com.angkorteam.android.master.support.BuildToolsVersionProvider;
import com.angkorteam.android.master.support.CompileSdkVersionProvider;
import com.angkorteam.android.master.support.ComposeVersionProvider;
import com.angkorteam.android.master.support.ConstraintLayoutComposeVersionProvider;
import com.angkorteam.android.master.support.CoreKtxVersionProvider;
import com.angkorteam.android.master.support.DatastoreVersionProvider;
import com.angkorteam.android.master.support.GradleVersionProvider;
import com.angkorteam.android.master.support.HiltPluginVersionProvider;
import com.angkorteam.android.master.support.HiltVersionProvider;
import com.angkorteam.android.master.support.KotlinVersionProvider;
import com.angkorteam.android.master.support.LifecycleKtxVersionProvider;
import com.angkorteam.android.master.support.MaterialVersionProvider;
import com.angkorteam.android.master.support.MinSdkVersionProvider;
import com.angkorteam.android.master.support.NavigationComposeVersionProvider;
import com.angkorteam.android.master.support.NavigationKtxVersionProvider;
import com.angkorteam.android.master.support.OkHttpVersionProvider;
import com.angkorteam.android.master.support.PagingComposeVersionProvider;
import com.angkorteam.android.master.support.RetrofitVersionProvider;
import com.angkorteam.android.master.support.RoomVersionProvider;
import com.angkorteam.android.master.support.TargetSdkVersionProvider;
import com.angkorteam.android.master.support.ViewModelComposeVersionProvider;
import org.apache.commons.io.FileUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import static org.springframework.shell.standard.ShellOption.NULL;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@ShellComponent
public class ProjectCommand {

    @ShellMethod(key = "create", value = "create project")
    public String create(
            @ShellOption(help = "name") String name,
            @ShellOption(help = "applicationId") String applicationId,
            @ShellOption(help = "gradle version", defaultValue = GradleVersionProvider.SELECTED, valueProvider = GradleVersionProvider.class) String gradleVersion,
            @ShellOption(help = "compile sdk version", defaultValue = CompileSdkVersionProvider.SELECTED, valueProvider = CompileSdkVersionProvider.class) String compileSdkVersion,
            @ShellOption(help = "build tools version", defaultValue = BuildToolsVersionProvider.SELECTED, valueProvider = BuildToolsVersionProvider.class) String buildToolsVersion,
            @ShellOption(help = "min sdk version", defaultValue = MinSdkVersionProvider.SELECTED, valueProvider = MinSdkVersionProvider.class) String minSdkVersion,
            @ShellOption(help = "target sdk version", defaultValue = TargetSdkVersionProvider.SELECTED, valueProvider = TargetSdkVersionProvider.class) String targetSdkVersion,
            @ShellOption(help = "core ktx version", defaultValue = CoreKtxVersionProvider.SELECTED, valueProvider = CoreKtxVersionProvider.class) String coreKtxVersion,
            @ShellOption(help = "appcompat version", defaultValue = AppCompatVersionProvider.SELECTED, valueProvider = AppCompatVersionProvider.class) String appCompatVersion,
            @ShellOption(help = "material version", defaultValue = MaterialVersionProvider.SELECTED, valueProvider = MaterialVersionProvider.class) String materialVersion,
            @ShellOption(help = "navigation ktx version", defaultValue = NavigationKtxVersionProvider.SELECTED, valueProvider = NavigationKtxVersionProvider.class) String navigationKtxVersion,
            @ShellOption(help = "navigation compose version", defaultValue = NavigationComposeVersionProvider.SELECTED, valueProvider = NavigationComposeVersionProvider.class) String navigationComposeVersion,
            @ShellOption(help = "paging compose version", defaultValue = PagingComposeVersionProvider.SELECTED, valueProvider = PagingComposeVersionProvider.class) String pagingComposeVersion,
            @ShellOption(help = "accompanist glide version", defaultValue = AccompanistGlideVersionProvider.SELECTED, valueProvider = AccompanistGlideVersionProvider.class) String accompanistGlideVersion,
            @ShellOption(help = "activity compose version", defaultValue = ActivityComposeVersionProvider.SELECTED, valueProvider = ActivityComposeVersionProvider.class) String activityComposeVersion,
            @ShellOption(help = "hilt version", defaultValue = HiltVersionProvider.SELECTED, valueProvider = HiltVersionProvider.class) String hiltVersion,
            @ShellOption(help = "room version", defaultValue = RoomVersionProvider.SELECTED, valueProvider = RoomVersionProvider.class) String roomVersion,
            @ShellOption(help = "retrofit version", defaultValue = RetrofitVersionProvider.SELECTED, valueProvider = RetrofitVersionProvider.class) String retrofitVersion,
            @ShellOption(help = "okhttp version", defaultValue = OkHttpVersionProvider.SELECTED, valueProvider = OkHttpVersionProvider.class) String okHttpVersion,
            @ShellOption(help = "constraint layout compose version", defaultValue = ConstraintLayoutComposeVersionProvider.SELECTED, valueProvider = ConstraintLayoutComposeVersionProvider.class) String constraintLayoutComposeVersion,
            @ShellOption(help = "compose version", defaultValue = ComposeVersionProvider.SELECTED, valueProvider = ComposeVersionProvider.class) String composeVersion,
            @ShellOption(help = "datastore version", defaultValue = DatastoreVersionProvider.SELECTED, valueProvider = DatastoreVersionProvider.class) String datastoreVersion,
            @ShellOption(help = "kotlin version", defaultValue = KotlinVersionProvider.SELECTED, valueProvider = KotlinVersionProvider.class) String kotlinVersion,
            @ShellOption(help = "hilt plugin version", defaultValue = HiltPluginVersionProvider.SELECTED, valueProvider = HiltPluginVersionProvider.class) String hiltPluginVersion,
            @ShellOption(help = "build tools gradle version", defaultValue = BuildToolGradleVersionProvider.SELECTED, valueProvider = BuildToolGradleVersionProvider.class) String buildToolGradleVersion,
            @ShellOption(help = "compose view model version", defaultValue = ViewModelComposeVersionProvider.SELECTED, valueProvider = ViewModelComposeVersionProvider.class) String viewModelComposeVersion,
            @ShellOption(help = "lifecycle ktx version", defaultValue = LifecycleKtxVersionProvider.SELECTED, valueProvider = LifecycleKtxVersionProvider.class) String lifecycleKtxVersion,
            @ShellOption(help = "android sdk dir", defaultValue = NULL) String sdkDir) throws Throwable {

        Map<String, String> params = new HashMap<>();

        params.put("name", name);
        params.put("sdkDir", sdkDir);
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
        params.put("accompanistGlideVersion", accompanistGlideVersion);
        params.put("hiltVersion", hiltVersion);
        params.put("roomVersion", roomVersion);
        params.put("retrofitVersion", retrofitVersion);
        params.put("okHttpVersion", okHttpVersion);
        params.put("constraintLayoutComposeVersion", constraintLayoutComposeVersion);
        params.put("datastoreVersion", datastoreVersion);
        params.put("viewModelComposeVersion", viewModelComposeVersion);
        params.put("lifecycleKtxVersion", lifecycleKtxVersion);
        params.put("composeVersion", composeVersion);
        params.put("kotlinVersion", kotlinVersion);
        params.put("hiltPluginVersion", hiltPluginVersion);
        params.put("buildToolGradleVersion", buildToolGradleVersion);

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

        File workspace = Utilities.generate(params);

        File zip = new File(System.getProperty("user.dir"), name + ".zip");
        Utilities.zip(workspace, name, zip);

        FileUtils.deleteDirectory(workspace);
        return zip.getName();
    }

}
