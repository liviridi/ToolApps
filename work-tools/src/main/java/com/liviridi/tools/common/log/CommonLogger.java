package com.liviridi.tools.common.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * アプリケーションログ
 *
 * @author ly
 * @version 2016-08-15 11:11:59
 */
public final class CommonLogger {
    private static final int METHOD_LAYER = 3;
    private static final String START = "start";
    private static final String END = "end";

    /**
     * log4j Logger.
     */
    private Logger logger;

    private CommonLogger(String name) {
        this.logger = Logger.getLogger(name);
    }

    /**
     * <p>初期化。</p>
     * <p>ログ配置をロードする。</p>
     *
     * @param configPath
     *            configuration path
     */
    public static void initConfig(String configPath) {
        DOMConfigurator.configure(configPath);
        //PropertyConfigurator.configure(configPath);
    }

    /**
     * ログ輸出対象取得
     *
     * @param clazz
     *            ログが属するクラス
     * @return ログ輸出対象
     */
    @SuppressWarnings("rawtypes")
    public static CommonLogger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * ログ輸出対象取得
     *
     * @param name
     *            ログ輸出対象名。通常にログが属するクラス名となる
     * @return ログ輸出対象
     */
    public static CommonLogger getLogger(String name) {
        return new CommonLogger(name);
    }

    /**
     * デバッグログ
     *
     * @param message
     *            輸出メッセージ
     */
    public void warn(Object message) {
        String method = this.getMethod(true);
        doLog(method, Level.WARN, message, null);
    }

    /**
     * デバッグログ
     *
     * @param message
     *            輸出メッセージ
     */
    public void info(Object message) {
        String method = this.getMethod(true);
        doLog(method, Level.INFO, message, null);
    }

    /**
     * デバッグログ
     *
     * @param message
     *            輸出メッセージ
     */
    public void error(Object message) {
        String method = this.getMethod(true);
        doLog(method, Level.ERROR, message, null);
    }

    /**
     * デバッグログ
     *
     * @param message
     *            輸出メッセージ
     */
    public void error(Object message, Throwable t) {
        String method = this.getMethod(true);
        doLog(method, Level.ERROR, message, t);
    }

    /**
     * デバッグログ
     *
     * @param message
     *            輸出メッセージ
     */
    public void debug(Object message) {
        String method = this.getMethod(true);
        doLog(method, Level.DEBUG, message, null);
    }

    /**
     * デバッグログ
     *
     * @param message
     *            輸出メッセージ
     * @param t
     *            異常
     */
    public void debug(Object message, Throwable t) {
        String method = this.getMethod(true);
        doLog(method, Level.DEBUG, message, t);
    }

    /**
     * This is the most generic printing method.
     *
     * @param callerFQCN
     *            The wrapper class' fully qualified class name.
     * @param level
     *            The level of the logging request.
     * @param message
     *            The message of the logging request.
     * @param t
     *            The Throwable of the logging request, may be null.
     */
    protected void doLog(String callerFQCN, Priority level, Object message, Throwable t) {
        if (this.logger.getLoggerRepository().isDisabled(level.toInt())) {
            return;
        }

        logger.log(level, callerFQCN + " " + message, t);
    }

    /**
     *
     * <p>
     * [概 要]log trace
     * </p>
     * <p>
     * [備 考]
     * </p>
     *
     * @param message
     *            出力メッセージ
     */
    public void trace(String message) {
        String method = this.getMethod(true);
        this.doLog(method, Level.TRACE, message, null);
    }

    /**
     *
     * <p>
     * [概 要]trace start
     * </p>
     * <p>
     * [備 考]
     * </p>
     */
    public void traceStart() {
        String method = this.getMethod(true);
        this.doLog(method, Level.TRACE, START, null);
    }

    /**
     *
     * <p>
     * [概 要]trace end
     * </p>
     * <p>
     * [備 考]
     * </p>
     */
    public void traceEnd() {
        String method = this.getMethod(true);
        this.doLog(method, Level.TRACE, END, null);
    }

    /**
     *
     * <p>
     * [概 要]メソッド名を取得する
     * </p>
     * <p>
     * [備 考]
     * </p>
     *
     * @param additional
     *            パッケージ名とクラス名 も一緒に返すかどうかの指示
     *
     * @return String(例:com.hp.isas.common.TestClass#getUserId)
     */
    private String getMethod(boolean additional) {
        StackTraceElement st = Thread.currentThread().getStackTrace()[METHOD_LAYER];
        String className = st.getClassName();
        String method = st.getMethodName();
        if (additional) {
            method = className + "#" + method + "()";
        }
        return method;
    }
}
