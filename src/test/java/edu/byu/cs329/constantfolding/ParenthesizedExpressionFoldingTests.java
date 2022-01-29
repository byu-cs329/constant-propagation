package edu.byu.cs329.constantfolding;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;

import org.eclipse.jdt.core.dom.ASTNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.byu.cs329.TestUtils;

@DisplayName("Tests for folding ParenthesizedExpression types")
public class ParenthesizedExpressionFoldingTests {
  ParenthesizedExpressionFolding folderUnderTest = null;

  @BeforeEach
  void beforeEach() {
    folderUnderTest = new ParenthesizedExpressionFolding();
  }

  @Test
  @DisplayName("Should throw RuntimeException when root is null")
  void should_ThrowRuntimeException_when_RootIsNull() {
    assertThrows(RuntimeException.class, () -> {
      folderUnderTest.fold(null);
    });
  }

  @Test
  @DisplayName("Should throw RuntimeException when root is not a CompilationUnit and has no parent")
  void should_ThrowRuntimeException_when_RootIsNotACompilationUnitAndHasNoParent() {
    assertThrows(RuntimeException.class, () -> {
      URI uri = TestUtils.getUri(this, "");
      ASTNode compilationUnit = TestUtils.getCompilationUnit(uri);
      ASTNode root = compilationUnit.getAST().newNullLiteral();
      folderUnderTest.fold(root);
    });
  }

  @Test
  @DisplayName("Should not fold anything when there are no parenthesized literals")
  void should_NotFoldAnything_when_ThereAreNoParenthesizedLiterals() {
    String rootName = "foldingInputs/parenthesizedLiterals/should_NotFoldAnything_when_ThereAreNoParenthesizedLiterals.java";
    TestUtils.assertDidNotFold(this, rootName, folderUnderTest);
  }

  @Test
  @DisplayName("Should only fold parenthesized literals when given multiple types")
  void should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes() {
    String rootName = "foldingInputs/parenthesizedLiterals/should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes-root.java";
    String expectedName = "foldingInputs/parenthesizedLiterals/should_OnlyFoldParenthesizedLiterals_when_GivenMultipleTypes.java";
    TestUtils.assertDidFold(this, rootName, expectedName, folderUnderTest);
  }

  @Test
  @DisplayName("Should only fold parenthesized literal when given nested parenthesized expressions")
  void should_OnlyFoldParenthesizedLiteral_when_GivenNestedParentheses() {
    String rootName = "foldingInputs/parenthesizedLiterals/should_OnlyFoldParenthesizedLiteral_when_GivenNestedParentheses-root.java";
    String expectedName = "foldingInputs/parenthesizedLiterals/should_OnlyFoldParenthesizedLiteral_when_GivenNestedParentheses.java";
    TestUtils.assertDidFold(this, rootName, expectedName, folderUnderTest);
  }
}