package io.github.plume.oss.querying
import io.github.plume.oss.PlumeCodeToCpgSuite
import io.shiftleft.semanticcpg.language._

class MethodTests extends PlumeCodeToCpgSuite {

  override val code =
    """ class Foo {
      |  int foo(int param1, int param2) {
      | return 1;
      | }
      |}
      |""".stripMargin

  "should contain exactly one method node with correct fields" in {
    val List(x) = cpg.method.nameNot("<init>").l
    x.name shouldBe "foo"
    x.fullName shouldBe "Foo.foo"
    // x.code shouldBe "int foo (int param1,int param2)"
    x.signature shouldBe "int foo(int,int)"
    x.isExternal shouldBe false
    x.order shouldBe 1
    x.filename.startsWith("/") shouldBe true
    x.filename.endsWith(".class") shouldBe true
    x.lineNumber shouldBe Some(2)
    // x.lineNumberEnd shouldBe Some(3)
    // x.columnNumber shouldBe Some(2)
    // x.columnNumberEnd shouldBe Some(1)
  }

//  "should return correct number of lines" in {
//    cpg.method.name("foo").numberOfLines.l shouldBe List(2)
//  }

  "should allow traversing to parameters" in {
    cpg.method.name("foo").parameter.name.toSet shouldBe Set("param1", "param2")
  }

  "should allow traversing to methodReturn" in {
    cpg.method.name("foo").methodReturn.typeFullName.l shouldBe List("int")
  }

  "should allow traversing to file" in {
    cpg.method.name("foo").file.name.l should not be empty
  }

}
