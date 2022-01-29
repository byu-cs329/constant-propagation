package parenthesizedLiterals;

public class should_NotFoldAnything_when_ThereAreNoParenthesizedLiterals {
  private int double(int val) {
    return val * val;
  }

  public int name(final int y) {
    final int x = (y);
    final String s = new String("Hello");
    final boolean t = (Name.class == Name.class);
    x = double(13);
    return x;
  }
}
