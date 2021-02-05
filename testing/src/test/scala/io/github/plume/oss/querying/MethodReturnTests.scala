package io.github.plume.oss.querying

import io.github.plume.oss.PlumeCodeToCpgSuite
import io.shiftleft.semanticcpg.language._

class MethodReturnTests extends PlumeCodeToCpgSuite {

  override val code =
    """
      | int foo() { return 1; }
      |""".stripMargin

  "should have METHOD_RETURN node with correct fields" in {
    val List(x) = cpg.methodReturn.l
    x.code shouldBe "int"
    x.typeFullName shouldBe "int"
    x.lineNumber shouldBe Some(2)
    // we expect the METHOD_RETURN node to be the right-most
    // child so that when traversing the AST from left to
    // right in CFG construction, we visit it last.
    x.order shouldBe 2
  }

  "should allow traversing to method" in {
    cpg.methodReturn.method.name.l shouldBe List("foo")
  }

}
