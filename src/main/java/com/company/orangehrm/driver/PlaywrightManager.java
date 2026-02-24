package com.company.orangehrm.driver;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.company.orangehrm.config.BrowserConfig;
import com.company.orangehrm.config.Config;
import com.company.orangehrm.config.ConfigLoader;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.ColorScheme;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.Proxy;

public final class PlaywrightManager {

    private static final Logger log = LogManager.getLogger(PlaywrightManager.class);

    private static final ThreadLocal<Playwright>     TL_PLAYWRIGHT = new ThreadLocal<>();
    private static final ThreadLocal<Browser>        TL_BROWSER    = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> TL_CONTEXT    = new ThreadLocal<>();
    private static final ThreadLocal<Page>           TL_PAGE       = new ThreadLocal<>();
    private static final ThreadLocal<Config>         TL_CONFIG     = new ThreadLocal<>();

    private PlaywrightManager() {}

    // ─────────────────────────────────────────────────────────────
    //  start() → مورد استفاده TestHooks
    // ─────────────────────────────────────────────────────────────
    public static void start() {
        Config cfg = ConfigLoader.load();
        TL_CONFIG.set(cfg);

        BrowserConfig bc = cfg.browser();

        Playwright pw = Playwright.create();
        TL_PLAYWRIGHT.set(pw);

        Browser br = launchBrowser(pw, bc);
        TL_BROWSER.set(br);

        BrowserContext ctx = createContext(br, cfg);
        TL_CONTEXT.set(ctx);

        Page pg = ctx.newPage();
        TL_PAGE.set(pg);

        applyTimeouts(pg, cfg);
        attachNetworkLogger(ctx, cfg);

        log.info("Browser started → type='{}' headless={} viewport={}x{}",
                bc.browserType(), bc.headless(),
                bc.viewportWidth(), bc.viewportHeight());
    }

    // ─────────────────────────────────────────────────────────────
    //  stop() → مورد استفاده TestHooks
    // ─────────────────────────────────────────────────────────────
    public static void stop() {
        closeSilently(TL_PAGE.get());
        closeSilently(TL_CONTEXT.get());
        closeSilently(TL_BROWSER.get());
        closeSilently(TL_PLAYWRIGHT.get());

        TL_PAGE.remove();
        TL_CONTEXT.remove();
        TL_BROWSER.remove();
        TL_PLAYWRIGHT.remove();
        TL_CONFIG.remove();

        log.info("Browser stopped.");
    }

    // ─────────────────────────────────────────────────────────────
    //  Accessors
    // ─────────────────────────────────────────────────────────────
    public static Page page() {
        Page p = TL_PAGE.get();
        if (p == null)
            throw new IllegalStateException("Page not initialized. Did you call start()?");
        return p;
    }

    public static BrowserContext context() {
        BrowserContext ctx = TL_CONTEXT.get();
        if (ctx == null)
            throw new IllegalStateException("BrowserContext not initialized.");
        return ctx;
    }

    public static Browser browser() {
        Browser br = TL_BROWSER.get();
        if (br == null)
            throw new IllegalStateException("Browser not initialized.");
        return br;
    }

    public static Config config() {
        Config cfg = TL_CONFIG.get();
        if (cfg == null)
            throw new IllegalStateException("Config not initialized. Did you call start()?");
        return cfg;
    }

    public static void navigate(String url) {
        Page p = page();
        p.navigate(url);
        p.waitForLoadState(LoadState.DOMCONTENTLOADED);
        log.info("Navigated to {}", url);
    }

    // ─────────────────────────────────────────────────────────────
    //  Private helpers
    // ─────────────────────────────────────────────────────────────
    private static Browser launchBrowser(Playwright pw, BrowserConfig bc) {

        List<String> args = new ArrayList<>(bc.launchArgs());

        BrowserType.LaunchOptions opts = new BrowserType.LaunchOptions()
                .setHeadless(bc.headless())
                .setSlowMo(bc.slowMo())
                .setArgs(args);

        if (bc.hasExecutablePath()) {
            opts.setExecutablePath(Paths.get(bc.executablePath()));
        }

        return switch (bc.browserType().toLowerCase()) {
            case "firefox" -> pw.firefox().launch(opts);
            case "webkit"  -> pw.webkit().launch(opts);
            default        -> pw.chromium().launch(opts);
        };
    }

    private static BrowserContext createContext(Browser br, Config cfg) {
        BrowserConfig bc = cfg.browser();

        Browser.NewContextOptions ctxOpts = new Browser.NewContextOptions()
                .setViewportSize(bc.viewportWidth(), bc.viewportHeight())
                .setDeviceScaleFactor(bc.deviceScaleFactor())
                .setHasTouch(bc.hasTouch())
                .setLocale(bc.locale())
                .setTimezoneId(bc.timezoneId())
                .setBypassCSP(bc.bypassCsp())
                .setIgnoreHTTPSErrors(bc.ignoreHttpsErrors())
                .setAcceptDownloads(bc.acceptDownloads())
                .setColorScheme(mapColorScheme(bc.colorScheme()));

        if (!bc.permissions().isEmpty()) {
            ctxOpts.setPermissions(bc.permissions());
        }

        if (bc.hasProxy()) {
            Proxy proxy = new Proxy(bc.proxyServer());
            if (!bc.proxyUsername().isBlank()) proxy.setUsername(bc.proxyUsername());
            if (!bc.proxyPassword().isBlank()) proxy.setPassword(bc.proxyPassword());
            if (!bc.proxyBypass().isBlank())   proxy.setBypass(bc.proxyBypass());
            ctxOpts.setProxy(proxy);
        }

        if (bc.videoEnabled()) {
            ctxOpts.setRecordVideoDir(Paths.get(bc.videoOutputDir()))
                   .setRecordVideoSize(bc.videoWidth(), bc.videoHeight());
        }

        return br.newContext(ctxOpts);
    }

    private static void applyTimeouts(Page pg, Config cfg) {
        pg.setDefaultTimeout(cfg.timeoutMs());
        pg.setDefaultNavigationTimeout(cfg.pageLoadTimeoutMs());
    }

    private static void attachNetworkLogger(BrowserContext ctx, Config cfg) {
        if (!cfg.networkLoggingEnabled()) return;
        ctx.onRequest(req  -> log.info("-->  {} {}", req.method(), req.url()));
        ctx.onResponse(res -> log.info("<--  {} {}", res.status(), res.url()));
        ctx.onRequestFailed(req -> log.warn("xx>  {} {} | {}", req.method(), req.url(), req.failure()));
    }

    private static ColorScheme mapColorScheme(String value) {
        return switch (value.toLowerCase()) {
            case "dark"          -> ColorScheme.DARK;
            case "no-preference" -> ColorScheme.NO_PREFERENCE;
            default              -> ColorScheme.LIGHT;
        };
    }

    private static void closeSilently(AutoCloseable resource) {
        if (resource == null) return;
        try { resource.close(); } catch (Exception ignored) {}
    }
}
