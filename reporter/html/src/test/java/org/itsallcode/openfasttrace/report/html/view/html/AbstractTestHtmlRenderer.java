package org.itsallcode.openfasttrace.report.html.view.html;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.itsallcode.openfasttrace.testutil.matcher.MultilineTextMatcher.matchesAllLines;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.itsallcode.openfasttrace.report.html.HtmlReport;
import org.itsallcode.openfasttrace.report.html.view.ViewFactory;
import org.junit.jupiter.api.BeforeEach;

class AbstractTestHtmlRenderer
{
    protected OutputStream outputStream;
    protected ViewFactory factory;

    @BeforeEach
    void prepareEachTest()
    {
        this.outputStream = new ByteArrayOutputStream();
        this.factory = HtmlViewFactory.create(this.outputStream, HtmlReport.getCssUrl());
    }

    protected void assertOutputLines(final String... lines)
    {
        assertThat(this.outputStream.toString(), matchesAllLines(lines));
    }

    protected void assertOutputLinesWithoutCSS(final String... lines)
    {
        final String html = this.outputStream.toString();
        final String htmlWithoutCSS = Pattern //
                .compile("<style>.*</style>", Pattern.DOTALL) //
                .matcher(html) //
                .replaceAll("<style></style>");
        assertThat(htmlWithoutCSS, matchesAllLines(lines));
    }
}
