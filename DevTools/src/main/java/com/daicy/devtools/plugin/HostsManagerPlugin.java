package com.daicy.devtools.plugin;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.concurrent.Task;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import org.apache.commons.lang3.StringUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daicy.core.ExceptionHandler;
import com.daicy.devtools.TextPlugin;
import com.daicy.devtools.plugin.util.HostsUtil;

/**
 * Hosts 文件管理插件
 * 
 * <p>提供 hosts 文件的编辑和管理功能，支持语法高亮。
 * 
 * @author daicy
 */
public class HostsManagerPlugin implements TextPlugin {
    
    private static final Logger logger = LoggerFactory.getLogger(HostsManagerPlugin.class);

    private static final String KEYWORD_PATTERN = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}\\b";
    private static final String COMMENT_PATTERN = "#[^\n]*";

    final KeyCombination keyCombinationCtrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

    private CodeArea codeArea;
    private ExecutorService executor;

    private final StackPane contentPane;

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    public HostsManagerPlugin() {
        executor = Executors.newSingleThreadExecutor();
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        codeArea.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(codeArea.richChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        logger.error("语法高亮计算失败", t.getFailure());
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
        codeArea.replaceText(0, 0, HostsUtil.getHostsContent());

        contentPane = new StackPane(new VirtualizedScrollPane<>(codeArea));
        contentPane.getStylesheets().add(getResource("css/hosts-keywords.css").toExternalForm());
    }

    private URL getResource(String image) {
        return getClass().getClassLoader().getResource(image);
    }

    @Override
    public void destroy() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                // 等待任务完成，最多等待5秒
                if (!executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("COMMENT") != null ? "comment" :
                                    null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    @Override
    public Pane getContentPane() {
        return contentPane;
    }

    @Override
    public Path getDefaultPath() {
        return Path.of(HostsUtil.getHostsFile().getPath());
    }

    @Override
    public void open(String filePath) {
        try {
            String content = Files.readString(java.nio.file.Paths.get(filePath));
            setContent(content);
        } catch (IOException e) {
            ExceptionHandler.handleException(e, "文件打开失败", "无法打开文件", 
                    "无法读取文件: " + filePath + "\n错误: " + e.getMessage());
        }
    }

    @Override
    public void save(String filePath) {
        try {
            if(StringUtils.isEmpty(filePath)){
                HostsUtil.saveHostsContent(codeArea.getText());
            }else{
                Files.write(java.nio.file.Paths.get(filePath), 
                        codeArea.getText().getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            ExceptionHandler.handleException(e, "文件保存失败", "无法保存文件", 
                    "无法保存文件: " + filePath + "\n错误: " + e.getMessage());
        }
    }

    @Override
    public String getContent() {
        return codeArea.getText();
    }

    @Override
    public void setContent(String content) {
        codeArea.clear();
        codeArea.replaceText(0, 0,content);    }
}