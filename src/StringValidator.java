import java.util.*;
import java.util.stream.Collectors;

public class StringValidator {

    public static void main(String[] args) {
    	StringValidator validator = new StringValidator();
        validator.validate("([test](string){<>})");
        System.out.println("First test completed.");

        try {
            validator.validate("no_braces");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        try {
            validator.validate("(((]]]");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        try {
            validator.validate("[]()<>{}last_char_has_no_brace");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final Set<String> openBraces = new HashSet<>(Arrays.asList("[", "(", "{", "<"));
    private static final Set<String> closeBraces = new HashSet<>(Arrays.asList("]", ")", "}", ">"));
    private static final Set<String> allBraces = new HashSet<>();

    static {
        allBraces.addAll(openBraces);
        allBraces.addAll(closeBraces);
    }

    /**
     * Validates string
     * @param input target string
     * @throws ValidationException if validation fails
     */
    public void validate(String input) throws ValidationException {
        List<String> braces = isContainBraces(input);
        checkFirstAndLast(input);
        checkBraceBalancing(braces);
    }

    private List<String> isContainBraces(String input) {
        List<String> braces = Arrays.stream(input.split(""))
                .filter(c -> allBraces.contains(c))
                .collect(Collectors.toList());
        if (braces.isEmpty()) {
            throw new ValidationException("String doesn't contain any braces");
        }
        return braces;
    }

    private void checkFirstAndLast(String input) {
        if (!allBraces.contains(input.substring(0, 1)) || !allBraces.contains(input.substring(input.length() - 1))) {
            throw new ValidationException("The string should start and end with a braces.");
        }
    }

    private void checkBraceBalancing(List<String> braces) {
        Deque<String> braceQueue = new LinkedList<>();
        for (String c : braces) {
            if (openBraces.contains(c)) {
                braceQueue.add(c);
            }
            if (closeBraces.contains(c)) {
                String prevBrace = braceQueue.peekLast();
                if (openBraces.contains(prevBrace)) {
                    // close balanced braces
                    if (isMatchingBraces(prevBrace, c)) {
                        braceQueue.pollLast();
                    } else {
                        braceQueue.add(c);
                    }
                } else {
                    braceQueue.add(c);
                }
            }
        }
        if (!braceQueue.isEmpty()) {
            throw new ValidationException("Braces are not balanced");
        }
    }

    private boolean isMatchingBraces(String prevBrace, String c) {
        if (prevBrace.equals("[") && c.equals("]")) return true;
        if (prevBrace.equals("(") && c.equals(")")) return true;
        if (prevBrace.equals("{") && c.equals("}")) return true;
        if (prevBrace.equals("<") && c.equals(">")) return true;
        return false;
    }

    static class ValidationException extends RuntimeException {
        /**
		 * 
		 */
		private static final long serialVersionUID = -8833621657417550312L;

		public ValidationException(String message) {
            super(message);
        }
    }
}


