package io.github.plume.oss.extractor.intraprocedural

import io.github.plume.oss.Extractor
import io.github.plume.oss.drivers.DriverFactory
import io.github.plume.oss.drivers.GraphDatabase
import io.github.plume.oss.drivers.TinkerGraphDriver
import io.shiftleft.codepropertygraph.generated.EdgeTypes.CFG
import io.shiftleft.codepropertygraph.generated.nodes.Call
import io.shiftleft.codepropertygraph.generated.nodes.ControlStructure
import io.shiftleft.codepropertygraph.generated.nodes.JumpTarget
import io.shiftleft.codepropertygraph.generated.nodes.Local
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import overflowdb.Graph
import java.io.File
import java.io.IOException

class ConditionalIntraproceduralTest {
    private lateinit var testResourcePath: String

    companion object {
        private val driver = DriverFactory(GraphDatabase.TINKER_GRAPH) as TinkerGraphDriver
        private lateinit var g: Graph
        private var PATH: File
        private val TEST_PATH = "intraprocedural${File.separator}conditional"

        init {
            val testFileUrl = ConditionalIntraproceduralTest::class.java.classLoader.getResource(TEST_PATH)
                ?: throw NullPointerException("Unable to obtain test resource")
            PATH = File(testFileUrl.file)
        }
    }

    @BeforeEach
    @Throws(IOException::class)
    fun setUp(testInfo: TestInfo) {
        val extractor = Extractor(driver)
        // Select test resource based on integer in method name
        val currentTestNumber = testInfo
            .displayName
            .replace("[^0-9]".toRegex(), "")
        testResourcePath = "${PATH.absolutePath}${File.separator}Conditional$currentTestNumber"
        val testSourceFile = "$testResourcePath.java"
        // Load test resource and project + export graph
        val f = File(testSourceFile)
        extractor.load(f)
        extractor.project()
        g = driver.getWholeGraph()
    }

    @AfterEach
    fun tearDown() {
        driver.close()
        g.close()
    }

    @Test
    fun conditional1Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        ns.filterIsInstance<Call>().filter { it.name() == "ADD" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "SUB" }
            .let { assertEquals(2, it.toList().size) }
        assertEquals(2, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            val ifVert = csv.firstOrNull(); assertNotNull(ifVert); ifVert!!
            assertTrue(g.V(ifVert.id()).next().outE(CFG).hasNext())
            g.V(ifVert.id()).next().out(CFG).asSequence().filterIsInstance<JumpTarget>().toList().let {
                assertEquals(2, it.toList().size)
                assertNotNull(it.find { jtv -> jtv.name() == "TRUE" })
                assertNotNull(it.find { jtv -> jtv.name() == "FALSE" })
            }
        }
    }

    @Test
    fun conditional2Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        ns.filterIsInstance<Call>().filter { it.name() == "ADD" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "SUB" }
            .let { assertEquals(2, it.toList().size) }
        assertEquals(2, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            val ifVert = csv.firstOrNull(); assertNotNull(ifVert); ifVert!!
            assertTrue(g.V(ifVert.id()).next().outE(CFG).hasNext())
            g.V(ifVert.id()).next().out(CFG).asSequence().filterIsInstance<JumpTarget>().toList().let {
                assertEquals(2, it.toList().size)
                assertNotNull(it.find { jtv -> jtv.name() == "TRUE" })
                assertNotNull(it.find { jtv -> jtv.name() == "FALSE" })
            }
        }
    }

    @Test
    fun conditional3Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        ns.filterIsInstance<Call>().filter { it.name() == "ADD" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "SUB" }
            .let { assertEquals(2, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "MUL" }
            .let { assertEquals(1, it.toList().size) }
        assertEquals(2, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            val ifVert = csv.firstOrNull(); assertNotNull(ifVert); ifVert!!
            assertTrue(g.V(ifVert.id()).next().outE(CFG).hasNext())
            g.V(ifVert.id()).next().out(CFG).asSequence().filterIsInstance<JumpTarget>().toList().let {
                assertEquals(2, it.toList().size)
                assertNotNull(it.find { jtv -> jtv.name() == "TRUE" })
                assertNotNull(it.find { jtv -> jtv.name() == "FALSE" })
            }
        }
    }

    @Test
    fun conditional4Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        ns.filterIsInstance<Call>().filter { it.name() == "ADD" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "SUB" }
            .let { assertEquals(2, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "MUL" }
            .let { assertEquals(1, it.toList().size) }
        assertEquals(4, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "EQ" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            assertEquals(2, csv.toList().size)
        }
    }

    @Test
    fun conditional5Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        ns.filterIsInstance<Call>().filter { it.name() == "SUB" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "MUL" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "DIV" }
            .let { assertEquals(1, it.toList().size) }
        assertEquals(6, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "EQ" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "LT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            assertEquals(3, csv.toList().size)
        }
    }

    @Test
    fun conditional6Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        ns.filterIsInstance<Call>().filter { it.name() == "MUL" }
            .let { assertEquals(1, it.toList().size) }
        assertEquals(2, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "EQ" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "LT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }
            .let { csv -> assertEquals(1, csv.toList().size) }
    }

    @Test
    fun conditional7Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        ns.filterIsInstance<Call>().filter { it.name() == "ADD" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "SUB" }
            .let { assertEquals(2, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "MUL" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "DIV" }
            .let { assertEquals(1, it.toList().size) }
        assertEquals(6, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "EQ" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "LT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            assertEquals(3, csv.toList().size)
        }
    }

    @Test
    fun conditional8Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        ns.filterIsInstance<Call>().filter { it.name() == "ADD" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "SUB" }
            .let { assertEquals(2, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "MUL" }
            .let { assertEquals(1, it.toList().size) }
        ns.filterIsInstance<Call>().filter { it.name() == "DIV" }
            .let { assertEquals(1, it.toList().size) }
        assertEquals(6, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "EQ" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "LT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            assertEquals(3, csv.toList().size)
        }
    }

    @Test
    fun conditional9Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        assertNotNull(ns.find { it is Local && it.name() == "c" })
        assertEquals(2, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            assertEquals(1, csv.toList().size)
        }
    }

    @Test
    fun conditional10Test() {
        val ns = g.nodes().asSequence().toList().distinct()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        assertNotNull(ns.find { it is Local && it.name() == "c" })
        assertEquals(4, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "LT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            assertEquals(2, csv.toList().size)
        }
    }

    @Test
    fun conditional11Test() {
        val ns = g.nodes().asSequence().toList()
        assertNotNull(ns.find { it is Local && it.name() == "a" })
        assertNotNull(ns.find { it is Local && it.name() == "b" })
        assertNotNull(ns.find { it is Local && it.name() == "c" })
        assertEquals(4, ns.filterIsInstance<JumpTarget>().toList().size)
        ns.filterIsInstance<Call>().filter { it.name() == "GT" }.let { assertNotNull(it) }
        ns.filterIsInstance<Call>().filter { it.name() == "LT" }.let { assertNotNull(it) }
        ns.filterIsInstance<ControlStructure>().filter { it.code() == "IF" }.let { csv ->
            assertEquals(2, csv.toList().size)
        }
    }
}