package com.sebastmar.module.report.internal.host

import com.sebastmar.module.report.info.Module
import com.sebastmar.module.report.info.ModuleType
import com.sebastmar.module.report.info.PullRequest
import com.sebastmar.module.report.info.VersionedFile
import com.sebastmar.module.report.info.VersionedFile.Status
import com.sebastmar.module.report.internal.ShouldLinkifyFiles
import com.sebastmar.module.report.internal.ShowCircleIndicators
import com.sebastmar.module.report.internal.ShowLineIndicators
import com.sebastmar.module.report.internal.domain.GetPullRequest
import com.sebastmar.module.report.internal.domain.StringProvider
import com.sebastmar.module.report.internal.host.github.GithubTableReportBuilder
import com.sebastmar.module.report.internal.system.SystemWrapper
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GithubTableReportBuilderTest {

    private val stringProvider: StringProvider = mockk()
    private val systemWrapper: SystemWrapper = mockk(relaxed = true)
    private val getPullRequest: GetPullRequest = mockk(relaxed = true)
    private val writer: StringBuilder = mockk(relaxed = true)

    private lateinit var reportBuilder: GithubTableReportBuilder

    private fun setUpReportBuilder(
        showLineIndicators: Boolean = false,
        shouldLinkifyFiles: Boolean = false,
        showCircleIndicators: Boolean = false,
    ) {
        reportBuilder = GithubTableReportBuilder(
            showLineIndicators = ShowLineIndicators(showLineIndicators),
            shouldLinkifyFiles = ShouldLinkifyFiles(shouldLinkifyFiles),
            showCircleIndicators = ShowCircleIndicators(showCircleIndicators),
            stringProvider = stringProvider,
            systemWrapper = systemWrapper,
            getPullRequest = getPullRequest,
            builder = writer,
        )
    }

    @BeforeEach
    fun setUp() {
        setUpReportBuilder()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `verify it writes the top section when on github`() {
        val givenTopSection = "# My Title"

        every { systemWrapper.onGithub() } returns true
        every { stringProvider.topSection() } returns givenTopSection

        reportBuilder.topSection()

        verifySequence {
            writer.append(givenTopSection)
        }
    }

    @Test
    fun `verify it writes the top section and the warning when not on github`() {
        val givenTopSection = "# My Title"
        val incorrectHostWarning = "IncorrectHost"

        every { systemWrapper.onGithub() } returns false
        every { stringProvider.topSection() } returns givenTopSection
        every { stringProvider.incorrectHostWarning() } returns incorrectHostWarning

        reportBuilder.topSection()

        verifySequence {
            writer.append(incorrectHostWarning)
            writer.append(givenTopSection)
        }
    }

    @Test
    fun `verify it doesn't write the top section when it is null`() {
        every { systemWrapper.onGithub() } returns true
        every { stringProvider.topSection() } returns null

        reportBuilder.topSection()

        verify { writer wasNot Called }
    }

    @Test
    fun `verify it writes the bottom section when it is not null`() {
        val givenBottomSection = "# Bottom"

        every { stringProvider.bottomSection() } returns givenBottomSection

        reportBuilder.bottomSection()

        verifySequence {
            writer.append(givenBottomSection)
        }
    }

    @Test
    fun `verify it doesn't write the bottom section when it is null`() {
        every { stringProvider.bottomSection() } returns null

        reportBuilder.bottomSection()

        verify { writer wasNot Called }
    }

    @Test
    fun `verify it writes a table around the content`() {
        val givenContent = "content"

        every { writer.appendLine(any<String>()) } returns writer

        reportBuilder.table {
            writer.append(givenContent)
        }

        verifySequence {
            writer.append("<table>")
            writer.append(givenContent)
            writer.append("</table>")
            writer.append("\n\n")
        }
    }

    @Test
    fun `verify it write the header row with an exhaustive list of files and with the line indicators enabled`() {
        val givenModule = Module(
            name = "Module",
            files = getModifiedFiles() + getDeletedFiles() + getAddedFiles(),
        )

        val givenPullRequest = PullRequest(htmlLink = "", modules = listOf(givenModule))

        every { getPullRequest() } returns givenPullRequest

        setUpReportBuilder(showLineIndicators = true)

        reportBuilder.headerRow()

        verifySequence {
            writer.append("<tr>")
            writer.append("<th>")
            writer.append("</th>")

            writer.append("<th>")
            writer.append("Added")
            writer.append(" (\$\\color{Green}{\\textsf{+3}}\$)")
            writer.append("</th>")

            writer.append("<th>")
            writer.append("Modified")
            writer.append(" (\$\\color{Green}{\\textsf{+3}}\$ / \$\\color{Red}{\\textsf{-3}}\$)")
            writer.append("</th>")

            writer.append("<th>")
            writer.append("Deleted")
            writer.append(" (\$\\color{Red}{\\textsf{-3}}\$)")
            writer.append("</th>")

            writer.append("</tr>")
        }
    }

    @Test
    fun `verify it write the header row with the Added column only and with the line indicators disabled`() {
        val givenModule = Module(name = "Module", files = getAddedFiles())
        val givenPullRequest = PullRequest(htmlLink = "", modules = listOf(givenModule))

        every { getPullRequest() } returns givenPullRequest

        reportBuilder.headerRow()

        verifySequence {
            writer.append("<tr>")
            writer.append("<th>")
            writer.append("</th>")

            writer.append("<th>")
            writer.append("Added")
            writer.append("</th>")

            writer.append("</tr>")
        }
    }

    @Test
    fun `verify it write the header row with the Modified column only and with the line indicators disabled`() {
        val givenModule = Module(name = "Module", files = getModifiedFiles())
        val givenPullRequest = PullRequest(htmlLink = "", modules = listOf(givenModule))

        every { getPullRequest() } returns givenPullRequest

        reportBuilder.headerRow()

        verifySequence {
            writer.append("<tr>")
            writer.append("<th>")
            writer.append("</th>")

            writer.append("<th>")
            writer.append("Modified")
            writer.append("</th>")

            writer.append("</tr>")
        }
    }

    @Test
    fun `verify it write the header row with the Deleted column only and with the line indicators disabled`() {
        val givenModule = Module(name = "Module", files = getDeletedFiles())
        val givenPullRequest = PullRequest(htmlLink = "", modules = listOf(givenModule))

        every { getPullRequest() } returns givenPullRequest

        reportBuilder.headerRow()

        verifySequence {
            writer.append("<tr>")
            writer.append("<th>")
            writer.append("</th>")

            writer.append("<th>")
            writer.append("Deleted")
            writer.append("</th>")

            writer.append("</tr>")
        }
    }

    @Test
    fun `verify moduleRows sort the modules by their type`() {
        val givenModule = listOf(
            Module("ModuleStandard", ModuleType.STANDARD, getModifiedFiles(size = 0)),
            Module("ModuleNotKnown", ModuleType.NOT_KNOWN, getDeletedFiles(size = 0)),
            Module("ModuleRoot", ModuleType.PROJECT_ROOT, getAddedFiles(size = 0)),
        )

        every { getPullRequest() } returns PullRequest("PR", givenModule)

        reportBuilder.moduleRows()

        verifySequence {
            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>ModuleRoot</b></div>")
            writer.append("</td>")
            writer.append("</tr>")

            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>ModuleStandard</b></div>")
            writer.append("</td>")
            writer.append("</tr>")

            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>ModuleNotKnown</b></div>")
            writer.append("</td>")
            writer.append("</tr>")
        }
    }

    @Test
    fun `verify moduleRows display the added files when circle indicator and links are enabled`() {
        val givenModule = listOf(
            Module("Module1", ModuleType.STANDARD, getAddedFiles(size = 2)),
        )

        every { getPullRequest() } returns PullRequest("linkPr://", givenModule)

        setUpReportBuilder(
            shouldLinkifyFiles = true,
            showCircleIndicators = true,
        )

        reportBuilder.moduleRows()

        val file1Link =
            "<a href=\"linkPr:///files#diff-b6cf7031d592ca6997e983302aa5d42c3ec5eb01c2dc2896ff556c4d07ff5269\">file0.kt</a><br>"

        val file2Link =
            "<a href=\"linkPr:///files#diff-d92b0ed59db1125c6f24fbbedc10fcf10eb12b971772a581b8054f57c23e1067\">file1.kt</a><br>"

        verifySequence {
            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>Module1</b></div>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("🟢&nbsp;")
            writer.append(file1Link)
            writer.append("🟢&nbsp;")
            writer.append(file2Link)
            writer.append("</td>")
            writer.append("</tr>")
        }
    }

    @Test
    fun `verify moduleRows display the deleted files when circle indicator and links are enabled`() {
        val givenModule = listOf(
            Module("Module1", ModuleType.STANDARD, getDeletedFiles(size = 2)),
        )

        every { getPullRequest() } returns PullRequest("linkPr://", givenModule)

        setUpReportBuilder(
            shouldLinkifyFiles = true,
            showCircleIndicators = true,
        )

        reportBuilder.moduleRows()

        val file1Link =
            "<a href=\"linkPr:///files#diff-b6cf7031d592ca6997e983302aa5d42c3ec5eb01c2dc2896ff556c4d07ff5269\">file0.kt</a><br>"

        val file2Link =
            "<a href=\"linkPr:///files#diff-d92b0ed59db1125c6f24fbbedc10fcf10eb12b971772a581b8054f57c23e1067\">file1.kt</a><br>"

        verifySequence {
            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>Module1</b></div>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("🔴&nbsp;")
            writer.append(file1Link)
            writer.append("🔴&nbsp;")
            writer.append(file2Link)
            writer.append("</td>")
            writer.append("</tr>")
        }
    }

    @Test
    fun `verify moduleRows display the modified files when circle indicator and links are enabled`() {
        val givenModule = listOf(
            Module("Module1", ModuleType.STANDARD, getModifiedFiles(size = 2)),
        )

        every { getPullRequest() } returns PullRequest("linkPr://", givenModule)

        setUpReportBuilder(
            shouldLinkifyFiles = true,
            showCircleIndicators = true,
        )

        reportBuilder.moduleRows()

        val file1Link =
            "<a href=\"linkPr:///files#diff-b6cf7031d592ca6997e983302aa5d42c3ec5eb01c2dc2896ff556c4d07ff5269\">file0.kt</a><br>"

        val file2Link =
            "<a href=\"linkPr:///files#diff-d92b0ed59db1125c6f24fbbedc10fcf10eb12b971772a581b8054f57c23e1067\">file1.kt</a><br>"

        verifySequence {
            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>Module1</b></div>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("🟡&nbsp;")
            writer.append(file1Link)
            writer.append("🟡&nbsp;")
            writer.append(file2Link)
            writer.append("</td>")
            writer.append("</tr>")
        }
    }

    @Test
    fun `verify moduleRows display multiple lines of modules and files`() {
        val givenModule = listOf(
            Module("Project's Root", ModuleType.PROJECT_ROOT, getModifiedFiles(2)),
            Module("data", ModuleType.STANDARD, getModifiedFiles(2) + getDeletedFiles(1)),
            Module("data:room", ModuleType.STANDARD, getModifiedFiles(2) + getAddedFiles(1)),
            Module("Others", ModuleType.NOT_KNOWN, getDeletedFiles(2)),
        )

        every { getPullRequest() } returns PullRequest("", givenModule)

        reportBuilder.moduleRows()

        verifySequence {
            // Project's Root Module
            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>Project's Root</b></div>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("file0.kt<br>")
            writer.append("file1.kt<br>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("</td>")
            writer.append("</tr>")

            // data Module
            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>data</b></div>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("file0.kt<br>")
            writer.append("file1.kt<br>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("file0.kt<br>")
            writer.append("</td>")
            writer.append("</tr>")

            // data:room Module
            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>data:room</b></div>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("file0.kt<br>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("file0.kt<br>")
            writer.append("file1.kt<br>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("</td>")
            writer.append("</tr>")

            // Others Module
            writer.append("<tr>")
            writer.append("<td>")
            writer.append("<div style=\"display: inline-block;\"><b>Others</b></div>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("</td>")
            writer.append("<td>")
            writer.append("file0.kt<br>")
            writer.append("file1.kt<br>")
            writer.append("</td>")
            writer.append("</tr>")
        }
    }

    @Test
    fun `verify buildReport returns the writer content`() {
        val givenContent = "WriterContent"

        every { writer.toString() } returns givenContent

        val result = reportBuilder.getReport()

        assertEquals(givenContent, result)
    }

    companion object {
        private fun getModifiedFiles(size: Int = 3): List<VersionedFile> = List(size) { index -> versionedFile(index, Status.Modified) }

        private fun getDeletedFiles(size: Int = 3): List<VersionedFile> = List(size) { index -> versionedFile(index, Status.Deleted) }

        private fun getAddedFiles(size: Int = 3): List<VersionedFile> = List(size) { index -> versionedFile(index, Status.Created) }

        private fun versionedFile(index: Int, status: Status) = VersionedFile(
            name = "file$index.kt",
            fullPath = "path/to/file$index.kt",
            status = status,
            insertions = index,
            deletions = index,
        )
    }
}
