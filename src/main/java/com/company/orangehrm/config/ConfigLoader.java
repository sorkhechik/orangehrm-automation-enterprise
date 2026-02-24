package com.company.orangehrm.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public final class ConfigLoader {

    private static final Logger log = LogManager.getLogger(ConfigLoader.class);

    private ConfigLoader() {}

    public static Config load() {
        String env = Env.current();
        Properties props = new Properties();

        String resource = "config/application-" + env + ".properties";
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException(
                        "Config file not found on classpath: " + resource);
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + resource, e);
        }

        // ── Application ───────────────────────────────────────────
        String baseUrl = get("base_url", props, null);
        Objects.requireNonNull(baseUrl, "base_url is required in " + resource);

        int timeout         = getInt ("timeout",           props, 30_000);
        int pageLoadTimeout = getInt ("page.load.timeout", props, 90_000);
        boolean networkLog  = getBool("network.logging.enabled", props, false);
        boolean demoMode    = getBool("demo.mode.enabled",       props, false);

        // ── Browser ───────────────────────────────────────────────
        BrowserConfig browser = loadBrowserConfig(props);

        // ── Trace ─────────────────────────────────────────────────
        String      traceDir      = get ("trace.output.dir",            props, "target/traces");
        String      tracePattern  = get ("trace.name.pattern",          props, "{scenario}_{timestamp}");
        CaptureMode traceMode     = CaptureMode.from(
                                        get("trace.capture.mode", props, "FAILED"), CaptureMode.FAILED);
        boolean traceScreens      = getBool("trace.include.screenshots", props, true);
        boolean traceSnaps        = getBool("trace.include.snapshots",   props, true);
        boolean traceSources      = getBool("trace.include.sources",     props, true);

        // ── Screenshot ────────────────────────────────────────────
        String      ssDir     = get ("screenshot.output.dir",    props, "target/screenshots");
        String      ssPattern = get ("screenshot.name.pattern",  props, "{scenario}_{timestamp}");
        CaptureMode ssMode    = CaptureMode.from(
                                    get("screenshot.capture.mode", props, "FAILED"), CaptureMode.FAILED);

        log.info("Loaded config env='{}' base_url='{}' browser='{}'",
                env, baseUrl, browser.browserType());

        return new Config(
                baseUrl, timeout, pageLoadTimeout,
                networkLog, demoMode,
                browser,
                traceDir, tracePattern, traceMode, traceScreens, traceSnaps, traceSources,
                ssDir, ssPattern, ssMode
        );
    }

    // ─────────────────────────────────────────────────────────────
    //  Browser config builder
    // ─────────────────────────────────────────────────────────────
    private static BrowserConfig loadBrowserConfig(Properties props) {

        String browserType        = get    ("browser.type",               props, "chromium");
        boolean headless          = getBool("browser.headless",            props, false);
        int slowMo                = getInt ("browser.slow.mo",             props, 0);
        String executablePath     = get    ("browser.executable.path",     props, "");

        int viewportWidth         = getInt ("browser.viewport.width",      props, 1920);
        int viewportHeight        = getInt ("browser.viewport.height",     props, 1080);
        double deviceScaleFactor  = getDbl ("browser.device.scale.factor", props, 1.0);
        boolean hasTouch          = getBool("browser.has.touch",           props, false);
        String colorScheme        = get    ("browser.color.scheme",        props, "light");

        List<String> permissions  = BrowserConfig.parseList(
                                        get("browser.context.permissions", props, ""));
        String locale             = get    ("browser.locale",              props, "en-US");
        String timezoneId         = get    ("browser.timezone.id",         props, "Asia/Tehran");
        boolean bypassCsp         = getBool("browser.bypass.csp",          props, false);
        boolean ignoreHttps       = getBool("browser.ignore.https.errors", props, true);
        boolean acceptDownloads   = getBool("browser.accept.downloads",    props, true);
        String downloadDir        = get    ("browser.download.dir",        props, "target/downloads");

        boolean proxyEnabled      = getBool("browser.proxy.enabled",       props, false);
        String proxyServer        = get    ("browser.proxy.server",        props, "");
        String proxyUsername      = get    ("browser.proxy.username",      props, "");
        String proxyPassword      = get    ("browser.proxy.password",      props, "");
        String proxyBypass        = get    ("browser.proxy.bypass",        props, "");

        CaptureMode videoMode     = CaptureMode.from(
                                        get("browser.video.capture.mode", props, "NONE"), CaptureMode.NEVER);
        String videoDir           = get    ("browser.video.output.dir",    props, "target/videos");
        int videoWidth            = getInt ("browser.video.width",         props, 1280);
        int videoHeight           = getInt ("browser.video.height",        props, 720);

        List<String> launchArgs   = BrowserConfig.parseList(
                                        get("browser.launch.args", props, ""));

        return new BrowserConfig(
                browserType, headless, slowMo, executablePath,
                viewportWidth, viewportHeight, deviceScaleFactor, hasTouch, colorScheme,
                permissions, locale, timezoneId, bypassCsp, ignoreHttps,
                acceptDownloads, downloadDir,
                proxyEnabled, proxyServer, proxyUsername, proxyPassword, proxyBypass,
                videoMode, videoDir, videoWidth, videoHeight,
                launchArgs
        );
    }

    // ─────────────────────────────────────────────────────────────
    //  Helpers  (CLI > ENV > file > default)
    // ─────────────────────────────────────────────────────────────
    private static String get(String key, Properties fileProps, String def) {
        String cli = System.getProperty(key);
        if (cli != null && !cli.isBlank()) return cli.trim();

        String envKey = key.toUpperCase().replace('.', '_');
        String envVal = System.getenv(envKey);
        if (envVal != null && !envVal.isBlank()) return envVal.trim();

        String fromFile = fileProps.getProperty(key);
        if (fromFile != null && !fromFile.isBlank()) return fromFile.trim();

        return def;
    }

    private static int getInt(String key, Properties props, int def) {
        String v = get(key, props, String.valueOf(def));
        try { return Integer.parseInt(v); }
        catch (NumberFormatException e) { return def; }
    }

    private static double getDbl(String key, Properties props, double def) {
        String v = get(key, props, String.valueOf(def));
        try { return Double.parseDouble(v); }
        catch (NumberFormatException e) { return def; }
    }

    private static boolean getBool(String key, Properties props, boolean def) {
        String v = get(key, props, String.valueOf(def));
        return Boolean.parseBoolean(v);
    }
}
